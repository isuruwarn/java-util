package org.warn.utils.security.crypto;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

import org.warn.utils.text.TextHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Provides a client interface to the Java cryptographic service encryption and decryption ciphers.
 */
@Slf4j
public class CipherClient {
	
	private String algorithm;
	private SecretKey key;
	private Cipher encryptionCipher;
	private Cipher decryptionCipher;
	
	/**
	 * Creates Java cryptographic encryption and decryption cipher instances based on the provided algorithm. A {@link SecretKey} 
	 * object is generated at runtime (based on the provided algorithm), and will be used for encryption/decryption.<br><br> 
	 * 
	 * Note - The Key generated at runtime will be lost when object is destroyed.
	 * 
	 * @param algorithm An algorithm provided by the Java cryptographic service, such as AES, DESEDE, DES, etc
	 */
	public CipherClient( String algorithm ) {
		
		if( TextHelper.isEmpty( algorithm ) ) {
			throw new CryptographyException("Algorithm cannot be empty");
		}
		
		this.algorithm = algorithm;
		this.key = CryptographyHelper.generateKey( algorithm );
		this.encryptionCipher = CryptographyHelper.getEncryptionCipher( this.algorithm, this.key );
		this.decryptionCipher = CryptographyHelper.getDecryptionCipher( this.algorithm, this.key );
	}

	/**
	 * Creates Java cryptographic encryption and decryption cipher instances based on the provided algorithm
	 * and security key.<br><br> 
	 * 
	 * @param algorithm An algorithm provided by the Java cryptographic service, such as AES, DESEDE, DES, etc
	 * @param key A valid {@link SecretKey} object (based on the given algorithm), which will be used for encryption/decryption
	 */
	public CipherClient( String algorithm, SecretKey key ) {
		
		if( TextHelper.isEmpty( algorithm ) ) {
			throw new CryptographyException("Algorithm cannot be empty");
		}
		
		if( key == null ) {
			throw new CryptographyException("Key cannot be null");
		}
		
		this.algorithm = algorithm;
		this.key = key;
		this.encryptionCipher = CryptographyHelper.getEncryptionCipher( this.algorithm, this.key );
		this.decryptionCipher = CryptographyHelper.getDecryptionCipher( this.algorithm, this.key );
	}
	
	/**
	 * Creates Java cryptographic encryption and decryption cipher instances based on provided algorithm
	 * and security key.<br><br> 
	 * 
	 * @param algorithm An algorithm provided by the Java cryptographic service, such as AES, DESEDE, DES, etc
	 * @param key A string containing the filepath to a valid {@link SecretKey} object (based on the given algorithm), which will be used for encryption/decryption
	 */
	public CipherClient( String algorithm, String keyFilePath ) {
		
		if( TextHelper.isEmpty( algorithm ) ) {
			throw new CryptographyException("Algorithm cannot be empty");
		}
		
		if( TextHelper.isEmpty( keyFilePath )) {
			throw new CryptographyException("Key cannot be null");
		}
		
		this.algorithm = algorithm;
		this.key = CryptographyHelper.readKey( keyFilePath );
		this.encryptionCipher = CryptographyHelper.getEncryptionCipher( this.algorithm, this.key );
		this.decryptionCipher = CryptographyHelper.getDecryptionCipher( this.algorithm, this.key );
	}
	
	public String encrypt( String message ) {
		try {
			byte[] utf8 = message.getBytes("UTF8");
			byte[] enc = this.encryptionCipher.doFinal(utf8);
			return Base64.getEncoder().encodeToString(enc);
		} catch( BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e ) {
			log.error("Error while encrypting message", e);
		}
		return null;
	}

	public String decrypt( String cipherText ) {
		try {
			byte[] dec = Base64.getDecoder().decode(cipherText);
			byte[] utf8 = this.decryptionCipher.doFinal(dec);
			return new String(utf8, "UTF8");
		} catch( BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e ) {
			log.error("Error while decrypting message", e);
		}
		return null;
	}

}
