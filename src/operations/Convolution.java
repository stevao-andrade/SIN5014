package operations;

import java.awt.Color;
import java.awt.image.BufferedImage;

import model.Mask;

/**
 * This class handles convolution operations
 * @author stevao
 *
 */
public class Convolution {
	
	private Mask kernel;
	
	
	/**
	 * Constructor
	 * @param kernel Implementation of a Kernel defined in model package
	 */
	public Convolution (Mask kernel){
		
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
	 * @param image Object that the operation is applied
	 * @param imgLine position X of the pixel
	 * @param imgColumn position Y of the pixel
	 * @param kernel mask that wants to apply into the pixel
	 */
	public int pixelConvolution(BufferedImage image, int imgLine, int imgColumn, Mask kernel){
		
		int kernelWidth = kernel.getWidth();
		int kernelHeigth = kernel.getHeight();
		

		
		int weight = 0;
		
		//run each value of the kernel
		for (int kernelLine = 0; kernelLine < kernelHeigth; kernelLine++){
			for (int kernelColumn = 0; kernelColumn < kernelWidth; kernelColumn++){
				
				try{
					
					//get the color of the pixel
					Color color = new Color(image.getRGB(imgColumn + kernelColumn, imgLine + kernelLine));
					
					//It's in gray scale, so RGB have always the same value
					int point = color.getRed();
					
					//apply operation to pixel
					weight = weight + point * kernel.getWeight(kernelLine, kernelColumn);
					
					//check if it's a valid value
					weight = Utils.checkBoundaries(weight);
					
				}catch (Exception e){
					System.out.println(e.getStackTrace());
				}
			}
		}
		
		return weight;
	}
	
	

}
