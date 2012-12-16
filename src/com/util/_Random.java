package com.util;

import java.sql.Date;

public class _Random {
	/**
	 * create key
	 * @return
	 */
	public static String createKey(){
		Date now = new Date(0); 
		 java.util.Random random = new java.util.Random(); 
		return now.toString() + "_" + random.nextInt() + "_" + random.nextInt();
	}
}
