package com.seraphim.td.remote.imp;

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


import android.util.Log;

public class UuseeUpnpControlPoint implements NotifyListener,
DeviceChangeListener,
SearchResponseListener,
EventListener
{
	static private final String  TAG="com.seraphim.td.upnp";
	static private UuseeUpnpControlPoint sulf;
	private ControlPoint mCP;
	private List<Device> deviceList;
	private XmlPullParser xmlParser;
	UuseeAddListener listener;
	UuseeRemoteWarp root;
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
	private UuseeUpnpControlPoint(UuseeRemoteWarp root,UuseeAddListener listener){
		
		
		deviceList = new ArrayList<Device>();
		mCP = new ControlPoint();
		mCP.addDeviceChangeListener(this);
		mCP.addEventListener(this);
		mCP.addNotifyListener(this);
		mCP.addSearchResponseListener(this);
		this.root = root;
		this.listener = listener;
		setState(STATE.INITED);
		
		
	}
	/**
	 * 
	 * @return 
	 */
	static public UuseeUpnpControlPoint getInstance(UuseeRemoteWarp root,UuseeAddListener listener){
		if(sulf == null){
			sulf = new UuseeUpnpControlPoint(root,listener);
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
		String name =dev.getFriendlyName();
		String uuid = dev.getUUID();
		String udn = dev.getUDN();
		DlanDevice d = new DlanDevice(root,name,uuid,udn);
		if(root.addDevice(d)){
			listener.onAddDevice(d);
		}
	}
	/**
	 * 
	 * @param dev
	 */
	@Override
	public void deviceRemoved(Device dev) {
		// TODO Auto-generated method stub
		Log.d(TAG,"deviceRemoved");
		
		
		deviceList.remove(dev);
	}
	/**
	 * 
	 * @param ssdpPacket
	 */
	@Override
	public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
		// TODO Auto-generated method stub

			String s = ssdpPacket.getHost();
			String a = ssdpPacket.getLocalAddress();
			String b = ssdpPacket.getMAN();
			Log.d(TAG,s+a+b);

	}
	@Override
	public void eventNotifyReceived(String uuid, long seq, String varName,
			String value) {
		// TODO Auto-generated method stub
		Log.e(TAG,"eventNotifyReceived: uuid="+uuid+"\t seq="+seq+"\tvarName"+varName);
	}
	
	
}
