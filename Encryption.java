import java.io.*;

public class Encryption {

	public static int[] encrypt(String data, String key){
		//Append extra '\0' if data.length() is odd
		if (data.length()%2 != 0){
			data += '\0'; 
		}
		
		int[] formattedKey = formatKey(key);

		int[] tempData = {0,0};
		
		int result[] = new int[data.length()];
		int j = 0;
		//pass string 2 chars at a time to C encryption routine
		for(int i=0; i<data.length(); i+=2)
		{
			tempData[0] = (int)data.charAt(i);
			tempData[1] = (int)data.charAt(i+1);
			System.out.println(tempData[0]);
			System.out.println(tempData[1]);
			System.loadLibrary("encryption");
			int tempresult[] = encrypt(tempData, formattedKey);
			
			result[j] = tempresult[0];
			result[j+1] = tempresult[1];
			j+=2;
		}
		System.out.println("");
		for (int k=0; k < result.length; k++)
			System.out.println(result[k]);
		return result;
	}

	
	public static String decrypt(int[] data, String key){
		for (int k=0; k < data.length; k++)
			System.out.println(data[k]);
		
		int[] formattedKey = formatKey(key);

		String deciphertext = new String();
		int[] tempData = {0,0};
System.out.println("");
		//pass string 2 chars at a time to C decryption routine
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
	
	private static String intArrayToString(int[] encrypted){
		//takes in result array of size 2 and writes result to 4 chars in a string
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
	}
	
	private static int[] stringToIntArray(String encrypted){
		//takes in string of length 4 and writes result to 2 ints
		int result[] = {0,0};
		
		result[0] = (((int)encrypted.charAt(1)) << 16) | (encrypted.charAt(0) & 0xffff);
		result[1] = (((int)encrypted.charAt(3)) << 16) | (encrypted.charAt(2) & 0xffff);
		
		return result;
	}
		
}
