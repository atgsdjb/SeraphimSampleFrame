package com.seraph.remote.airplay;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class AirplayHttpWarp {
	/*
	 * 
	 */
	private static final String TAG="com.seraph.remote.airplay";
	private InetAddress addr = null;
	private int port = -1;
	
	
	
	public AirplayHttpWarp(InetAddress addr, int port) {
		super();
		this.addr = addr;
		this.port = port;
	}


	public AirplayServerInfo getAirplayServerInfo(){
		AirplayServerInfo info = null;
		getWitchRead(AirplayHttphead.getServerInfo);
		return info;
	}
	
	
	public boolean AirplayCRLPlay(String url,float postion){
		String head = AirplayHttphead.getPlayHead(url,postion);
		Log.e(TAG,head);
		getWitchRead(head);
		return true;
	}
	public boolean AirplayCRLPuase(){
		return true;
	}

	public boolean AirplayCRLStop(){
	return true;	
	}
	public boolean AirplayCRLSeek(long postion){
		return true;
	}
	private String getWitchRead(String head){
		String response = null;
        try { 
           Socket socket = new Socket(); 
           boolean autoflush = true;
           InetSocketAddress _addr =  new InetSocketAddress(addr,port);
           socket.connect(_addr);
           PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush); 
           out.write(head);
           out.flush();
           InputStream in = socket.getInputStream();
           StringBuffer sb = new StringBuffer(8096); 
           byte[]  t_buff= new byte[2048];
           while(in.available() >0){
        	   in.read(t_buff);
        	   sb.append(new String(t_buff));
           }
           response = sb.toString();
           Log.d(TAG,"response===="+response);
          socket.close();
       } catch (UnknownHostException e) {
            System.err.println("Don't know about host: Victest."); 
            System.exit(1); 
       } catch (IOException e) { 
           System.err.println("Couldn't get I/O for " + "the connection to: Victest.");  
           System.exit(1); 
       } 
  
		return response;
	}
}
