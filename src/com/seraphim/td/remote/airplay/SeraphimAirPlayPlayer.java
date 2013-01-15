package com.seraphim.td.remote.airplay;

import java.net.InetAddress;
//import java.util.LinkedList;
import java.util.List;

import com.seraphim.td.R;
import com.seraphim.td.remote.airplay.mdns.DNSAnswer;
import com.seraphim.td.remote.airplay.mdns.DNSMessage;
import com.seraphim.td.remote.airplay.mdns.DNSSRV;
import com.seraphim.td.remote.airplay.mdns.DNSComponent.DNSType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
/*
 * get head
 */


public class SeraphimAirPlayPlayer extends Activity {

	private static final String TAG="com.seraphim.remote.airplay.player";
	
	
	
	
	private int port = -1;
	private InetAddress addr;
	private DNSSRV srv;
	private AirplayHttpWarp httpWarp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_play_player);
		initData();
		httpWarp = new AirplayHttpWarp(addr, port);
		Log.d(TAG,"ADDR==="+addr.toString());
		Log.d(TAG,"PORT==="+port);
		Log.d(TAG,"SRV==="+srv.toString());
	}

	public void onClick(View view){
		int id = view.getId();
		switch(id){
		case R.id.play:
			httpWarp.AirplayCRLPlay("http://player.uusee.com/mobile/apple/ipad2/love2.mp4",0f);
			break;
		case R.id.pause:
			if(addr != null && port != -1){
				httpWarp.getAirplayServerInfo();
			}
			break;
		default :
			break;
		}
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent =getIntent();
		if(intent != null){
			Bundle data = intent.getExtras();
			Object object = data.getSerializable(APGlobal.AIR_DESCRIPTION);
			if(object!=null){
				Packet packet = (Packet)object;
				DNSMessage dnsmsg = packet.getMsg();
				if(dnsmsg!=null){
					List<DNSAnswer> l_list = dnsmsg.getAnswerList();
					for(DNSAnswer answer : l_list){
						if(answer == null){
							continue ;///  严重的bug无论如何要FIX
						}
						if(answer.getType().equals(DNSType.SRV)){
							DNSSRV srv =  ((DNSSRV)answer);
							addr =packet.getSrc();
							port = srv.getPort();
							String protocol = srv.getProtocol();
							if(protocol.contains("airplay")){
								this.srv = srv;
								break;
							}
						}else{
							continue;
						}
					}
				}else{
					/****NOT FIND DNSMSG************/
					Log.e(TAG,"NOT FIND DNSMSG");
				}
				
				
			}else{
				/*******NOT FIND PARCELABLE********/
				Log.e(TAG,"NOT FIND PARCELABLE");
			}
		}
		
	}
	

}
