package org.warn.utils.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

	public static void copyImage( String sourceImageFilePath, String targetImageFilePath ) {
		try {
			BufferedImage sourceImg = ImageIO.read( new File(sourceImageFilePath) );
			int w = sourceImg.getWidth();
			int h = sourceImg.getHeight();
			BufferedImage targetImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = targetImg.createGraphics();
			g2.drawImage( sourceImg, 0, 0, null );
			g2.dispose();
			FileOutputStream f = new FileOutputStream(targetImageFilePath);
			String fileExtension = FilenameUtils.getExtension(sourceImageFilePath);
			ImageIO.write( targetImg, fileExtension, f );
		} catch (FileNotFoundException e) {
			LOGGER.error("Error while copying image", e);
		} catch (IOException e) {
			LOGGER.error("Error while copying image", e);
		}
	}
	
	public static boolean convertToGrayscale( String sourceImageFilePath, String targetImageFilePath ) {
		try {
			File input = new File( sourceImageFilePath );
			BufferedImage image = ImageIO.read(input);
			int height = image.getHeight();
			int width = image.getWidth();
			for( int i=0; i<height; i++ ) {
				for(int j=0; j<width; j++ ) {
					int p = getGrayscaleValue( image.getRGB( j, i ) );
					image.setRGB( j, i, p );
					//LOGGER.debug( "%09d ", p );
				}
				//LOGGER.debug("");
			}
			File output = new File( targetImageFilePath );
			String fileExtension = FilenameUtils.getExtension(sourceImageFilePath);
			return ImageIO.write( image, fileExtension, output );
		} catch( IOException e ) {
			LOGGER.error("Error while converting image", e);
			return false;
		}
	}
	
	public static int[][] getRGBValues( String imgFilePath ) {
		int[][] rgbValues = null;
		try {
			BufferedImage image = ImageIO.read( new File(imgFilePath) );
			int width = image.getWidth();
			int height = image.getHeight();
			rgbValues = new int [height] [width]; 
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					rgbValues[i][j] = image.getRGB(j, i);
					//Color c = new Color( image.getRGB(j, i) );
					//LOGGER.debug("Pixel ({}, {}) RED: {}, GREEN: {}, BLUE: {} ", i, j, c.getRed(), c.getGreen(), c.getBlue() );
					//LOGGER.debug("Pixel ({}, {}) RGB: {}", i, j, image.getRGB(j, i) );
				}
			}
		} catch( IOException e ) {
			LOGGER.error("Error while loading image", e);
		}
		return rgbValues;
	}
	

	/**
	 * Takes an RGB integer value and returns the grayscale integer value.
	 * 
	 * Thanks to http://www.dyclassroom.com/image-processing-project/how-to-convert-a-color-image-into-grayscale-image-in-java
	 * 
	 * @param pixelRGB Integer containing original RGB value
	 * @return Integer containing grayscale value
	 */
	public static int getGrayscaleValue( int pixelRGB ) {
		int a = (pixelRGB >> 24) & 0xff;
		int r = (pixelRGB >> 16) & 0xff;
		int g = (pixelRGB >> 8) & 0xff;
		int b = pixelRGB & 0xff;
		
		// calculate average
		int avg = (r + g + b) / 3;
		
		// replace RGB value with avg
		pixelRGB = (a << 24) | (avg << 16) | (avg << 8) | avg;
		return pixelRGB;
	}

}