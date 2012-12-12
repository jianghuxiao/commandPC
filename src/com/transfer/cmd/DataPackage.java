package com.transfer.cmd;

import com.util.Config;

public class DataPackage {
	public static String createCmdInfo(int cmd, String message){
		if(message == null)
			message = "null";
		
		return cmd + Config.DELIMITER + message;
	}
}
