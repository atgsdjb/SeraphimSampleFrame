package com.seraphim.td.remote.airplay;


public class AirplayHttphead {
	static public final String getServerInfo="GET /server-info HTTP/1.1\r\n"+
			 "X-Apple-Device-ID: 0xdc2b61a0ce79\r\n"+
			 "Content-Length: 0\r\n"+
			 "User-Agent: MediaControl/1.0\r\n\r\n"+
			 "X-Apple-Session-ID: 1bd6ceeb-fffd-456c-a09c-996053a7a08c\r\n";	
	/**
	 * 
	 */
	static public final String formatPlayHead="POST /play HTTP/1.1"+
										"User-Agent: iTunes/10.6 (Macintosh; Intel Mac OS X 10.7.3) AppleWebKit/535.18.5\r\n"+
										"Content-Length: %d\r\n"+
										"Content-Type: text/parameters\r\n\r\n"+
										"%s";
	static public final String formatPlayBody="Content-Location: %s?database-spec='dmap.persistentid:0x63b5e5c0c201542e'&item-spec='dmap.itemid:0x21d'\r\n"+
										"Start-Position: %f\r\n";
	/**
	 * 									 
	 */
	static public final String formatRateHead= "POST /rate?value=%f HTTP/1.1\r\n"+
                                             "User-Agent: iTunes/10.6 (Macintosh; Intel Mac OS X 10.7.3) AppleWebKit/535.18.5\r\n"+
											 "Content-Length: 0\r\n\r\n";
	/**
	 * 
	 */
	static public final String StopHead="POST /stop HTTP/1.1\r\n"+
									   "User-Agent: iTunes/10.6 (Macintosh; Intel Mac OS X 10.7.3) AppleWebKit/535.18.5\r\n"+
			                           "Content-Length: 0\r\n\r\n";
	/**
	 * 
	 */
	static public final String formatScurbHead2="POST /scrub?position=%f HTTP/1.1\r\n"+  // 用这个User-Agent 取回的postion 和 duration  都为0
										      "User-Agent: iTunes/10.6 (Macintosh; Intel Mac OS X 10.7.3) AppleWebKit/535.18.5\r\n"+
										      "Content-Length: 0\r\n\r\n";
	static public final String formatScurbHead="POST /scrub?position=%f HTTP/1.1\r\n"+ //抓IPAD取得的数据
		      								  "User-Agent: MediaControl/1.0 X-Apple-Session-ID=87de90a7-6fc2-40c5-bdb1-f7d1172d46b5\r\n"+
		      								  "Content-Length: 0\r\n\r\n";
	/**
	 * 
	 */
	static public final String getPlaybackProgressHead2="GET /scrub HTTP/1.1\r\n"+
                                                             "User-Agent: iTunes/10.6 (Macintosh; Intel Mac OS X 10.7.3) AppleWebKit/535.18.5\r\n"+
                                                             "Content-Length: 0\r\n\r\n";
	static public final String getPlaybackProgressHead="GET /scrub HTTP/1.1\r\n"+  
                                                             "User-Agent: MediaControl/1.0 X-Apple-Session-ID=87de90a7-6fc2-40c5-bdb1-f7d1172d46b5\r\n"+
                                                             "Content-Length: 0\r\n\r\n";
	/**
	 * 
	 * @param local
	 * @param postion
	 * @return
	 */
	
	static public final String wiplugPauseVideo="GET /pause wiPlug 1.0\r\n"+
										   "X-WiPlug-Type : video\r\n"+
										   "X-WiPlug-Adapter: wiplug\r\n"+
										   "Content-Length: 0\r\n\r\n";
	
	
	static public final String wiplugResumeVideo="GET /resume wiPlug 1.0\r\n"+
			   								"X-WiPlug-Type : video\r\n"+
			   								"X-WiPlug-Adapter: wiplug\r\n"+
			   								"Content-Length: 0\r\n\r\n";
	
	static public final String wiplugStopVideo="GET /resume wiPlug 1.0\r\n"+
				                            "X-WiPlug-Type : video\r\n"+
				                            "X-WiPlug-Adapter: wiplug\r\n"+
				                            "Content-Length: 0\r\n\r\n";

	static public String getPlayHead(String local,float postion){
		String body = String.format(formatPlayBody,local,postion);
		return String.format(formatPlayHead, body.length(),body);
	}
}
