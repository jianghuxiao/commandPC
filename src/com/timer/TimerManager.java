package com.timer;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import Dock.SocketDock;

import com.transfer.CommandSocket;

/**
 * Timer manager
 * @author Roy
 *
 */
public class TimerManager {
	public static void run(){
		Timer timer = new Timer();
		timer.schedule(new MyTask(), 0, 50000);
	}
	
	private static class MyTask extends java.util.TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Map<String, CommandSocket> socketList = SocketDock.getAllItems();
			if(socketList == null)
				return;
			
			Set<Map.Entry<String, CommandSocket>> set = socketList.entrySet();
	        for (Iterator<Map.Entry<String, CommandSocket>> it = set.iterator(); it.hasNext();) {
	            Map.Entry<String, CommandSocket> entry = (Map.Entry<String, CommandSocket>) it.next();
	            CommandSocket socket = entry.getValue();
	            if(socket != null)
	            	socket.checkConnect();
	        }
		}
		
	}
}
