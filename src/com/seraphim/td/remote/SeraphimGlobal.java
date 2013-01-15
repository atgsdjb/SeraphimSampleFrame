package com.seraphim.td.remote;

import com.seraphim.td.remote.upnp.SeraphimUpnpControlPoint;

public class SeraphimGlobal {
	static public int GPORT = 5852;
	static public final String TAG="com.uusee.remote";
	
	static public final int MW_BEGIN_SCANNED = 201;
	static public final int MW_SCANNING = 202;
	static public final int MW_FINISH_SCANNED = 203;
	
	/**************与服务器传输数据*********/

	static public final int MW_CONNECT_OK = 204;
	static public final int MW_CONNECT_ERROR=205;
	static public final int MW_START_PROCESS_HTML = 206;
	static public final int MW_FINISH_PROCESS_HTML = 207;

	
	
	
	static public final String MSG_KEY_DEVICE="host";
	static public final String MSG_KEY_IP="ip";
	static public final String MSG_KEY_CONNECT="connect";
	
	static public final String SCAN_CMD="UUSEE_SCAN_SERVER\n";
	static public final int DPORTL = 25852;
	static public final int SPORT =  33405;
	static public final String UUSEE_EXIT_LISTEN="UUSEE_EXIT_LISTEN";
	static public final String webUrl ="http://player.uusee.com/mobile/apple/tv/tvIndex.html?";
//	static protected final String webUrl="http://www.youku.com?";
	static public final String ipadUserAgent ="Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5";
	static public String exJS =null;
	/*************UPNP GLOBAL DATA**************/
	static public SeraphimUpnpControlPoint mCP;
	static public SeraphimUpnpControlPoint getCP(){
		return mCP;
	}
	static public void setCP(SeraphimUpnpControlPoint cp){
		mCP = cp;
	}
	static public final String BROADCASET_REVICE_NOTITY_ACTION="com.seraphim.td.broadcaset";
	static public final String BORADCASET_NOTITY_DATA_KEY="key_device_notify";
	static public final int MW_DEVICE_LIST_CHANGE = 2001;
	static public final int MW_ADD_DEVICE = 2002;
		   static public final String MK_ADD_DEVICE_NAME="device_name"; 
	static public final String KEY_DEVICE_INDEX="key_device_index";
	static public final String KEY_DEVICE_UDN ="key_device_udn";
	static public final String KEY_DEVICE_LOCATION="key_device_location";
	static public final String KEY_DEVICE_NAME="key_device_name";
	static public final String KEY_SERVER_ID ="key_server_id";
	static public final String KEY_SERVER_TYPE="key_server_type";
	/********************play video Message What**************/
	static public final int MW_GET_VIDEO_OK=4001;
	static public final int MW_GET_VIDEO_ERROR=4002;
	static public final int MW_BEGIN_PLAY=4003;
	static public final int MW_STOP_PLAY=4004;
	static public final int MW_PUSH_PLAY=4005;
	static public final int MW_SEEK_PLAY=4006;
		static public final String KEY_VIDEO_URL="key_video_url";
		static public final String KEY_VIDEO_URL_ERROR="key_video_url_error";
}
