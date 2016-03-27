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

public class ConfigurationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFormattedTextField intensityTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConfigurationDialog dialog = new ConfigurationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigurationDialog() {
		setResizable(false);
		setModal(true);
		setTitle("Change default configuration values");
		setBounds(100, 100, 500, 400);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(10, 327, 474, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{ // update the intensity value
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						// get the new value of intensity
						int newIntensity;
						newIntensity = Integer.parseInt(intensityTextField.getText());
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

		JLabel lblIntensity = new JLabel("Intensity value (Default = 10):");
		lblIntensity.setBounds(10, 31, 166, 14);
		getContentPane().add(lblIntensity);

		// Define the format to accept just numbers
		NumberFormat longFormat = NumberFormat.getIntegerInstance();
		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Long.class); // optional, ensures you will
													// always get a long value
		numberFormatter.setAllowsInvalid(false); // this is the key!!
		numberFormatter.setMinimum(0l); // Optional

		intensityTextField = new JFormattedTextField(numberFormatter);
		intensityTextField.setBounds(186, 28, 76, 20);
		getContentPane().add(intensityTextField);
		
		JLabel lblKernelSizedefault = new JLabel("Kernel size (Default = 3x3)");
		lblKernelSizedefault.setBounds(10, 62, 155, 14);
		getContentPane().add(lblKernelSizedefault);
		
		JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.setBounds(186, 59, 76, 20);
		getContentPane().add(formattedTextField);
	}
}
