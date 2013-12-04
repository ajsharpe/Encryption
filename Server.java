import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	private String[] userIDs = {"aj", "scott", "peter parker"};
	private String[] keys = {};
	private Socket client;
	private String currentUser;
	
    public Server(Socket clientSocket){
    	this.client = clientSocket;
    }
    
    public static void main(String args[]) throws Exception {
       ServerSocket serverSocket = new ServerSocket(16000);
       System.out.println("Waiting for clients...");
       
       while (true) {
          Socket client = serverSocket.accept();
          new Thread(new Server(client)).start();
       }
    }
    
    public void run() {
    	try{
    		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        	BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
        	String fromServer;
			String fromUser;
        	
			if ((fromUser = in.readLine()) != null && fromUser.substring(0,9).equals("Username:")){
				currentUser = fromUser.substring(9);
				System.out.println("User " + currentUser + " connected.");
			}
			
			while(true){
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
        System.out.println(currentUser + " disconnected.");
    }
    public void waitForInput(BufferedReader in){
    	
    }
}
