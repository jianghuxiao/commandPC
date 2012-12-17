package com.transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Dock.ISocket;
import Dock.SocketDock;

import com.transfer.cmd.Command;
import com.transfer.cmd.DataPackage;
import com.util.Config;
import com.util.LogTool;
import com.util._Random;

public class CommandSocket implements ISocket {
	
	Socket socket = null;
	DataOutputStream out = null;
	DataInputStream in = null;
	
	String currentID = null;
	
	public CommandSocket(String currentId)
	{
		CreateSocker(currentId);
	}
	
	/**
	 * create socket
	 */
	private void CreateSocker(String currentId){
		Thread thread = new Thread(runnable);
		thread.start();
		
		if(currentId != null)
			currentID = currentId;
		else
			currentID = _Random.createKey();

		SocketDock.Register(currentID, this);
	}
	
	Runnable runnable = new Runnable(){
		public void run() {
			// TODO Auto-generated method stub
			try {
				socket = new Socket(Config.IP_ADDRESS, Config.PORT);
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				
				sendCommand(Command.CONNECTING, null);

				while(true){
					String info = in.readUTF();
					
					//System.out.println("Response info: " + info);
					
					CmdInfo cmdInfo = parseCmdInfo(info);
					if(cmdInfo == null)
						break;
					
					switch(cmdInfo.command){
						case Command.SUCCESS:
							break;
						case Command.OTHER:
							System.out.println(cmdInfo.message);
							break;
					}
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogTool.printException(e);
			}finally{
				try{
					in.close();
					out.close();
					socket.close();
				}catch(IOException e){}
			}
		}
	};
	
	private class CmdInfo{
		public int command = Command.OPEN_BROWER;
		public String message = null;
	}
	
	/**
	 * parse cmd info
	 * @param commandInfo
	 * @return
	 */
	private CmdInfo parseCmdInfo(String commandInfo){
		String[] cmdStr = commandInfo.split(Config.DELIMITER);
		if(cmdStr.length < 2)
			return null;
		
		try{
			CmdInfo info = new CmdInfo();
			info.command = Integer.parseInt(cmdStr[0]);
			info.message = cmdStr[1];
			
			return info;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * send command
	 * @param cmd
	 */
	public synchronized void sendCommand(int cmd, String message){
		try{
			switch(cmd){
				case Command.CONNECTING:
					out.writeUTF(DataPackage.createCmdInfo(Command.CONNECTING, message));
					out.flush();
					break;
				case Command.CLOSE_CONNECT:
					out.writeUTF(DataPackage.createCmdInfo(Command.CLOSE_CONNECT, message));
					out.flush();
					break;
				case Command.OPEN_BROWER:
					out.writeUTF(DataPackage.createCmdInfo(Command.OPEN_BROWER, message));
					out.flush();
					break;
				case Command.CLOSE_BROWER:
					out.writeUTF(DataPackage.createCmdInfo(Command.CLOSE_BROWER, message));
					out.flush();
					break;
				case Command.CLOSE_MACHINE:
					out.writeUTF(DataPackage.createCmdInfo(Command.CLOSE_MACHINE, message));
					out.flush();
					break;
				case Command.RESTART_MACHINE:
					out.writeUTF(DataPackage.createCmdInfo(Command.RESTART_MACHINE, message));
					out.flush();
					break;
				case Command.OTHER:
					out.writeUTF(DataPackage.createCmdInfo(Command.OTHER, message));
					out.flush();
					break;
			}
		}catch(Exception e){
			LogTool.printException(e);
			try{
				in.close();
				out.close();
				socket.close();
			}catch(IOException ex){
				LogTool.printException(ex);
			}
			
			handleException();
		}
	}
	
	/**
	 * handle exception
	 */
	private void handleException(){
		SocketDock.Unregister(currentID);
		
		new CommandSocket(currentID);
	}

	@Override
	public void checkConnect() {
		// TODO Auto-generated method stub
		sendCommand(Command.CONNECTING, null);
	}
}
