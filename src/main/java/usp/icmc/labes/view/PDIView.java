package usp.icmc.labes.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.ui.HorizontalAlignment;

import net.coobird.thumbnailator.Thumbnails;
import usp.icmc.labes.control.Histogram;
import usp.icmc.labes.control.PDI;
import usp.icmc.labes.control.Utils;
import usp.icmc.labes.model.Mask;


public class PDIView {

	private final int WITDH_PANEL_SIZE = 750;
	private final int HEIGHT_PANEL_SIZE = 600;
	
	//higher the value less noise
	int INTERVAL_NOISE = 50;
	
	//intensity
	int INTENSITY = 10;
	
	//mean
	int KERNEL_SIZE = 3;
	
	//quantization
	int FINAL_COLORS_NUMBER = 12;
	
	//splitting
	int COLOR_DISPLACEMENT = 128;
	int SPLITING_DISPLACEMENT = 20;

	private JFrame frmSin;

	private PDI pdi = new PDI();

	BufferedImage image = null;
	BufferedImage processedImage = null;


	/**
	 * Resize the loaded image to a pre defined resolution
	 * 
	 * @param imageToResize
	 * @param labelToDisplay
	 */
	private void resizeDisplay(BufferedImage imageToResize, JLabel labelToDisplay) {
		
		processedImage = imageToResize;
		
		int imageHeight = imageToResize.getHeight();
		int imageWidth = imageToResize.getWidth();
		
		if( (imageHeight > HEIGHT_PANEL_SIZE) && ( imageWidth > WITDH_PANEL_SIZE)){
			
			try {
				processedImage = Thumbnails.of(imageToResize).size(WITDH_PANEL_SIZE, HEIGHT_PANEL_SIZE).asBufferedImage();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File Not Found.");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Can't Read the File.");
			}
		}
		
		// set into the label
		labelToDisplay.setIcon(new ImageIcon(processedImage));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PDIView window = new PDIView();
					window.frmSin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PDIView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSin = new JFrame();
		frmSin.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmSin.setTitle("SIN5014 - Fundamentos de Processamento Gr\u00E1fico (Stev\u00E3o Andrade)");
		frmSin.setResizable(false);
		frmSin.setBounds(100, 100, 900, 700);
		frmSin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmSin.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");

		mnFile.add(mntmOpen);

		// Exit menu option
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.exit(0);

			}
		});

		// save the processed image
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fs = new JFileChooser(new File("c:\\"));
				fs.setDialogTitle("Save a File (*.jpg)");

				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image File", "jpg", "jpge", "png");
				fs.setFileFilter(filter);

				int result = fs.showSaveDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {

					String filename = fs.getSelectedFile().toString() + ".jpg";

					File file = new File(filename);

					try {

						ImageIO.write(processedImage, "jpg", file);

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Can't save the file");
					}

				}

			}
		});
		mnFile.add(mntmSave);
		mnFile.add(mntmExit);

		JMenu mnIntensity = new JMenu("Setup");
		menuBar.add(mnIntensity);

		// change the intensity value
		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ConfigurationDialog intensityDialog = new ConfigurationDialog();

				intensityDialog.setVisible(true);

			}
		});
		mnIntensity.add(mntmConfiguration);
		frmSin.getContentPane().setLayout(null);

		final JPanel originalPanel = new JPanel();
		originalPanel.setBackground(Color.LIGHT_GRAY);
		originalPanel.setBounds(67, 49, 750, 600);
		frmSin.getContentPane().add(originalPanel);

		final JLabel imageLabel = new JLabel("");
		originalPanel.add(imageLabel);

		// create a histogram of the image
		JButton btnHistogram = new JButton("");
		btnHistogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (processedImage == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					// histogram test
					int[] array;

					// get the frequency of each color and store into a array
					array = Utils.getFrequencyToHistogram(processedImage);

					Histogram h = new Histogram("Frequency Histogram", "Frequency Histogram", array);
					h.pack();
					h.setVisible(true);

				}
			}
		});
		btnHistogram.setToolTipText("Histogram");
		btnHistogram.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/histogram.png")));
		btnHistogram.setBounds(0, 0, 43, 38);
		frmSin.getContentPane().add(btnHistogram);

		// Set a image to gray scale
		JButton btnGrayScale = new JButton("");
		btnGrayScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {
					BufferedImage grayImage = null;
					grayImage = pdi.setGrayScale(processedImage);

					processedImage = grayImage;

					resizeDisplay(processedImage, imageLabel);

				}

			}
		});
		btnGrayScale.setToolTipText("Gray Scale");
		btnGrayScale.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/gray.png")));
		btnGrayScale.setBounds(53, 0, 43, 38);
		frmSin.getContentPane().add(btnGrayScale);

		// increases the value of the intensity in the pixels of the image
		JButton btnIntensityUp = new JButton("");
		btnIntensityUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage intensityImage = null;
					

					intensityImage = pdi.changeColorIntensity(processedImage, INTENSITY);
					processedImage = intensityImage;
					resizeDisplay(processedImage, imageLabel);

				}

			}
		});
		btnIntensityUp.setToolTipText("Intensity Up");
		btnIntensityUp.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/up.png")));
		btnIntensityUp.setBounds(106, 0, 43, 38);
		frmSin.getContentPane().add(btnIntensityUp);

		// reduces the value of the intensity in the pixels of the image
		JButton btnIntensityDown = new JButton("");
		btnIntensityDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage intensityImage = null;

					intensityImage = pdi.changeColorIntensity(processedImage, (-INTENSITY));

					processedImage = intensityImage;
					resizeDisplay(processedImage, imageLabel);

				}

			}
		});
		btnIntensityDown.setToolTipText("Intensity Down");
		btnIntensityDown.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/down.png")));
		btnIntensityDown.setBounds(159, 0, 43, 38);
		frmSin.getContentPane().add(btnIntensityDown);
		
		
		//mean filter
		JButton btnMeanFilter = new JButton("");
		btnMeanFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage meanImage = null;
					Mask kernel = new Mask(KERNEL_SIZE, KERNEL_SIZE);
					
					// mean filter is always 1/ (width x height) of the kernel
					double meanFilter;
					meanFilter = (double) 1 / (kernel.getWidth() * kernel.getHeight());
					
					//weights is a matrix with kernel size
					double[][] weights = new double [kernel.getWidth()][kernel.getHeight()];
					
					//fulfill the matrix with the values of the meanFilter
					for(int i= 0; i< kernel.getWidth(); i++)
						for(int j = 0; j < kernel.getHeight(); j++)
							weights[i][j] = meanFilter;
						
					
					//set the weights to the kernel
					kernel.setWeights(weights);

					
					meanImage = pdi.convolutionFilter(processedImage, kernel, weights);

					processedImage = meanImage;
					resizeDisplay(processedImage, imageLabel);

				}
				
				
			}
		});
		btnMeanFilter.setToolTipText("Mean Filter");
		btnMeanFilter.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/mean.png")));
		btnMeanFilter.setBounds(212, 0, 43, 38);
		frmSin.getContentPane().add(btnMeanFilter);
		
		
		//median filter
		JButton btnMedianFilter = new JButton("");
		btnMedianFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage medianImage = null;
					Mask kernel = new Mask(KERNEL_SIZE, KERNEL_SIZE);
					
					medianImage = pdi.medianFilter(processedImage, kernel);

					processedImage = medianImage;
					resizeDisplay(processedImage, imageLabel);

				}

				
			}
		});
		btnMedianFilter.setToolTipText("Median Filter");
		btnMedianFilter.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/median.png")));
		btnMedianFilter.setBounds(265, 0, 43, 38);
		frmSin.getContentPane().add(btnMedianFilter);

		// equalize a image
		JButton btnEqualization = new JButton("");
		btnEqualization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage equalizedImage = null;

					equalizedImage = pdi.imageEqualization(processedImage);

					processedImage = equalizedImage;
					resizeDisplay(processedImage, imageLabel);

				}

			}
		});
		btnEqualization.setToolTipText("Equalizer");
		btnEqualization.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/equalizer.png")));
		btnEqualization.setBounds(318, 0, 43, 38);
		frmSin.getContentPane().add(btnEqualization);
		
		
		//high pass filter
		JButton btnHighPassFilter = new JButton("");
		btnHighPassFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage highImage = null;
					Mask kernel = new Mask(KERNEL_SIZE, KERNEL_SIZE);
					
					
					//weights is a matrix with kernel size -> Highpass filter
					double[][] weights = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
										
					//set the weights to the kernel
					kernel.setWeights(weights);

					highImage = pdi.convolutionFilter(processedImage, kernel, weights);

					processedImage = highImage;
					resizeDisplay(processedImage, imageLabel);

				}
				
			}
		});
		btnHighPassFilter.setToolTipText("High Pass Filter");
		btnHighPassFilter
				.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/low_frequency.png")));
		btnHighPassFilter.setBounds(530, 0, 43, 38);
		frmSin.getContentPane().add(btnHighPassFilter);
		
		
		//border filter
		JButton btnBorderOperator = new JButton("");
		btnBorderOperator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage borderImage = null;
					Mask kernel = new Mask(KERNEL_SIZE, KERNEL_SIZE);
					
					
					//weights is a matrix with kernel size -> Sobel filter
					double[][] weights = {{-1,0,1},{-2,0,2},{-1,0,1}}; //vertical
					//double[][] weights = {{-1,-2,-1},{0,0,0},{1,2,1}}; //horizontal
					
					//set the weights to the kernel
					kernel.setWeights(weights);

					borderImage = pdi.convolutionFilter(processedImage, kernel, weights);

					processedImage = borderImage;
					resizeDisplay(processedImage, imageLabel);

				}
				
			}
		});
		btnBorderOperator.setToolTipText("Border Operator");
		btnBorderOperator.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/border.png")));
		btnBorderOperator.setBounds(583, 0, 43, 38);
		frmSin.getContentPane().add(btnBorderOperator);

		// set noise to a image
		JButton btnNoise = new JButton("");
		btnNoise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage noisedImage = null;
										
					noisedImage = pdi.generateNoise(processedImage, INTERVAL_NOISE);

					processedImage = noisedImage;
					resizeDisplay(processedImage, imageLabel);

				}

			}
		});
		btnNoise.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/noise.png")));
		btnNoise.setToolTipText("Noise (Salt and pepper)");
		btnNoise.setBounds(636, 0, 43, 38);
		frmSin.getContentPane().add(btnNoise);
		
		//image quantization
		JButton btnQuantization = new JButton("");
		btnQuantization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage quantizedImage = null;
					
					//this parameter should be controlled
					quantizedImage = pdi.quantizationImage(processedImage, FINAL_COLORS_NUMBER);

					processedImage = quantizedImage;
					resizeDisplay(processedImage, imageLabel);
				}
				
			}
		});
		btnQuantization.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/quantization.png")));
		btnQuantization.setToolTipText("Quantization");
		btnQuantization.setBounds(371, 0, 43, 38);
		frmSin.getContentPane().add(btnQuantization);
		
		
		//image splitting
		JButton btnSpliting = new JButton("");
		btnSpliting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage splitImage = null;
					
					//this parameters need to be controllable
					splitImage = pdi.splitImage(processedImage, COLOR_DISPLACEMENT, SPLITING_DISPLACEMENT);

					processedImage = splitImage;
					resizeDisplay(processedImage, imageLabel);

				}
			}
		});
		btnSpliting.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/split.png")));
		btnSpliting.setToolTipText("Spliting");
		btnSpliting.setBounds(424, 0, 43, 38);
		frmSin.getContentPane().add(btnSpliting);
		
		JButton btnGradient = new JButton("");
		btnGradient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage gradientImage = null;

					gradientImage = pdi.gradientImage(processedImage);

					processedImage = gradientImage;
					resizeDisplay(processedImage, imageLabel);

				}
			}
				
		});
		btnGradient.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/gradient.png")));
		btnGradient.setToolTipText("Gradient");
		btnGradient.setBounds(477, 0, 43, 38);
		frmSin.getContentPane().add(btnGradient);
		
		JButton btnArrows = new JButton("");
		btnArrows.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/lines.png")));
		btnArrows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					Mask kernel = new Mask(KERNEL_SIZE, KERNEL_SIZE);
					
					//weights is a matrix with kernel size -> Sobel filter
					double[][] horizontal = {{-1,-1,-1},{2,2,2},{-1,-1,-1}}; //horizontal
					double[][] vertical   = {{-1,2,-1},{-1,2,-1},{-1,2,-1}}; //vertical
					double[][] diagonal1  = {{2,-1,-1},{-1,2,-1},{-1,-1,2}}; //diagonal1
					double[][] diagonal2  = {{1,-1,2},{-1,2,-1},{2,-1,-1}}; //diagonal2
					
					//store the number of black pixels that have appeared in column, line
					long totalVertical    = 0; 
					long totalHorizontal  = 0;
					long totalDiagonal    = 0;
					
					//set the weights to the kernel and process vertical
					kernel.setWeights(vertical);
					totalVertical = pdi.lineDetector(processedImage, kernel, vertical);

					//set the weights to the kernel and process horizontal
					kernel.setWeights(horizontal);
					totalHorizontal = pdi.lineDetector(processedImage, kernel, horizontal);

					//set the weights to the kernel and process diagonal
					kernel.setWeights(diagonal1);
					totalDiagonal = pdi.lineDetector(processedImage, kernel, diagonal1);
					
//					System.out.println(totalVertical);
//					System.out.println(totalHorizontal);
//					System.out.println(totalDiagonal);
					
					//vertical
					if((totalVertical > totalHorizontal) && (totalVertical > totalDiagonal)){
						
						JOptionPane.showMessageDialog(null, "Image with a vertical line!");
						
					//horizontal	
					}else if((totalHorizontal > totalVertical) && (totalHorizontal > totalDiagonal)){
						
						JOptionPane.showMessageDialog(null, "Image with a horizontal line!");
					
					//diagonal
					}else{
						
						JOptionPane.showMessageDialog(null, "Image with a diagonal line!");
					}				
				}
					
			}
		});
		btnArrows.setToolTipText("Detect Line Orientation");
		btnArrows.setBounds(689, 0, 43, 38);
		frmSin.getContentPane().add(btnArrows);
		
		JButton btnObjects = new JButton("");
		btnObjects.setIcon(new ImageIcon(PDIView.class.getResource("/usp/icmc/labes/resources/objects.png")));
		btnObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Flood fill
				
				if (image == null) {

					JOptionPane.showMessageDialog(null, "Open a image first!");
				} else {

					BufferedImage fillImage = null;
					
					//fill with blue color
					Color srcColor = null;
					srcColor = Color.BLUE;
					
					//controller
					boolean [][] mark = new boolean [processedImage.getHeight()][processedImage.getWidth()];
					
				
					//go though each pixel in the image
					for (int line = 0; line < processedImage.getHeight(); line++) {
						for (int column = 0; column < processedImage.getWidth(); column++) {
							fillImage = pdi.flood_fill(processedImage, mark, line, column, srcColor);
						}
					}
					
					processedImage = fillImage;
					resizeDisplay(processedImage, imageLabel);

				}
				
			}
		});
		btnObjects.setToolTipText("Detect the number of objects in a image");
		btnObjects.setBounds(741, 0, 43, 38);
		frmSin.getContentPane().add(btnObjects);
		
		JButton btnPass1 = new JButton("");
		btnPass1.setToolTipText("Noise (Salt and pepper)");
		btnPass1.setBounds(794, 0, 43, 38);
		frmSin.getContentPane().add(btnPass1);
		
		JButton btnPass2 = new JButton("");
		btnPass2.setToolTipText("Noise (Salt and pepper)");
		btnPass2.setBounds(847, 0, 43, 38);
		frmSin.getContentPane().add(btnPass2);

		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fs = new JFileChooser(new File("c:\\"));
				fs.setDialogTitle("Select a image");

				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image File", "jpg", "jpge", "png", "bmp");
				fs.setFileFilter(filter);

				int result = fs.showOpenDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {

					try {

						String path = fs.getSelectedFile().getPath();

						// have the path.. create the buffered image

						image = ImageIO.read(new File(path));

						// resize and displays
						resizeDisplay(image, imageLabel);

					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null, "File Not Found.");
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Can't Read the File.");
					}
				}

			}
		});

	}
}
