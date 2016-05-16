package usp.icmc.labes.control;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Skeleton {

	// Possible neighborhood of a pixel
	final static int[][] neighborhood = { { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 },
			{ -1, -1 }, { 0, -1 } };

	// possible set of steps of the algorithm
	final static int[][][] steps = { { { 0, 2, 4 }, { 2, 4, 6 } }, { { 0, 2, 6 }, { 0, 4, 6 } } };

	/**
	 * Count the number of neighbors of a pixel
	 * 
	 * @param px
	 *            point X of a pixel
	 * @param py
	 *            point y of a pixel
	 * 
	 * @return number of neighbors of a pixel
	 */
	private int countNeighbors(int px, int py, BufferedImage targetImage) {

		// number of neighbors
		int count = 0;

		// will check every possible neighbor
		for (int i = 0; i < neighborhood.length - 1; i++) {

			// coordinates of a possible neighbor
			int nx = px + neighborhood[i][1];
			int ny = py + neighborhood[i][0];

			// avoid get an invalid pixel
			try {

				// get the color of the possible neighbor
				Color color = new Color(targetImage.getRGB(nx, ny));

				// It's in gray scale, so RGB have always the same value
				int pointColor = color.getRed();

				// if point color isn't white, and it's a valid pixel, so it's a
				// valid neighbor of (px,py)
				if (pointColor != 255)
					count++;

			} catch (ArrayIndexOutOfBoundsException e) {
				// Do nothing
			}

		}
		// count represents the number of neighbors
		return count;
	}

	/**
	 * Count the number of valid transitions of a pixel neighbors
	 * 
	 * @param px
	 *            point X of a pixel
	 * @param py
	 *            point y of a pixel
	 * 
	 * @return number of valid transitions of the neighborhood of a pixel
	 */
	private int numberOfTransitions(int px, int py, BufferedImage targetImage) {
		int count = 0;

		// will check every possible neighbor
		for (int i = 0; i < neighborhood.length - 1; i++) {

			// build neighbors coordinates
			int n1x = px + neighborhood[i][1];
			int n1y = py + neighborhood[i][0];

			int n2x = px + neighborhood[i + 1][1];
			int n2y = py + neighborhood[i + 1][0];

			// to avoid reach invalid pixels
			try {

				// get the color of the possible neighbor
				Color color1 = new Color(targetImage.getRGB(n1x, n1y));

				// get the color of the possible neighbor
				Color color2 = new Color(targetImage.getRGB(n2x, n2y));

				// get the intensity color of each neighbor
				int point1Color = color1.getRed();
				int point2Color = color2.getRed();

				// now time to make the test..
				if (point1Color == 255) {

					if (point2Color != 255)
						count++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// Do nothing
			}

		}

		return count;
	}

	/**
	 * Check if at least one neighbor have a with pixel
	 * 
	 * @param px
	 *            coordinate X of the pixel
	 * @param py
	 *            coordinate Y of the pixel
	 * @param step
	 * @param targetImage
	 *            target image that wants to make skeleton process
	 * @return
	 */
	private boolean atLeastOneIsWhite(int px, int py, int step, BufferedImage targetImage) {

		int count = 0;

		int[][] group = steps[step];

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < group[i].length; j++) {

				// get neighbor
				int[] n = neighborhood[group[i][j]];

				// coordinates
				int nx = px + n[1];
				int ny = py + n[0];

				try {

					// get the intensity color of the possible neighbor
					Color color = new Color(targetImage.getRGB(nx, ny));
					int pointColor = color.getRed();

					// check if it's background
					if (pointColor == 255) {

						count++;
						break;

					}

				} catch (ArrayIndexOutOfBoundsException e) {
					// Do nothing
				}
			}
		}

		// if count > 0 then it's true..
		return count >= 1;
	}

	public BufferedImage doSkeletonProcess(BufferedImage targetImage) {

		// create result image
		BufferedImage resultImage = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		// get the image dimensions
		int imageHeight = targetImage.getHeight();
		int imageWidth = targetImage.getWidth();

		// control structure
		boolean firstPass = false;
		boolean changed;

		// will thin the image with this color
		int whiteColor = 255;
		Color resultColor = new Color(whiteColor, whiteColor, whiteColor);

		do {
			changed = false; // at the beginning nothing change
			firstPass = !firstPass;

			// go through the image pixels
			for (int line = 0; line < imageHeight; line++) {
				for (int column = 0; column < imageWidth; column++) {

					// do the verifications steps of the algorithm

					// get the intensity color of the possible neighbor
					Color color = new Color(targetImage.getRGB(column, line));
					int pointColor = color.getRed();

					// check if it's background
					if (pointColor == 255)
						continue;

					// get the number of neighbors of the pixel
					int numNeighbors = countNeighbors(column, line, targetImage);

					// check the size of the neighborhood
					if (numNeighbors < 2 || numNeighbors > 6)
						continue;

					// check the number of transitions of the processed pixel
					if (numberOfTransitions(column, line, targetImage) != 1)
						continue;

					if (!atLeastOneIsWhite(column, line, firstPass ? 0 : 1, targetImage))
						continue;

					// update the value in result image
					resultImage.setRGB(column, line, resultColor.getRGB());

					changed = true;
					System.out.println("I'm trapped :(");
				}

			}

		} while (firstPass || changed);

		return resultImage;
	}
}
