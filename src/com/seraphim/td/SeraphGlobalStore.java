package com.seraphim.td;

import com.seraphim.td.remote.upnp.SeraphimUpnpControlPoint_temp;


public class SeraphGlobalStore {
	static public SeraphimUpnpControlPoint_temp mCP;
	static public SeraphimUpnpControlPoint_temp getCP(){
		return mCP;
	}
	static public void setCP(SeraphimUpnpControlPoint_temp cp){
		mCP = cp;
	}
	/**
	 * 
	 */
	static public final String BROADCASET_REVICE_NOTITY_ACTION="com.seraphim.td.broadcaset";
		static public final String BORADCASET_NOTITY_DATA_KEY="key_device_notify";

}
