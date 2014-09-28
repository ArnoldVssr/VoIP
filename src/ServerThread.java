import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * 
 * @author A Visser, 17224047
 * 		   T Butler, sit jou nommer orals in asb ken dit nogsteeds nie
 *
 */
public class ServerThread extends Thread
{
	private Socket socket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
    private static User cur_user = new User();
	
    public ServerThread(Socket socket)
    {
    	super();
    	this.socket = socket;
    }
    
	public void run()
	{
        try
        {
        	int state;
        	System.out.println("remote Socket Address " + socket.getRemoteSocketAddress());
        	
        	sendbuf = new byte[socket.getSendBufferSize()];
        	recbuf = new byte[socket.getReceiveBufferSize()];
        	
        	state = socket.getInputStream().read();
    		if (state == Message.USER)
    		{
    			Server.prevLen = Server.users.size();
	        	socket.getInputStream().read(recbuf);
				cur_user = (User) toObject(recbuf);
				Server.users.add(cur_user);
				Server.Maptest.put(cur_user.getName(),socket);
				Server.usernames.add(cur_user.getName());
				Server.curLen = Server.users.size();
    		}
			
        	while(true)
        	{
        		
        		PrintUsers();
        		state = socket.getInputStream().read();
        		if (state == Message.WHISPER)
        		{
        			socket.getInputStream().read(recbuf);
        			Message Temp = (Message) toObject(recbuf);
        			Socket rec = Server.Maptest.get(Temp.getRecipient());
        			if (rec != null)
        			{
        				sendbuf = toByteArray(Temp);
        				rec.getOutputStream().write(sendbuf);
        				rec.getOutputStream().flush();
        			}
        			rec = Server.Maptest.get(Temp.getOrigin());
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();
        		}
        		else if (state == Message.LOBBY)
        		{
        			socket.getInputStream().read(recbuf);
        			Message Temp = (Message) toObject(recbuf);
        			System.out.println("CH recv: " + Temp.getMessage());
    				for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
    				{
    					sendbuf = toByteArray(Temp);
    					entry.getValue().getOutputStream().write(sendbuf);
    					entry.getValue().getOutputStream().flush();
    				}
        		}
        		else if (state == Message.HASHSET)
        		{
        			for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
    				{
        				sendbuf = toByteArray(Message.HASHSET);
    					entry.getValue().getOutputStream().write(sendbuf);
    					entry.getValue().getOutputStream().flush();
        				
    					sendbuf = toByteArray(Server.usernames);
    					entry.getValue().getOutputStream().write(sendbuf);
    					entry.getValue().getOutputStream().flush();
    				}
        		}
        		else if (state == Message.CALL)
        		{
        			System.out.println("shit what to do now");
        			socket.getInputStream().read(recbuf);
        			Message temp = (Message) toObject(recbuf);
        			Socket rec = null;
        			if (!Server.usernames.contains(temp.getRecipient()))
        			{
        				Message noUser = new Message("server", temp.getOrigin(), "%NOPE NOPE NOPE%");
        				sendbuf = toByteArray(noUser);
        				rec = Server.Maptest.get(temp.getOrigin());
            			rec.getOutputStream().write(sendbuf);
        				rec.getOutputStream().flush();
        				
        			}
        			else
        			{
        				sendbuf = toByteArray(temp);
        				rec = Server.Maptest.get(temp.getRecipient());
        				rec.getOutputStream().write(sendbuf);
        				rec.getOutputStream().flush();
        			}
        		}
        		else if (state == Message.IGNORE)
        		{
        			System.out.println("declined");
        			socket.getInputStream().read(recbuf);
        			Message temp = (Message) toObject(recbuf);
        			Socket rec = Server.Maptest.get(temp.getRecipient());
        			sendbuf = toByteArray(temp);
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();
        		}
        		else if (state == Message.ACCEPT)
        		{
        			System.out.println("accepted");
        			socket.getInputStream().read(recbuf);
        			Message temp = (Message) toObject(recbuf);
        			Socket rec = Server.Maptest.get(temp.getRecipient());
        			sendbuf = toByteArray(temp);
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();
        		}
        		else if (state == Message.BYE)
        		{
        			socket.getInputStream().read(recbuf);
        			Message Temp = (Message) toObject(recbuf);
        			
        			Message send = new Message("server", "", Temp.getOrigin() + " disconnected...");
        			for(Map.Entry<String, Socket> entry: Server.Maptest.entrySet())
    				{
    					sendbuf = toByteArray(send);
    					entry.getValue().getOutputStream().write(sendbuf);
    					entry.getValue().getOutputStream().flush();
    				}
        			
        			sendbuf = toByteArray(Temp);
        			Socket rec = Server.Maptest.get(Temp.getOrigin());
        			rec.getOutputStream().write(sendbuf);
    				rec.getOutputStream().flush();      			
        			
        			User dc = new User(Temp.getOrigin(), null, 0);
            		//Server.Maptest.remove(Temp.getOrigin());
        			Server.prevLen = Server.usernames.size();
            		Server.Maptest.remove(Temp.getOrigin());
            		Server.users.remove(dc);
            		Server.usernames.remove(Temp.getOrigin());
            		Server.curLen = Server.usernames.size();
            		PrintUsers();
            		break;
        		}
        	}
        	socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e)
        {
			e.printStackTrace();
		}
	}
	
	public static void PrintUsers()
	{
		if (Server.curLen == 0)
		{
			Server._serverLog.append("Users: \n");
			Server._serverLog.append(" none\n");
		}
		else if (Server.prevLen != Server.curLen)
		{
			Server._serverLog.append("Users: \n");
			for (int i = 0; i < Server.usernames.size(); i++)
			{
				Server._serverLog.append(" " + Server.usernames.get(i) + "\n"); 
			}
		}
		
		Server._serverLog.append("\n");
	}
	
	public static byte[] toByteArray(Object obj) throws IOException
    {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        
        try 
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        }
        finally
        {
            if (oos != null)
            {
                oos.close();
            }
            if (bos != null)
            {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException
    {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        
        try
        {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        }
        finally
        {
            if (bis != null)
            {
                bis.close();
            }
            if (ois != null)
            {
                ois.close();
            }
        }
        return obj;
    }

}
