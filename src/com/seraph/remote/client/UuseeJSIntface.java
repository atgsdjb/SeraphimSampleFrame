package com.seraph.remote.client;

import java.util.Map;

import com.seraph.remote.UuseeGlobal;
import com.seraph.remote.UuseePlayContralListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;


public class UuseeJSIntface implements UuseePlayContralListener {

	/**
	 * @param args
	 */
	static private final String TAG="";
	
	private Context mContext;
	
	private Handler localHandler;
	private UuseeScannerClient mScanner;
	private UuseeRemoteClient mRemoteClient;

	private UuseeHTMLProcess mHTMLProcess;
	public UuseeJSIntface(Context context,Handler _handler ){
		mContext = context;
		localHandler = _handler;
	}
	public void uusee_cmd_scanner(){
		if(mScanner==null)
			mScanner = new UuseeScannerClient(localHandler);
		mScanner.startScanner();
		Log.d(TAG,"uusee_cmd_scanner");
		
	}
	public int  uusee_cmd_connect(String ip){

		mRemoteClient = UuseeRemoteClient.connectServer(ip, UuseeGlobal.GPORT);
		Log.d(TAG,ip);
		if(mRemoteClient == null)
			return 0;
		else return 1;
	}
	public  void uusee_cmd_play(String url,String title){
		if(mRemoteClient == null)
			return;
		if(url.endsWith(".m3u8") || url.endsWith(".mp4")){
			if(mRemoteClient == null)
				//ERROR
				return;
			localHandler.sendEmptyMessage(UuseeGlobal.MW_FINISH_PROCESS_HTML);
			String cmd =  new 	UuseeRemoteCMD.PLAY(url).toString();
			mRemoteClient.sendCMD(cmd);
		}
			
		/**接受到的URL为页面地址****/
		else{
			if(mHTMLProcess == null)
				mHTMLProcess = UuseeHTMLProcess.createProcessHTML(mContext, new HTMLProceeLisener());
			/**3***/
			boolean loadImg = false;
			/**3***/
			mHTMLProcess.strarProcessHtml(url,loadImg);
		}
		
		
		
		Log.d(TAG,url);
	}
	public void uusee_cmd_push(){
		if(mRemoteClient == null)
			return;
		Log.d(TAG,"uusee_cmd_push");
		String cmd = new UuseeRemoteCMD.PAUSE().toString();
		mRemoteClient.sendCMD(cmd);
	}
	public void uusee_cmd_resume(){
		if(mRemoteClient == null)
			return;
		String cmd = new UuseeRemoteCMD.RESUME().toString();
		mRemoteClient.sendCMD(cmd);
		Log.d(TAG,"resume");
		
	}

	public void uusee_cmd_fast_forward(){
		if(mRemoteClient == null)
			return;
		Log.d(TAG,"uusee_cmd_fast_forward");
		String cmd = new UuseeRemoteCMD.FORWARD().toString();
		mRemoteClient.sendCMD(cmd);
		
	}
	public void uusee_cmd_fast_forward_stop(){
		if(mRemoteClient == null)
			return;
		Log.d(TAG,"uusee_cmd_fast_forward");
		String cmd = new UuseeRemoteCMD.FORWARD_STOP().toString();
		mRemoteClient.sendCMD(cmd);
		
	}
	public void uusee_cmd_rewind(){
		if(mRemoteClient == null)
			return;
		Log.d(TAG,"uusee_cmd_rewind");
		String cmd = new UuseeRemoteCMD.REWIND().toString();
		mRemoteClient.sendCMD(cmd);
		
	}
	public void uusee_cmd_rewind_stop(){
		if(mRemoteClient == null)
			return;
		Log.d(TAG,"uusee_cmd_rewind");
		String cmd = new UuseeRemoteCMD.REWIND_STOP().toString();
		mRemoteClient.sendCMD(cmd);
		
	}
	
	public void uusee_cmd_volume_up(){
		if(mRemoteClient == null)
			return;
		String cmd = new UuseeRemoteCMD.VOLUME_UP().toString();
		mRemoteClient.sendCMD(cmd);
		Log.d(TAG,"uusee_cmd_volume_up");
	}
	public void uusee_cmd_volume_down(){
		if(mRemoteClient == null)
			return;
		String cmd = new UuseeRemoteCMD.VOLUME_DOWN().toString();
		mRemoteClient.sendCMD(cmd);
		Log.d(TAG,"uusee_cmd_volume_down");
		
	}
	public void uusee_cmd_stop(){
		if(mRemoteClient == null)
			return;
		String cmd = new UuseeRemoteCMD.STOP().toString();
		mRemoteClient.sendCMD(cmd);
	}
	/****处理外站页面****/
	public void getExVideoOK(String url){
		Log.e(TAG,"ex----url==="+url);
		if(mRemoteClient==null)
			return;
		String cmd = new UuseeRemoteCMD.PLAY(url).toString();
		mRemoteClient.sendCMD(cmd);
	
	}
	public void getExVideoFailed(int errorCode){
		Log.e(TAG,"error Code ===="+errorCode);
		
	}
	public void exlog(String url){
		Log.e(TAG,"exlog===="+url);
	}
	/**
	 * HTML解析回调接口
	 */
	class HTMLProceeLisener implements UuseeHTMLProcessListener{
		@Override
		public void startHTMLProcess() {
			// TODO Auto-generated method stub
			localHandler.sendEmptyMessage(UuseeGlobal.MW_START_PROCESS_HTML);
		}
		@Override
		public void finishHTMLProcess(String url) {
			// TODO Auto-generated method stub
			if(mRemoteClient == null || mHTMLProcess==null)
				return ;
		 localHandler.sendEmptyMessage(UuseeGlobal.MW_FINISH_PROCESS_HTML);
		 String cmd =  new 	UuseeRemoteCMD.PLAY(url).toString();
		 mRemoteClient.sendCMD(cmd);
		}
		@Override
		public void errorHTMLProcess(int errorCode) {
			// TODO Auto-generated method stub
			if(errorCode == -2){
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
	@Override
	public void uusee_cmd_play(String url) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void uusee_cmd_play(String url, Map<String, String> mimeData) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void uusee_cmd_seekTo(long position) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getExVideoFailed(String code) {
		// TODO Auto-generated method stub
		
	}
}
