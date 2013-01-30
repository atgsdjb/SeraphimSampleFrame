package com.seraphim.td.remote.upnp;

import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.Device;

import com.seraphim.td.R;
import com.seraphim.td.remote.SeraphimGlobal;
import com.seraphim.td.remote.client.tools.SeraphListAdapter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SeraphimUpnpActivity extends Activity {
	static public final String TAG="com.seraphim.td";
	/**
	 * MESSAG_WHAT_CONST
	 */
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
			case	SeraphimGlobal.MW_DEVICE_LIST_CHANGE:
				break;
			case SeraphimGlobal.MW_ADD_DEVICE :
				String name = msg.getData().getString(SeraphimGlobal.MK_ADD_DEVICE_NAME);
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
	private SeraphimUpnpControlPoint_temp mCP;
	/*********************		DATA ELEMENT			***************/
	List<String> 	deviceList = new ArrayList<String>();
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(SeraphimUpnpActivity.this,SeraphimDeviceActivity.class);
			Device device = mCP.getDeviceOfIndex(position);
			String name = device.getFriendlyName();
			String udn = device.getUDN();
			String location = device.getLocation();
			intent.putExtra(SeraphimGlobal.KEY_DEVICE_LOCATION, location);
			intent.putExtra(SeraphimGlobal.KEY_DEVICE_UDN, udn);
			intent.putExtra(SeraphimGlobal.KEY_DEVICE_NAME, name);
			startActivity(intent);
			
		}
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/**
		 * 
		 */
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		setContentView(R.layout.activity_upnp_main);
		mCP = SeraphimUpnpControlPoint_temp.getInstance(this,handler);
		SeraphimGlobal.setCP(mCP);
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
