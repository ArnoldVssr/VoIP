import java.io.*;
import java.net.*;

/**
 * 
 * @author A Visser, 17224047
 * 		   T Butler, sit jou nommer orals in asb ken dit nogsteeds nie
 *
 */
public class Server
{
	
	public static void main(String[] args)
	{
		int portNumber = 3000;
	    boolean listening = true;
	    
		try
	    {
	    	ServerSocket serverSocket = new ServerSocket(portNumber);
	    	
            while (listening)
            {
	            //new NATboxThread(serverSocket.accept()).start();
	        }
	    }
	    catch (IOException IOerror)
	    {
            System.err.println("Could not start server on port: " + portNumber);
            System.exit(-1);
        }

	}

}
