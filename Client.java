import java.io.*;
import java.net.*;

public class Client {

	private static String currentUser;
	private static PrintWriter out;
	private static BufferedReader in;
	
	
	public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println(
                "Usage: java Client <username>");
            System.exit(1);
        }
        currentUser = new String(args[0]);
        
		try {
			
			
			Socket clientSocket = new Socket(InetAddress.getByName("localhost"), 16000);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			BufferedReader stdIn =
				new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;
			
			if (!authenticate(currentUser)){
				System.out.println("Bad username, exiting.");
				System.exit(1);
			}
			
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
				if (fromServer.length() > 5 && fromServer.substring(0,5).equals("Bye, "))
					break;

				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("Client: " + fromUser);
					out.println(fromUser);
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Unable to connect to localhost.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("The connection to localhost experienced an I/O error.");
			System.exit(1);
		}
	}
	
	private static boolean authenticate(String user) throws IOException{
		out.println("Username:" + currentUser);
		String fromServer;
		if ((fromServer = in.readLine()) != null && fromServer.length() > 9 
				&& fromServer.substring(0, 9).equals("Welcome, ")){
			return true;
		}
		else{
			return false;
		}
	}
}
