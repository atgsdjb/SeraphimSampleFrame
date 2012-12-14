package com.seraphim.td.upnp;

public class GlobalStore {
	static public SeraphUpnpControlPoint mCP;
	static public SeraphUpnpControlPoint getCP(){
		return mCP;
	}
	static public void setCP(SeraphUpnpControlPoint cp){
		mCP = cp;
	}

}
