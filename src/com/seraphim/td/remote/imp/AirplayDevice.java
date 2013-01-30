package com.seraphim.td.remote.imp;

import java.net.InetAddress;

import com.seraphim.td.remote.airplay.AirplayHttpWarp;
import com.seraphim.td.remote.airplay.mode.AirPlayEvent;
import com.seraphim.td.remote.imp.UuseeRemoteWarp;



public class AirplayDevice extends AbstractDevice {
	protected AirplayHttpWarp httpWarp;
	protected InetAddress addr;
	private short port;
	public AirplayDevice(UuseeRemoteWarp root,String _name,InetAddress addr ,short port) {
		super(root, _name);
		setType(DeviceType.AirPlay);
		this.addr = addr;
		this.port = port;
		httpWarp = new AirplayHttpWarp(addr, port);
	}
	public AirplayDevice(UuseeRemoteWarp root,String _name,DeviceType _type,InetAddress addr ,short port) {
		super(root, _name);
		setType(_type);
		this.addr = addr;
		this.port = port;
		httpWarp = new AirplayHttpWarp(addr, port);
	}

	@Override
	public boolean play(String url, float postion) {
		// TODO Auto-generated method stub
		
		return httpWarp.airplayCRLPlay(url,postion);
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return httpWarp.airPlayCRLStop();
	}

	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		return httpWarp.airPlayCRLPause();
	}

	@Override
	public boolean resume() {
		// TODO Auto-generated method stub
		return httpWarp.airPlayCRLResume();
	}

	@Override
	public boolean seek(SeekType type, float step) {
		// TODO Auto-generated method stub
		boolean result = false;
		switch(type){
		case SPEED:
			result = httpWarp.airPlayCRLSpeed(step);
			break;
		case REWIND:
			result = httpWarp.airPlayCRLRewind(step);
			break;
		default:
			break;
		
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof AirplayDevice){
			AirplayDevice d = (AirplayDevice)o;
			if(d.name.equals(this.name) && d.addr.equals(this.addr)&& d.port == this.port)
				return true;
		}
		return false;
	}
	

}
