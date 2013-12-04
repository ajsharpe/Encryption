import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	private String[] userIDs = {"aj", "scott", "peter parker"};
	private String[] keys = {};
	private Socket client;
	private String currentUser;
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
				out.println("Invalid UserID");
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
			for (int i = 0; i < userIDs.length-1; i++){
				if (currentUser.equals(userIDs[i])){
					System.out.println("User " + currentUser + " connected.");
					return true;
				}
			}
		}
		return false;
	}
	
	public void waitForInput(BufferedReader in){

	}
}
