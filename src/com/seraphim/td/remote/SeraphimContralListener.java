package com.seraphim.td.remote;

import java.util.Map;

public interface SeraphimContralListener {
	public void uusee_cmd_play(String url);
	public void uusee_cmd_play(String url,Map<String,String> mimeData);
	public void uusee_cmd_push();
	public void uusee_cmd_seekTo(long position);
	public void uusee_cmd_resume();
	public void uusee_cmd_fast_forward();
	public void uusee_cmd_fast_forward_stop();
	public void uusee_cmd_rewind();
	public void uusee_cmd_rewind_stop();
	public void getExVideoFailed(String code);
	public void exlog(String msg);
}
