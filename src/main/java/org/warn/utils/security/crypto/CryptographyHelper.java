package org.warn.utils.security.crypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.warn.utils.text.TextHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptographyHelper {

	public static SecretKey generateKey( String algorithm ) throws CryptographyException {
		
		if( TextHelper.isEmpty( algorithm ) ) {
			throw new IllegalArgumentException("Algorithm cannot be empty");
		}
		
		SecretKey key = null;
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
			key = keygen.generateKey();
			
		} catch( NoSuchAlgorithmException e ) {
			log.error("Error while generating key", e);
			throw new CryptographyException( e.getMessage(), e );
		}
		return key;
	}
	
	public static boolean writeKey( String filePathAndName, String algorithm ) throws CryptographyException {

		if( TextHelper.isEmpty( filePathAndName ) ) {
			throw new IllegalArgumentException("File path cannot be empty");
		}
		
		SecretKey key = generateKey( algorithm );
		try {
			ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( filePathAndName ) );
			out.writeObject(key);
			out.close();
		} catch (IOException e) {
			log.error("Error while saving key", e);
			throw new CryptographyException( e.getMessage(), e );
		}

		return true;
	}

	public static SecretKey readKey( String filePathAndName ) throws CryptographyException {
		
		if( TextHelper.isEmpty( filePathAndName ) ) {
			throw new IllegalArgumentException("File path cannot be empty");
		}
		
		SecretKey key = null;
		try {
			ObjectInputStream in = new ObjectInputStream( new FileInputStream( filePathAndName ) );
			key = (SecretKey) in.readObject();
			in.close();
			return key;
			
		} catch( IOException | ClassNotFoundException e ) {
			log.error("Error while loading key", e);
			throw new CryptographyException( e.getMessage(), e );
		}
		
	}

	public static Cipher getEncryptionCipher( String algorithm, SecretKey key ) throws CryptographyException {
		try {
			Cipher encryptionCipher = Cipher.getInstance(algorithm);
			encryptionCipher.init( Cipher.ENCRYPT_MODE, key );
			return encryptionCipher;
		} catch( NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e ) {
			log.error("Error while creating encryption cipher", e);
			throw new CryptographyException( e.getMessage(), e );
		}
	}

	public static Cipher getDecryptionCipher( String algorithm, SecretKey key ) throws CryptographyException {
		try {
			Cipher decryptionCipher = Cipher.getInstance(algorithm);
			decryptionCipher.init( Cipher.DECRYPT_MODE, key );
			return decryptionCipher;
		} catch( NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e ) {
			log.error("Error while creating decryption cipher", e);
			throw new CryptographyException( e.getMessage(), e );
		}
	}
	
	public static KeyPair generateAsymmetricKeyPair( String algorithm ) throws CryptographyException {
		try {
			KeyPair keyPair = KeyPairGenerator.getInstance(algorithm).generateKeyPair();
			log.debug("Public Key: " + keyPair.getPublic());
			return keyPair;
		} catch( NoSuchAlgorithmException e ) {
			log.error("Error while creating asymmetric key pair", e);
			throw new CryptographyException( e.getMessage(), e );
		}
	}
	
	public static void printProviderList() {
		Provider[] providers = Security.getProviders();
		for( Provider p: providers ) {
			log.info("Provider: " + p.getName());
		}
	}
	
	/**
	 * 
	 * @param cryptographicService Valid values are Signature, Cipher, MessageDigest, Mac, KeyStore
	 */
	public static void printAlgorithmList( String cryptographicService ) {
		Set<String> algorithms = Security.getAlgorithms( cryptographicService ); 
		algorithms.stream().forEach( algo -> {
			log.info("Algorithm: " + algo );
		});
	}
	
}
