package labes.icmc.usp.control;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * This class handles digital image process operations
 * 
 * @author stevao
 *
 */
public class PDI {

	/**
	 * This method is used to change the color intensity of a image
	 * 
	 * @param intensity
	 *            -> value of intensity that wants to change in a image
	 * @param image
	 *            -> Image that wants to modify
	 * @return resultImage -> Buffered image with new values of intensity
	 */
	public BufferedImage changeColorIntensity(BufferedImage image, int intensity) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color color = new Color(image.getRGB(column, line));

				// get the values of RGB channel
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				int alpha = color.getAlpha();

				// update the values with the intensity
				red = red + intensity;
				green = green + intensity;
				blue = blue + intensity;

				// check if the new values explodes the boundaries and fix this
				// issue
				red = Utils.checkBoundaries(red);
				green = Utils.checkBoundaries(green);
				blue = Utils.checkBoundaries(blue);

				// creates a new color with the new values
				Color newColor = new Color(red, green, blue, alpha);

				// update the result image
				resultImage.setRGB(column, line, newColor.getRGB());

			}
		}
		return resultImage;
	}

	/**
	 * Set an image to gray scale
	 * 
	 * @param image
	 *            image that wants to set on gray scale
	 * @return a processed image on gray scale
	 */
	public BufferedImage setGrayScale(BufferedImage image) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color color = new Color(image.getRGB(column, line));

				// get the values of RGB channel
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);

				int gray = red + green + blue;

				Color newColor = new Color(gray, gray, gray);

				resultImage.setRGB(column, line, newColor.getRGB());

			}
		}
		return resultImage;
	}

	/**
	 * This method applies salt and pepper noise to a image
	 * 
	 * @param image
	 *            image that wants to apply noise
	 * @return image with a noise effect
	 */
	public BufferedImage generateNoise(BufferedImage image) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// first of all set the image to gray scale
		BufferedImage resultImage = null;
		resultImage = setGrayScale(image);

		// set the noise level
		int whiteNoise = 253;
		int blackNoise = 3;

		Random rand = new Random();

		int noise;

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color color = new Color(image.getRGB(column, line));

				// get the values of RGB channel. RGB will always have the same
				// value because it's in gray scale, so get just one value
				int red = color.getRed();

				// set this value to noise pixel
				noise = red;

				// generate a random value between 0 and 10
				int x = rand.nextInt(21);

				// set noise?
				if (x == 0) { // if x == 0 set a black noise into the image

					noise = blackNoise;

				} else if (x == 10) { // if x == 10 set a white noise

					noise = whiteNoise;

				}

				// now update the pixel in the processed image
				Color newColor = new Color(noise, noise, noise);

				resultImage.setRGB(column, line, newColor.getRGB());
			}
		}

		return resultImage;
	}

	public BufferedImage imageEqualization(BufferedImage image) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// set the image to gray scale
		BufferedImage resultImage = null;
		resultImage = setGrayScale(image);

		// ideal level of pixels
		int pixelLevel;
		pixelLevel = (imageWidth * imageHeight) / 256;

		// histograms
		int[] q = new int[256];
		int[] amountedHistogram = new int[256];
		int[] frequencyHistogram = new int[256];

		// get the frequency histogram
		frequencyHistogram = Utils.getFrequencyToHistogram(resultImage);

		// fill with zero all positions in amountedHistogram
		for (int i = 0; i < amountedHistogram.length; i++)
			amountedHistogram[i] = 0;

		// operation to calculate summation of frequencyHistogram
		for (int i = 0; i < 256; i++) {
			try {
				amountedHistogram[i] = amountedHistogram[i - 1] + frequencyHistogram[i];
			
			//handle position 0
			} catch (ArrayIndexOutOfBoundsException e) {
				amountedHistogram[i] = amountedHistogram[i] + frequencyHistogram[i];
			}
		}

		// define the best distribution of color
		for (int i = 0; i < 256; i++)
			q[i] = Math.max(0, (amountedHistogram[i] / pixelLevel - 1));

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color positionColor = new Color(image.getRGB(column, line));

				// get the values of RGB channel. RGB will always have the same
				int red = positionColor.getRed();

				// update the pixel in the processed image with the ideal gray
				// leval for that collor
				Color newColor = new Color(q[red], q[red], q[red]);

				resultImage.setRGB(column, line, newColor.getRGB());

			}
		}

		return resultImage;

	}

}
