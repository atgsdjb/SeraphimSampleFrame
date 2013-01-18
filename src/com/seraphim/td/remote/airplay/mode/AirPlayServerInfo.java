package com.seraphim.td.remote.airplay.mode;

public class AirPlayServerInfo extends AirPlayMode {
	
	private String deviceid;
	private int features;
	private String model;
	private String protovers;
	private String srcvers;
	
	
	
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
	/**
	 * 
	 * @param deviceid
	 * @param features
	 * @param model
	 * @param protovers
	 * @param srcvers
	 */
	public AirPlayServerInfo( String deviceid, int features,
			String model, String protovers, String srcvers) {
		super(Category.XML);
		this.deviceid = deviceid;
		this.features = features;
		this.model = model;
		this.protovers = protovers;
		this.srcvers = srcvers;
	}
	/**
	 * 
	 * @param xml
	 * @return
	 */
	static public AirPlayServerInfo create(String xml){
		AirPlayServerInfo info = null;
		return info;
	}


}
