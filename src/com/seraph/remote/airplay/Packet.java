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
package com.seraph.remote.airplay;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.os.Parcel;
import com.seraph.remote.airplay.mdns.DNSMessage;

/**
 * Encapsulate packet details that we are interested in.
 * @author simmons
 */
public class Packet implements Serializable/* , Parcelable*/   {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1615806139615794034L;
	/**
	 * 
	 */
	private InetAddress src;
    private int srcPort;
    private InetAddress dst;
    private int dstPort;
    private DNSMessage msg;
//    static public final Parcelable.Creator<Packet> Creator = new Creator<Packet>() {
//		
//		@Override
//		public Packet[] newArray(int size) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Packet createFromParcel(Parcel source) {
//			// TODO Auto-generated method stub
//			
//			return new Packet(source);
//		}
//	};
    public Packet(DatagramPacket dp, DatagramSocket socket,DNSMessage msg) {
        src = dp.getAddress();
        srcPort = dp.getPort();
        dst = socket.getLocalAddress();
        dstPort = socket.getLocalPort();
        this.msg = msg;
    }
    
    
    
    
	public Packet(Parcel source) {
		// TODO Auto-generated constructor stub
		this.src = (InetAddress) source.readSerializable();
		this.srcPort = source.readInt();
		this.dst =(InetAddress) source.readSerializable();
		this.dstPort = source.readInt();
		this.msg = (DNSMessage) source.readSerializable();
	}
	/**
	 * geter
	 */
	public InetAddress getSrc() {
		return src;
	}




	public int getSrcPort() {
		return srcPort;
	}




	public InetAddress getDst() {
		return dst;
	}




	public int getDstPort() {
		return dstPort;
	}




	public DNSMessage getMsg() {
		return msg;
	}




//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		// TODO Auto-generated method stub
//		dest.writeSerializable(src);
//		dest.writeInt(srcPort);
//		dest.writeSerializable(dst);
//		dest.writeInt(dstPort);
//		dest.writeSerializable(msg);
//	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(src.toString());
		sb.append(":");
		if(msg != null)
			sb.append("MDNS ="+msg.toString());
		return sb.toString();
	}
}
