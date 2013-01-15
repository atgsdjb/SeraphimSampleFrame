/*
 * Copyright 2011 David Simmons
 * http://cafbit.com/entry/testing_multicast_support_on_android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seraphim.td.airplay.mdns;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.util.Log;

/**
 * This class represents a DNS "answer" component.
 * @author simmons
 */
public class DNSAnswer extends DNSComponent implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1449914273512440999L;
	protected String name;
    protected DNSType type;
    protected int aclass;

    protected DNSAnswer(String _name ,DNSType _type) {
        name = _name;
        type = _type;
    }
    public int getAclass(){
    	return this.aclass;
    }
    public String getName(){
    	return this.name;
    }
    public DNSType getType(){
    	return this.type;
    }
    @Override
    public int length() {
        // TODO: implement
        return 0;
    }
    
    @Override
    public void serialize(DNSBuffer buffer) {
        // TODO: implement
    }

   static  public  DNSAnswer parse(DNSBuffer buffer) {
	   DNSAnswer answer = null;
        String name = buffer.readName();
        String rdataString;
        DNSType type = DNSType.getType(buffer.readShort());
        
        int aclass = buffer.readShortAsInt();
        //boolean cacheFlush = ((aclass & 0x8000) != 0);
        aclass = aclass & 0x7FFF;
   
        @SuppressWarnings("unused")
		int ttl = buffer.readInteger();
        
        
        if (type.equals(DNSType.A) || type.equals(DNSType.AAAA)) {
        	byte[] rdata = buffer.readRdata();
            try {
                rdataString = InetAddress.getByAddress(rdata).toString();
            } catch (UnknownHostException e) {
                throw new DNSException("problem parsing rdata");
            }
        } else if (type.equals(DNSType.TXT)) {
        	byte[] rdata = buffer.readRdata();
            rdataString = "";
            for (int i=0; i<rdata.length; ) {
                int length = rdata[i++];
                rdataString += DNSBuffer.bytesToString(rdata, i, length);
                i += length;
                if (i != rdata.length) {
                    rdataString += " // ";
                }
            }
        } else if (type.equals(DNSType.PTR)) {
        	byte[] rdata = buffer.readRdata();
            int oldoffset = buffer.offset;
            buffer.offset -= rdata.length;
            rdataString = buffer.readName();
            if (oldoffset != buffer.offset) {
                throw new DNSException("bad PTR rdata");
            }
        } else if(type.equals(DNSType.SRV)){
        	
        	String service;
			String protocol;
			
			String[] l_strS = name.split("\\.");
			service = l_strS[0];
			protocol = l_strS[1];
			byte[] buffer_l = buffer.readRdata();
			int old_offset = buffer.offset;
			
			int t_offset = buffer_l.length;
			buffer.offset -= t_offset;
			short prority ;
			short wight;
			short port;
			String target;
			prority = buffer.readShort();
			wight = buffer.readShort();
			port = buffer.readShort();
			//buffer.readShort();//target Length
			target = buffer.readName();
			answer = new DNSSRV(name, service, protocol, ttl, prority, wight, port, target);
			buffer.offset = old_offset;
			Log.e("print------ DNSTYPE",answer.toString());
        }else {
        	byte[] rdata = buffer.readRdata();  //pass
            rdataString = "data["+rdata.length+"]";
        }
        if (aclass != 1) {
            throw new DNSException("only class IN supported.  (got "+aclass+")");
        }
        return answer;

    }

@Override
public String toString() {
	return "DNSAnswer [name=" + name + ", type=" + type + ", aclass=" + aclass
			+ "]";
}
   

}