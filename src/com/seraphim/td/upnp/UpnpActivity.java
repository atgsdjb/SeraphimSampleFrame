package com.seraphim.td.upnp;

import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.Device;

import com.seraphim.td.R;
import com.seraphim.td.adapter.SeraphListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class UpnpActivity extends Activity {
	static public final String TAG="com.seraphim.td";
	/**
	 * MESSAG_WHAT_CONST
	 */
	static public final int MW_DEVICE_LIST_CHANGE = 2001;
	static public final int MW_ADD_DEVICE = 2002;
		   static public final String MK_ADD_DEVICE_NAME="device_name"; 
	static public final String KEY_DEVICE_INDEX="key_device_index";
	static public final String KEY_DEVICE_UDN ="key_device_udn";
	static public final String KEY_DEVICE_LOCATION="key_device_location";
	static public final String KEY_DEVICE_NAME="key_device_name";
	/**
	 * FUCTHON   COMPONENT
	 */
	private Handler handler=new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch(what){
			case	MW_DEVICE_LIST_CHANGE:
				break;
			case MW_ADD_DEVICE :
				String name = msg.getData().getString(MK_ADD_DEVICE_NAME);
				if(name == null)
					Log.e(TAG,"NULL -----NAME -----getData()");
				deviceList.add(name);
				mAdapter.notifyDataSetInvalidated();
				mListView.invalidate();
			default :
				break;
			}
		}
		
	};
	
	private ListView mListView;
	private SeraphListAdapter<String> mAdapter;
	private SeraphUpnpControlPoint mCP;
	/*********************		DATA ELEMENT			***************/
	List<String> 	deviceList = new ArrayList<String>();
	private OnItemClickListener listener = new OnItemClickListener() {

		

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(UpnpActivity.this,DeviceActivity.class);
			Device device = mCP.getDeviceOfIndex(position);
			String name = device.getFriendlyName();
			String udn = device.getUDN();
			String location = device.getLocation();
			intent.putExtra(KEY_DEVICE_LOCATION, location);
			intent.putExtra(KEY_DEVICE_UDN, udn);
			intent.putExtra(KEY_DEVICE_NAME, name);
			startActivity(intent);
			
		}
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upnp_main);
		mCP = SeraphUpnpControlPoint.getInstance(handler);
		GlobalStore.setCP(mCP);
		mListView = (ListView) findViewById(R.id.list);
		mListView.setOnItemClickListener(listener);
		mAdapter =  new SeraphListAdapter<String>(this,deviceList );
		mListView.setAdapter(mAdapter);
		mCP.start();
		
	}
	
	public void click(View view){
		int id = view.getId();
		switch(id){
		case R.id.search:
			mCP.search();
			break;
		case R.id.back:
			finish();
			break;
		default :
			break;
		}
	}
}
