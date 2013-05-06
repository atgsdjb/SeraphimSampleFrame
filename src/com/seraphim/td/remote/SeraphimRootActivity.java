package com.seraphim.td.remote;

import java.util.ArrayList;

import com.seraphim.td.R;
import com.seraphim.td.remote.client.tools.SeraphListAdapter;
import com.seraphim.td.remote.imp.AbstractDevice;
import com.seraphim.td.remote.imp.UuseeAddListener;
import com.seraphim.td.remote.imp.UuseeRemoteWarp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SeraphimRootActivity extends Activity implements UuseeAddListener {

	
	String videoURL ="http://player.uusee.com/mobile/apple/test_video/test3.mp4";
	UuseeRemoteWarp root;
	ListView mListView;
	Handler mHandler  = new Handler();
	SeraphListAdapter<AbstractDevice> mAdapter;
	static private final String TAG="com.seraphim.td"; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.layout_seraphim_root);
        mListView = (ListView)findViewById(R.id.list);
        mAdapter = new SeraphListAdapter<AbstractDevice>(this, new ArrayList<AbstractDevice>());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AbstractDevice device = (AbstractDevice) mAdapter.getItem(position);
				root.bindDevice(device);
				
			}
		});
        root = new UuseeRemoteWarp(this,this);
       
    }
    
    
    
    
    
    
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				root.startScan();
			}
			
		}.start();
		
	
		
		
	}

	
	
	
	
	






	@Override
	public void onAddDevice(final AbstractDevice device) {
		// TODO Auto-generated method stub
		Log.d(TAG,device.toString());
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mAdapter.addData(device);
				mAdapter.notifyDataSetChanged();
			}
		});
		
	}
	/*******************MENU***********************/
	private static final int menuPlay = 1;
	private static final int menuPush=2;
	private static final int menuStop=3;
	private static final int menuSeek=4;
	private static final int menuResume=5;
	private static final int menuNone=6;
	private static final int menuEXNext=7;
	private static final int menuEXGetCurrentAction = 8;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, menuPlay, menuPlay, "PLAY");
		menu.add(0, menuPush, menuPush, "PUSH");
		menu.add(0, menuStop, menuStop, "STOP");
		menu.add(0, menuSeek, menuSeek, "SEEK");
		menu.add(0, menuResume, menuResume, "RESU");
		menu.add(0, menuNone, menuNone, "None");
		menu.add(0, menuEXNext, menuEXNext, "Next");
		menu.add(0,menuEXGetCurrentAction,menuEXGetCurrentAction,"getAction");
	
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		videoURL="http://player.uusee.com/mobile/apple/ipad2/love2.mp4";
		switch(id){
		case menuPlay:
			if(videoURL != null && videoURL.contains("http")){
				root.play(videoURL);
			
			}else{
				Toast.makeText(getApplicationContext(),"无法播放,请等待生产正确URL" , Toast.LENGTH_LONG).show();
			}
			break;
		case menuPush:{
			
		}
			
			break;
		case menuStop:{
			root.stop();
		}
			break;
		case menuSeek:{
			root.seek(10f);
		}
			
			break;
		
		default : break;
		}
	
		return super.onOptionsItemSelected(item);
	}
	public void play(View view){
		root.play(videoURL);
	}
	public void stop(View view){
		root.stop();
	}
	public void resume(View view){
		root.resume();
	}
	public void pause(View view){
		root.pause();
	}
	public void seekN(View view){
		root.seek(10f);
	}
	public void  seekP(View view){
		root.seek(-10f);
	}






}
