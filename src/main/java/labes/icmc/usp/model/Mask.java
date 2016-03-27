package labes.icmc.usp.model;


/**
 * Simple kernel abstraction
 * @author stevao
 *
 */
public class Mask {
	
	private int width;
	private int height;
	
	private double[][]  weights;
	
	
	public Mask(int width, int heigth){
		this.width  = width;
		this.height = heigth;
	}

	
	
	public int getWidth() {
		return width;
	}

	
	public int getHeight() {
		return height;
	}
	
	
	
	/**
	 * 
	 * @param line index of the kernel
	 * @param column index of the kernel
	 * @return weight for that position
	 */
	public double getWeight(int line, int column) {
		return weights[line][column];
	}



	public void setWeights(double[][] weights) {
		this.weights = weights;
	}
	
	
}
