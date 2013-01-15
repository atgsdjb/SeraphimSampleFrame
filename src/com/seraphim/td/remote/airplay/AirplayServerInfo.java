package com.seraphim.td.remote.airplay;

public class AirplayServerInfo {
/**
 * deviceid	string	58:55:CA:1A:E2:88	MAC address
features	integer	14839	0x39f7
model	string	AppleTV2,1	device model
protovers	string	1.0	protocol version
srcvers	string	120.2	server version
 */
	private String deviceid ;
	private int features;
	private String model;
	private String protovers;
	private String srvers;
	public AirplayServerInfo(String deviceid, int features, String model,
			String protovers, String srvers) {
		super();
		this.deviceid = deviceid;
		this.features = features;
		this.model = model;
		this.protovers = protovers;
		this.srvers = srvers;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public int getFeatures() {
		return features;
	}
	public String getModel() {
		return model;
	}
	public String getProtovers() {
		return protovers;
	}
	public String getSrvers() {
		return srvers;
	}
	
}
