package com.seraph.remote.upnp;

import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.seraph.remote.UuseeGlobal;
import com.seraph.remote.UuseePlayContralListener;

public class UuseeUpnpJSIntface implements UuseePlayContralListener {
	Handler handler;
	public UuseeUpnpJSIntface(Handler _handler){
		handler = _handler;
	}
	@Override
	public void uusee_cmd_play(String url) {
		// TODO Auto-generated method stub
		Log.d("","");
	}

	@Override
	public void uusee_cmd_play(String url, Map<String, String> mimeData) {
		// TODO Auto-generated method stub
		Log.d("","");
	}

	@Override
	public void uusee_cmd_push() {
		// TODO Auto-generated method stub
		Log.d("","");

	}

	@Override
	public void uusee_cmd_seekTo(long position) {
		// TODO Auto-generated method stub
		Log.d("","");

	}

	@Override
	public void uusee_cmd_resume() {
		// TODO Auto-generated method stub
		Log.d("","");

	}

	@Override
	public void uusee_cmd_fast_forward() {
		// TODO Auto-generated method stub
		Log.d("","");

	}

	@Override
	public void uusee_cmd_fast_forward_stop() {
		// TODO Auto-generated method stub
		Log.d("","");

	}

	@Override
	public void uusee_cmd_rewind() {
		// TODO Auto-generated method stub
		Log.d("","");
	}

	@Override
	public void uusee_cmd_rewind_stop() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getExVideoFailed(String code) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = UuseeGlobal.MW_GET_VIDEO_ERROR;
		Bundle data = new Bundle();
		data.putString(UuseeGlobal.KEY_VIDEO_URL_ERROR, code);
	}
	@Override
	public void exlog(String msg) {
		// TODO Auto-generated method stub
		Message m = new Message();
		m.what = UuseeGlobal.MW_GET_VIDEO_OK;
		Bundle data = new Bundle();
		data.putString(UuseeGlobal.KEY_VIDEO_URL, msg);
		m.setData(data);
		handler.sendMessage(m);
		Log.e(UuseeGlobal.TAG,msg);
	}

}
