package com.seraphim.td;

import com.seraphim.td.remote.upnp.SeraphimUpnpControlPoint;


public class SeraphGlobalStore {
	static public SeraphimUpnpControlPoint mCP;
	static public SeraphimUpnpControlPoint getCP(){
		return mCP;
	}
	static public void setCP(SeraphimUpnpControlPoint cp){
		mCP = cp;
	}
	/**
	 * 
	 */
	static public final String BROADCASET_REVICE_NOTITY_ACTION="com.seraphim.td.broadcaset";
		static public final String BORADCASET_NOTITY_DATA_KEY="key_device_notify";

}
