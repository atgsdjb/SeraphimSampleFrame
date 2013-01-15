package com.seraphim.td.airplay.mdns;

import java.io.Serializable;

public class DNSSRV extends DNSAnswer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3148206419095083328L;
	private String service;
	private String protocol;
	private int timeToLive ;
	private short prority;
	private short wight;
	private short  port;
	private String target;
	public DNSSRV(String _name) {
		super(_name,DNSType.SRV);
		// TODO Auto-generated constructor stub
	}
	public DNSSRV(String _name, String service, String protocol,
			int timeToLive, short prority, short wight, short port, String target) {
		super(_name, DNSType.SRV);
		this.service = service;
		this.protocol = protocol;
		this.timeToLive = timeToLive;
		this.prority = prority;
		this.wight = wight;
		this.port = port;
		this.target = target;
	}
	
	public String getService() {
		return service;
	}
	public String getProtocol() {
		return protocol;
	}
	public int getTimeToLive() {
		return timeToLive;
	}
	public short getPrority() {
		return prority;
	}
	public short getWight() {
		return wight;
	}
	public short getPort() {
		return port;
	}
	public String getTarget() {
		return target;
	}
	@Override
	public String toString() {
		return "DNSAnswer {"+super.toString()+"}"+"DNSSRV [service=" + service + ", protocol=" + protocol
				+ ", timeToLive=" + timeToLive + ", prority=" + prority
				+ ", wight=" + wight + ", port=" + port + ", target=" + target
				+ "]";
	}
	
	
}
