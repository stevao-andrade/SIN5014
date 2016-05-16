package usp.icmc.labes.control;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import usp.icmc.labes.model.Mask;

/**
 * This class handles convolution operations
 * 
 * @author stevao
 *
 */
public class Convolution {

	private Mask kernel;

	/**
	 * Constructor
	 * 
	 * @param kernel
	 *            Implementation of a Kernel defined in model package
	 */
	public Convolution(Mask kernel) {

		this.setKernel(kernel);

	}

	public Mask getKernel() {
		return kernel;
	}

	public void setKernel(Mask kernel) {
		this.kernel = kernel;
	}

	/**
	 * Convolution operation applied just in one pixel
	 * 
	 * @param image
	 *            Object that the operation is applied
	 * @param imgLine
	 *            position X of the pixel
	 * @param imgColumn
	 *            position Y of the pixel
	 * @param kernel
	 *            mask that wants to apply into the pixel
	 */
	public int pixelConvolution(BufferedImage image, int imgLine, int imgColumn, Mask kernel) {

		int kernelWidth = kernel.getWidth();
		int kernelHeigth = kernel.getHeight();

		int point = 0;
		double average = 0;

		// run each value of the kernel
		for (int kernelLine = 0; kernelLine < kernelHeigth; kernelLine++) {
			for (int kernelColumn = 0; kernelColumn < kernelWidth; kernelColumn++) {

				// try to get the color of the neighbor pixel.. can be invalid
				// because of the edges.. so make a try catch
				try {

					// get the color
					Color color = new Color(image.getRGB(imgColumn + kernelColumn, imgLine + kernelLine));

					// It's in gray scale, so RGB have always the same value
					point = color.getRed();

					// check the value in the mask
					double weight = kernel.getWeight(kernelLine, kernelColumn);

					// apply operation to pixel
					average = average + (point * weight);

				} catch (ArrayIndexOutOfBoundsException e) {
					average = average + 0; // handling borders.. right now just
											// define the weight as zero
				}
			}
		}

		int int_average = (int) average;

		// check if it's a valid value
		int_average = Utils.checkBoundaries(int_average);

		return int_average;
	}

	/**
	 * This convolution applies median filter
	 * 
	 * @param image
	 *            Target image
	 * @param imgLine
	 *            Number of lines of the image
	 * @param imgColumn
	 *            Number of columns of the image
	 * @param kernel
	 *            Mask used in the filter
	 * @return
	 */
	public int medianConvolution(BufferedImage image, int imgLine, int imgColumn, Mask kernel) {

		int kernelWidth = kernel.getWidth();
		int kernelHeigth = kernel.getHeight();

		// list to store the neighbors
		List<Integer> pixels = new ArrayList<Integer>();

		// run each value of the kernel
		for (int kernelLine = 0; kernelLine < kernelHeigth; kernelLine++) {
			for (int kernelColumn = 0; kernelColumn < kernelWidth; kernelColumn++) {

				// try to get the color of the neighbor pixel.. can be invalid
				// because of the edges.. so make a try catch
				try {

					// get the color
					Color color = new Color(image.getRGB(imgColumn + kernelColumn, imgLine + kernelLine));

					// It's in gray scale, so RGB have always the same value
					int point = color.getRed();
					Integer objPoint = new Integer(point);

					pixels.add(objPoint);

					// if pixel don't exist's add 0 to the list
				} catch (ArrayIndexOutOfBoundsException e) {
					Integer objPoint = new Integer(0);
					pixels.add(objPoint);
				}
			}
		}

		// sort the list
		Collections.sort(pixels);

		// get the size of the list
		int listSize = pixels.size();

		// returning the element of the center
		if (listSize % 2 == 0) {

			int index = listSize / 2;

			return pixels.get(index);

		} else {
			int index = (listSize / 2) + 1;
			return pixels.get(index);
		}
	}

}
