package org.warn.utils.image;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class TestImageUtils {
	
	private static final String IMG_NAME = "src/test/resources/tulips.jpg";
	private static final String IMG_COPY_NAME = "tulips_copy.jpg";
	private static final String BW_IMG_NAME = "tulips_bw.jpg";
	
	@Test
	public void testCopyImage() throws IOException {
		ImageUtils.copyImage( IMG_NAME, IMG_COPY_NAME );
		File copiedImage = new File(IMG_COPY_NAME);
		Assert.assertTrue( copiedImage.exists() );
	}
	
	@Test
	public void testLogRGBValues() {
		int[][] rgbValues = ImageUtils.getRGBValues(IMG_NAME);
		Assert.assertNotNull(rgbValues);
		Assert.assertTrue( rgbValues.length > 0 );
	}
	
	@Test
	public void testConvertToGrayscale() {
		boolean success = ImageUtils.convertToGrayscale( IMG_NAME, BW_IMG_NAME );
		Assert.assertTrue( success );
	}
	
	@After
	public void cleanUp() {
		File copiedImage = new File(IMG_COPY_NAME);
		copiedImage.delete();
		File bwImage = new File(BW_IMG_NAME);
		bwImage.delete();
	}
	
}
