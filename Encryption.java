import java.io.*;

public class Encryption {

	public static String encrypt(String data, String key){
		//Append extra '\0' if data.length() is odd
		if (data.length()%2 != 0){
			data += '\0'; 
		}
System.out.println(key);
		int[] formattedKey = formatKey(key);

		String ciphertext = new String();
		int[] tempData = {0,0};

		//pass string 2 chars at a time to C encryption routine
		System.out.println("Encrypting");
		for(int i=0; i<data.length(); i+=2)
		{
			tempData[0] = data.charAt(i);
			tempData[1] = data.charAt(i+1);

//System.out.println("q " + tempData[0]+ ""+tempData[1]);
			
			System.loadLibrary("encryption");
			int result[] = encrypt(tempData, formattedKey);
			

//System.out.println("a "+result[0] + "" + result[1]);
//System.out.println(intArrayToString(result));

			ciphertext += intArrayToString(result);
		}
		System.out.println("");
		return ciphertext;
	}

	public static String decrypt(String data, String key){

		int[] formattedKey = formatKey(key);

		String deciphertext = new String();
		int[] tempData = {0,0};

		//pass string 2 chars at a time to C decryption routine
		System.out.println("Decrypting");
		for(int i=0; i<data.length(); i+=4)
		{
			tempData = stringToIntArray(data.substring(i, i+4));


//System.out.println("q " + tempData[0]+ ""+tempData[1]);
			
			System.loadLibrary("encryption");
			int result[] = decrypt(tempData, formattedKey);

//System.out.println("a "+result[0] + "" + result[1]);
//System.out.println(intArrayToString(result));
			
			deciphertext += intArrayToString(result);
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
		a = a << 16;  
		result[1] = (char) (a & 0xFF);  
		
		result[2] = (char) (a & 0xFF);  
		b = b << 16;  
		result[3] = (char) (a & 0xFF);  
		
		return new String(result);
	}
	
	private static int[] stringToIntArray(String encrypted){
		//takes in string of length 4 and writes result to 2 ints
		int result[] = {0,0};
		
		result[0] = (((int)encrypted.charAt(0)) << 16) | (encrypted.charAt(1) & 0xffff);
		result[1] = (((int)encrypted.charAt(2)) << 16) | (encrypted.charAt(3) & 0xffff);
		
		return result;
	}
		
}
