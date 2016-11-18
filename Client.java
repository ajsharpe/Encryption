import java.io.*;
import java.nio.*;
import java.net.*;

public class Client {

	private static String currentUser, key;
	private static PrintWriter out;
	private static BufferedReader in;
	
	
	public static void main(String[] args) throws IOException {
        //User must enter username and password as args[]
        if (args.length != 2) {
            System.err.println(
                "Usage: java Client <username> <password>");
            System.exit(1);
        }
        currentUser = new String(args[0]);
        key = new String(args[1]);
        
		try {
			//Initialize connection
			Socket clientSocket = new Socket(InetAddress.getByName("localhost"), 16000);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;
			
			//Authenticate user
			if (!authenticate()){
				System.out.println("Invalid username or password, exiting.");
				System.exit(1);
			}
			
			//Infinite loop for I/O to/from server
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
				if (fromServer.length() > 5 && fromServer.substring(0,5).equals("Bye, "))
					break;

				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("Client: " + fromUser);
					//out.println(fromUser);
					toServer(fromUser);
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
	
	private static boolean authenticate() throws IOException{
		out.println("Username:" + currentUser);
		out.println("Password:" + key);
		String fromServer;
		if ((fromServer = in.readLine()) != null && fromServer.length() > 9 
				&& fromServer.substring(0, 9).equals("Welcome, ")){
			System.out.println(fromServer);
			return true;
		}
		return false;
	}
	
	private static void toServer(String str){
		int[] encryptedData = Encryption.encrypt(str, key);
		
		String send = Encryption.intArrayToString(encryptedData);
		System.out.println(send);
		out.println(send);//send);//Encryption.intArrayToString(encryptedData));
		//b.clear();
	}
}
