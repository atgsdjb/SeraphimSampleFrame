package com.seraphim.td.remote.upnp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.xml.parser.XmlPullParser;

import com.seraphim.td.remote.SeraphimGlobal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SeraphimUpnpControlPoint_temp implements NotifyListener,
DeviceChangeListener,
SearchResponseListener,
EventListener
{
	static private final String  TAG="com.seraphim.td.upnp";
	static private SeraphimUpnpControlPoint_temp sulf;
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
	private SeraphimUpnpControlPoint_temp(Context context,Handler handler){
		
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
	static public SeraphimUpnpControlPoint_temp getInstance(Context context,Handler handler){
		if(sulf == null){
			sulf = new SeraphimUpnpControlPoint_temp(context, handler);
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
	public Service getServiceOfType(Device device,String type){
		Service result = null;
		ServiceList list = device.getServiceList();
		for(int i =0;i<list.size();i++){
			Service s = (Service) list.get(i);
			if(s.getServiceType().equals(type)){
				result = s;
				break;
			}else{
				continue;
			}
		}
		return result;
	}
	public boolean  subScribe(Service service){
		boolean result = false;
		result = mCP.subscribe(service);
		return result;
	}
	public int  sendAction2(Service service,String s_action,Map<String,String> m_argumentList){
		int result = -1;
		Action  action = service.getAction(s_action);
		if(action !=null){
			ArgumentList alist = action.getArgumentList();
			for(int i = 0;i<alist.size();i++){
				Argument ag =  (Argument) alist.get(i);
				Log.e("com.seraphim.td","ARGUMENT===="+ag.getName());
				
			}
			Set<String> vlueNameSet = m_argumentList.keySet();
			for(String vlueName : vlueNameSet){
				Argument ag = action.getArgument(vlueName);//.
				if(ag != null){
					ag.setValue(m_argumentList.get(vlueName));
				}else{
					Log.e(TAG,"MISS ARGUMENT "+vlueName);
				}
			}
			result =action.postControlAction2();
		}else
		{
			Log.e(TAG,"service haven't  "+s_action+" action");
		}
		return result;
	}
	public boolean  sendAction(Service service,String s_action,Map<String,String> m_argumentList){
		boolean result = false;
		Action  action = service.getAction(s_action);
		if(action !=null){
			ArgumentList alist = action.getArgumentList();
			for(int i = 0;i<alist.size();i++){
				Argument ag =  (Argument) alist.get(i);
				Log.e("com.seraphim.td","ARGUMENT===="+ag.getName());
				
			}
			Set<String> vlueNameSet = m_argumentList.keySet();
			for(String vlueName : vlueNameSet){
				Argument ag = action.getArgument(vlueName);//.
				if(ag != null){
					ag.setValue(m_argumentList.get(vlueName));
				}else{
					Log.e(TAG,"MISS ARGUMENT "+vlueName);
				}
			}
			result = action.postControlAction();
		}else
		{
			Log.e(TAG,"service haven't  "+s_action+" action");
		}
		return result;
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
		msg.what =  SeraphimGlobal.MW_ADD_DEVICE;
		Bundle data = new Bundle();
		String name = dev.getFriendlyName();
		if(name == null)
			name ="UNKNOWN  DEVICE";
		data.putString(SeraphimGlobal.MK_ADD_DEVICE_NAME, name);
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
		handler.sendEmptyMessage(SeraphimGlobal.MW_DEVICE_LIST_CHANGE);
		
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
		String msg = new String(ssdpPacket.getData());
		intent.setAction(SeraphimGlobal.BROADCASET_REVICE_NOTITY_ACTION);
		intent.putExtra(SeraphimGlobal.BORADCASET_NOTITY_DATA_KEY, 
						msg);
		mContext.sendBroadcast(intent);
	}
	@Override
	public void eventNotifyReceived(String uuid, long seq, String varName,
			String value) {
		// TODO Auto-generated method stub
		Log.e(TAG,"eventNotifyReceived: uuid="+uuid+"\t seq="+seq+"\tvarName"+varName);
	}
	
	
}
