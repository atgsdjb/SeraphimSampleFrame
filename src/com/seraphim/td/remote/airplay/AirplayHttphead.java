package com.seraphim.td.remote.airplay;

import android.util.StringBuilderPrinter;

public class AirplayHttphead {
	static public final String getServerInfo="GET /server-info HTTP/1.1\r\n"+
			 "X-Apple-Device-ID: 0xdc2b61a0ce79\r\n"+
			 "Content-Length: 0\r\n"+
			 "User-Agent: MediaControl/1.0\r\n\r\n"+
			 "X-Apple-Session-ID: 1bd6ceeb-fffd-456c-a09c-996053a7a08c\r\n";	
	static public final String formatPlayHead="POST /play HTTP/1.1"+
										"User-Agent: iTunes/10.6 (Macintosh; Intel Mac OS X 10.7.3) AppleWebKit/535.18.5\r\n"+
										"Content-Length: %d\r\n"+
										"Content-Type: text/parameters\r\n\r\n"+
										"%s";
	static public final String formatPlayBody="Content-Location: %s?database-spec='dmap.persistentid:0x63b5e5c0c201542e'&item-spec='dmap.itemid:0x21d'\r\n"+
											 "Start-Position: %f\r\n";
	static public String getPlayHead(String local,float postion){
		String body = String.format(formatPlayBody,local,postion);
		return String.format(formatPlayHead, body.length(),body);
	}
}
