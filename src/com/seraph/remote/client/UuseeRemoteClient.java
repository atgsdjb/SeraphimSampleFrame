package com.seraph.remote.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

public class UuseeRemoteClient {
	static private UuseeRemoteClient sulf;
	private static final String head ="UUSEE_REMOVE_CMD_BEGIN";
	private static final String tail ="UUSEE_REMOVE_CMD_END";
	private Socket sSocket;
	private InputStream in = null ;
	private OutputStream out = null;
	private String webUrl ="http://player.uusee.com/mobile/apple/tv/tvIndex.html?222aaaa222231147";
	private BufferedReader readerNET =null;// new BufferedReader( new InputStreamReader(in) );
	private InetSocketAddress endpoint;
	private final  String TAG ="com.uusee.remote.client";
	static public UuseeRemoteClient connectServer(String ip,int port){
		if(sulf==null){
			sulf = new UuseeRemoteClient();
		}else{
			sulf.close();
		}
		if(!sulf.connect(ip, port))
			sulf =null;
		return sulf;
	}
	private void close(){
		try {
			sulf.sSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean connect(String ip,int port){

		sSocket = new Socket();
		try {
			endpoint = new InetSocketAddress(ip,port);
			sSocket.connect(endpoint);
			in = sSocket.getInputStream() ;
			out = sSocket.getOutputStream();
			readerNET =new BufferedReader(new InputStreamReader(in));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	
		return true;
	}
	
	public void sendCMD(String cmd){
		if(cmd ==null)
			return;
		try {
			
				StringBuilder sb = new StringBuilder();
				System.out.println(cmd);
				sb.append(head);
				sb.append('\n');
				sb.append(cmd);
				sb.append('\n');
				sb.append(tail);
				sb.append('\n');
				out.write(sb.toString().getBytes());
				String requset = readerNET.readLine();
				System.out.println(requset);
				Log.d(TAG,requset);
		} catch (IOException e) {
			// TODO Auto-generated catch blockT
			e.printStackTrace();
			if(in !=null){
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(out !=null){
				try {
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(sSocket !=null && !sSocket.isClosed()){
				try {
					sSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				
		}finally{
			
		}
	
	}
	/**
	 * heartthrob 
	 */
	private void starTickTock(){
		
	}
	private void stopTickTock(){
		
	}
	private UuseeRemoteClient(){
	}
}
