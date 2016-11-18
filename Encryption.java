import java.io.*;
import java.nio.*;

public class Encryption {

	public static int[] encrypt(String data, String key){
		//Append extra '\0' if data.length() is odd
		if (data.length()%2 != 0){
			data += '\0'; 
		}
		
		int[] formattedKey = formatKey(key);

		int[] tempData = {0,0};
		
		int result[] = new int[data.length()];
		//pass string 2 chars at a time to C encryption routine
		System.out.println("Before Encryption");
		
		for(int i=0; i<data.length()-1; i+=2)
		{
			tempData[0] = (int)data.charAt(i);
			tempData[1] = (int)data.charAt(i+1);
			System.out.println(tempData[0]);
			System.out.println(tempData[1]);
			System.loadLibrary("encryption");
			int tempresult[] = encrypt(tempData, formattedKey);
			
			result[i] = tempresult[0];
			result[i+1] = tempresult[1];
		}
		System.out.println("\nAfter Encryption:");
		for (int k=0; k < result.length; k++)
			System.out.println(result[k]);
		System.out.println("");
		return result;
	}

	
	public static String decrypt(int[] data, String key){
		
		System.out.println("\nBefore Decryption");
		for (int k=0; k < data.length; k++)
			System.out.println(data[k]);
		
		int[] formattedKey = formatKey(key);

		String deciphertext = new String();
		int[] tempData = {0,0};
		System.out.println("");
		//pass string 2 chars at a time to C decryption routine
		System.out.println("\nAfter decryption");
		for(int i=0; i<data.length; i+=2)
		{
			tempData[0] = data[i];
			tempData[1] = data[i+1];
			
			System.loadLibrary("encryption");
			int tempresult[] = decrypt(tempData, formattedKey);
			System.out.println(tempresult[0]);
			System.out.println(tempresult[1]);
			deciphertext += (char)tempresult[0];
			deciphertext += (char)tempresult[1];
		}
		
		//strip extra '\0' if it was added
		int length = deciphertext.length();
		if (deciphertext.charAt(length-1)==deciphertext.charAt(length-2)
				&& deciphertext.charAt(length-1)=='\0'){
			deciphertext = deciphertext.substring(0, length-1);
		}

		return deciphertext;
	}

	private static native int[] decrypt(int[] v, int[] k);

	private static native int[] encrypt(int[] v, int[] k);

	private static int[] formatKey(String key){
		int[] formattedKey = {0,0,0,0};
		for (int i=0; i<8; i+=2){
			//store 2 chars as 1 int, up to first 8 chars of key
			formattedKey[i/2] = (((int)key.charAt(i)) << 16) | (key.charAt(i+1) & 0xffff);
		}
		
		return formattedKey;
	}
	
	public static int charArrayToInt(char[] b) {
		return   b[3] & 0xFF |
				(b[2] & 0xFF) << 8 |
				(b[1] & 0xFF) << 16 |
				(b[0] & 0xFF) << 24;
	}

	public static char[] intToCharArray(int a){
		return new char[] {
			(char) ((a >> 24) & 0xFF),
			(char) ((a >> 16) & 0xFF),   
			(char) ((a >> 8) & 0xFF),   
			(char) (a & 0xFF)
		};
	}
		
	
	/*
	public static int[] stringToInts(String s){
		int[] result = new int[s.length()];
		for (int i=1; i < s.length(); i++){
			result[i] = (int)s.charAt(i);			
		}
		return result;
	}
	
	public static String intsToString(int[] s){
		String result = "";
		for (int i=1; i < s.length; i++){
			result += (char)s[i];			
		}
		result+="\0";
		return result;
	}
	*/
	
	/*The following approach was abandoned for a 1:1 char:int binding instead
	*/
	
	public static String intArrayToString(int[] encrypted){
		/*takes in result array of size 2 and writes result to 4 chars in a string
		int a = encrypted[0];
		int b = encrypted[1];
		
		char[] result = {0,0,0,0};
		result[0] = (char) (a & 0xFF);  
		a = a >> 16;  
		result[1] = (char) (a & 0xFF);  
		
		result[2] = (char) (a & 0xFF);  
		b = b >> 16;  
		result[3] = (char) (a & 0xFF);  
		
		return new String(result);
		*/
		String result = "";
		for (int i = 0; i < encrypted.length; i++){
			char[] f = intToCharArray(encrypted[i]);
			result += (char)f[0] + "" + (char)f[1]+ "" + (char)f[2] + "" + (char)f[3];
		}
		return result;
			
	}
	
	public static int[] stringToIntArray(String encrypted){
		/*takes in string of length 4 and writes result to 2 ints
		int result[] = {0,0};
		
		result[0] = (((int)encrypted.charAt(1)) << 16) | (encrypted.charAt(0) & 0xffff);
		result[1] = (((int)encrypted.charAt(3)) << 16) | (encrypted.charAt(2) & 0xffff);
		
		return result;
		*/
		int[] result = new int[encrypted.length()/4];
		for (int i=0, j=0; i < encrypted.length()-3; i+=4, j++){
			char[] c = {(char)encrypted.charAt(i), (char)encrypted.charAt(i+1), 
				(char)encrypted.charAt(i+2), (char)encrypted.charAt(i+3)};
			result[j] = charArrayToInt(c);
		}
		return result;
			
	}
	
}
