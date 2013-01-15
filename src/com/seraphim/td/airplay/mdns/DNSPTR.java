package com.seraphim.td.airplay.mdns;

public class DNSPTR extends DNSAnswer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3496391614498649783L;

	protected DNSPTR(){
		super(null,DNSType.PTR);
	}
}
