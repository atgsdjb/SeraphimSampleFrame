package com.seraphim.td.remote.upnp;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;

public class SeraphimUpnpStaticTools {
	static public String getActionInfo(Action a){
		StringBuilder sb = new StringBuilder();
		sb.append("{ name = ");
		sb.append(a.getName());
		sb.append(" argumentList(");
		ArgumentList argList = a.getArgumentList();
		for(int i = 0;i<argList.size();i++){
			sb.append('<');
			Argument arg =argList.getArgument(i);
			sb.append(arg.getName());
			sb.append(",");
			sb.append(arg.getDirection());
			sb.append('>');
		}
		sb.append('}');
		return sb.toString();
	}
}
