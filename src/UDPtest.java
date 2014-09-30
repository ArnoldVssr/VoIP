import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPtest {
	
	public static DatagramSocket socket;
	
	public static void main(String[] args) throws IOException
	{
		
		int choice = Integer.parseInt(args[0]);
		
		
		if (choice == 1)	//Sender
		{
			socket = new DatagramSocket();
			byte[] buffer = "Sender".getBytes();
			
			InetAddress[] testAddr = InetAddress.getAllByName("narga-h62");

			socket.connect(testAddr[0], 3000);
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, testAddr[0], 3000);
			socket.send(packet);
			
			packet = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(packet);
			byte[] message = packet.getData();
			
			for (byte mes: message)
			{
				System.out.print((char)mes);
			}
			System.out.println();
			
		}
		else				//Receiver
		{
			socket = new DatagramSocket(3000);
			
			byte[] buffer = new byte[100];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(packet);
			
			byte[] message = packet.getData();
			
			for (byte mes: message)
			{
				System.out.print((char)mes);
			}
			System.out.println();
			
			buffer = "Received".getBytes();
			DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
			
			
			socket.send(rPacket);
		}
	}
}
