package debug;

/**
 * Logger.java: Outputs messages for both debugging and UI purposes.
 * @author Patrick
 *
 */
public class Logger {

	boolean debug = false;
	
	public Logger(boolean setDebug){
		this.debug = setDebug;
	}
	
	public static void outError(String s){
		outError(s, null);
	}
	
	public static void outError(String s, Exception e){
		System.out.println("[err]  "+s);
		if(e!=null){
			e.printStackTrace(); //TODO don't dump a stack trace on the user. Write this to a file.
		}
	}
	
	public static void warn(String s){
		System.out.println("[warn]  "+s);
	}
	
	public void outDebug(String s){
		if(debug){
			System.out.println("[debug] "+s);
		}
	}
	
}
