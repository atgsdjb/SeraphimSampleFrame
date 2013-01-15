package com.seraphim.td.remote.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.seraphim.td.R;
import com.seraphim.td.remote.SeraphimGlobal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class SeraphimClient extends Activity implements View.OnClickListener{

/***
 * 
 */
	void temp(){
		Executor exec = Executors.newCachedThreadPool();
		exec.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d(TAG,"I am tread");
			}
		});
	}
    /** Called when the activity is first created. */
	private final String TAG = "td.seraph.cn";
	private int createConst = 0;
	private Button bankImage ;
	private Button forwardImage;
	private Button updataButton;
	private Button closeButton;
	private EditText mEditText;
	private WebView mWebView;
	private ProgressDialog mDialog;
	
//	private UuseeJSIntface mJSIntface;
	/**
	 * 
	 */


	private int wDIP;
	private int hDIP;
	private AudioManager am;
	private PowerManager pm;
	private boolean connectFlg = false;
	private String serverIP;
	private String currentURL;
//	
	
	
	
	
	
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
				Toast.makeText(SeraphimClient.this, "HOST_NAME=="+host+"\tip=="+ip+"\tconnectState=="+connect, Toast.LENGTH_LONG).show();
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
	/**
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	DisplayMetrics  dm  =new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);   
		wDIP = px2dip(dm.widthPixels);
		hDIP = px2dip(dm.heightPixels)-50; /****/
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
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
//		mJSIntface = new UuseeJSIntface(this,localHandler );
		
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
		mWebView.setWebViewClient(new WebViewClient() {
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
//						String str = null;
						if(SeraphimGlobal.exJS == null){
							AssetManager am = SeraphimClient.this.getAssets();
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

		});
//		mWebView.setOnTouchListener(new View.OnTouchListener() {});
		mWebView.addJavascriptInterface(new SeraphimJSIntface(this, localHandler), "uusee");
		mWebView.loadUrl(SeraphimGlobal.webUrl+""+Math.random()+"&width="+wDIP+"&heigth="+hDIP);
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
//
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
