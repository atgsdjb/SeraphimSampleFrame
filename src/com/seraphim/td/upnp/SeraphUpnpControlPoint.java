package com.seraphim.td.upnp;

import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.xml.parser.XmlPullParser;

import com.seraphim.td.SeraphGlobalStore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.BulletSpan;
import android.util.Log;

public class SeraphUpnpControlPoint implements NotifyListener,
DeviceChangeListener,
SearchResponseListener,
EventListener
{
	static private final String  TAG="com.seraphim.td.upnp";
	static private SeraphUpnpControlPoint sulf;
	private ControlPoint mCP;
	private List<Device> deviceList;
	private XmlPullParser xmlParser;
	private Context mContext;
	private Handler handler;
	private 
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
	private SeraphUpnpControlPoint(Context context,Handler handler){
		
		this.handler = handler;
		this.mContext = context;
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
	static public SeraphUpnpControlPoint getInstance(Context context,Handler handler){
		if(sulf == null){
			sulf = new SeraphUpnpControlPoint(context, handler);
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
	
	public Device getDeviceOfudn(String udn){
		Device device = null;
		for(Device d : deviceList){
			if(d.getUDN().equals(udn)){
				device = d;
				break;
			}
				
		}
		return device;
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
	}
	@Override
	public void deviceAdded(Device dev) {
		// TODO Auto-generated method stub
		deviceList.add(dev);
		Message msg = new Message();
		msg.what =  UpnpActivity.MW_ADD_DEVICE;
		Bundle data = new Bundle();
		String name = dev.getFriendlyName();
		if(name == null)
			name ="UNKNOWN  DEVICE";
		data.putString(UpnpActivity.MK_ADD_DEVICE_NAME, name);
		msg.setData(data);
		handler.sendMessage(msg);
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
		
		deviceList.remove(dev);
	}
	/**
	 * 
	 * @param ssdpPacket
	 */
	@Override
	public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(SeraphGlobalStore.BROADCASET_REVICE_NOTITY_ACTION);
		intent.putExtra(SeraphGlobalStore.BORADCASET_NOTITY_DATA_KEY, 
						ssdpPacket.getData());
		mContext.sendBroadcast(intent);
	}
	@Override
	public void eventNotifyReceived(String uuid, long seq, String varName,
			String value) {
		// TODO Auto-generated method stub
		Log.d(TAG,"eventNotifyReceived");
		Log.e(TAG,"uuid==="+uuid+"\t varName"+varName);
	}
	
	
}
