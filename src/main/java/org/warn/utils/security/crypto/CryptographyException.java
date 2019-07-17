package org.warn.utils.security.crypto;

public class CryptographyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CryptographyException() {
		super();
	}

	public CryptographyException(String message) {
		super(message);
	}

	public CryptographyException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptographyException(Throwable cause) {
		super(cause);
	}

}
