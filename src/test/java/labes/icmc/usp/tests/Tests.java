package labes.icmc.usp.tests;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import labes.icmc.usp.control.Convolution;
import labes.icmc.usp.control.Histogram;
import labes.icmc.usp.control.PDI;
import labes.icmc.usp.model.Mask;

/**
 * Unit tests
 */
public class Tests {

	static String path = "src\\main\\java\\labes\\icmc\\usp\\resources\\gray.png";
	
	static PDI pdi;
	Histogram h;
	static BufferedImage image;

	@BeforeClass
	public static void setUp() {

		pdi = new PDI();

		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("File don't exists!");
		}

	}
	
	
	@Ignore
	@Test
	public void histogramTest() {

		int[] array;

		// get the frequency of each color and store into a array
		array = pdi.getFrequencyToHistogram(image);

		// plotting
		h = new Histogram("Frequency Histogram", "Frequency Histogram", array);
		h.pack();
		h.setVisible(true);
	}
	
	@Test
	public void convolutionBoundariesTest(){

		int [][] weights =  {{1,1,1},{1,1,1},{1,1,1}};

		//define kernel and set weights
		Mask kernel = new Mask(3, 3);
		kernel.setWeights(weights);		
		
		Convolution c = new Convolution(kernel);
		
		
		// get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();
		
		// run for each pixel of the image
		for (int line = 0; line < imageHeight; line++) {
			for (int column = 0; column < imageWidth; column++) {

				// get the color of the pixel
				Color color = new Color(image.getRGB(column, line));
				
				c.pixelConvolution(color, line, column, kernel);
				
			}
		}
		
	}

}