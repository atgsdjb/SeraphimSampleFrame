package com.seraphim.td.remote.airplay.mode;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class AirPlayProperty extends AirPlayMode{
	private int errorCode;
	private List<Value> valueList;

	//SAX Prase  XML
	private ContentHandler mHandler = new DefaultHandler() {
		AirPlayProperty father = AirPlayProperty.this;
		String currKey;//防止频分出栈.
		String currValue;
		Value value;
		List<Value> valueList;
		boolean isKey = false;
		int deep = -1;
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			deep++;
			if(localName.equals("plist")){
				
			}else if(localName.equals("dict")){
				if(deep == 3){  //针对该格式XML
					value = new Value();
				}
			}else if(localName.equals("array")){
				//该格式XML遇到array标签时一定是空的,不判断
				valueList = new ArrayList<Value>();
			}else if(localName.equals("key")){
				isKey=true;
			}else{
				isKey=false;
			}
			
		}
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			String l_str = new String(ch,start,length);
			if(l_str.trim().equals("")){
				return;
			}
			if(isKey){
				currKey = l_str;
			}else{
				currValue = l_str;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			
			deep--;
			if(localName.equals("dict")){
				if(deep == 2){
					valueList.add(value);
				}
				
			}else if(localName.equals("array")){
				father.valueList = valueList;
				
			}else if(localName.equals("key")){
				
			}else{
				if(deep == 3){
					if(currKey.equals("bytes")){
						value.bytes = currValue;
					}else if(currKey.equals("c-duration-downloaded")){
						value.cDurationDownloaded = currValue;
						
					}else if(currKey.equals("c-duration-watched")){
						value.cDurationWatched = currValue;
						
					}else if(currKey.equals("c-frames-dropped")){
						value.cFramesDropped = currValue;
						
					}else if(currKey.equals("c-observed-bitrate")){
						value.cObservedBitrate = currValue;
						
					}else if(currKey.equals("c-overdue")){
						value.cOverdue = currValue;
						
					}else if(currKey.equals("c-stalls")){
						value.cStalls = currValue;
						
					}else if(currKey.equals("c-start-time")){
						value.cStartTime = currValue;
						
					}else if(currKey.equals("c-startup-time")){
						value.cStartupTime = currValue;
						
					}else if(currKey.equals("cs-guid")){
						value.csGuid = currValue;
						
					}else if(currKey.equals("date")){
						value.date = currValue;
						
					}else if(currKey.equals("s-ip")){
						value.sIp = currValue;
						
					}else if(currKey.equals("s-ip-changes")){
						value.sIpChanges = currValue;
						
					}else if(currKey.equals("sc-count")){
						value.scCount = currValue;
						
					}else if(currKey.equals("uri")){
						value.uri = currValue;
						
					}
					
				}else if(currKey.equals("errorCode")){
					father.errorCode = Integer.valueOf(currValue);
				}else{
					System.out.println("error ---- NL 84");
				}
			}
			
			
			
			
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
			if(deep!= -1){
				System.out.println("$$$$$$$$$$$$$   ERROR $$$$$$$$$$$$$$$$$$$$");
			}
		}
		
		
	};
	
	/**
	 * 
	 * @param type
	 * @param errorCode
	 * @param value
	 */
	public AirPlayProperty( int errorCode, List<Value> value) {
		super(Category.XML);
		this.errorCode = errorCode;
		this.valueList = value;
	}
	public AirPlayProperty(String xml){
		super(Category.XML);
		/**
		 * input
		 */
		try{
			byte[] buffer = new byte[102400];
			FileInputStream fin = new FileInputStream("/mnt/sdcard/xml/airplay_property.xml");
			int len = fin.read(buffer);
			String xml2 = new String(buffer,0,len);
			SAXParserFactory factory=SAXParserFactory.newInstance();
			XMLReader reader=factory.newSAXParser().getXMLReader();
			reader.setContentHandler(mHandler);
			reader.parse(new InputSource(new StringReader(xml2)));
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public int getErrorCode() {
		return errorCode;
	}


	public List<Value> getValue() {
		return valueList;
	}

	
	@Override
	public String toString() {
		return "AirPlayProperty [errorCode=" + errorCode + ", valueList="
				+ valueList + ", mHandler=" + mHandler + "]";
	}


	/**
	 * 
	 * @author root
	 *
	 */
	static public class Value{
		
		
		/*
		 * 
		 */
		public Value(String bytes, String cDurationDownloaded,
				String cDurationWatched, String cFramesDropped,
				String cObservedBitrate, String cOverdue, String cStalls,
				String cStartTime, String cStartupTime, String csGuid, String date,
				String sIp, String sIpChanges, String scCount, String uri) {  //怕麻烦,都当作字符串处理
			super();
			this.bytes = bytes;
			this.cDurationDownloaded = cDurationDownloaded;
			this.cDurationWatched = cDurationWatched;
			this.cFramesDropped = cFramesDropped;
			this.cObservedBitrate = cObservedBitrate;
			this.cOverdue = cOverdue;
			this.cStalls = cStalls;
			this.cStartTime = cStartTime;
			this.cStartupTime = cStartupTime;
			this.csGuid = csGuid;
			this.date = date;
			this.sIp = sIp;
			this.sIpChanges = sIpChanges;
			this.scCount = scCount;
			this.uri = uri;
		}
		private Value() {
			// TODO Auto-generated constructor stub
		}
		public String bytes;
		public String cDurationDownloaded;
		public String cDurationWatched;
		public String cFramesDropped;
		public String cObservedBitrate;
		public String cOverdue;
		public String cStalls;
		public String cStartTime;
		public String  cStartupTime;
		public String csGuid;
		public String date;
		public String sIp;
		public String  sIpChanges;
		public String scCount;
		public String uri;
		@Override
		public String toString() {
			return "Value [bytes=" + bytes + ", cDurationDownloaded="
					+ cDurationDownloaded + ", cDurationWatched="
					+ cDurationWatched + ", cFramesDropped=" + cFramesDropped
					+ ", cObservedBitrate=" + cObservedBitrate + ", cOverdue="
					+ cOverdue + ", cStalls=" + cStalls + ", cStartTime="
					+ cStartTime + ", cStartupTime=" + cStartupTime
					+ ", csGuid=" + csGuid + ", date=" + date + ", sIp=" + sIp
					+ ", sIpChanges=" + sIpChanges + ", scCount=" + scCount
					+ ", uri=" + uri + "]";
		}
		
		
		
		
	}
	
		
}
