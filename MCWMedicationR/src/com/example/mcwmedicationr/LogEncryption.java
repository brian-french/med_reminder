package com.example.mcwmedicationr;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.util.Base64;
import android.util.Log;

public class LogEncryption {

	/*
	 * Basic Encryption/Decryption Utility Functions
	 * from: https://gist.github.com/aogilvie/6267013#file-string_encrypt_decrypt-md
	 * 
	 * It uses potentially insecure random numbers see: 
	 * https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
	 * for details.
	 */
	
	private static final String TAG = "LogEncryption";
	private static final String S_PHRASE = "Encryption phrase";
	private static final String MODE = "DES";

	public static String encryptIt(String value) {
	    try {
	        DESKeySpec keySpec = new DESKeySpec(S_PHRASE.getBytes("UTF8"));
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MODE);
	        SecretKey key = keyFactory.generateSecret(keySpec);

	        byte[] clearText = value.getBytes("UTF8");
	        // Cipher is not thread safe
	        Cipher cipher = Cipher.getInstance(MODE);
	        cipher.init(Cipher.ENCRYPT_MODE, key);

	        String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.NO_WRAP);
	        Log.d(TAG, "Encrypted: " + value + " -> " + encrypedValue);
	        return encrypedValue;

	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (InvalidKeySpecException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (BadPaddingException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
	    }
	    return value;
	}
	
	public static String decryptIt(String value) {
	    try {
	        DESKeySpec keySpec = new DESKeySpec(S_PHRASE.getBytes("UTF8"));
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MODE);
	        SecretKey key = keyFactory.generateSecret(keySpec);

	        byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
	        // cipher is not thread safe
	        Cipher cipher = Cipher.getInstance(MODE);
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

	        String decrypedValue = new String(decrypedValueBytes);
	        Log.d(TAG, "Decrypted: " + value + " -> " + decrypedValue);
	        return decrypedValue;

	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (InvalidKeySpecException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (BadPaddingException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
	    }
	    return value;
	} 
}
