import java.io.*;
import java.nio.*;

public class Encryption {

	public static String encrypt(String data, String key){

		//Append extra '\0' to check for null termination
		data += '\0';

		//Append extra '\0' if data.length() is odd
		if (data.length()%2 != 0){
			data += '\0'; 
		}
		
		int[] formattedKey = formatKey(key);

		int[] tempData = {0,0};
		
		int result[] = new int[data.length()];
		//pass string 2 chars at a time to C encryption routine
		////System.out.println("Before Encryption");
		
		for(int i=0; i<data.length()-1; i+=2)
		{
			tempData[0] = (int)data.charAt(i);
			tempData[1] = (int)data.charAt(i+1);
			////System.out.println(tempData[0]);
			//System.out.println(tempData[1]);
			System.loadLibrary("encryption");
			int tempresult[] = encrypt(tempData, formattedKey);
			
			result[i] = tempresult[0];
			result[i+1] = tempresult[1];
		}
		return intArrayToString(result);
	}

	
	public static String decrypt(String data, String key){
		if (data == null || data.isEmpty()){
			return null;
		}

		return decrypt(stringToIntArray(data),key);
	}

	private static String decrypt(int[] data, String key){
		
		int[] formattedKey = formatKey(key);

		String deciphertext = new String();
		int[] tempData = {0,0};
		
		//pass string 2 chars at a time to C decryption routine
		for(int i=0; i<data.length-1; i+=2)
		{
			tempData[0] = data[i];
			tempData[1] = data[i+1];
			
			System.loadLibrary("encryption");
			int tempresult[] = decrypt(tempData, formattedKey);
			deciphertext += (char)tempresult[0];
			deciphertext += (char)tempresult[1];
		}
		
		//strip extra '\0' if it was added
		int length = deciphertext.length();
		if (length > 1 && deciphertext.charAt(length-1)=='\0'
				&& deciphertext.charAt(length-2)=='\0'){
			deciphertext = deciphertext.substring(0, length-1);
			length = deciphertext.length();
		}


		//ensure string is null terminated, as \0 was added in first step of encryption
		//if it isn't, we don't have the full message yet, so return null
		if (length > 0 && deciphertext.charAt(length-1) != '\0'){
			return null;
		} else if (length > 0){
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
	


	//String and int array conversion
	private static String intArrayToString(int[] encrypted){
		String result = "";
		for (int i = 0; i < encrypted.length; i++){
			char[] f = intToCharArray(encrypted[i]);
			result += (char)f[0] + "" + (char)f[1]+ "" + (char)f[2] + "" + (char)f[3];
		}
		//replace \r with \0 so println doesn't miss part of the message
		return result.replace('\r', '\0');
	}

	private static char[] intToCharArray(int a){
		return new char[] {
			(char) ((a >> 24) & 0xFF),
			(char) ((a >> 16) & 0xFF),   
			(char) ((a >> 8) & 0xFF),   
			(char) (a & 0xFF)
		};
	}

	
	private static int[] stringToIntArray(String encrypted){
		//BUGFIX: \r characters are throwing things off... change them back from \0!
		//replace \0 with \r to revert last step of encryption
		encrypted = encrypted.replace('\0', '\r');

		int[] result = new int[encrypted.length()/4];
		for (int i=0, j=0; i < encrypted.length()-3; i+=4, j++){
			char[] c = {(char)encrypted.charAt(i), (char)encrypted.charAt(i+1), 
				(char)encrypted.charAt(i+2), (char)encrypted.charAt(i+3)};
			result[j] = charArrayToInt(c);
		}
		return result;	
	}

	private static int charArrayToInt(char[] b) {
		return   b[3] & 0xFF |
				(b[2] & 0xFF) << 8 |
				(b[1] & 0xFF) << 16 |
				(b[0] & 0xFF) << 24;
	}
	
}
