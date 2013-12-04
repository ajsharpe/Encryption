import java.io.*;
import java.net.*;

public class Client {

	private static String currentUser;
	
	public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println(
                "Usage: java Client <username>");
            System.exit(1);
        }
        currentUser = new String(args[0]);
        
		try {
			
			
			Socket clientSocket = new Socket(InetAddress.getByName("localhost"), 16000);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));

			BufferedReader stdIn =
				new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;
			
			out.println("Username:" + args[0]);
			
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
}
