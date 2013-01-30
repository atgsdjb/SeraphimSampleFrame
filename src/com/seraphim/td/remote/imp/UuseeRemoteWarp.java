package com.seraphim.td.remote.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.seraphim.td.remote.imp.AbstractRemoteControl.SeekType;


public class UuseeRemoteWarp {
	AbstractDevice device = null;
	UuseeAddListener listener;
	private UuseeMdnsThread mdnsThread;
	private UuseeUpnpControlPoint mCP;
	private List<AbstractDevice> mSet;
	
	public UuseeRemoteWarp(Context context,UuseeAddListener listener) {
		super();
		if(listener == null)
			throw new  IllegalArgumentException(); 
		mCP = UuseeUpnpControlPoint.getInstance(this,listener);
		mCP.start();
		mdnsThread =  new UuseeMdnsThread(context, this, listener);
		mSet = new ArrayList<AbstractDevice>();
		this.listener = listener;
	}
	public boolean play(String url){
		if(device == null){
			return false;
		}
		return device.play(url, 0f);
	}
	/**
	 * 
	 * @return
	 */
	public boolean stop(){
		if(device == null){
			return false;
		}
		return device.stop();
	}
	/**
	 * 
	 * @return
	 */
	public boolean resume(){
		if(device == null){
			return false;
		}
		return device.resume();
	}
	/**
	 * 
	 * @param postion
	 * @return
	 */
	public boolean seek(float postion){
		boolean result = false;
		if(device == null){
			return false;
		}
		if(postion >0){
			result = device.seek(SeekType.SPEED, postion);
		}else{
			result = device.seek(SeekType.REWIND, -postion);
		}
		return result;
	}
	/**
	 * 
	 * @param device
	 * @return
	 */
	public boolean bindDevice(AbstractDevice device){
		boolean b = false;
		this.device = device;
		if(device.isUpnp() ){
			DlanDevice d = (DlanDevice)device;
			if(d.getAVTransportService() != null){
				b = true;
			}
				
		}
		return b;
	}
	/**
	 * 
	 */
	public void startScan(){
		mCP.search();
		if(mdnsThread.isAlive()){
			mdnsThread.submitQuit();
		}
		mdnsThread.start();
		mdnsThread.submitQueryWigplug();
		
	}
	/**
	 * 
	 */
	public void stopScan(){
		mdnsThread.submitQuit();
	}
	public UuseeMdnsThread getMdnsThread(){
		return mdnsThread;
	}
	public UuseeUpnpControlPoint getControlPoint (){
		return mCP;
	}
	protected boolean addDevice(AbstractDevice device){
		if(!mSet.contains(device)){
			mSet.add(device);
			return true;
		}
		return false;
	}
	/*protected */public void sarchWigplug(){
		
	}
}
