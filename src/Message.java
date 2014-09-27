import java.io.Serializable;


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
    public static final int BYE = 9;
	
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