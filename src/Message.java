import java.io.Serializable;

/**
 * 
 * @author A Visser, 17224047
 * 		   T Butler, 17403812
 *
 */
public class Message implements Serializable
{
	private String toUser = "";
	private String fromUser = "";
	private String body = "";
    private static final long serialVersionUID = 42L;
    
    public static final int USER = 0;
	public static final int WHISPER = 1;
    public static final int LOBBY = 2;
    public static final int HASHSET = 3;
    public static final int DC = 4;
    public static final int REMOVED = 5;
    public static final int CALL = 6;
    public static final int NONEXISTANT = 41;
    public static final int REQUEST = 48;
    public static final int DECLINE = 7;
    public static final int ACCEPT = 8;
    public static final int BYE = 9;
    public static final int SERVERDOWN = 10;
    public static final int ERROR = 115;
	
	public Message()
    {
        this.toUser = "";
        this.body = "";
        this.fromUser = "";
    }
	
	public Message(String fromUser,String toUser, String body)
    {
		this.fromUser = fromUser;
        this.toUser = toUser;
        this.body = body;
    }
	
	public String getMessage()
	{
		return body;
	}
	
	public String getRecipient()
	{
		return toUser;
	}
	
	public String getOrigin()
	{
		return fromUser;
	}
}