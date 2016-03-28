package usp.icmc.labes.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import usp.icmc.labes.control.Convolution;
import usp.icmc.labes.control.Histogram;
import usp.icmc.labes.control.PDI;
import usp.icmc.labes.control.Utils;
import usp.icmc.labes.model.Mask;



/**
 * Unit tests
 */
public class Tests {

	static String path = "src\\main\\java\\labes\\icmc\\usp\\resources\\black.png";
	
	static PDI pdi;
	Histogram h;
	static BufferedImage image;
	
	@Ignore
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
		array = Utils.getFrequencyToHistogram(image);

		// plotting
		h = new Histogram("Frequency Histogram", "Frequency Histogram", array);
		h.pack();
		h.setVisible(true);
	}
	
	@Ignore
	@Test
	public void convolutionBoundariesTest(){
		
		BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		double [][] weights =  {{1,1,1},{0,0,1},{0,1,0}};

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
				
				int newColor;
				
				//newColor = c.pixelConvolution(image, line, column, kernel);
				
				//gray scale RGB is always the same
				//Color color = new Color(newColor, newColor, newColor);
				
				//update the value in result image
				//resultImage.setRGB(column, line, color.getRGB());
			}
		}
		
		//save result image
		
	}

}