package Dock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.transfer.CommandSocket;
import com.transfer.cmd.Command;

public class SocketDock {
	private static Map<String, CommandSocket> sSocketMap = new HashMap<String, CommandSocket>();
	
	/**
	 * register
	 * @param threadId
	 * @param commandSockert
	 */
	public static void Register(String threadId, CommandSocket commandSockert){
		handleContext(HandleType.ADD, threadId, commandSockert);
	}
	
	/**
	 * unregister
	 * @param threadId
	 */
	public static void Unregister(String threadId){
		handleContext(HandleType.REMOVE, threadId, null);
	}
	
	/**
	 * get all items
	 * @return
	 */
	public static Map<String, CommandSocket> getAllItems(){
		return (new HashMap<String, CommandSocket>(sSocketMap));
	}
	
	/**
	 * send message
	 * @param message
	 */
	public static void sendMessage(String message){
		Map<String, CommandSocket> map = new HashMap<String, CommandSocket>(sSocketMap);
		Set<Map.Entry<String, CommandSocket>> set = map.entrySet();
        for (Iterator<Map.Entry<String, CommandSocket>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, CommandSocket> entry = (Map.Entry<String, CommandSocket>) it.next();
            CommandSocket socket = entry.getValue();
            if(socket != null)
            	socket.sendCommand(Command.OTHER, message);
        }
	}
	
	private enum HandleType{
		ADD,
		REMOVE
	}
	private synchronized static void handleContext(HandleType type, String threadId, CommandSocket commandSockert){
		if(type == HandleType.ADD){
			sSocketMap.put(threadId, commandSockert);
		}else if(type == HandleType.REMOVE){
			sSocketMap.remove(threadId);
		}
	}
}
