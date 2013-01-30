package com.seraphim.td.remote.imp;

import java.util.HashMap;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;

import com.seraphim.td.remote.imp.UuseeRemoteWarp;
import com.seraphim.td.remote.imp.UuseeUpnpControlPoint;



public class DlanDevice extends AbstractDevice{
	private String uuid;
	private String udn;
	private Device mDevice = null;
	private UuseeUpnpControlPoint mCP;
	private Service mService;
	public DlanDevice(UuseeRemoteWarp root,String _name,String _uuid,String _udn) {
		super(root,_name);
		setType(DeviceType.Upnp);
		this.uuid = _uuid;
		this.udn = _udn;
		mCP = root.getControlPoint();
		
	}

	@Override
	public boolean play(String url, float postion) {
		// TODO Auto-generated method stub
		int result = -1;
		if(mService != null){
			HashMap<String,String> arg0 = new HashMap<String,String>();
			arg0.put("CurrentURI",url);
			result = mCP.sendAction2(mService, "SetAVTransportURI", arg0);
		}
		return result!=-1;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		int code = -1;
		if(mService!=null){
			
			HashMap<String,String> arg = new HashMap<String,String>();
			 code =  mCP.sendAction2(mService, "Stop", arg);	
		}
		return code != -1;
	}

	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		int code = -1;
	if(mService!=null){
		HashMap<String,String> arg = new HashMap<String,String>();
		code = mCP.sendAction2(mService, "Pause", arg);
		}
		return code != -1;
	}

	@Override
	public boolean resume() {
		// TODO Auto-generated method stub
	int code  = -1;
	if(mService!=null){
		HashMap<String,String> arg = new HashMap<String,String>();
		arg.put("Speed", "1");
		code = mCP.sendAction2(mService, "Play", arg);	
		}
		return code!= -1;
	}

	@Override
	public boolean seek(SeekType type, float postion) {
		// TODO Auto-generated method stub
		int code = -1;
		if(mService != null){
			HashMap<String,String> arg = new HashMap<String,String>();
			arg.put("Unit", "ABS_COUNT");
			arg.put("Target", "100");
			code = mCP.sendAction2(mService, "Stop", arg);
		}
		return code != -1;
	}
	protected Service  getAVTransportService(){
		mDevice = mCP.getDeviceOfudn(this.udn);
		if(mDevice != null){
			mService = mCP.getServiceOfType(mDevice, "urn:schemas-upnp-org:service:AVTransport:1");
		}
		
		return mService;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof DlanDevice ){
			DlanDevice d = (DlanDevice )o;
			if(d.name.equals(this.name)&& d.udn.equals(this.udn)){
				return true;
			}
			
		}
			return false;
		
	}
	

}
