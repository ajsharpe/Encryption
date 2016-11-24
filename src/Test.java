import java.io.*;


public class Test {

	public static void main(String args[]) throws Exception {
		if (args.length > 1) {
            System.err.println(
                "Usage: java Test <text>");
            System.exit(1);
        }

		
		String a="w'ef"; //TODO: This breaks everything for some strange reason
		//w' works fine, so does w'e...
		// but "w'ef" causes StringIndexOutOfBoundsException on the Server

		if (args.length > 0 && args[0] != null) {
			a = new String(args[0]);
		} else { 
			a = "w'rt";
		}
		System.out.println("Before Encryption: " + a);
		String key = "password";
		String b = Encryption.encrypt(a, key);
		System.out.println("After Encryption:  " + b);
		String c = Encryption.decrypt(b, key);
		System.out.println("After Decryption:  " + c);
	}
}