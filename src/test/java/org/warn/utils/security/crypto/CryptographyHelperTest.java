package org.warn.utils.security.crypto;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.crypto.SecretKey;

import org.junit.Test;
import org.warn.utils.file.FileOperations;

public class CryptographyHelperTest {
	
	private static final String AES_ALGORITHM = "AES";
	private static final String INCORRECT_ALGORITHM = "DEF";
	private static final String SAMPLE_DIR = "./";
	private static final String SAMPLE_FILENAME = "aes";
	private static final String SAMPLE_FILEPATH = SAMPLE_DIR + SAMPLE_FILENAME;
	private static final String INCORRECT_FILEPATH = "./abc/aes";
	
	@Test
	public void testPrintProviderList() {
		CryptographyHelper.printProviderList();
	}

	@Test
	public void testPrintAlgorithmList() {
		CryptographyHelper.printAlgorithmList("Cipher");
	}
	
	@Test( expected = CryptographyException.class )
	public void testWriteKeyWithInvalidAlgorithm() {
		CryptographyHelper.writeKey( SAMPLE_FILEPATH, INCORRECT_ALGORITHM );
	}
	
	@Test( expected = CryptographyException.class )
	public void testWriteKeyWithInvalidFilePath() {
		CryptographyHelper.writeKey( INCORRECT_FILEPATH, AES_ALGORITHM );
	}
	
	@Test
	public void testWriteReadKeyFile() {
		assertTrue( CryptographyHelper.writeKey( SAMPLE_FILEPATH, AES_ALGORITHM ) );
		assertNotNull( CryptographyHelper.readKey( SAMPLE_FILEPATH ) );
		FileOperations.deleteFiles( ".", SAMPLE_FILENAME );
	}
	
	public void testGetEncryptionCipher() {
		SecretKey key = CryptographyHelper.generateKey( AES_ALGORITHM );
		assertNotNull( key );
		assertNotNull( CryptographyHelper.getEncryptionCipher( AES_ALGORITHM, key ) );
	}
	
	public void testGetDecryptionCipher() {
		SecretKey key = CryptographyHelper.generateKey( AES_ALGORITHM );
		assertNotNull( key );
		assertNotNull( CryptographyHelper.getDecryptionCipher( AES_ALGORITHM, key ) );
	}
	
}
