package com.seraphim.td.upnp;


import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ActionList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;

import com.seraphim.td.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceActivity extends Activity {
	private String name="UNKNOWN  DEVICE";
	private String location="UNKNOWN  DEVICE";
	private String udn="UNKNOWN  DEVICE";
	private TextView mTextView;
	private TextView titleText;
	
	private SeraphUpnpControlPoint mCP;
	private Device mDevice;
	private ServiceList serverList;
	private List<Service> serviceList;
	private ListView mList;
	private ArrayAdapter<String> mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_device);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.latyou_title);  
		mTextView = (TextView)findViewById(R.id.text);
		titleText = (TextView) findViewById(R.id.text_title);
		mList = (ListView) findViewById(R.id.list);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		mList.setAdapter(mAdapter);
		initData();
		initUpnpDevice();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		titleText.setText(name+"#"+location);
	}
	/**
	 * 
	 */
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String l_location = intent.getStringExtra(UpnpActivity.KEY_DEVICE_LOCATION);
		String l_name = intent.getStringExtra(UpnpActivity.KEY_DEVICE_NAME);
		String l_udn = intent.getStringExtra(UpnpActivity.KEY_DEVICE_UDN);
		if(l_name != null)
			name = l_name;
		if(l_udn!=null)
			udn = l_udn;
		if(l_location!=null)
			location = l_location;
		serviceList = new ArrayList<Service>();
		
	}
	/**
	 * 
	 */
	private void initUpnpDevice(){
	mCP = GlobalStore.getCP();
	mDevice = mCP.getDeviceOfudn(udn);
	serverList = mDevice.getServiceList();
	
	for(int i = 0;i<serverList.size();i++){
		Service s = (Service) serverList.get(i);
		serviceList.add(s);
		mAdapter.add(s.getServiceType());
	}
	mAdapter.notifyDataSetChanged();
	}
	
}
