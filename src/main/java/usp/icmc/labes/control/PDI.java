package usp.icmc.labes.control;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import usp.icmc.labes.model.Mask;

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
				Color newColor = new Color(red, green, blue);

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
	public BufferedImage generateNoise(BufferedImage image, int interval) {

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
				int x = rand.nextInt(interval);

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

	/**
	 * performs equalization in a image
	 * 
	 * @param image
	 *            target image
	 * @return a image with the histogram equalized
	 */
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

				// handle position 0
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
				// (gray scale)
				int colorPixel = positionColor.getRed();

				// update the pixel in the processed image with the ideal gray
				// leval for that collor
				Color newColor = new Color(q[colorPixel], q[colorPixel], q[colorPixel]);

				resultImage.setRGB(column, line, newColor.getRGB());

			}
		}

		return resultImage;
	}

	/**
	 * Apply gradient to a image
	 * 
	 * @param image
	 *            Target image
	 * @return processed image
	 */

	public BufferedImage gradientImage(BufferedImage image) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// set the image to gray scale
		BufferedImage resultImage = null;
		resultImage = setGrayScale(image);

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color positionColor = new Color(image.getRGB(column, line));

				// define neighbor pixels
				Color positionColor2;
				Color positionColor3;

				// handle array index out bounds exception
				try {
					positionColor2 = new Color(image.getRGB(column, line + 1));
				} catch (ArrayIndexOutOfBoundsException e) {
					positionColor2 = new Color(image.getRGB(column, line - 1));
				}

				try {
					positionColor3 = new Color(image.getRGB(column + 1, line));
				} catch (ArrayIndexOutOfBoundsException e) {
					positionColor3 = new Color(image.getRGB(column - 1, line));
				}

				// get one value of RGB. Values are always the same because it's
				// in gray scale
				int positionPixel = positionColor.getRed();
				int positionPixel2 = positionColor2.getRed();
				int positionPixel3 = positionColor3.getRed();

				// define a new value to position pixel
				positionPixel = Math.abs(positionPixel - positionPixel2) + Math.abs(positionPixel - positionPixel3);

				positionPixel = Utils.checkBoundaries(positionPixel);

				// define a new color with the positionPixel
				Color newColor = new Color(positionPixel, positionPixel, positionPixel);

				// set into resultImage
				resultImage.setRGB(column, line, newColor.getRGB());

			}
		}

		return resultImage;
	}

	/**
	 * Add quantization algorithm to a image
	 * 
	 * @param image
	 *            Target image
	 * @param finalColorSize
	 *            Defines the number of colors that will be considered in the
	 *            result image
	 * @return quantized image
	 */
	public BufferedImage quantizationImage(BufferedImage image, int finalColorSize) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// set the image to gray scale
		BufferedImage resultImage = null;
		resultImage = setGrayScale(image);

		int colorSize = 256;

		int levelSize = colorSize / finalColorSize;

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color positionColor = new Color(image.getRGB(column, line));

				// gray scale.. RGB is always the same
				int color = positionColor.getRed();

				int valueColor = (int) (color / levelSize) * levelSize;

				valueColor = Utils.checkBoundaries(valueColor);

				Color newColor = new Color(valueColor, valueColor, valueColor);

				// set into resultImage
				resultImage.setRGB(column, line, newColor.getRGB());

			}
		}

		return resultImage;
	}

	/**
	 * Apply splitting algorithm to a image
	 * 
	 * @param image
	 *            Target image
	 * @param finalColorSize
	 * @return
	 */
	public BufferedImage splitImage(BufferedImage image, int colorD, int displacement) {

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// set the image to gray scale
		BufferedImage resultImage = null;
		resultImage = setGrayScale(image);

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color positionColor = new Color(image.getRGB(column, line));

				int color = positionColor.getRed();

				if (color < colorD) {
					color = color - displacement;
					color = Utils.checkBoundaries(color);
				} else {
					color = color + displacement;
					color = Utils.checkBoundaries(color);
				}

				Color newColor = new Color(color, color, color);

				// set into resultImage
				resultImage.setRGB(column, line, newColor.getRGB());

			}
		}

		return resultImage;
	}

	/**
	 * Applies the mean filter to a image (reduce noise)
	 * 
	 * @param image
	 *            Target image
	 * @param kernel
	 *            Object that represents the kernel with information about the
	 *            size and width and height
	 * @return processed image
	 */
	public BufferedImage convolutionFilter(BufferedImage image, Mask kernel, double[][] weights) {

		// create result image
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		// set weights
		kernel.setWeights(weights);

		// creates convolution object
		Convolution c = new Convolution(kernel);

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// color after apply the mean filter
				int newColor;

				// operation in one pixel
				newColor = c.pixelConvolution(image, line, column, kernel);

				// gray scale RGB is always the same
				Color color = new Color(newColor, newColor, newColor);

				// update the value in result image
				resultImage.setRGB(column, line, color.getRGB());
			}
		}

		return resultImage;
	}

	/**
	 * Applies the mean filter to a image (reduce noise)
	 * 
	 * @param image
	 *            Target image
	 * @param kernel
	 *            Object that represents the kernel with information about the
	 *            size and width and height
	 * @return processed image
	 */
	public BufferedImage medianFilter(BufferedImage image, Mask kernel) {

		// create result image
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		// creates convolution object
		Convolution c = new Convolution(kernel);

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// color after apply the mean filter
				int newColor;

				// operation in one pixel
				newColor = c.medianConvolution(image, line, column, kernel);

				// gray scale RGB is always the same
				Color color = new Color(newColor, newColor, newColor);

				// update the value in result image
				resultImage.setRGB(column, line, color.getRGB());
			}
		}

		return resultImage;
	}

	/**
	 * Applies the mean filter to a image (reduce noise)
	 * 
	 * @param image
	 *            Target image
	 * @param kernel
	 *            Object that represents the kernel with information about the
	 *            size and width and height
	 * @return processed image
	 */
	public long lineDetector(BufferedImage image, Mask kernel, double[][] weights) {

		// create result image
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		// set weights
		kernel.setWeights(weights);

		// creates convolution object
		Convolution c = new Convolution(kernel);

		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();

		long totalColor = 0;

		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// color after apply the mean filter
				int newColor;

				// operation in one pixel
				newColor = c.pixelConvolution(image, line, column, kernel);
				totalColor = totalColor + newColor;

				// gray scale RGB is always the same
				Color color = new Color(newColor, newColor, newColor);

				// update the value in result image
				resultImage.setRGB(column, line, color.getRGB());

			}
		}

		return totalColor;
	}

	/**
	 * Go through the pixels of the image and paint each object with one color
	 * 
	 * @param img
	 *            target image
	 * @param mark
	 *            boolean matrix
	 * @param row
	 *            line
	 * @param col
	 *            column
	 * @param srcColor
	 *            will print with this color
	 */
	public BufferedImage flood_fill(BufferedImage img, boolean[][] mark, int row, int col, Color srcColor) {

		// check the pixel color
		Color pixelColor = new Color(img.getRGB(col, row));
		int gray = pixelColor.getRed();
		
		//if it's white just ignore the pixel
		if (gray == 255)
			return img;

		// make sure row and col are inside the image
		if (row < 0)
			return img;
		if (col < 0)
			return img;
		if (row >= img.getHeight())
			return img;
		if (col >= img.getWidth())
			return img;

		// make sure this pixel hasn't been visited yet
		if (mark[row][col])
			return img;

		// fill pixel with target color and mark it as visited
		img.setRGB(col, row, srcColor.getRGB());
		mark[row][col] = true;

		// recursively fill surrounding pixels
		flood_fill(img, mark, row - 1, col, srcColor);
		flood_fill(img, mark, row + 1, col, srcColor);
		flood_fill(img, mark, row, col - 1, srcColor);
		flood_fill(img, mark, row, col + 1, srcColor);

		return img;
	}
}
