package control;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;


//This class handles the image operations
public class PDI {
	
		
	
	/**
	 *Blank constructor 
	 */ 
	public PDI() {
	}
	
	
	
	
	/**
	 * Fix boundaries problems when change color pixel values 
	 * @param color represent the color that wants to check the bondaries
	 * @return return the color with a fixed value
	 */
	private int checkBoundaries(int color){
		
		if(color < 0){
			color = 0;
		}
		else{
			if(color > 255)
				color = 255;
		}
		
		return color;
	}
	
	
	
	/**
	 * This method builds the matrix of the image and return a array that represents the frequency of the colors in the image 
	 * @return a vector of integer that represents the frequency of the colors in a image
	 */
	public int[] getFrequencyToHistogram(BufferedImage image){
		
		//get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth  = image.getWidth();
		
		
		//need an array to store the values of each pixel
		int[] arrayCollor = new int[256]; 
		
		//initiate the position of arrayColor with ZERO
		for(int i =0; i< arrayCollor.length; i++){
			arrayCollor[i] = 0;
		}
		

		//run for each pixel of the image
		for(int line = 0; line < imageHeight; line++){
			for(int column = 0; column < imageWidth; column++){
				
				//get the color of the pixel
				Color color = new Color(image.getRGB(column, line));
				
				//count the number of time that color appear in the image
				arrayCollor[color.getRed()] = arrayCollor[color.getRed()] + 1;	
			}
		}	
		return arrayCollor;
	}
	
	
	
	/**
	 * This method is used to change the color intensity of a image
	 * @param intensity -> value of intensity that wants to change in a image
	 * @param image -> Image that wants to modify
	 * @return resultImage -> Buffered image with new values of intensity
	 */
	public BufferedImage changeColorIntensity(BufferedImage image, int intensity) {
		
		//get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth  = image.getWidth();
		
		BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);


		//run for each pixel of the image
		for(int line = 0; line < imageHeight; line++){
			for(int column = 0; column < imageWidth; column++){
				
				//get the color of the pixel
				Color color = new Color(image.getRGB(column, line));
				
				//get the values of RGB channel
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				int alpha = color.getAlpha();
				
				//update the values with the intensity
				red   = red   + intensity;
				green = green + intensity;
				blue  = blue  + intensity;
				
				//check if the new values explodes the boundaries and fix this issue
				red   = checkBoundaries(red);
				green = checkBoundaries(green);
				blue  = checkBoundaries(blue);
				
				
				//creates a new color with the new values
				Color newColor = new Color(red, green, blue, alpha);
				
				//update the result image
				resultImage.setRGB(column, line, newColor.getRGB());
				
			}
		}
		return resultImage;
	}
	
	
	
	/**
	 * Set an image to gray scale
	 * @param image image that wants to set on gray scale
	 * @return a processed image on gray scale
	 */
	public BufferedImage setGrayScale(BufferedImage image){
		
		//get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth  = image.getWidth();
		
		BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		//run for each pixel of the image
		for(int line = 0; line < imageHeight; line++){
			for(int column = 0; column < imageWidth; column++){
				
				//get the color of the pixel
				Color color = new Color(image.getRGB(column, line));
				
				//get the values of RGB channel
				int red   = (int) (color.getRed()   * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue  = (int) (color.getBlue()  * 0.114);
				
				int gray = red + green + blue;
				
				Color newColor = new Color(gray, gray, gray);
				
				resultImage.setRGB(column, line, newColor.getRGB());
				
			}
		}
		return resultImage;
	}
	
	
	
	/**
	 * Simple check if an image is in gray scale schema
	 * @param image -> image that wants to check
	 * @return -> a boolean that indicates if the image is on grayScale or not
	 */
	public boolean isGrayScale(BufferedImage image){
		
		//get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth  = image.getWidth();
				
		
		//run for each pixel of the image
		for(int line = 0; line < imageHeight; line++){
			for(int column = 0; column < imageWidth; column++){
				
				//get the color of the pixel
				Color color = new Color(image.getRGB(column, line));
				
				//get the values of RGB channel
				int red   = color.getRed();
				int green = color.getGreen();
				int blue  = color.getBlue();
				
				//check if they are equals
				if ((red == blue) && (blue == green)){
					continue;
				}else
					return false;
				
			}
		}
		
		//if reach here, all pixels have the same value to RGB, so the image is on grayscale
		return true;
	
	}
	
	/**
	 * This method applies salt and pepper noise to a image
	 * @param image image that wants to apply noise
	 * @return image with a noise effect
	 */
	public BufferedImage generateNoise(BufferedImage image){
	
		//get the image dimensions
		int imageHeight = image.getHeight();
		int imageWidth  = image.getWidth();
		
		//first of all set the image to gray scale
		BufferedImage resultImage = null;
		resultImage = setGrayScale(image);
		
		//set the noise level
		int whiteNoise = 253;
		int blackNoise = 3;
		
		Random rand = new Random();
		
		int noise;
		
		//run for each pixel of the image
		for(int line = 0; line < imageHeight; line++){
			for(int column = 0; column < imageWidth; column++){
								
				//get the color of the pixel
				Color color = new Color(image.getRGB(column, line));
				
				//get the values of RGB channel. RGB will always have the same value because it's in gray scale, so get just one value
				int red   = color.getRed();
				
				//set this value to noise pixel
				noise = red;
				
				//generate a random value between 0 and 10
				int x = rand.nextInt(21);
				
				
				//set noise?
				if(x == 0){  //if x == 0 set a black noise into the image
					
					noise = blackNoise;
					
				}else if(x == 10){ //if x == 10 set a white noise
					
					noise = whiteNoise;
					
				}
				
				//now update the pixel in the processed image
				Color newColor = new Color(noise, noise, noise);
				
				resultImage.setRGB(column, line, newColor.getRGB());
			}
		}
		
		return resultImage;
	}
	
	
	
	

}
