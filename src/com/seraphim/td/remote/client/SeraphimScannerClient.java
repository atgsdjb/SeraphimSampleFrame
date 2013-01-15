package com.seraphim.td.remote.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.seraphim.td.remote.SeraphimGlobal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class SeraphimScannerClient {
	
	private final String  TAG = "com.uusee.remote.client";
	

	private Handler handler;
	private boolean WORK_RUNNING_FLG =false;
	private class WorkThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			workTask();
		}
		
	}
	private WorkThread workThread;
	 private void workTask(){

		// TODO Auto-generated method stub
		DatagramSocket mDatagramSocket = null;
		ServerSocket lServer = null;
		BufferedReader reader = null;
		try{
			byte[] data = SeraphimGlobal.SCAN_CMD.getBytes();
			InetSocketAddress addr = new InetSocketAddress("255.255.255.255",SeraphimGlobal.DPORTL);
			mDatagramSocket = new DatagramSocket();
			DatagramPacket pack = new DatagramPacket(data, data.length, addr);
			mDatagramSocket.send(pack);
			handler.sendEmptyMessage(SeraphimGlobal.MW_BEGIN_SCANNED);
			lServer = new ServerSocket(SeraphimGlobal.SPORT);
			while(true){
				Socket lSocket = lServer.accept();
				reader = new BufferedReader(new InputStreamReader(lSocket.getInputStream()));
				String json = reader.readLine();
				JSONObject jobj = new JSONObject(json);
				String hostName = jobj.getString("host");
				if(hostName.equalsIgnoreCase(SeraphimGlobal.UUSEE_EXIT_LISTEN)){
					handler.sendEmptyMessage(SeraphimGlobal.MW_FINISH_SCANNED);
					break;
				}
				Message msg = new Message();
				msg.what = SeraphimGlobal.MW_SCANNING;
				Bundle mBundle = new Bundle();
				mBundle.putString(SeraphimGlobal.MSG_KEY_DEVICE, jobj.getString("host"));
				String ip = lSocket.getInetAddress().getHostAddress();
				mBundle.putString(SeraphimGlobal.MSG_KEY_IP, ip);
				mBundle.putBoolean(SeraphimGlobal.MSG_KEY_CONNECT, jobj.getBoolean("connect"));
				msg.setData(mBundle);
				handler.sendMessage(msg);
				reader.close();
				lSocket.close();
			}
			handler.sendEmptyMessage(SeraphimGlobal.MW_FINISH_SCANNED);
		}catch (IOException e) {
			// TODO: handle exception
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(mDatagramSocket!=null && !mDatagramSocket.isClosed())
					mDatagramSocket.close();
				if(lServer!=null && !lServer.isClosed())
					lServer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	}
	public SeraphimScannerClient(Handler handler) {
		this.handler = handler;
	}
	
	public void startScanner(){
		try{
			if(workThread!=null){
				
					new Thread(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							sendStopToThread();
						}
						
					}.start();
					workThread.join();
	
			}
		}catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG,e.getMessage());
		}
		workThread = new WorkThread();
		workThread.start();
	}

	public void stopScanner(){
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				sendStopToThread();
			}
			
		}.start();
	}
	/**
	 * 发送到本地监听端口结束监听命令
	 */
	private void sendStopToThread(){

		Socket socket = new Socket();
		InetSocketAddress addr = new InetSocketAddress("127.0.0.1", SeraphimGlobal.SPORT);
		OutputStream out ;
		try{
		socket.connect(addr);
		out = socket.getOutputStream();
		JSONObject obj = new JSONObject();
		obj.put("host",SeraphimGlobal.UUSEE_EXIT_LISTEN);
		out.write(obj.toString().getBytes());
		out.flush();
		out.close();
		socket.close();
		}catch (IOException e) {
			// TODO: handle exception
			Log.e(TAG,"e:=="+e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"e:=="+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	synchronized	public boolean isWORK_RUNNING_FLG() {
		return WORK_RUNNING_FLG;
	}
	synchronized public void setWORK_RUNNING_FLG(boolean wORK_RUNNING_FLG) {
		WORK_RUNNING_FLG = wORK_RUNNING_FLG;
	}
	
	
}

//}
