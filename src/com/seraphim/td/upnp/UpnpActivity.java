package com.seraphim.td.upnp;

import java.util.List;

import org.cybergarage.upnp.Device;

import com.seraphim.td.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UpnpActivity extends Activity {
	/**
	 * MESSAG_WHAT_CONST
	 */
	static public final int MW_DEVICE_LIST_CHANGE = 2001;
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
				
				mAdapter.notifyDataSetChanged();
				mListView.invalidate();
				break;
			default :
				break;
			}
		}
		
	};
	
	private ListView mListView;
	private ArrayAdapter<Device> mAdapter;
	private UpnpControlPoint mCP;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upnp_main);
		mCP = UpnpControlPoint.getInstance(handler);
		List<Device> t_list = mCP.getDevices();
		mListView = (ListView) findViewById(R.id.list);
		mAdapter =  new ArrayAdapter<Device>(this,android.R.layout.simple_list_item_1 ,t_list);
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
