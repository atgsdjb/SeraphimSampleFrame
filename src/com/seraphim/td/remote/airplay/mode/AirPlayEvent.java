package com.seraphim.td.remote.airplay.mode;

public class AirPlayEvent extends AirPlayMode {
	
	private String deviceid ;
	private int features;
	private String model;
	private String protovers;
	private String srcvers;
	
	
	public AirPlayEvent( String deviceid, int features, String model,
			String protovers, String srcvers) {
		super(Category.XML);
		this.deviceid = deviceid;
		this.features = features;
		this.model = model;
		this.protovers = protovers;
		this.srcvers = srcvers;
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


	public String getSrcvers() {
		return srcvers;
	}

}
