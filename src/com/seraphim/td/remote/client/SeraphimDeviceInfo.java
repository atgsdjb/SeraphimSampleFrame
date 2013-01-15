package com.seraphim.td.remote.client;

public class SeraphimDeviceInfo {
	private String ip;
	private String name;
	private boolean connect;
	public SeraphimDeviceInfo(String ip, String name, boolean connect) {
		super();
		this.ip = ip;
		this.name = name;
		this.connect = connect;
	}
	public String getIp() {
		return ip;
	}
	public String getName() {
		return name;
	}
	public boolean isConnect() {
		return connect;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ip="+ip+"\tHostName="+name+"\tconnect"+connect;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		String str = toString();
		return str.equals(o.toString());
	}
	
	
}
