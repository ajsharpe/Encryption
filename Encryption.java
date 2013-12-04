import java.io.*;

public class Encryption {
	
	public static String encrypt(String data, String key){
		if (data.length()%2 != 0){
			//append extra "\0"
		}
		return "";
	}
	
	public static String decrypt(String data, String key){
		return "";
	}
	
	private native int[] decrypt(int[] v, int[] k);

	private native int[] encrypt(int[] v, int[] k);
    
}
