package com.seraphim.td.remote.imp;

public interface AbstractRemoteControl {
	boolean play(String url,float postion);
	boolean stop();
	boolean pause();
	boolean resume();
	
	boolean seek(SeekType type,float postion);
	static public enum SeekType{
		SPEED,REWIND
	}
}
