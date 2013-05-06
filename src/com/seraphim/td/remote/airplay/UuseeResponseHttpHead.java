package com.seraphim.td.remote.airplay;
/**
 * http response head
 * from String create
 * 从字符串构建http应答头
 * 假设,一次从TCP读取所有应答的数据.
 * --20130116只处理AirPlay void 服务的应答头
 * @author root
 *
 */
public class UuseeResponseHttpHead {
	private int codeState;
	private String stateStr;
	private String date;
	private int  contentLength;
	private String contentType;
	private String xTransmitDate;
	private String xAppleSessionID;
	
	public UuseeResponseHttpHead(String buff){
		try{
			int index = buff.indexOf("\n");
			String state = null;
			String para = null;
			if(index >0 && index <= buff.length()){
				state = buff.substring(0, index);
				String l_state_a[] = state.split(" ");
				if(l_state_a.length == 3){
					this.codeState = Integer.valueOf(l_state_a[1]);
					this.stateStr = l_state_a[2];
					para = buff.substring(index+1);
					if(para != null)
						process(para);
				}else{
					
				}
				
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	/**
	 * 
	 */
	private void process(String buff){
		String[] str_a = buff.split("\r\n");
		for(String s :str_a){
			String[] keyValue= s.split(":");
			if(keyValue.length !=2){
				//bad  line
				continue;
			}
			String key  = keyValue[0].trim();
			String value = keyValue[1].trim();
			if(key.equals("Date")){
				this.date = value;
				
			}else if(key.equals("Content-Length")){
				this.contentLength = Integer.valueOf(value);
			}else if(key.equals("Content-Type")){
				this.contentType = value;
			}else if(key.equals("X-Apple-Session-ID")){
				this.xAppleSessionID = value;
				
			}else if(key.equals("")){
				
			}else if(key.equals("")){
				
			}
			
			
			
			
		}
	}
	/**
	 * 
	 * @return
	 */
	public int getCodeState() {
		return codeState;
	}
	public String getStateStr() {
		return stateStr;
	}
	public String getDate() {
		return date;
	}
	public int getContentLength() {
		return contentLength;
	}
	public String getContentType() {
		return contentType;
	}
	public String getxTransmitDate() {
		return xTransmitDate;
	}
	public String getxAppleSessionID() {
		return xAppleSessionID;
	}
	
}

////Content mime type 
//static private class Type {
//	static private class SubType{};
//	static private class Text extends SubType{
//	static final String xml="x-apple-binary-plist";
//	static final String parameters ="parameters";
//	}
//	static SubType getSubType(String sub){
//		SubType type = null;
//		if(sub.equals("text")){
//			type = new SubType();
//		}
//		return  type;
//	}
//}
//private void process(){
//	Type.SubType t = Type.getSubType(""); 
//	if(t instanceof Type.Text){
//		Type.Text text = (Type.Text)t;
//		if(text.xml.equals("")){
//			
//		}else if(text.parameters.equals("")){
//			
//		}
//	}
//}