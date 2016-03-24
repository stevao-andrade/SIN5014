package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

import control.Histogram;
import control.PDI;
import net.coobird.thumbnailator.Thumbnails;

public class PDIView {
	
	
	private final int WITDH_PANEL_SIZE = 682;
	private final int HEIGHT_PANEL_SIZE = 477;
	
	private JFrame frmSin;
	
	
	private PDI pdi = new PDI();
	
	
	BufferedImage image = null;
	BufferedImage processedImage = null;
	
	private int intensity = 10;
	
	
	//set a new value to be used in intensity buttons 
	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	
	/**
	 * Resize the loaded image to a pre defined resolution 
	 * @param imageToResize
	 * @param labelToDisplay
	 */
	private void resizeDisplay(BufferedImage imageToResize, JLabel labelToDisplay){	
		
		try {
			processedImage = Thumbnails.of(imageToResize).size(WITDH_PANEL_SIZE, HEIGHT_PANEL_SIZE).asBufferedImage();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File Not Found.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Can't Read the File.");
		}
		
		//set into the label
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
		frmSin.setBounds(100, 100, 800, 600);
		frmSin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmSin.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		
		mnFile.add(mntmOpen);
		
		
		
		//Exit menu option
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				System.exit(0);
				
			}
		});
		
		
		//save the processed image
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fs = new JFileChooser(new File("c:\\"));
				fs.setDialogTitle("Save a File (*.jpg)");
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image File", "jpg", "jpge", "png");
				fs.setFileFilter(filter);
				
				int result = fs.showSaveDialog(null);
				
				if (result == JFileChooser.APPROVE_OPTION){
					
					String filename = fs.getSelectedFile().toString() + ".jpg";
					
					File file = new File(filename);

					try {
						
						ImageIO.write(processedImage, "jpg", file);
						System.out.println(file.getPath());
						
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Can't save the file");
					} 
							
				} 
				
			}
		});
		mnFile.add(mntmSave);
		mnFile.add(mntmExit);
		
		JMenu mnIntensity = new JMenu("Intensity");
		menuBar.add(mnIntensity);
		
		//change the intensity value
		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				IntensityViewer intensityDialog = new IntensityViewer();
				
				intensityDialog.setVisible(true);
				
				
			}
		});
		mnIntensity.add(mntmConfiguration);
		frmSin.getContentPane().setLayout(null);
		
		
		
		final JPanel originalPanel = new JPanel();
		originalPanel.setBackground(Color.LIGHT_GRAY);
		originalPanel.setBounds(67, 49, 682, 477);
		frmSin.getContentPane().add(originalPanel);
		
		final JLabel imageLabel = new JLabel("");
		originalPanel.add(imageLabel);
		
		
		//create a histogram of the image
		JButton btnHistogram = new JButton("");
		btnHistogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(processedImage == null){
					
					JOptionPane.showMessageDialog(null, "Open a image first!");
				}
				else{
				
					//histogram test
					int[] array;
					
					//get the frequency of each color and store into a array
					array = pdi.getFrequencyToHistogram(processedImage);
					
					for(int i: array){
						System.out.println(i);
					}
					
					Histogram h = new Histogram("Frequency Histogram", "Frequency Histogram", array);
			        h.pack();
			        h.setVisible(true);
			        
				}
			}
		});
		btnHistogram.setToolTipText("Histogram");
		btnHistogram.setIcon(new ImageIcon(PDIView.class.getResource("/images/histogram (Custom).png")));
		btnHistogram.setBounds(0, 0, 51, 38);
		frmSin.getContentPane().add(btnHistogram);
		
		
		//Set a image to gray scale
		JButton btnGrayScale = new JButton("");
		btnGrayScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(image == null){
					
					JOptionPane.showMessageDialog(null, "Open a image first!");
				}
				else{
					BufferedImage grayImage = null;
					grayImage = pdi.setGrayScale(processedImage); 
					
					processedImage = grayImage;
					
					resizeDisplay(processedImage, imageLabel);
					
				}
				
			}
		});
		btnGrayScale.setToolTipText("Gray Scale");
		btnGrayScale.setIcon(new ImageIcon(PDIView.class.getResource("/images/gray (Custom).png")));
		btnGrayScale.setBounds(57, 0, 51, 38);
		frmSin.getContentPane().add(btnGrayScale);
		
		
		//increases the value of the intensity in the pixels of the image
		JButton btnIntensityUp = new JButton("");
		btnIntensityUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(image == null){
					
					JOptionPane.showMessageDialog(null, "Open a image first!");
				}
				else{
					
					BufferedImage intensityImage = null;
					
					intensityImage = pdi.changeColorIntensity(processedImage, intensity); 
					processedImage = intensityImage;
					resizeDisplay(processedImage, imageLabel);
					
				}
				
			}
		});
		btnIntensityUp.setToolTipText("Intensity Up");
		btnIntensityUp.setIcon(new ImageIcon(PDIView.class.getResource("/images/up (Custom).png")));
		btnIntensityUp.setBounds(118, 0, 51, 38);
		frmSin.getContentPane().add(btnIntensityUp);
		
		
		//reduces the value of the intensity in the pixels of the image
		JButton btnIntensityDown = new JButton("");
		btnIntensityDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(image == null){
					
					JOptionPane.showMessageDialog(null, "Open a image first!");
				}
				else{
					
					BufferedImage intensityImage = null;
					
					intensityImage = pdi.changeColorIntensity(processedImage, - intensity); 
					
					processedImage = intensityImage;
					resizeDisplay(processedImage, imageLabel);

				}
				
			}
		});
		btnIntensityDown.setToolTipText("Intensity Down");
		btnIntensityDown.setIcon(new ImageIcon(PDIView.class.getResource("/images/down (Custom).png")));
		btnIntensityDown.setBounds(179, 0, 51, 38);
		frmSin.getContentPane().add(btnIntensityDown);
		
		JButton btnMeanFilter = new JButton("");
		btnMeanFilter.setToolTipText("Mean Filter");
		btnMeanFilter.setIcon(new ImageIcon(PDIView.class.getResource("/images/mean (Custom).png")));
		btnMeanFilter.setBounds(240, 0, 51, 38);
		frmSin.getContentPane().add(btnMeanFilter);
		
		JButton btnMedianFilter = new JButton("");
		btnMedianFilter.setToolTipText("Median Filter");
		btnMedianFilter.setIcon(new ImageIcon(PDIView.class.getResource("/images/median (Custom).png")));
		btnMedianFilter.setBounds(301, 0, 51, 38);
		frmSin.getContentPane().add(btnMedianFilter);
		
		JButton btnEqualization = new JButton("");
		btnEqualization.setToolTipText("Equalizer");
		btnEqualization.setIcon(new ImageIcon(PDIView.class.getResource("/images/equalizer (Custom).png")));
		btnEqualization.setBounds(362, 0, 51, 38);
		frmSin.getContentPane().add(btnEqualization);
		
		JButton btnLowPassFilter = new JButton("");
		btnLowPassFilter.setToolTipText("Low Pass Filter");
		btnLowPassFilter.setIcon(new ImageIcon(PDIView.class.getResource("/images/low_frequency (Custom).png")));
		btnLowPassFilter.setBounds(423, 0, 51, 38);
		frmSin.getContentPane().add(btnLowPassFilter);
		
		JButton btnBorderOperator = new JButton("");
		btnBorderOperator.setToolTipText("Border Operator");
		btnBorderOperator.setIcon(new ImageIcon(PDIView.class.getResource("/images/border (Custom).png")));
		btnBorderOperator.setBounds(484, 0, 51, 38);
		frmSin.getContentPane().add(btnBorderOperator);
		
		
		//set noise to a image
		JButton btnNoise = new JButton("");
		btnNoise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(image == null){
					
					JOptionPane.showMessageDialog(null, "Open a image first!");
				}
				else{
					
					BufferedImage noisedImage = null;
					
					noisedImage = pdi.generateNoise(processedImage); 
					
					processedImage = noisedImage;
					resizeDisplay(processedImage, imageLabel);

				}
				
			}
		});
		btnNoise.setIcon(new ImageIcon(PDIView.class.getResource("/images/noise (Custom).png")));
		btnNoise.setToolTipText("Noise (Salt and pepper)");
		btnNoise.setBounds(545, 0, 51, 38);
		frmSin.getContentPane().add(btnNoise);
		

		
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fs = new JFileChooser(new File("c:\\"));
				fs.setDialogTitle("Select a image");
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image File", "jpg", "jpge", "png");
				fs.setFileFilter(filter);
				
				int result = fs.showOpenDialog(null);
				
				if (result == JFileChooser.APPROVE_OPTION){
					
						try {
							
							String path = fs.getSelectedFile().getPath();
							
							//have the path.. create the buffered image
							
							image = ImageIO.read(new File(path));
							
							//resize and displays
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
