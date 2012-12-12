package com.seraphim.td.upnp;

import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.xml.parser.XmlPullParser;

import android.os.Handler;
import android.util.Log;

public class UpnpControlPoint implements NotifyListener,
DeviceChangeListener,
SearchResponseListener,
EventListener
{
	static private final String  TAG="com.seraphim.td.upnp";
	static private UpnpControlPoint sulf;
	private ControlPoint mCP;
	private List<Device> deviceList;
	private XmlPullParser xmlParser;
	private Handler handler;
	/**
	 *自定义内部状态枚举 
	 */
	enum STATE{
		INITED,SEARCHING,IDLESSE,FINISH
	}
	private STATE state;
	
	/******************************            CONSTRUCTOR               ****************************************************/
	
	/**
	 * 
	 */
	private UpnpControlPoint(Handler handler){
		
		this.handler = handler;
		deviceList = new ArrayList<Device>();
		mCP = new ControlPoint();
		mCP.addDeviceChangeListener(this);
		mCP.addEventListener(this);
		mCP.addNotifyListener(this);
		mCP.addSearchResponseListener(this);
		setState(STATE.INITED);
		
		
	}
	/**
	 * 
	 * @return 
	 */
	static public UpnpControlPoint getInstance(Handler handler){
		if(sulf == null){
			sulf = new UpnpControlPoint(handler);
		}
		return sulf;
		
	}
	/******************************            INTERFACE                   ****************************************************/
	public void start(){
		mCP.start();
		setState(STATE.IDLESSE);
	}
	public void stop(){
		mCP.stop();
		setState(STATE.FINISH);
		
	}
	
	/**search device
	 * 
	 * @param target
	 */
	public void search(){
		mCP.search();
		
	}
	public void search(String target){
		mCP.search(target);
	}
	public void search(String target,int mx){
		mCP.search(target, mx);
	}
	public List<Device> getDevices(){
		return deviceList;
	}
	public Device getDeviceOfIndex(int index){
		return deviceList.get(index);
	}
	/******************************            TOOL_METHOD               ****************************************************/
	void setState(STATE _state){
		this.state = _state;
	}
	/******************************             LISTENER                 ****************************************************/
	/**
	 * 
	 * @param ssdpPacket
	 */
	@Override
	public void deviceSearchResponseReceived(SSDPPacket ssdpPacket) {
		// TODO Auto-generated method stub
		Log.d(TAG,"deviceSearchResponseReceived");
		Log.e(TAG,new String(ssdpPacket.getData()));
		handler.sendEmptyMessage(UpnpActivity.MW_DEVICE_LIST_CHANGE);
		
	}
	@Override
	public void deviceAdded(Device dev) {
		// TODO Auto-generated method stub
		deviceList.add(dev);
		handler.sendEmptyMessage(UpnpActivity.MW_DEVICE_LIST_CHANGE);
		Log.d(TAG,"deviceAdded");
	}
	/**
	 * 
	 * @param dev
	 */
	@Override
	public void deviceRemoved(Device dev) {
		// TODO Auto-generated method stub
		Log.d(TAG,"deviceRemoved");
		handler.sendEmptyMessage(UpnpActivity.MW_DEVICE_LIST_CHANGE);
		deviceList.remove(deviceList.indexOf(dev));
	}
	/**
	 * 
	 * @param ssdpPacket
	 */
	@Override
	public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
		// TODO Auto-generated method stub
		Log.d(TAG,"deviceNotifyReceived");
		Log.e(TAG,new String(ssdpPacket.getData()));
	}
	@Override
	public void eventNotifyReceived(String uuid, long seq, String varName,
			String value) {
		// TODO Auto-generated method stub
		Log.d(TAG,"eventNotifyReceived");
		Log.e(TAG,"uuid==="+uuid+"\t varName"+varName);
	}
	
	
}
