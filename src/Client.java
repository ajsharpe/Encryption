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
		System.out.println(currentUser + " : " + key);
        Socket clientSocket = null;
		try {
			//Initialize connection
			clientSocket = new Socket(InetAddress.getByName("localhost"), 16000);
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
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
			while (true) {
				if ((fromServer = in.readLine()) != null){
					System.out.println("Server: " + fromServer);
				}
				if (fromServer!= null && fromServer.length() > 5 && 
						fromServer.substring(0,5).equals("Bye, "))
					break;
				System.out.print("Client: ");
				while( (fromUser = stdIn.readLine()) == null || fromUser.isEmpty() 
						|| fromUser.length() < 1);
				if (fromUser != null && fromUser.length() > 0) {
					toServer(fromUser);
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Unable to connect to localhost.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("The connection to localhost experienced an I/O error.");
			System.exit(1);
		} finally {
			if (in != null) in.close();
			if (out != null) out.close();
			if (clientSocket != null) clientSocket.close();
		}
	}
	
	private static boolean authenticate() throws IOException{
		out.println("Username:" + currentUser);
		toServer("Password:" + key);
		String fromServer;
		if ((fromServer = in.readLine()) != null){
			System.out.println(fromServer);
			if (fromServer.length() > 9 && fromServer.substring(0, 9).equals("Welcome, ")){
				return true;
			}
		}
		return false;
	}
	
	private static void toServer(String str){
		String encryptedData = Encryption.encrypt(str, key);
		System.out.println("Encrypted: " + encryptedData + "\n");
		out.println(encryptedData);
	}
}
