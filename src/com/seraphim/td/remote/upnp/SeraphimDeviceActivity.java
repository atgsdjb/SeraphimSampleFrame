package com.seraphim.td.remote.upnp;


import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ActionList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.UPnP;

import com.seraphim.td.R;
import com.seraphim.td.remote.SeraphimGlobal;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class SeraphimDeviceActivity extends Activity {
	static  private final String TAG =SeraphimGlobal.TAG;
	private String name="UNKNOWN  DEVICE";
	private String location="UNKNOWN  DEVICE";
	private String udn="UNKNOWN  DEVICE";
	private TextView mTextView;
//	private TextView titleText;
	
	private SeraphimUpnpControlPoint mCP;
	private Device mDevice;
	private ServiceList serverList;
	private List<Service> serviceList;
	private ListView mList;
	private ArrayAdapter<String> mAdapter;
	private Dialog mDialog =null;
	
	
	private Service currentService;
	
	/***********************************************/
	/**
	 * 
	 * @author root
	 *
	 */
	private class NotivyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String msg = intent.getStringExtra(SeraphimGlobal.BORADCASET_NOTITY_DATA_KEY);
//			Log.e("com.seraphim.td","--------RECEIVE A MSG---------");
		}
		
	}
	private NotivyReceiver mNotivyReceiver = new NotivyReceiver();
	/**
	 * 
	 * @author root
	 *
	 */
	private class ListViewOnItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			showDialog(position,"");
			currentService = serviceList.get(position);
			return ;
		}
		
	}
	private ListViewOnItemListener mOnListener = new ListViewOnItemListener();
	/**
	 * 
	 * @author root
	 *
	 */
    private class DialogListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String ver = UPnP.getVer();
			Log.e(TAG,ver);
			int id = v.getId();
			switch(id){
			case R.id.show_all_action:
				String serviceType = currentService.getServiceType();
				String controlURL  = currentService.getControlURL();
				String  DescriptionURL  = currentService.getDescriptionURL();
				String eventSubURl = currentService.getEventSubURL();
			
				String SCPDURL = currentService.getSCPDURL();
				String  ServiceID = currentService.getServiceID();
				String SID = currentService.getSID();
				Log.d(TAG,serviceType+controlURL+DescriptionURL+eventSubURl+ServiceID+SCPDURL+SID);
				ActionList al = currentService.getActionList();
				for(int i = 0;i<al.size();i++){
					Action a = al.getAction(i);
					String log = SeraphimUpnpStaticTools.getActionInfo(a);
					Log.e(TAG,log);
				}
				break;
			case R.id.clos_sulf:
				mDialog.dismiss();
				break;
				
			case R.id.subscribe:
				boolean b = mCP.subScribe(currentService);
				Log.e("com.seraphim.td","SUBSCRIBE ====="+b);
				break;
			case R.id.call_play_action:
//				HashMap<String,String> arg = new HashMap<String,String>();
//				arg.put("CurrentURI","http://player.uusee.com/mobile/apple/ipad2/love2.mp4");
//				mCP.sendAction(currentService, "SetAVTransportURI", arg);
				mDialog.dismiss();
				Intent intent = new Intent(SeraphimDeviceActivity.this,SeraphimUpnpBrowseActivity.class);
				String type = currentService.getServiceType();
				String udn = mDevice.getUDN();
				intent.putExtra(SeraphimGlobal.KEY_DEVICE_UDN, udn);
				intent.putExtra(SeraphimGlobal.KEY_SERVER_TYPE,type);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
    	
    }
    private DialogListener  mDialogListener = new DialogListener();
	/***********************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		setContentView(R.layout.activity_device);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.latyou_title);  
		mTextView = (TextView)findViewById(R.id.text);
//		titleText = (TextView) findViewById(R.id.text_title);
		mList = (ListView) findViewById(R.id.list);
		mList.setOnItemClickListener(mOnListener);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		mList.setAdapter(mAdapter);
		initElement();
		initData();
		initUpnpDevice();
		initView();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
//		if(mNotivyReceiver.)
//		unregisterReceiver(mNotivyReceiver);
		super.onStop();
	}

	/**********************************************************/
	/**
	 * 
	 */
	private void initElement(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(SeraphimGlobal.BROADCASET_REVICE_NOTITY_ACTION);
		registerReceiver(mNotivyReceiver, filter);
	}
	private void initView() {
		// TODO Auto-generated method stub
//		titleText.setText(name+"#"+location);
	}
	/**
	 * 
	 * @param id
	 * @param title
	 */
	private void showDialog(int id,String title ){
		Button dialogButtonClose;
		Button dialogButtonShwoALL;
		Button dialogCallAction;
		Button dialogSubscribe;
		TextView dialogTextTitle;
		if(mDialog ==null){
			mDialog = new Dialog(this);
			mDialog.setContentView(R.layout.layout_upnp_service_op_dialog);
		}
        dialogButtonClose = (Button) mDialog.findViewById(R.id.clos_sulf);
        dialogButtonShwoALL = (Button) mDialog.findViewById(R.id.show_all_action);
        dialogCallAction = (Button)mDialog.findViewById(R.id.call_play_action);
        dialogSubscribe=(Button)mDialog.findViewById(R.id.subscribe);
        dialogButtonClose.setOnClickListener(mDialogListener);
        dialogButtonShwoALL.setOnClickListener(mDialogListener);
        dialogCallAction.setOnClickListener(mDialogListener);
        dialogSubscribe.setOnClickListener(mDialogListener);
        
        dialogTextTitle = (TextView)mDialog.findViewById(R.id.text);
        dialogTextTitle.setText(title);
        mDialog.show();
	}
	/**
	 * 
	 */
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String l_location = intent.getStringExtra(SeraphimGlobal.KEY_DEVICE_LOCATION);
		String l_name = intent.getStringExtra(SeraphimGlobal.KEY_DEVICE_NAME);
		String l_udn = intent.getStringExtra(SeraphimGlobal.KEY_DEVICE_UDN);
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
	mCP = SeraphimGlobal.getCP();
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
