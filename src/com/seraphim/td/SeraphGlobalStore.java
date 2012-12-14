package com.seraphim.td;

import com.seraphim.td.upnp.SeraphUpnpControlPoint;

public class SeraphGlobalStore {
	static public SeraphUpnpControlPoint mCP;
	static public SeraphUpnpControlPoint getCP(){
		return mCP;
	}
	static public void setCP(SeraphUpnpControlPoint cp){
		mCP = cp;
	}
	/**
	 * 
	 */
	static public final String BROADCASET_REVICE_NOTITY_ACTION="com.seraphim.td.broadcaset";
		static public final String BORADCASET_NOTITY_DATA_KEY="key_device_notify";

}
