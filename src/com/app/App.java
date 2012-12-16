package com.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Dock.SocketDock;

import com.timer.TimerManager;
import com.transfer.CommandSocket;

public class App {
	public static void main(String[] args){
		//start timer manager
		TimerManager.run();
		
		new CommandSocket(null);
		
		try{
			while(true){
				BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
				String message = strin.readLine();
				if(message == null || message.isEmpty())
					continue;
				SocketDock.sendMessage(message);
			}
		}catch(Exception ex){
			
		}
	}
}
