package usp.icmc.labes.control;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * This class cover simple routines that are used frequently
 * 
 * @author stevao
 *
 */

public abstract class Utils {
	
	/**
	 * This method builds the matrix of the image and return a array that
	 * represents the frequency of the colors in the image
	 * 
	 * @return a vector of integer that represents the frequency of the colors
	 *         in a image
	 */
	public static int[] getFrequencyToHistogram(BufferedImage image) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// need an array to store the values of each pixel
		int[] arrayCollor = new int[256];

		// initiate the position of arrayColor with ZERO
		for (int i = 0; i < arrayCollor.length; i++) {
			arrayCollor[i] = 0;
		}

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color color = new Color(image.getRGB(column, line));

				// count the number of time that color appear in the image
				arrayCollor[color.getRed()] = arrayCollor[color.getRed()] + 1;
			}
		}
		return arrayCollor;
	}
	
	

	/**
	 * Fix boundaries problems when change color pixel values
	 * 
	 * @param color
	 *            represent the color that wants to check the bondaries
	 * @return return the color with a fixed value
	 */
	public static int checkBoundaries(int color) {

		if (color < 0) {
			color = 0;
		} else {
			if (color > 255)
				color = 255;
		}

		return color;
	}

	/**
	 * Simple check if an image is in gray scale schema
	 * 
	 * @param image
	 *            -> image that wants to check
	 * @return -> a boolean that indicates if the image is on grayScale or not
	 */
	public boolean isGrayScale(BufferedImage image) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color color = new Color(image.getRGB(column, line));

				// get the values of RGB channel
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();

				// check if they are equals
				if ((red == blue) && (blue == green)) {
					continue;
				} else
					return false;

			}
		}

		// if reach here, all pixels have the same value to RGB, so the image is
		// on grayscale
		return true;
	}
}
