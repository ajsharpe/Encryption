import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	/* userIDs are hardcoded for now, but for maximum 
	/  security should be read in from an encrypted file */
	private String[] userIDs = {"aj", "scott", "peter parker"};
	//keys are 8 chars (128 bit) (first 8 chars are used in encryption)
	private String[] keys = {"password", "dr. dick", "spiderman"};
	//key[0] is intended as a joke. please don't take marks off for that =P

	//Client server stuff
	private Socket client;
	private String currentUser, currentKey;
	private PrintWriter out;
	private BufferedReader in;

	public Server(Socket clientSocket){
		this.client = clientSocket;
	}

	public static void main(String args[]) throws Exception {
		//System.loadLibrary()

		ServerSocket serverSocket = new ServerSocket(16000);
		System.out.println("Waiting for clients...");

		while (true) {
			Socket client = serverSocket.accept();
			new Thread(new Server(client)).start();
		}
	}

	public void run() {
		try{
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			String fromServer;
			String fromUser;

			if (authenticate()){
				out.println("Welcome, " + currentUser);
			}
			else{
				out.println("Invalid UserID or password.");
				currentUser = null;
			}


			while(currentUser != null){
				out.println(currentUser + " enter a command:");
				if ((fromUser = in.readLine()) != null){

					System.out.println(currentUser + ": " + fromUser);
					if (fromUser.equals("end")){
						out.println("Bye, "+ currentUser + "!");
						break;
					}
				}
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
		if (currentUser != null){
			System.out.println(currentUser + " disconnected.");
		}
	}

	private boolean authenticate() throws IOException{
		String fromUser;

		if ((fromUser = in.readLine()) != null && fromUser.length() > 9 
				&& fromUser.substring(0,9).equals("Username:")){
			currentUser = fromUser.substring(9);
			if ((fromUser = in.readLine()) != null && fromUser.length() > 9 
					&& fromUser.substring(0,9).equals("Password:")){
				currentKey = fromUser.substring(9);
				
				for (int i = 0; i < userIDs.length; i++){
					if (currentUser.equals(userIDs[i]) && currentKey.equals(keys[i])){
						System.out.println("User " + currentUser + " connected.");
						return true;
					}
				}
			}
		}
		return false;
	}
}
