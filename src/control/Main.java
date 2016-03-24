package control;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {
			
	public static void main(String[] args) {
		
		//Path to some image
		String path = "input1.jpg";
		
		BufferedImage image = null;
		
		try {
			
			image = ImageIO.read(new File(path));
			
		} catch (IOException e1) {
			System.out.println("File Note Found");
		}
		
		//Will process the image in the path
		PDI pdi = new PDI();
		
		
		//histogram test
		int[] array;
		
		//get the frequency of each color and store into a array
		array = pdi.getFrequencyToHistogram(image);
		
		//plotting
		Histogram h = new Histogram("Frequency Histogram", "Frequency Histogram", array);
        h.pack();
        h.setVisible(true);
		
		
        pdi.changeColorIntensity(image,150);		
		

	}

}
