import java.io.*;
import java.net.*;

public class EncryptionTest {

	private static String currentUser, key;
	private static PrintWriter out;
	private static BufferedReader in;
	
	
	public static void main(String[] args) throws IOException {
		String test = "boogers";
		System.out.println(test);
		String encrypted = Encryption.encrypt(test, "asdfasdf");
		System.out.println("Encrypted: "+encrypted);
		String decrypted = Encryption.decrypt(encrypted, "asdfasdf");
		System.out.println(decrypted);
	}
}