import javax.swing.*;

/**
 * 
 * @author A Visser, 17224047
 * 		   T Butler, sit jou nommer orals in asb ken dit nogsteeds nie
 *
 */
public class Client
{
	private static ClientThread _myClient;
	private static String _userName = "";
	private static String _hostName = "";
	private static String _portNumber = "";
		
	//main window
	private static JFrame mainWindow = new JFrame();
	private static JTextField messageField = new JTextField(20);
	public static JTextArea chatArea = new JTextArea();
	public static JList onlineUsers = new JList();
	
	private static JButton connectButton = new JButton();
	private static JButton disconnectButton = new JButton();
	private static JButton sendButton = new JButton();
	private static JButton callButton = new JButton();
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
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		
		disconnectButton.setText("Disconnect");
		mainWindow.getContentPane().add(disconnectButton);
		disconnectButton.setBounds(350, 40, 115, 25);
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
	
	public static void RequestCall()
	{
		if (!callUserField.getText().equals(""))
		{
			Message call = new Message(_userName, callUserField.getText().trim(), "%CALL ME MAYBE%");
			ClientThread.Send(call);
			System.out.println("call request sent");
			callUserField.setText("");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter user to call.");
		}
	}
}
