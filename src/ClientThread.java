import java.io.*;
import java.net.*;

/**
 * 
 * @author A Visser, 17224047
 * 		   T Butler, sit jou nommer orals in asb ken dit nogsteeds nie
 *
 */
public class ClientThread extends Thread
{
	private static Socket socket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
	public static User user = new User();
	public static String username = "";
	public static String hostname = "";
	public static String portnum = "";
	public static String musername = "";
	public static String message = "";	
	
	/**
	 * constuctor to start a ClientThread
	 * 
	 * @param 
	 */
	public ClientThread(String user, String host, String port)
	{
		super();
		ClientThread.username = user;
		ClientThread.hostname = host;
		ClientThread.portnum = port;
	}
	
	public void run()
	{
		InetAddress address;
		int port = -1;
		
		try 
		{
			socket = new Socket(hostname, Integer.parseInt(portnum));
						
			sendbuf = new byte[socket.getSendBufferSize()];
			recbuf = new byte[socket.getReceiveBufferSize()];
						
			address = socket.getInetAddress();
			port = socket.getPort();
			user = new User(username, address, port);
						
			sendbuf = toByteArray(user);
			socket.getOutputStream().write(Message.USER);
			socket.getOutputStream().write(sendbuf);
			socket.getOutputStream().flush();
			
			while(true)
			{
				Message rec = new Message();
				try
				{
					socket.getInputStream().read(recbuf);
					rec = (Message) toObject(recbuf);
				}
				finally
				{
					if (rec.getMessage().equals("%BYE%"))
					{
						System.out.println("in break");
						rec = new Message();
						break;
					}
					else if (rec.getRecipient() != "" || rec.getOrigin().equalsIgnoreCase("server"))
					{	
						System.out.println("in if");
						Client.chatArea.append("[" + rec.getOrigin() +"]: " + rec.getMessage() + "\n");

					}
				}
				//for if server suddenly crashes.
				//dis wat die infinite prints veroorsaak, die ou msg is nog in recvbuf.........
				recbuf = new byte[socket.getReceiveBufferSize()];
			}
			System.out.println("out of while");
        	socket.close();
        	System.exit(0);
		}
		catch (SocketException socketError) 
		{
			System.err.println(socketError.getMessage());
		} 
		catch (IOException e)
		{
			System.err.println("IO Exception");
		} 
		catch (ClassNotFoundException e) 
		{
			System.err.println(e.getMessage());
		} 
	}
	
	public static void Send(Message message)
	{
		try
		{
			sendbuf = toByteArray(message);
			if (message.getRecipient().equalsIgnoreCase("") &&
				message.getMessage().equalsIgnoreCase("%BYE%"))
			{
				socket.getOutputStream().write(Message.BYE);
			}
			else if (message.getRecipient().equalsIgnoreCase(""))
			{
				socket.getOutputStream().write(Message.LOBBY);
			}
			else
			{
				socket.getOutputStream().write(Message.WHISPER);
			}
			socket.getOutputStream().write(sendbuf);
			socket.getOutputStream().flush();
		}
		catch (Exception e)
		{
			System.out.println("error in send MyClient");
		}
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
