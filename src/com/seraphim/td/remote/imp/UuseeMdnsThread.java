/*
 * Copyright 2011 David Simmons
 * http://cafbit.com/entry/testing_multicast_support_on_android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seraphim.td.remote.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.seraphim.td.remote.airplay.APGlobal;
import com.seraphim.td.remote.airplay.NetUtil;
import com.seraphim.td.remote.airplay.Packet;
import com.seraphim.td.remote.airplay.mdns.DNSAnswer;
import com.seraphim.td.remote.airplay.mdns.DNSComponent;
import com.seraphim.td.remote.airplay.mdns.DNSMessage;
import com.seraphim.td.remote.airplay.mdns.DNSQuestion;
import com.seraphim.td.remote.airplay.mdns.DNSComponent.DNSType;
import com.seraphim.td.remote.airplay.mdns.DNSSRV;
import com.seraphim.td.remote.imp.AbstractDevice.DeviceType;


import android.content.Context;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UuseeMdnsThread extends Thread {

    public static final String TAG = "org.seraphim.td.NetThread";
    // the standard mDNS multicast address and port number
    private static final byte[] MDNS_ADDR =
        new byte[] {(byte) 224,(byte) 0,(byte) 0,(byte) 251};
    private static final int MDNS_PORT = 5353;
    private static final int BUFFER_SIZE = 4096;
    private NetworkInterface networkInterface;
    private InetAddress groupAddress;
    private MulticastSocket multicastSocket;
    private NetUtil netUtil;
    private UuseeRemoteWarp root;
    private UuseeAddListener listener;
    /**
     * Construct the network thread.
     * @param activity
     */
    public UuseeMdnsThread(Context context,UuseeRemoteWarp _root, UuseeAddListener _listener) {
        super("net");
        root = _root;
        listener = _listener;
        netUtil = new NetUtil(context);
    }
    
    private void openSocket() throws IOException {
        multicastSocket = new MulticastSocket(MDNS_PORT);
        multicastSocket.setTimeToLive(2);
        multicastSocket.setReuseAddress(true);
        multicastSocket.setNetworkInterface(networkInterface);
        multicastSocket.joinGroup(groupAddress);
    }

    @Override
    public void run() {
        Log.v(TAG, "starting network thread");
        Set<InetAddress> localAddresses = NetUtil.getLocalAddresses();
        MulticastLock multicastLock = null;
        @SuppressWarnings("unused")
		int len = 0;
        try {
            networkInterface = netUtil.getFirstWifiOrEthernetInterface();
            if (networkInterface == null) {
                throw new IOException("Your WiFi is not enabled.");
            }
            groupAddress = InetAddress.getByAddress(MDNS_ADDR); 
            multicastLock = netUtil.getWifiManager().createMulticastLock("unmote");
            multicastLock.acquire();
            openSocket();
        } catch (IOException e1) {
//            pass
        	Log.e(TAG,"OPEN SOCKET ERROR");
            return;
        }
        // set up the buffer for incoming packets
        byte[] responseBuffer = new byte[BUFFER_SIZE];
        DatagramPacket response = new DatagramPacket(responseBuffer, BUFFER_SIZE);
        DNSQuestion  question = new DNSQuestion(DNSComponent.DNSType.PTR,
				"_airplay._tcp.local");
		query(question);
        while (true) {
        	
            // zero the incoming buffer for good measure.
            java.util.Arrays.fill(responseBuffer, (byte) 0); // clear buffer
            
            // receive a packet (or process an incoming command)
            try {
                multicastSocket.receive(response);
            } catch (IOException e) {
                // check for commands to be run
                Command cmd = commandQueue.poll();
                if (cmd == null) {
//                    pass
                    return;
                }

                // reopen the socket
                try {
                    openSocket();
                } catch (IOException e1) {
                	//pass
                	Log.e(TAG,"IOException for line no 133="+e.getMessage());
                    return;
                }

                if (cmd instanceof QueryCommand) {
                    try {
                        query(((QueryCommand)cmd).host);
                    } catch (IOException e1) {
//                        activity.ipc.error(e1);
                    	Log.e(TAG,e1.getMessage());
                    }
                } else if (cmd instanceof QuitCommand) {
                    break;
                }else if(cmd instanceof QueryWigplugCommand){
                	try{
                		queryWigplug();
                	}catch (Exception e2) {
						// TODO: handle exception
					}
                }
                
                continue;
            }
          
            if (localAddresses.contains(response.getAddress())) {
                continue;
            }
            DNSMessage message = null;
            try {
                message = new DNSMessage(response.getData(), response.getOffset(), response.getLength());
            } catch (Exception e) {
            	Log.e(TAG,""+e.getMessage());
//                continue;
            }
            if(message.getAnswerList() != null && message.getAnswerList().size() >0){
            	for(DNSAnswer answer : message.getAnswerList()){
            		 if(answer == null){
        				 break;
        			 }
        				 
            		 if(answer.getType()== DNSType.SRV){
            			
            			 DNSSRV srv = (DNSSRV)answer;
            			 String protocol = srv.getProtocol();
            			 if(protocol.contains("airplay")){
            				 String service = srv.getService();
            				 String name = srv.getName();
            				 InetAddress addr = response.getAddress();
            				 short port = srv.getPort();
            				 AirplayDevice device = new AirplayDevice(root, service+name, addr, port);
            				 if((root.addDevice(device))){
            					 listener.onAddDevice(device);
            				 }
            					 
            				 
            			 }else if(protocol.contains("wiplug")){
            				 String service = srv.getService();
            				 String name = srv.getName();
            				 InetAddress addr = response.getAddress();
            				 short port = srv.getPort();
            				 WiplugDevice device = new WiplugDevice(root, service+name, addr, port);
            				 if((root.addDevice(device))){
            					 listener.onAddDevice(device);
            				 }
            			 }
            		 }
            		
            		
            	}
            	
            }

            
        }
        multicastLock.release();
        multicastLock = null;
        Log.v(TAG, "stopping network thread");
    }
    /**
     * 3
     * 
     */
    public void sendQuestionSimIPAD(){
    	
    }
    /**
     * Transmit an mDNS query on the local network.
     * @param host
     * @throws IOException
     */
    private void query(String host) throws IOException {
        byte[] requestData = (new DNSMessage(host)).serialize();
        DatagramPacket request =
            new DatagramPacket(requestData, requestData.length, InetAddress.getByAddress(MDNS_ADDR), MDNS_PORT);
        multicastSocket.send(request);
    }
    /**
     * 
     * @param question
     * @throws IOException 
     */
    private void queryWigplug() throws IOException{
    	DNSQuestion question = new DNSQuestion(DNSType.PTR, "_wiplug._tcp._local");
    	 byte[] requestData = (new DNSMessage(question)).serialize();
    	 DatagramPacket request =  new DatagramPacket (requestData, requestData.length, InetAddress.getByAddress(MDNS_ADDR), MDNS_PORT);
    	  multicastSocket.send(request);
    }
    /**
     * 
     * @param question
     */
    private void query(DNSQuestion question){
    	byte[] requestData = (new DNSMessage(question).serialize());
    	DatagramPacket request = null;
		try {
			request = new DatagramPacket(requestData, requestData.length,InetAddress.getByAddress(MDNS_ADDR), MDNS_PORT);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e(TAG,""+e1.getMessage());
		}
    	try {
			multicastSocket.send(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG,""+e.getMessage());
		}
   }


    private Queue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();
    private static abstract class Command {
    }
    private static class QuitCommand extends Command {}
    private static class QueryWigplugCommand extends Command{};
    private static class QueryCommand extends Command {
        public QueryCommand(String host) { this.host = host; }
        public String host;
    }
    public void submitQueryWigplug(){
    	commandQueue.offer(new QueryWigplugCommand());
    	try{
    		while(multicastSocket == null)
    			sleep(200);
    	}catch (Exception e) {
			// TODO: handle exception
    		Log.e(TAG,e.getMessage());
		}
        multicastSocket.close();
    }
    
    public void submitQuery(String host) {
        commandQueue.offer(new QueryCommand(host));
    	try{
    		while(multicastSocket == null)
    			sleep(200);
    	}catch (Exception e) {
			// TODO: handle exception
		}
        multicastSocket.close();
    }
    public void submitQuit() {
        commandQueue.offer(new QuitCommand());
        if (multicastSocket != null) {
            multicastSocket.close();
        }
    }
    
    
    


}
