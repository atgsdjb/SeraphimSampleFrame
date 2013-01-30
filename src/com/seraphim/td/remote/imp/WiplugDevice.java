package com.seraphim.td.remote.imp;

import java.net.InetAddress;


public class WiplugDevice extends AirplayDevice {

	public WiplugDevice(UuseeRemoteWarp root, String _name, InetAddress addr,
			short port) {
		super(root, _name,DeviceType.Wiplug, addr, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return httpWarp.wiplugCRLStop();
	}

	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		return httpWarp.wiplugCRLPause();
	}

	@Override
	public boolean resume() {
		// TODO Auto-generated method stub
		return httpWarp.wiplugCRLResume();
	}

	@Override
	public boolean seek(SeekType type, float step) {
		// TODO Auto-generated method stub
		return super.seek(type, step);
	}
	
	
}
