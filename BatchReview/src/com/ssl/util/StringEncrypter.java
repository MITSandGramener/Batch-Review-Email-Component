package com.ssl.util;

//CIPHER / GENERATORS
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

// KEY SPECIFICATIONS
import java.security.spec.KeySpec;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

// EXCEPTIONS
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public  class StringEncrypter {
	
     private static StringEncrypter instance;
    Cipher ecipher;
    Cipher dcipher;
    String decryptvalue=null;
	   public StringEncrypter()
	{
	}

	   StringEncrypter(String passPhrase) {
         // 8-bytes Salt
        byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
        };

        // Iteration count
        int iterationCount = 19;
        try {

            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

       //     ecipher = Cipher.getInstance(key.getAlgorithm());       
       //     dcipher = Cipher.getInstance(key.getAlgorithm());
      
            ecipher = Cipher.getInstance("PBEWithMD5AndDES");   // Specially for IBM Websphere
            dcipher = Cipher.getInstance("PBEWithMD5AndDES");   // Specially for IBM Websphere
            
            // Prepare the parameters to the cipthers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
       
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
      

        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
       
        } catch (InvalidKeySpecException e) {
            System.out.println("EXCEPTION: InvalidKeySpecException");
        
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
       
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
       
        }
    }
     public String getEnMes(String data,String data1)
	{ 
			// Create encrypter/decrypter class
        StringEncrypter desEncrypter = new StringEncrypter(data);
    
        // Encrypt the string
        String desEncrypted       = desEncrypter.encrypt(data1);
     
        System.out.println("PBEWithMD5AndDES Encryption algorithm");
        return desEncrypted;
	}
     public String getDeMes(String data,String data1)
	{ 
		
        StringEncrypter desEncrypter = new StringEncrypter(data);
        String desDecrypted       = desEncrypter.decrypt(data1);
        return desDecrypted;
	}
    /**
     * Takes a single String as an argument and returns an Encrypted version
     * of that String.
     * @param str String to be encrypted
     * @return <code>String</code> Encrypted version of the provided String
     */
    public String encrypt(String str) {
		  try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);

        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }


    /**
     * Takes a encrypted String as an argument, decrypts and returns the 
     * decrypted String.
     * @param str Encrypted String to be decrypted
     * @return <code>String</code> Decrypted version of the provided String
     */
    public String decrypt(String str) {

        try {
        	
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);
      
            
            // Decode using utf-8
            return new String(utf8, "UTF8");
         
        } catch (BadPaddingException e) {
        	
        } catch (IllegalBlockSizeException e) {
        	 
        } catch (UnsupportedEncodingException e) {
        	  
        } catch (IOException e) {
        	 
        }
        return null;
    }

    public static synchronized StringEncrypter getInstance() //step 1
  {
    if(instance == null)
    {
      return new StringEncrypter();
    }
    else    
    {
      return instance;
    }
  }
    
    public static void main(String[] args) {
    	StringEncrypter strEnc = new StringEncrypter();
    	//System.out.println(strEnc.getEnMes("p8admin", "mits123$"));
    	System.out.println(strEnc.getDeMes("p8admin", "WCBDpf3dt+20Bgukxdr/dA=="));
    	//System.out.println(strEnc.getEnMes("p8admin", "P@ssw0rd"));
    	//System.out.println(strEnc.getDeMes("OETC_WF_USR", "VObXfej+riyf6/6jNfAxUg=="));
    	//System.out.println(strEnc.decrypt("WCBDpf3dt+20Bgukxdr/dA=="));
	}
  
}


