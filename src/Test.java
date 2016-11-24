import java.io.*;


public class Test {

	public static void main(String args[]) throws Exception {
		String c = "Password:password";
		System.out.println(c);
		String key = "password";
		String a = Encryption.encrypt(c, key);
		String b = Encryption.decrypt(a, key);
		System.out.println(b);
	}
}