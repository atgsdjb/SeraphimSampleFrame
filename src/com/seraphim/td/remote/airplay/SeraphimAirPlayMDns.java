package com.seraphim.td.remote.airplay;

import java.util.ArrayList;

import com.seraphim.td.R;
import com.seraphim.td.remote.client.tools.SeraphListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SeraphimAirPlayMDns extends Activity {
	//Component
	private ListView mList;
	private SeraphListAdapter<Packet> mAdapter;
	
	
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch(what){
			case APGlobal.AIR_ADD_PKG:
				Packet packet = (Packet) msg.getData().get(APGlobal.AIR_PKG_KEY);
				mAdapter.addData(packet);
				mAdapter.notifyDataSetChanged();
				mList.invalidate();
				mList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Packet packet = (Packet) mAdapter.getItem(position);
						Bundle data = new Bundle();
						data.putSerializable(APGlobal.AIR_DESCRIPTION, packet);
						Intent intent = new Intent(SeraphimAirPlayMDns.this,SeraphimAirPlayPlayer.class);
						intent.putExtras(data);
						startActivity(intent);
						
					}
				});
				break;
			}
		}
		
		
	};
	private SeraphimMdnsThread   mNetThread = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_play_mdns);
		mNetThread = new SeraphimMdnsThread(mHandler,this);
		mList =(ListView)findViewById(R.id.list);
		mAdapter = new SeraphListAdapter<Packet>(this, new ArrayList<Packet>());
		mList.setAdapter(mAdapter);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(mNetThread.isAlive()){
			mNetThread.submitQuit();
		}
		mNetThread.start();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mNetThread.submitQuit();
	}
	
	
	
}
