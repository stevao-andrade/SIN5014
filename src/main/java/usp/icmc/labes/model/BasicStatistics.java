package usp.icmc.labes.model;

import java.util.ArrayList;

/**
 * This class get information from a array list and compute some statistics
 * 
 * @author stevao
 *
 */
public class BasicStatistics {

	private ArrayList<Double> data = new ArrayList<Double>();

	// constructor
	public BasicStatistics(ArrayList<Double> data) {
		this.data = data;
	}

	/**
	 * compute the mean
	 * 
	 * @return mean
	 */
	public double getMean() {
		double sum = 0.0;
		for (double a : this.data)
			sum += a;
		return sum / this.data.size();
	}

	/**
	 * compute the variance
	 * 
	 * @return variance
	 */
	public double getVariance() {
		double mean = getMean();
		double temp = 0;
		for (double a : this.data)
			temp += (mean - a) * (mean - a);
		return temp / this.data.size();
	}

	/**
	 * compute the standard deviation
	 * 
	 * @return standard deviation
	 */
	public double getStdDev() {
		return Math.sqrt(getVariance());
	}

}
