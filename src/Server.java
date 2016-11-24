import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{

	//Store username/password as an in-memory HashMap
	private HashMap<String, String> db = new HashMap<String, String>();
	/* userIDs are hardcoded for now, but for maximum 
	/  security should be read in from an encrypted file 
	private String[] userIDs = {"aj", "scott", "peter parker"};
	//keys are 8 chars (128 bit) (first 8 chars are used in encryption)
	private String[] keys = {"password", "dr. dick", "spiderman"};*/

	//Client server stuff
	private Socket client;
	private String currentUser, currentKey;
	private PrintWriter out;
	private BufferedReader in;

	public Server(Socket clientSocket){
		this.client = clientSocket;
	}

	public static void main(String args[]) throws Exception {

		ServerSocket serverSocket = new ServerSocket(16000);
		System.out.println("Waiting for clients...");
		try{
			while (true) {
				Socket client = serverSocket.accept();
				new Thread(new Server(client)).start();
			}
		} catch (UnknownHostException e) {
			System.err.println("Unable to connect to localhost.");
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		} finally {
			if (serverSocket != null) serverSocket.close();
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
					String decrypted = Encryption.decrypt(fromUser, currentKey);
					System.out.println(currentUser + ": " + decrypted);
					if (decrypted.equals("end")){
						out.println("Bye, "+ currentUser + "!");
						currentUser = null;
						break;
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		} finally {
			try{
				this.cleanUp();
			} catch (IOException e) {
				System.out.println(e);
				System.exit(1);
			}
		}
		if (currentUser != null){
			System.out.println(currentUser + " disconnected.");
		}
	}

	private void cleanUp() throws IOException{
			if (in != null) in.close();
			if (out != null) out.close();
			if (client != null) client.close();
	}

	private boolean authenticate() throws IOException{
		String fromUser;

		if ((fromUser = in.readLine()) != null && fromUser.length() > 9 
				&& fromUser.substring(0,9).equals("Username:")){
			currentUser = fromUser.substring(9);
			if (!db.containsKey(currentUser)){
				return false;
			} else {
				System.out.println("User " + currentUser + " connected.");
				return true;
			}
			
			
			/*
			if ((fromUser = in.readLine()) != null)
				String decrypted = Encryption.decrypt(fromUser, currentKey);
					fromUser.length() > 9 
					&& fromUser.substring(0,9).equals("Password:")){
				currentKey = fromUser.substring(9);
			}*/
		}
		return false;
	}

	private void readUsers(File _db){
		//fake it for now
		db.put
	}

	//This is for debugging purposes only, and will list all users in memory
	private void printusers(){
		Iterator it = db.entrySet().iterator();
    	while (it.hasNext()) {
        	Map.Entry entry = (Map.Entry)it.next();
         	System.out.println("key is: "+ entry.getKey() + " & Value is: " + entry.getValue());
      }
	}
}
