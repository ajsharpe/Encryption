import java.io.*;


public class Test {

	public static void main(String args[]) throws Exception {
		if (args.length > 1) {
            System.err.println(
                "Usage: java Test <text>");
            System.exit(1);
        }

		
		//TODO: a single 'm' causes the server to get stuck in an infinite loop. investigate
		String a="lm";
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