package labes.icmc.usp.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

import labes.icmc.usp.control.Histogram;
import labes.icmc.usp.control.PDI;


/**
 * Unit tests
 */
public class Tests{
	
	String path = "Classpath:\\labes\\icmc\\usp\\resources\\gray (Custom).png";
	
	PDI pdi;
	Histogram h; 
	BufferedImage image;
	
	
	@BeforeClass
	public void setUp(){
		
		pdi = new PDI();
		
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("File don't exists!");
		}
		 
		
	}

   @Test
   public void histogramTest(){
       
	   int[] array;
	   
       //get the frequency of each color and store into a array
       array = pdi.getFrequencyToHistogram(image);
        
       //plotting
       h = new Histogram("Frequency Histogram", "Frequency Histogram", array);
       h.pack();
       h.setVisible(true);
   }
   
   
}