package com.seraphim.td.remote.client;


import java.io.File;

import org.json.JSONObject;

import com.seraphim.td.R;
import com.seraphim.td.remote.SeraphimGlobal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SeraphimClientAcitvty extends Activity {
    /** Called when the activity is first created. */
	final String TAG = "com.uusee.remote.client";
	
	
	
	private SeraphimScannerClient mScanner;
	private SeraphimRemoteClient  mRemoteClient;
	
//	static public int GPORT = 5852;
//	
//	
//	static public final int MW_BEGIN_SCANNED = 201;
//	static public final int MW_SCANNING = 202;
//	public final int MW_FINISH_SCANNED = 203;
	
	/**************与服务器传输数据*********/
	private boolean connectFlg = false;
	private String serverIP;
//	static public final int MW_CONNECT_OK = 204;
//	static public final int MW_CONNECT_ERROR=205;
//	static public final int MW_START_PROCESS_HTML = 206;
//	static public final int MW_FINISH_PROCESS_HTML = 207;
//	
	private WebView mWebView;
	
	
//	public final String MSG_KEY_DEVICE="host";
//	public final String MSG_KEY_IP="ip";
//	public final String MSG_KEY_CONNECT="connect";
	private SeraphimHTMLProcess mHTMLProcess;
	private Handler localHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case SeraphimGlobal.MW_BEGIN_SCANNED :
				Log.d(TAG,"MW_BEGIN_SCANNED");
				break;
			case SeraphimGlobal.MW_SCANNING :
				String host = msg.getData().getString(SeraphimGlobal.MSG_KEY_DEVICE);
				String ip = msg.getData().getString(SeraphimGlobal.MSG_KEY_IP);
				boolean connect = msg.getData().getBoolean(SeraphimGlobal.MSG_KEY_CONNECT,false);
				Log.d(TAG,"HOST_NAME=="+host+"\tip=="+ip+"\tconnectState=="+connect);
				SeraphimDeviceInfo info = new SeraphimDeviceInfo(ip, host, connect);
				Toast.makeText(SeraphimClientAcitvty.this, "HOST_NAME=="+host+"\tip=="+ip+"\tconnectState=="+connect, Toast.LENGTH_LONG).show();
				JSONObject json = new JSONObject();
				try{
				json.put("deiveHame", info.getName());
				json.put("ip",info.getIp());
				}catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG,e.toString());
				}
				String cmd = "javascript:window.add_divce_info(\'" +  json.toString() + "\')";
				Log.d(TAG,"load webCMD ===="+cmd);
				mWebView.loadUrl(cmd);
				break;
			case SeraphimGlobal.MW_FINISH_SCANNED :
				Log.d(TAG,"MW_FINISH_SCANNED");
				break;
			case SeraphimGlobal.MW_CONNECT_OK :
				connectFlg = true;
				Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_LONG).show();
				break;
			case SeraphimGlobal.MW_CONNECT_ERROR :
				Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_LONG).show();
				connectFlg = false;
				break;
			case SeraphimGlobal.MW_START_PROCESS_HTML :
				Toast.makeText(getApplicationContext(), "开始解析HTML", Toast.LENGTH_LONG).show();
				break;
			case SeraphimGlobal.MW_FINISH_PROCESS_HTML:
				Toast.makeText(getApplicationContext(), "完成解析HTML", Toast.LENGTH_LONG).show();
				break;
			default :
				break;
			}
		}
		
	};



	private int wDIP;
	private int hDIP;



	private AudioManager am;



	private PowerManager pm;



	private int MAX_AUDIO_VALUE;

	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	DisplayMetrics  dm  =new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);   
        //获得手机的宽度和高度像素单位为px   
		wDIP = px2dip(dm.widthPixels);
		hDIP = px2dip(dm.heightPixels);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.main_web);
       
        
        mWebView = (WebView) findViewById(R.id.web);
        initWebView();
        initElement();
        mScanner = new SeraphimScannerClient(localHandler);
      
        
    }
	private void initElement() {
		// TODO Auto-generated method stub
		pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		MAX_AUDIO_VALUE = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
	private void initWebView() {
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
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {}

		});
		mWebView.addJavascriptInterface(new JSIntface(), "uusee");
		mWebView.loadUrl(SeraphimGlobal.webUrl+""+Math.random()+"&width="+wDIP+"&heigth="+hDIP);
	}
	/**
	 * uusee_cmd_resume()  //恢复播放
	 * uusee_cmd_scanner() //返回当前网内设备
     * uusee_cmd_volume_up()    //增加音量
     * uusee_cmd_volume_down() //减小声量
     * uusee_cmd_fast_forward() //快进
     * uusee_cmd_rewind   //后退 
	 * @author root
	 *
	 */
	 class JSIntface{
		public void uusee_cmd_scanner(){
			if(mScanner==null)
				mScanner = new SeraphimScannerClient(localHandler);
			mScanner.startScanner();
			Log.d(TAG,"uusee_cmd_scanner");
			
		}
		public int  uusee_cmd_connect(String ip){
			mRemoteClient = SeraphimRemoteClient.connectServer(ip, SeraphimGlobal.GPORT);
			Log.d(TAG,ip);
			if(mRemoteClient == null)
				return 0;
			else return 1;
		}
		public  void uusee_cmd_play(String url,String title){
			if(mRemoteClient == null)
				return;
			if(mHTMLProcess == null)
				mHTMLProcess = SeraphimHTMLProcess.createProcessHTML(SeraphimClientAcitvty.this, new HTMLProceeLisener());
			mHTMLProcess.strarProcessHtml(url,false);
			
			
			Log.d(TAG,url);
		}
		public void uusee_cmd_push(){
			if(mRemoteClient == null)
				return;
			Log.d(TAG,"uusee_cmd_push");
			String cmd = new SeraphimRemoteCMD.PAUSE().toString();
			mRemoteClient.sendCMD(cmd);
		}
		public void uusee_cmd_resume(){
			if(mRemoteClient == null)
				return;
			String cmd = new SeraphimRemoteCMD.RESUME().toString();
			mRemoteClient.sendCMD(cmd);
			Log.d(TAG,"resume");
			
		}

		public void uusee_cmd_fast_forward(){
			if(mRemoteClient == null)
				return;
			Log.d(TAG,"uusee_cmd_fast_forward");
			String cmd = new SeraphimRemoteCMD.FORWARD().toString();
			mRemoteClient.sendCMD(cmd);
			
		}
		public void uusee_cmd_fast_forward_stop(){
			if(mRemoteClient == null)
				return;
			Log.d(TAG,"uusee_cmd_fast_forward");
			String cmd = new SeraphimRemoteCMD.FORWARD_STOP().toString();
			mRemoteClient.sendCMD(cmd);
			
		}
		public void uusee_cmd_rewind(){
			if(mRemoteClient == null)
				return;
			Log.d(TAG,"uusee_cmd_rewind");
			String cmd = new SeraphimRemoteCMD.REWIND().toString();
			mRemoteClient.sendCMD(cmd);
			
		}
		public void uusee_cmd_rewind_stop(){
			if(mRemoteClient == null)
				return;
			Log.d(TAG,"uusee_cmd_rewind");
			String cmd = new SeraphimRemoteCMD.REWIND_STOP().toString();
			mRemoteClient.sendCMD(cmd);
			
		}
		
		public void uusee_cmd_volume_up(){
			if(mRemoteClient == null)
				return;
			String cmd = new SeraphimRemoteCMD.VOLUME_UP().toString();
			mRemoteClient.sendCMD(cmd);
			Log.d(TAG,"uusee_cmd_volume_up");
		}
		public void uusee_cmd_volume_down(){
			if(mRemoteClient == null)
				return;
			String cmd = new SeraphimRemoteCMD.VOLUME_DOWN().toString();
			mRemoteClient.sendCMD(cmd);
			Log.d(TAG,"uusee_cmd_volume_down");
			
		}
		public void uusee_cmd_stop(){
			if(mRemoteClient == null)
				return;
			String cmd = new SeraphimRemoteCMD.STOP().toString();
			mRemoteClient.sendCMD(cmd);
		}
		
		
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
	/**
	 * HTML解析回调接口
	 */
	class HTMLProceeLisener implements SeraphimHTMLProcessListener{
		@Override
		public void startHTMLProcess() {
			// TODO Auto-generated method stub
			localHandler.sendEmptyMessage(SeraphimGlobal.MW_START_PROCESS_HTML);
		}
		@Override
		public void finishHTMLProcess(String url) {
			// TODO Auto-generated method stub
			if(mRemoteClient == null || mHTMLProcess==null)
				return ;
		 localHandler.sendEmptyMessage(SeraphimGlobal.MW_FINISH_PROCESS_HTML);
		 String cmd =  new 	SeraphimRemoteCMD.PLAY(url).toString();
		 mRemoteClient.sendCMD(cmd);
		}
		@Override
		public void errorHTMLProcess(int errorCode) {
			// TODO Auto-generated method stub
			if(errorCode == -2){
				AlertDialog.Builder builder = new AlertDialog.Builder(SeraphimClientAcitvty.this);
				builder.setMessage("确认退出？");
				builder.setTitle("请确认当前网络正常");
				builder.setPositiveButton("确定", new  DialogInterface.OnClickListener () {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
			
		}
	}
	
}
