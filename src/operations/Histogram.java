package operations;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;



/**
 * This class handle the creation of a histogram extending JDialog class
 * @author stevao
 *
 */
public class Histogram  extends JDialog{
	

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Constructor.. just call all methods and builds a frame
	 */
	public Histogram(String applicationTitle, String chartTitle, int[] array) {
		super();

		// This will create the dataset
		IntervalXYDataset dataset = createDataSet(array);

		// based on the dataset we create the chart
		JFreeChart chart = createChart(dataset, chartTitle);

		final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
	}
	
	
	

	/**
	 * Default plot conventions
	 * */
	private JFreeChart createChart(IntervalXYDataset dataset, String chartTitle) {
		final JFreeChart chart = ChartFactory.createXYBarChart("Frequency Histogram", "Color", false, "Occurrence", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot plot = (XYPlot) chart.getPlot();
		
		final IntervalMarker target = new IntervalMarker(100.0, 200.0);
		target.setLabel("Target Range");
		target.setLabelFont(new Font("SansSerif", Font.ITALIC, 8));
		target.setLabelAnchor(RectangleAnchor.LEFT);
		target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		target.setPaint(new Color(222, 222, 255, 128));
		plot.addRangeMarker(target, Layer.BACKGROUND);
		
		return chart;
	}
	
	
	/**
	 * Parsing the array values to a data set object
	 * */
	private IntervalXYDataset createDataSet(int[] array) {

		int len = array.length;

		XYSeries serie = new XYSeries("Intensity");

		for (int i = 0; i < len; i++) {
			serie.add(i, array[i]);
		}

		final XYSeriesCollection dataset = new XYSeriesCollection(serie);

		return dataset;
	}

}
