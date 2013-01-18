package com.seraphim.td.remote.airplay;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;



import android.annotation.SuppressLint;
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
		transferCmdWitchRead(AirplayHttphead.getServerInfo);
		return info;
	}
	
	
	public boolean airplayCRLPlay(String url,float postion){
		String head = AirplayHttphead.getPlayHead(url,postion);
		Log.e(TAG,head);
		String response = transferCmdWitchRead(head);
		int codeState = getCodeStateformResponse(response);	
		return codeState == 200;
	}

	public boolean airplayCRLSeek(long postion){
		return true;
	}
	
	public boolean airPlayCRLPause(){
		return airPlayPostRate(0);
	}
	
	public boolean airPlayCRLSpeed(){
		
		return airPlayPostScrub(10);
	}
	
	public boolean airPlayCRLRewind(){
		return airPlayPostScrub(-10);
	}
	
	public boolean airPlayCRLStop(){
		String response = transferCmdWitchRead(AirplayHttphead.StopHead);
		int codeState = getCodeStateformResponse(response);
		return codeState == 200;
		
	}
	
	
	
	/**
	 *   0 <= value <=1  , value=0 is pause , value=0 normal play
	 * @param value
	 * @return 
	 */
	@SuppressLint("DefaultLocale")
	private boolean   airPlayPostRate(float value){
		if(value <0 || value >1){
			throw  new IllegalArgumentException();
		}
		String head = String.format(AirplayHttphead.formatRateHead,value);
		String response = transferCmdWitchRead(head);
		
		int  codeState = getCodeStateformResponse(response);
		return codeState==200;
	}
	
	
	private boolean airPlayPostScrub(float value){
		  
		//seek action need for get  playback   postion;
		String response0= transferCmdWitchRead(AirplayHttphead.getPlaybackProgressHead);
		String body = getBodyFormResponse(response0);
		
		if(body == null || body.split("\n").length !=2){
			Log.e(TAG,"error get play postion ,response body = " + body);
			return false;
		}
		String[] l_str_a = body.split("\n");
		float duration = -1f;
		float postion = -1f;
		//不知道 两个参数的顺序是否会变化.应该先判断下参数名称,以后改善
		// seraphim3
		String duration_s = l_str_a[0].split(":")[1];
		String postion_s = l_str_a[1].split(":")[1];
		duration = Float.valueOf(duration_s);
		postion = Float.valueOf(postion_s);
		value += postion;
		if(value > duration){
			//seek 位置大于播放时长的时候按seek到末尾处理
			value = duration;
		}
		if(value <0){
			//小于0的时候 按0处理
			value = 0f;
		}
		String requestScrub = String.format(AirplayHttphead.formatScurbHead, value);
//		String requestScrub = String.format(AirplayHttphead.formatScurbHead, 60f);
		String response1 = transferCmdWitchRead(requestScrub);
		int codeState = getCodeStateformResponse(response1);
		return codeState==200;
	}
	
	/**
	 * 
	 * @param head
	 * @return
	 */
	private String transferCmdWitchRead(String head){
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
           Thread.sleep(500);
           while(in.available() >0){
        	   in.read(t_buff);
        	   sb.append(new String(t_buff));
           }
           response = sb.toString();
          socket.close();
       } catch (UnknownHostException e) {
    	   Log.e(TAG,"UnknownHostException :" + e.getMessage());
       } catch (IOException e) { 
    	   Log.e(TAG,"IOException : "+e.getMessage());
       } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} ;
  
		return response;
	}
	private int getCodeStateformResponse(String response){
		int codeState = -1;
		String[] t_response = response.split("\r\n\r\n");
		if(t_response.length >=1){
			String s_responseHead = t_response[0];
			UuseeResponseHttpHead  responseHead = new UuseeResponseHttpHead(s_responseHead);
			 codeState = responseHead.getCodeState();
		}else{
			Log.e(TAG,"parse sponse hewad error");
		}
		return codeState;
	}
	private String getBodyFormResponse(String response){
		String body = null;
		String[] t_response = response.split("\r\n\r\n");
		if(t_response.length ==2){
			body = t_response[1];
		}else if(t_response.length == 1){
			String s_responseHead = t_response[0];
			UuseeResponseHttpHead  responseHead = new UuseeResponseHttpHead(s_responseHead);
			Log.e(TAG,"get response body error , response code State ===" + responseHead.getCodeState());
		}else{
			Log.e(TAG,"error response format  response ==="+response);
		}
		return body;
	}
}
