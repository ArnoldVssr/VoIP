import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.transform.SourceLocator;

public class soundTest 
{

	private static float sampleRate = 44100;
	private static int sampleSizeInBits = 16;
	private static int channels = 2;
	private static boolean signed = true;
	private static boolean bigEndian = false;
	
	//All available Mixers
	public static void GetMixer()
	{
		Info[] test = AudioSystem.getMixerInfo();
		
		for (Info a: test) 
		{
			Mixer mixer = AudioSystem.getMixer(a);
			
			System.out.println("Name: " + a.getName());
			System.out.println("Vendor: " + a.getVendor());
			System.out.println("Descript: " + a.getDescription());					
			System.out.println("Version: " + a.getVersion());
			System.out.println("IsOpen: " + mixer.isOpen());
			System.out.println();
		}
	}

	// Getting a Line of a specific type
	// *not working really ;(
	public static void LineTest()
	{
		AudioFormat test = new AudioFormat(null, 0, 0, 0, 0, 0, false);
		TargetDataLine line;
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, 
		    test); // format is an AudioFormat object
		if (!AudioSystem.isLineSupported(info)) 
		{
		    // Handle the error.    
		}
		    // Obtain and open the line.
		try 
		{
		    line = (TargetDataLine) AudioSystem.getLine(info);
		    line.open(test);
		} 
		catch (LineUnavailableException ex) 
		{
		        // Handle the error.
		    	//... 
		}
	}

	public static void RecordTest()
	{
		TargetDataLine mic;
		SourceDataLine speaker;
		
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels,signed,bigEndian);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object
		DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
		
		if (!AudioSystem.isLineSupported(info))
		{
		    // Handle the error ... 
			System.err.println("Line not supported");
		}
		// Obtain and open the line.
		try 
		{
			int numBytesRead;
			
			System.out.println("Inside Try");
			
			mic = (TargetDataLine) AudioSystem.getLine(info);
			mic.open(format);	
			
			int total = -1;
			int totalToRead = mic.getBufferSize()*20;
			
			System.out.println("Got mic line");
			
			speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
			speaker.open(format);
			
			System.out.println("Got speaker line");
			
			// Assume that the TargetDataLine, line, has already
			// been obtained and opened.
			ByteArrayOutputStream out  = new ByteArrayOutputStream();
			
			byte[] data = new byte[mic.getBufferSize() / 10];
			
			System.out.println("LinebuffSize: " + mic.getBufferSize());
			// Begin audio capture.
			mic.start();
			speaker.start();
			
			System.out.println(totalToRead);
			
			// Here, stopped is a global boolean set by another thread.
			while (total <= totalToRead) 
			{
			   // Read the next chunk of data from the TargetDataLine.
			   numBytesRead =  mic.read(data, 0, data.length);
			   out.write(data,0,numBytesRead);
			   //System.out.println(numBytesRead);
			   if (numBytesRead == -1) break;
			   total += numBytesRead;
			   // Save this chunk of data.
			   speaker.write(data, 0, numBytesRead);
			}   
			speaker.drain();
			
			mic.stop();
			speaker.stop();
			 
			mic.close();
			speaker.close();
		} 
		catch (LineUnavailableException ex) 
		{
		    System.err.println("Line unavailable");// Handle the error ... 
		}		
	}
	
	// Playing file back
	// *not working at all
	public static void FileToByte() throws UnsupportedAudioFileException, IOException
	{
		File myFile = new File("boing003.au");
		byte[] samples;

		AudioInputStream is = AudioSystem.getAudioInputStream(myFile);
		DataInputStream dis = new DataInputStream(is);      //So we can use readFully()
		try
		{
		    AudioFormat format = is.getFormat();
		    samples = new byte[(int)(is.getFrameLength() * format.getFrameSize())];
		    dis.readFully(samples);
		}
		finally
		{
		    dis.close();
		}
		
	}
	
	// Reading sound file for testing purposes only
	// *not working at all really
	public static void ReadFile()
	{
		int totalFramesRead = 0;
		File fileIn = new File("/home/student/17403812/Rw354/Tut4/boing003.au");
		// somePathName is a pre-existing string whose value was
		// based on a user selection.
		try 
		{
			AudioInputStream audioInputStream = 
					AudioSystem.getAudioInputStream(fileIn);
			int bytesPerFrame = 
					audioInputStream.getFormat().getFrameSize();
		    if (bytesPerFrame == AudioSystem.NOT_SPECIFIED)
		    {
		    	// some audio formats may have unspecified frame size
		    	// in that case we may read any amount of bytes
		    	bytesPerFrame = 1;
		    } 
		    
		    // Set an arbitrary buffer size of 1024 frames.
		    int numBytes = 1024 * bytesPerFrame; 
		    byte[] audioBytes = new byte[numBytes];
		    try
		    {
		    	SourceDataLine sourceDataLine = null;
		    	int numBytesRead = 0;
		    	int numFramesRead = 0;
		    	// Try to read numBytes bytes from the file.
		    	while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) 
		    	{
		    		// Calculate the number of frames actually read.
		    		numFramesRead = numBytesRead / bytesPerFrame;
		    		totalFramesRead += numFramesRead;
		    		// Here, do something useful with the audio data that's 
		    		// now in the audioBytes array...
		    		
		    		/**********************************************/
		    		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioInputStream.getFormat());
		    		sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		    		sourceDataLine.open(audioInputStream.getFormat());
		    		sourceDataLine.start();
		    	}
		    	sourceDataLine.flush();
		    	sourceDataLine.drain();
		    	sourceDataLine.close();
		    } 
		    catch (Exception ex)
		    { 
		  		ex.printStackTrace();// Handle the error...
		    }
		} 
		catch (Exception e) 
		{
			e.printStackTrace();// Handle the error...
		}
	}
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException 
	{
		//GetMixer();
		//LineTest();
		RecordTest();
		//FileToByte();
		//Play
		//ReadFile();
		//System.out.println("Starting RecordTest");
		//RecordTest2();
		//System.out.println("Ending RecordTest");
	}
}
