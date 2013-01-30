package com.seraphim.td.remote.upnp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.json.JSONObject;

import com.seraphim.td.R;
import com.seraphim.td.remote.SeraphimGlobal;
import com.seraphim.td.remote.client.SeraphimDeviceInfo;
import com.seraphim.td.remote.client.SeraphimJSIntface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SeraphimUpnpBrowseActivity extends Activity implements View.OnClickListener{

    /** Called when the activity is first created. */
	private final String TAG = SeraphimGlobal.TAG;
	private Button bankImage ;
	private Button forwardImage;
	private Button updataButton;
	private Button closeButton;
	private EditText mEditText;
	private WebView mWebView;
	private ProgressDialog mDialog;
	
	/**
	 * 
	 */


	private int wDIP;
	private int hDIP;
	private AudioManager am;
	private PowerManager pm;
	private String currentURL;
	private String videoURL;
//	
	private SeraphimUpnpControlPoint_temp mCP = SeraphimGlobal.getCP();
	private Service mService;
	private Device mDevice;
	
	
	WebViewClient uuseeChromeClient = new WebViewClient() {
		@Override
		public void onPageFinished(WebView view, String u) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, u);
			updateButtonState();

			// TODO Auto-generated method stub
			String url = mWebView.getUrl();
			if(url==null || url.contains(SeraphimGlobal.webUrl))
				return ;//如果不是外站网页，直接返回。
			 localHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					String str = null;
					if(SeraphimGlobal.exJS == null){
						AssetManager am = SeraphimUpnpBrowseActivity.this.getAssets();
						InputStream in =null;
						ByteArrayOutputStream buf = null;
						try {
							in = am.open("exGetVideo.js");
							buf = new ByteArrayOutputStream();
							byte[]  temp = new byte[1024];
							int len = 0;
							len = in.read(temp);
							while(len >0){
								buf.write(temp, 0, len);
								len = in.read(temp);
							}
							buf.flush();
							SeraphimGlobal.exJS = new String(buf.toByteArray());
							Log.d(TAG,SeraphimGlobal.exJS );
							buf.close();
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.e(TAG,"read assets erro msg===="+e.getMessage());
							e.printStackTrace();
						}finally{
							
						}
					}
					
				
					Log.d(TAG,SeraphimGlobal.exJS);
					mWebView.loadUrl("javascript:"+SeraphimGlobal.exJS);
				}
			},1);
			 return ;
		    
		
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {}

	};
	/**
	 * 
	 */
	private Handler localHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case SeraphimGlobal.MW_START_PROCESS_HTML :
				Toast.makeText(getApplicationContext(), "开始解析HTML", Toast.LENGTH_LONG).show();
				break;
			case SeraphimGlobal.MW_FINISH_PROCESS_HTML:
				Toast.makeText(getApplicationContext(), "完成解析HTML", Toast.LENGTH_LONG).show();
				break;
			case SeraphimGlobal.MW_GET_VIDEO_OK :
				videoURL= msg.getData().getString(SeraphimGlobal.KEY_VIDEO_URL);
				if(videoURL != null){
					Toast.makeText(getApplicationContext(), "已取得URL\n在菜单选择播放", Toast.LENGTH_LONG).show();
				}
				break;
			case SeraphimGlobal.MW_GET_VIDEO_ERROR:
				String s = msg.getData().getString(SeraphimGlobal.KEY_VIDEO_URL_ERROR);
				Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
				break;
			default :
				break;
			}
		}
		
	};
	/**
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**3
         * 
         */
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    	DisplayMetrics  dm  =new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);   
		wDIP = px2dip(dm.widthPixels);
		hDIP = px2dip(dm.heightPixels)-50; /****/
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.new_layout);
        bankImage =(Button)findViewById(R.id.bank);
        forwardImage =(Button) findViewById(R.id.forward);
        updataButton = (Button)findViewById(R.id.updata);
        closeButton =(Button)findViewById(R.id.close);
        bankImage.setOnClickListener(this);
        forwardImage.setOnClickListener(this);
        updataButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        mWebView = (WebView)findViewById(R.id.shareweb);
        mEditText = (EditText) findViewById(R.id.edit);
        mDialog =new ProgressDialog(this);
		mDialog.setProgress(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("请稍候...");
		Window mwindow = mDialog.getWindow();
		WindowManager.LayoutParams lp=mwindow.getAttributes(); 
		lp.x=0; 
		lp.y=100; 
		mwindow.setAttributes(lp); 
        initwebView();
        initElement();
    }

   /**
    *  
    */
	private void initElement() {
		// TODO Auto-generated method stub
		pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		Intent intent = getIntent();
		if(intent == null){
			Log.e(TAG,"intent is null in UuseeUpnpBrowseActivity");
			return;
		}
		if(mCP==null){
			Log.e(TAG,"MCP==NULL in UuseeUpnpBrowseActivity");
			return ;
		}
		String udn = intent.getExtras().getString(SeraphimGlobal.KEY_DEVICE_UDN);
		String type = intent.getExtras().getString(SeraphimGlobal.KEY_SERVER_TYPE);
		mDevice = mCP.getDeviceOfudn(udn);
		mService =mCP.getServiceOfType(mDevice, type);
		
	}
	private void initwebView() {
		// TODO Auto-generated method stub
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setVerticalScrollBarEnabled(false);
		WebSettings setting = mWebView.getSettings();
		setting.setUseWideViewPort(true);
		setting.setJavaScriptEnabled(true);
		setting.setDomStorageEnabled(true);
		setting.setPluginsEnabled(true);
		setting.setAllowFileAccess(true);
		setting.setAppCacheEnabled(true);
		setting.setBuiltInZoomControls(false);
		setting.setDatabaseEnabled(true);
		File workPath = getDir("uuseeData", Context.MODE_WORLD_WRITEABLE);
		setting.setDatabasePath(workPath.getPath());
		setting.setDomStorageEnabled(true);
		setting.setJavaScriptCanOpenWindowsAutomatically(true);
		setting.setUserAgentString(SeraphimGlobal.ipadUserAgent);
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(uuseeChromeClient);
		mWebView.addJavascriptInterface(new SeraphimUpnpJSIntface(localHandler), "uusee");
//		mWebView.loadUrl("http://www.youku.com");
	}
  /**********************
   * 
   */



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.bank :
				if(mWebView.canGoBack())mWebView.goBack();
				break;
			case R.id.forward :
				if(mWebView.canGoForward())mWebView.goForward();
				break;
			case R.id.updata :
				currentURL = mEditText.getText().toString();
				mWebView.loadUrl(currentURL);
				break;
			case R.id.close:
				mWebView.loadUrl(SeraphimGlobal.webUrl+Math.random()+"&width="+wDIP+"&heigth="+hDIP);
				break;
			default : break;
		}
	}
	private void updateButtonState(){
	 bankImage.setEnabled(mWebView.canGoBack());
	 forwardImage.setEnabled(mWebView.canGoForward());
	 bankImage.invalidate();
	 forwardImage.invalidate();
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

		// TODO Auto-generated method stub
		int id = item.getItemId();
		int code;
		switch(id){
		case menuPlay:
			if(videoURL != null && videoURL.contains("http")){
				HashMap<String,String> arg0 = new HashMap<String,String>();
				arg0.put("CurrentURI",videoURL);
				code = mCP.sendAction2(mService, "SetAVTransportURI", arg0);
				Toast.makeText(getApplicationContext(),"begin play  result="+code , Toast.LENGTH_LONG).show();
				Log.e(TAG,"SEND SetAVTransportURI ACTION  RESULT === "+code);
			}else{
				Toast.makeText(getApplicationContext(),"无法播放,请等待生产正确URL" , Toast.LENGTH_LONG).show();
			}
			break;
		case menuPush:{
			HashMap<String,String> arg = new HashMap<String,String>();
			code = mCP.sendAction2(mService, "Pause", arg);
			Toast.makeText(getApplicationContext(),"SEND PUSH ACTION RESULT ==="+code , Toast.LENGTH_LONG).show();
			Log.e(TAG,"SEND PUSH ACTION RESULT ==="+code);
		}
			
			break;
		case menuStop:{
			HashMap<String,String> arg = new HashMap<String,String>();
			code = mCP.sendAction2(mService, "Stop", arg);
			Toast.makeText(getApplicationContext(),"SEND Stop ACTION RESULT ==="+code , Toast.LENGTH_LONG).show();
			Log.e(TAG,"SEND Stop ACTION RESULT ==="+code);
		}
			break;
		case menuSeek:{
			HashMap<String,String> arg = new HashMap<String,String>();
			arg.put("Unit", "ABS_COUNT");
			arg.put("Target", "100");
			code = mCP.sendAction2(mService, "Stop", arg);
			Toast.makeText(getApplicationContext(),"SEND Stop ACTION RESULT ==="+code , Toast.LENGTH_LONG).show();
			Log.e(TAG,"SEND Seek ACTION RESULT ==="+code);
		}
			
			break;
		case menuResume:{
			HashMap<String,String> arg = new HashMap<String,String>();
			arg.put("Speed", "1");
			code = mCP.sendAction2(mService, "Play", arg);
			Toast.makeText(getApplicationContext(),"SEND Play ACTION RESULT ==="+code , Toast.LENGTH_LONG).show();
			Log.e(TAG,"SEND Resume ACTION RESULT ==="+code);
		}
			break;
		case menuNone:{
			HashMap<String,String> arg = new HashMap<String,String>();
			arg.put("CurrentURI","http://player.uusee.com/mobile/apple/ipad2/love2.mp4");
			code = mCP.sendAction2(mService, "SetAVTransportURI", arg);
			Toast.makeText(getApplicationContext(),"SEND None ACTION RESULT ==="+code , Toast.LENGTH_LONG).show();
			Log.e(TAG,"Send None ACTION RESULT ==="+code);
		}
			
			break;
		case menuEXNext:{
			HashMap<String,String> arg = new HashMap<String,String>();
			code = mCP.sendAction2(mService, "Next", arg);
			Toast.makeText(getApplicationContext(),"SEND Next ACTION RESULT ==="+code , Toast.LENGTH_LONG).show();
			Log.e(TAG,"SEND Next ACTION RESULT ==="+code);
		}
		break;
		case menuEXGetCurrentAction :{
			HashMap<String,String> arg = new HashMap<String,String>();
			String action=new String();
			arg.put("Actions", action);
			code = mCP.sendAction2(mService, "GetCurrentTransportActions", arg);
			Log.e(TAG,"SEND Next ACTION RESULT ==="+code);
		}
		break;
		default : break;
		}
	
		return super.onOptionsItemSelected(item);
	}
	/**
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public  int dip2px(Context context, float dipValue){ 
	        final float scale = context.getResources().getDisplayMetrics().density; 
	        return (int)(dipValue * scale + 0.5f); 
	} 
	/**
	 * 
	 * @param pxValue
	 * @return
	 */
	public  int px2dip( float pxValue){ 
	        final float scale = getResources().getDisplayMetrics().density; 
	        return (int)(pxValue / scale + 0.5f); 
	} 


}
