package labes.icmc.usp.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

public class IntensityViewer extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFormattedTextField formattedTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			IntensityViewer dialog = new IntensityViewer();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public IntensityViewer() {
		setResizable(false);
		setModal(true);
		setTitle("Change the intensity value (Default = 10)");
		setBounds(100, 100, 345, 127);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(10, 59, 316, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{ // update the intensity value
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						// get the new value of intensity
						int newIntensity;
						newIntensity = Integer.parseInt(formattedTextField.getText());
						System.out.println(newIntensity);

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{ // close the dialog
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						dispose();

					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		JLabel lblIntensityValue = new JLabel("Intensity Value:");
		lblIntensityValue.setBounds(10, 31, 86, 14);
		getContentPane().add(lblIntensityValue);

		// Define the format to accept just numbers
		NumberFormat longFormat = NumberFormat.getIntegerInstance();
		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Long.class); // optional, ensures you will
													// always get a long value
		numberFormatter.setAllowsInvalid(false); // this is the key!!
		numberFormatter.setMinimum(0l); // Optional

		formattedTextField = new JFormattedTextField(numberFormatter);
		formattedTextField.setBounds(92, 28, 76, 20);
		getContentPane().add(formattedTextField);
	}
}
