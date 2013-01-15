package com.seraphim.td.remote.client;

import org.json.JSONException;
import org.json.JSONObject;

public class SeraphimRemoteCMD {
	public static class PLAY{
		String url;
		public PLAY(String url){
			this.url = url;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "play"+"|"+url);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
		
		
	}
	public static class PAUSE{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "pause");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
		
	}
	public static class FORWARD{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "forward");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	public static class FORWARD_STOP{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "forward_stop");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	
	public static class REWIND{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "rewind");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	public static class REWIND_STOP{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "rewind_stop");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	public static class RESUME{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "resume");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	
//	volume_up
	public static class VOLUME_UP{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "volume_up");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	public static class VOLUME_DOWN{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "volume_down");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	public static class WAIY_URL{
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "wait_url");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
			
	   }
	}
	public static class STOP{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "stop");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	public static class TickTock{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("cmd", "tick-tock");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jobj.toString();
		}
	}
	
}
