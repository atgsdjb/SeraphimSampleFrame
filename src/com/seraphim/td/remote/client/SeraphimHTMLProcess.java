package com.seraphim.td.remote.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.seraphim.td.remote.SeraphimGlobal;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SeraphimHTMLProcess {
 static  public  SeraphimHTMLProcess sulf;
 private Context mContext;
 private WebView processWebView;
 private SeraphimHTMLProcessListener mListener;
 private JSIntface tdProcess;
 private String userAgent;
 
 
 private static final String TAG="com.seraph.td";
 /**/
 
 
 
// private static final String js="var s;"+
//			"var url;"+
//			"url = location.href;"+
//			"s = document.getElementsByTagName('video');"+
//			"tdProcess.log(url)"+
//			"tdProcess.log(s[0].src)"+
//			"tdProcess.log(s.length);//[0].src"+
//			"tdProcess.log(s[0].outerHTML);"+
//			"if(typeof s != 'undefined'){"+
//			"tdProcess.getVideoOK(s[0])}"+
//			"else{tdProcess.getVideoFailed(-1);}";
/**/
 /**
  * 
  */
 private Handler mHanlder = new Handler(){

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
	}
	 
 };

/**
 * 
 * @param context
 * @param listener
 */
 private SeraphimHTMLProcess(Context context,SeraphimHTMLProcessListener listener){
	 mListener  =  listener;
	 mContext = context;
	 processWebView = new WebView(context);
	 initWebView();
 };
 private SeraphimHTMLProcess(Context context,WebView webView,SeraphimHTMLProcessListener listener){
	 mListener  =  listener;
	 mContext = context;
	 processWebView =webView;
	 initWebView();
 };
 static public SeraphimHTMLProcess createProcessHTML(Context context,SeraphimHTMLProcessListener listener){
	 if(sulf==null){
		 sulf = new SeraphimHTMLProcess(context,listener);
	 }
	 return sulf;
 }
 static public SeraphimHTMLProcess createProcessHTML(Context context,WebView webView,SeraphimHTMLProcessListener listener){
	 if(sulf==null){
		 sulf = new SeraphimHTMLProcess(context,webView,listener);
	 }
	 return sulf;
 }
 /**
  * 
  */
	private void initWebView() {
		// TODO Auto-generated method stub
		processWebView.setBackgroundColor(0);
		processWebView.setHorizontalScrollBarEnabled(false);
		processWebView.setVerticalScrollBarEnabled(false);
		WebSettings setting = processWebView.getSettings();
		setting.setUseWideViewPort(true);
		setting.setJavaScriptEnabled(true);
		setting.setDomStorageEnabled(true);
		setting.setLoadsImagesAutomatically(false);
		setting.setPluginsEnabled(true);
		setting.setAllowFileAccess(true);
		setting.setAppCacheEnabled(true);
		setting.setBuiltInZoomControls(false);
		setting.setDatabaseEnabled(true);
//		if(!loadImg){
//		setting.setUserAgentString("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5");
//		}
		userAgent = setting.getUserAgentString();
		File workPath = mContext.getDir("uuseeData", Context.MODE_WORLD_WRITEABLE);
		setting.setDatabasePath(workPath.getPath());
		setting.setDomStorageEnabled(true);
		setting.setJavaScriptCanOpenWindowsAutomatically(true);
		processWebView.setWebChromeClient(new WebChromeClient());
		processWebView.setWebViewClient(new UuseeWebViewClient());
		tdProcess = new JSIntface();
		processWebView.addJavascriptInterface(new JSIntface(), "tdProcess");
	}
	/**
	 * when loadImg is true then loading IMG,else not loading
	 */
	void strarProcessHtml(String url,boolean loadImg){
//		String tempUrl = "http://10.1.15.119:8080/play.html";
		WebSettings setting = processWebView.getSettings();
		if(loadImg){
		setting.setUserAgentString(userAgent);
		}else{
		setting.setUserAgentString(SeraphimGlobal.ipadUserAgent);
		}
		processWebView.loadUrl(url);
	}
	/**
	 * 
	 * @author root
	 *
	 */
	private class UuseeWebViewClient extends WebViewClient{
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			mHanlder.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String str = null;
					AssetManager am = mContext.getAssets();
					InputStream in =null;
					ByteArrayOutputStream buf = null;
					try {
						in = am.open("getVideo.js");
						buf = new ByteArrayOutputStream();
						byte[]  temp = new byte[1024];
						int len = 0;
						len = in.read(temp);
						while(len >0){
							buf.write(temp, 0, len);
							len = in.read(temp);
						}
						buf.flush();
						str = new String(buf.toByteArray());
						Log.d(TAG,str);
						buf.close();
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(TAG,"read assets erro msg===="+e.getMessage());
						e.printStackTrace();
					}finally{
						
					}
				
					Log.d(TAG,str);
					processWebView.loadUrl("javascript:"+str);
				}
			},1);
			
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			/******************3****************/
			tdProcess.getVideoFailed(-2);

		}

	
		
	}
	/**
	 * 
	 * @author root
	 *
	 */
	static class JSIntface{
		 int index = 0;
		 public void log(String str){
//			Log.e(TAG,"FROM WEIB MSG===="+str);
		}
		
		 public void getVideoOK(String str){
			 Log.e(TAG,"FROM VIDEO SRC===="+str);
			if(str.length() <5)
				str = null;
			sulf.mListener.finishHTMLProcess(str);
			
		}
		 public void getVideoFailed(int code){
			 Log.e(TAG,"FROM WEB ERROR CODE===="+code); 
			sulf.mListener.errorHTMLProcess(code);
		}
	}
	/*******************工具类**********************************/
}
