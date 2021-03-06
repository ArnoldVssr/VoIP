import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
/**
 * 
 * @author A Visser, 17224047
 * 		   T Butler, 17403812
 *
 */
public class Client
{
	private static ClientThread _myClient;
	private static String _userName = "";
	private static String _hostName = "";
	private static String _portNumber = "";
	
	//sound rates
	private static String[] SampleRates = { "8000", "11025", "16000", "22050", "32000",
										   "44100", "48000", "50000", "88200", "96000"};
		
	//main window
	private static JFrame mainWindow = new JFrame();
	private static JTextField messageField = new JTextField(20);
	public static JTextArea chatArea = new JTextArea();
	//public static JList<String> onlineUsers = new JList<String>();
	public static JList onlineUsers = new JList();
	private static JComboBox sampleList = new JComboBox(SampleRates);
	
	private static JButton connectButton = new JButton();
	private static JButton disconnectButton = new JButton();
	private static JButton sendButton = new JButton();
	private static JButton callButton = new JButton();
	private static JButton optionButton = new JButton();
	private static JButton voiceNoteButton = new JButton();
	private static JLabel messageLabel = new JLabel("Message: ");
	private static JLabel chatLabel = new JLabel();
	private static JLabel onlineLabel = new JLabel();
	private static JLabel userNameLabel = new JLabel();
	private static JLabel userNameBox = new JLabel();
	private static JScrollPane chatScroller = new JScrollPane();
	private static JScrollPane onlineScroller = new JScrollPane();
	
	//log in window
	private static JFrame loginWindow = new JFrame();
	private static JTextField userNameBoxField = new JTextField(20);
	private static JTextField portNumberField = new JTextField(20);
	private static JTextField hostNameField = new JTextField(20);
	private static JButton enterButton = new JButton();
	private static JLabel enterUserNameLabel = new JLabel();
	private static JLabel enterPortLabel = new JLabel();
	private static JLabel enterHostLabel = new JLabel();
	
	//call window
	private static JFrame callWindow = new JFrame();
	private static JTextField callUserField = new JTextField(15);
	private static JButton makeCallButton = new JButton();
	private static JLabel callUserLabel = new JLabel();
	
	//options
	private static JFrame optionWindow = new JFrame();
	
	private static JRadioButton bits8 = new JRadioButton();
	private static JRadioButton bits16 = new JRadioButton();
	
	private static JRadioButton channel1 = new JRadioButton();
	private static JRadioButton channel2 = new JRadioButton();
	
	private static JRadioButton sign1 = new JRadioButton();
	private static JRadioButton sign0 = new JRadioButton();
	
	private static JRadioButton big = new JRadioButton();
	private static JRadioButton small = new JRadioButton();
	
	
	
	
	//sound
	private static float sampleRate = 16000;
	private static int sampleSizeInBits = 16; //8 or 16
	private static int channels = 2; // 1 or 2
	private static boolean signed = true; //true or false
	private static boolean bigEndian = false; //true or false
	
	public static void main(String[] args)
	{
		BuildMainWindow();
	}
	
	public static void Connect()
	{
		try
		{			
			loginWindow.setVisible(false);
			mainWindow.setVisible(true);
			
			_myClient = new ClientThread(_userName, _hostName, _portNumber);
			_myClient.start();
		}
		catch (Exception e)
		{
			System.out.println("error in connect from clientgui");
			JOptionPane.showMessageDialog(null, "Server not responding");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	public static void BuildMainWindow()
	{
		mainWindow.addWindowListener(
				new WindowAdapter()
				{
					public void windowClosing(WindowEvent evt) 
					{
						DCButton();
					}
				});
		
		mainWindow.setSize(500, 320);
		mainWindow.setResizable(false);
		
		mainWindow.getContentPane().setLayout(null);
		mainWindow.setSize(500, 320);
		
		sendButton.setText("Send");
		mainWindow.getContentPane().add(sendButton);
		sendButton.setBounds(130, 40, 115, 25);
		sendButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						SendButton();
					}
				});
		
		voiceNoteButton.setText("Record");
		mainWindow.getContentPane().add(voiceNoteButton);
		voiceNoteButton.setBounds(250, 40, 115, 25);
		voiceNoteButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						//SendButton();
						//send voice note
					}
				});
		
		
		disconnectButton.setText("Disconnect");
		mainWindow.getContentPane().add(disconnectButton);
		disconnectButton.setBounds(370, 40, 115, 25);
		disconnectButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						DCButton();
					}
				});
		
		callButton.setText("Call");
		mainWindow.getContentPane().add(callButton);
		callButton.setBounds(10, 40, 115, 25);
		callButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						CallButton();
					}
				});
		
		optionButton.setText("Options");
		mainWindow.getContentPane().add(optionButton);
		optionButton.setBounds(370, 4, 115, 25);
		optionButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						OptionButton();
					}
				});
		
		messageLabel.setText("Message:");
		mainWindow.getContentPane().add(messageLabel);
		messageLabel.setBounds(5, 6, 80, 20);
		
		messageField.requestFocus();
		mainWindow.getContentPane().add(messageField);
		messageField.setBounds(80, 4, 260, 30);
		
		chatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		chatLabel.setText("Conversation");
		mainWindow.getContentPane().add(chatLabel);
		chatLabel.setBounds(100, 70, 140, 16);
		
		chatArea.setColumns(20);
		chatArea.setLineWrap(true);
		chatArea.setRows(5);
		chatArea.setEditable(false);
		
		chatScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroller.setViewportView(chatArea);
		mainWindow.getContentPane().add(chatScroller);
		chatScroller.setBounds(10, 90 , 330, 180);
		
		onlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		onlineLabel.setText("Online users:");
		mainWindow.getContentPane().add(onlineLabel);
		onlineLabel.setBounds(350, 70, 130, 16);
		
		onlineScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		onlineScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineScroller.setViewportView(onlineUsers);
		mainWindow.getContentPane().add(onlineScroller);
		onlineScroller.setBounds(350, 90 , 130, 180);
		
		userNameLabel.setText("");
		mainWindow.getContentPane().add(userNameLabel);
		userNameLabel.setBounds(350, 10 , 140, 15);
		
		userNameBox.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(userNameBox);
		userNameBox.setBounds(340, 17 , 150, 20);
		
		BuildLoginWindow();
	}
	
	public static void BuildLoginWindow()
	{		
		loginWindow.setTitle("Cr@p Talk Config");
		loginWindow.setLayout(null);
		loginWindow.setSize(400, 180);
		
		enterHostLabel.setText("Host name:");
		loginWindow.getContentPane().add(enterHostLabel);
		enterHostLabel.setBounds(5, 5, 100, 20);
		
		hostNameField.setText("localhost");
		loginWindow.getContentPane().add(hostNameField);
		hostNameField.setBounds(105, 5, 260, 20);
		
		enterPortLabel.setText("Port number:");
		loginWindow.getContentPane().add(enterPortLabel);
		enterPortLabel.setBounds(5, 35, 100, 20);
		
		portNumberField.setText("3000");
		loginWindow.getContentPane().add(portNumberField);
		portNumberField.setBounds(105, 35, 260, 20);
		
		enterUserNameLabel.setText("Username:");
		loginWindow.getContentPane().add(enterUserNameLabel);
		enterUserNameLabel.setBounds(5, 65, 100, 20);
		
		userNameBoxField.setText("");
		loginWindow.getContentPane().add(userNameBoxField);
		userNameBoxField.setBounds(105, 65, 260, 20);
		
		enterButton.setText("Login");
		loginWindow.getContentPane().add(enterButton);
		enterButton.setBounds(105, 95, 80, 20);
		enterButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						EnterChat();
					}
				});
		
		loginWindow.setVisible(true);
	}
	
	public static void EnterChat()
	{
		if (!userNameBoxField.getText().equals(""))
		{		
			_userName = userNameBoxField.getText().trim();		
			_hostName = hostNameField.getText().trim();
			_portNumber = portNumberField.getText().trim();
			loginWindow.setVisible(false);
			mainWindow.setTitle("Cr@p Talk: " + _userName);
			sendButton.setEnabled(true);
			disconnectButton.setEnabled(true);
			connectButton.setEnabled(false);
			Connect();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter a username.");
		}
	}
	
	public static void SendButton()
	{
		if (!messageField.getText().equals(""))
		{
			char isWhisp = messageField.getText().trim().charAt(0);
			if (isWhisp == '@')
			{
				String temp = messageField.getText().trim();
				int firstSpace = temp.indexOf(",");
				String toUser = temp.substring(1, firstSpace);
				String message = temp.substring(firstSpace +1);
				
				Message whisper = new Message(_userName, toUser, message.trim());
				ClientThread.Send(whisper);
				messageField.setText("");
				messageField.requestFocus();
				
			}
			else
			{
				Message lobby = new Message(_userName, "", messageField.getText());
				ClientThread.Send(lobby);
				messageField.setText("");
				messageField.requestFocus();
			}
		}
	}
	public static void DCButton()
	{
		Message bye = new Message(_userName, "", "%BYE%");
		ClientThread.Send(bye);
	}
	
	public static void OptionButton()
	{
		optionWindow = new JFrame();
		optionWindow.setTitle("Cr@p Sound Config");
		optionWindow.setLayout(null);
		optionWindow.setResizable(false);
		optionWindow.setSize(400, 300);
		
		sampleList.setSelectedIndex(2);
		sampleList.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						JComboBox cb = (JComboBox)event.getSource();
				        float temp = Float.parseFloat((String)cb.getSelectedItem());
				        sampleRate = temp;
					}
				});		
		
		bits8 = new JRadioButton("8bits");
		bits8.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(bits8))
						{
							bits8.setSelected(true);
							sampleSizeInBits = 8;
						}
					}
				});
		bits8.setSelected(false);

		bits16 = new JRadioButton("16bits");
		bits16.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(bits16))
						{
							bits16.setSelected(true);
							sampleSizeInBits = 16;
						}
					}
				});
		bits16.setSelected(true);
        
        ButtonGroup group1 = new ButtonGroup();
        group1.add(bits8);
        group1.add(bits16);
        
        //Put the radio buttons in a column in a panel.
        JPanel bitsPanel = new JPanel(new GridLayout(0, 1));
        bitsPanel.add(bits8);
        bitsPanel.add(bits16);    	
    	
    	channel1 = new JRadioButton("1 channel");
    	channel1.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(channel1))
						{
							channel1.setSelected(true);
							channels = 1;
						}
					}
				});
    	channel1.setSelected(false);

    	channel2 = new JRadioButton("2 channels");
    	channel2.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(channel2))
						{
							channel2.setSelected(true);
							channels = 2;
						}
					}
				});
    	channel2.setSelected(true);
        
        ButtonGroup group2 = new ButtonGroup();
        group2.add(channel1);
        group2.add(channel2);
        
        //Put the radio buttons in a column in a panel.
        JPanel channelPanel = new JPanel(new GridLayout(0, 1));
        channelPanel.add(channel1);
        channelPanel.add(channel2);
    	
    	/*private static JRadioButton big = new JRadioButton();
    	private static JRadioButton small = new JRadioButton();*/
		
    	sign1 = new JRadioButton("signed");
    	sign1.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(sign1))
						{
							sign1.setSelected(true);
							signed = true;
						}
					}
				});
    	sign1.setSelected(true);

    	sign0 = new JRadioButton("unsigned");
    	sign0.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(sign0))
						{
							sign0.setSelected(true);
							signed = false;
						}
					}
				});
    	sign0.setSelected(false);
        
        ButtonGroup group3 = new ButtonGroup();
        group3.add(sign1);
        group3.add(sign0);
        
        //Put the radio buttons in a column in a panel.
        JPanel signPanel = new JPanel(new GridLayout(0, 1));
        signPanel.add(sign1);
        signPanel.add(sign0);       
        
        big = new JRadioButton("big endian");
        big.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(big))
						{
							big.setSelected(true);
							bigEndian = true;
						}
					}
				});
        big.setSelected(false);

        small = new JRadioButton("little endian");
        small.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						if (event.getSource().equals(small))
						{
							small.setSelected(true);
							bigEndian = false;
						}
					}
				});
        small.setSelected(true);
        
        ButtonGroup group4 = new ButtonGroup();
        group4.add(big);
        group4.add(small);
        
        JPanel endianPanel = new JPanel(new GridLayout(0, 1));
        endianPanel.add(big);
        endianPanel.add(small);
	
        JLabel dropLabel = new JLabel("Sample Rates:");
        
        optionWindow.getContentPane().add(dropLabel);
        dropLabel.setBounds(50, 20, 260, 30);
        
		optionWindow.getContentPane().add(sampleList);
		sampleList.setBounds(50, 50, 260, 30);
		
		optionWindow.getContentPane().add(bitsPanel);
		bitsPanel.setBounds(50, 100, 100, 40);
		
		optionWindow.getContentPane().add(channelPanel);
		channelPanel.setBounds(180, 100, 130, 40);
		
		optionWindow.getContentPane().add(signPanel);
		signPanel.setBounds(50, 150, 130, 40);
		
		optionWindow.getContentPane().add(endianPanel);
		endianPanel.setBounds(180, 150, 130, 40);
		
		optionWindow.setVisible(true);
		
	}
	
	public static void CallButton()
	{		
		callWindow = new JFrame();
		callWindow.setTitle("Cr@p Call Config");
		callWindow.setLayout(null);
		callWindow.setSize(320, 100);
		
		callUserLabel.setText("Username:");
		callWindow.getContentPane().add(callUserLabel);
		callUserLabel.setBounds(5, 5, 100, 20);
		
		callUserField.setText("");
		callWindow.getContentPane().add(callUserField);
		callUserField.setBounds(105, 5, 200, 20);
		
		makeCallButton.setText("Call");
		callWindow.getContentPane().add(makeCallButton);
		makeCallButton.setBounds(105, 30, 80, 20);
		makeCallButton.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent event)
					{
						RequestCall();
					}
				});
		
		callWindow.setVisible(true);
		
	}
	
	public static void changeSampleRate()
	{
		
	}
	
	public static void changeBits()
	{
		
	}
	
	public static void changeChannels()
	{
		
	}
	
	public static void changeSigned()
	{
		
	}
	
	public static void changeEndian()
	{
		if (small.isSelected())
		{
			small.setSelected(false);
			big.setSelected(true);
			bigEndian = true;
		}
		else if (big.isSelected())
		{
			big.setSelected(false);
			small.setSelected(true);
			bigEndian = false;
		}
	}
	
	public static void RequestCall()
	{
		if (!callUserField.getText().equals(""))
		{
			Message call = new Message(_userName, callUserField.getText().trim(), "%CALL ME MAYBE%");
			ClientThread.Send(call);
			System.out.println("call request sent");
			callUserField.setText("");
			callWindow.dispose();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter user to call.");
		}
	}
}
