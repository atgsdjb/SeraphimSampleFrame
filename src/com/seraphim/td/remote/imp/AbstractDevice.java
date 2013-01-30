package com.seraphim.td.remote.imp;

import com.seraphim.td.remote.imp.UuseeRemoteWarp;

public abstract  class AbstractDevice implements AbstractRemoteControl {
	protected String name;
	protected DeviceType type;
	public AbstractDevice(UuseeRemoteWarp root,String _name){
		name = _name;
		
	}
	protected void setType(DeviceType _type){
		this.type = _type;
	}
	enum DeviceType{
		Upnp,AirPlay,Wiplug
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "type="+type+"\tName="+name;
	}
	/**
	 * 
	 * @return
	 */
	public DeviceType  getType(){
		return this.type;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isUpnp(){
		boolean b =false;
		if(getType() == DeviceType.Upnp){
			b = true;
		}
		return b;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isAirPlay(){
		boolean b =false;
		if(getType() == DeviceType.AirPlay){
			b = true;
		}
		return b;
	}
	
}
