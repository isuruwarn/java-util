package org.warn.utils.security.crypto;

import static org.junit.Assert.assertEquals;

import javax.crypto.SecretKey;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.warn.utils.file.FileOperations;

public class CipherClientTest {

	private static final String AES_ALGORITHM = "AES";
	private static final String SAMPLE_DIR = "./";
	private static final String SAMPLE_FILENAME = "aes";
	private static final String SAMPLE_FILEPATH = SAMPLE_DIR + SAMPLE_FILENAME;
	private static final String SAMPLE_MESSAGE = "The quick brown fox jumps over the lazy dog";
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void testNullAlgorithm1() {
		expectedEx.expect( CryptographyException.class );
		expectedEx.expectMessage("Algorithm cannot be empty");
		new CipherClient( null );
	}
	
	@Test
	public void testNullAlgorithm2() {
		expectedEx.expect( CryptographyException.class );
		expectedEx.expectMessage("Algorithm cannot be empty");
		SecretKey key = CryptographyHelper.generateKey( AES_ALGORITHM );
		new CipherClient( null, key );
	}
	
	@Test
	public void testEmptyAlgorithmString1() {
		expectedEx.expect( CryptographyException.class );
		expectedEx.expectMessage("Algorithm cannot be empty");
		new CipherClient( "" );
	}
	
	@Test
	public void testEmptyAlgorithmString2() {
		expectedEx.expect( CryptographyException.class );
		expectedEx.expectMessage("Algorithm cannot be empty");
		SecretKey key = CryptographyHelper.generateKey( AES_ALGORITHM );
		new CipherClient( "", key );
	}
	
	@Test
	public void testNullKey() {
		expectedEx.expect( CryptographyException.class );
		expectedEx.expectMessage("Key cannot be null");
		SecretKey key = null;
		new CipherClient( AES_ALGORITHM, key );
	}
	
	@Test
	public void testEmptyKeyFilePathString() {
		expectedEx.expect( CryptographyException.class );
		expectedEx.expectMessage("Key cannot be null");
		new CipherClient( AES_ALGORITHM, "" );
	}
	
	@Test
	public void testEncryptionDecryption() {
		CipherClient cipherClient = new CipherClient( AES_ALGORITHM );
		String encryptedMessage = cipherClient.encrypt( SAMPLE_MESSAGE );
		String decryptedMessage = cipherClient.decrypt( encryptedMessage );
		assertEquals(  SAMPLE_MESSAGE, decryptedMessage );
	}
	
	@Test
	public void testEncryptionDecryptionWithKey() {
		SecretKey key = CryptographyHelper.generateKey( AES_ALGORITHM );
		CipherClient cipherClient = new CipherClient( AES_ALGORITHM, key );
		String encryptedMessage = cipherClient.encrypt( SAMPLE_MESSAGE );
		String decryptedMessage = cipherClient.decrypt( encryptedMessage );
		assertEquals(  SAMPLE_MESSAGE, decryptedMessage );
	}
	
	@Test
	public void testEncryptionDecryptionWithKeyFile() {
		
		CryptographyHelper.writeKey( SAMPLE_FILEPATH, AES_ALGORITHM );
		
		CipherClient cipherClient = new CipherClient( AES_ALGORITHM, SAMPLE_FILEPATH );
		String encryptedMessage = cipherClient.encrypt( SAMPLE_MESSAGE );
		String decryptedMessage = cipherClient.decrypt( encryptedMessage );
		assertEquals(  SAMPLE_MESSAGE, decryptedMessage );
		
		FileOperations.deleteFiles( ".", SAMPLE_FILENAME );
	}
	
}
