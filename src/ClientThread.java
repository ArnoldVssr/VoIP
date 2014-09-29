import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

/**
 * 
 * @author A Visser, 17224047
 * 		   T Butler, sit jou nommer orals in asb ken dit nogsteeds nie
 *
 */
public class ClientThread extends Thread
{
	private static Socket socket = null;
	private static DatagramSocket callSocket = null;
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
		int state = 0;
		
		try 
		{
			socket = new Socket(hostname, Integer.parseInt(portnum));
						
			sendbuf = new byte[socket.getSendBufferSize()];
			recbuf = new byte[socket.getReceiveBufferSize()];
						
			address = socket.getInetAddress();
			port = socket.getPort();
			user = new User(username, address, port);
			
			//send user data to server
			sendbuf = toByteArray(user);
			socket.getOutputStream().write(Message.USER);
			socket.getOutputStream().write(sendbuf);
			socket.getOutputStream().flush();
			
			while(true)
			{
				Message rec = new Message();
				try
				{
					state = socket.getInputStream().read();
				}
				finally
				{
					if (state == Message.HASHSET)
					{
						socket.getInputStream().read(recbuf);
						Object[] online = (Object[]) toObject(recbuf);
						String[] onlineList = new String[online.length];
						for (int i = 0; i < online.length; i++)
						{
							onlineList[i] = (String) online[i];
						}
						Client.onlineUsers.setListData(onlineList);
					}
					else if (state == Message.LOBBY)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						
						Client.chatArea.append("[" + rec.getOrigin() +"]: " + rec.getMessage() + "\n");
					}
					else if (state == Message.WHISPER)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						
						Client.chatArea.append("[(whisp)" + rec.getOrigin() +"]: " + rec.getMessage() + "\n");
					}
					else if (state == Message.DC)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						
						Client.chatArea.append(rec.getMessage());
					}
					else if (state == Message.NONEXISTANT)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						JOptionPane.showMessageDialog(null, "Imaginary people like \"" + rec.getRecipient() + "\" don't count...");
					}
					else if (state == Message.REQUEST)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						
						Message confirmation = new Message();
						String prompt = "Accept call from " + rec.getOrigin() + "?";
						int reply = JOptionPane.showConfirmDialog(null, prompt, "Call confirmation", JOptionPane.YES_NO_OPTION);
				        if (reply == JOptionPane.YES_OPTION)
				        {
				        	//send true
				        	confirmation = new Message(rec.getOrigin(), rec.getRecipient(), "%SURE SURE%");
				        	Send(confirmation);
				        	callSocket = new DatagramSocket();
				        	//call a method to start calls?
				        }
				        else 
				        {
				        	//send false
				        	confirmation = new Message(rec.getOrigin(), rec.getRecipient(), "%IMPOSSIBRU%");
				        	Send(confirmation);
				        }
					}
					else if (state == Message.ACCEPT)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						JOptionPane.showMessageDialog(null, rec.getRecipient() +" Accepted");
						//call a method to start calls?
					}
					else if (state == Message.DECLINE)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						JOptionPane.showMessageDialog(null, rec.getRecipient() +" Declined");
						callSocket.close();
						
					}
					else if (state == Message.REMOVED)
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) toObject(recbuf);
						System.exit(0);
					}
				}
			}
		}
		catch (SocketException e) 
		{
			e.printStackTrace();
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
	
	public static void Send(Message message)
	{
		try
		{
			sendbuf = toByteArray(message);

			//dc message
			if (message.getRecipient().equalsIgnoreCase("") &&
				message.getMessage().equalsIgnoreCase("%BYE%"))
			{
				socket.getOutputStream().write(Message.BYE);
			}
			//call message
			else if (message.getMessage().equalsIgnoreCase("%CALL ME MAYBE%"))
			{
				socket.getOutputStream().write(Message.CALL);
				callSocket = new DatagramSocket();
			}
			//accepted call message
			else if (message.getMessage().equalsIgnoreCase("%SURE SURE%"))
			{
				socket.getOutputStream().write(Message.ACCEPT);
			}
			//rejected call message
			else if (message.getMessage().equalsIgnoreCase("%IMPOSSIBRU%"))
			{
				socket.getOutputStream().write(Message.DECLINE);
			}
			//lobby message
			else if (message.getRecipient().equalsIgnoreCase(""))
			{
				socket.getOutputStream().write(Message.LOBBY);
			}
			//whisper message
			else
			{
				socket.getOutputStream().write(Message.WHISPER);
			}
			socket.getOutputStream().write(sendbuf);
			socket.getOutputStream().flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
