/**
 * 	Options.java is part of Plant Evaluation.
 *
 *  Plant Evaluation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plant Evaluation is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Plant Evaluation.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.atomfrede.tools.evalutation.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class OptionsDialog extends JDialog {

	JCheckBox shiftByOneHour;
	JSpinner sampleSpinner;

	public OptionsDialog(JFrame parent) {
		super();

		setModalityType(ModalityType.APPLICATION_MODAL);

		initialize();

		setLocationRelativeTo(parent);
		setResizable(false);

		setTitle("Options");
		setVisible(true);
	}

	private JSpinner getSampleSpinner() {
		if (sampleSpinner == null) {
			sampleSpinner = new JSpinner();
			SpinnerNumberModel numberModel = new SpinnerNumberModel(
					Options.getSampleRate(), 2.0, 10.0, 1.0);
			sampleSpinner.setModel(numberModel);

			sampleSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					SpinnerNumberModel model = (SpinnerNumberModel) sampleSpinner
							.getModel();
					double selectedValue = model.getNumber().doubleValue();
					Options.setSampleRate((int) selectedValue);

				}
			});
		}
		return sampleSpinner;
	}

	private JCheckBox getShiftByOneHourCheckBox() {
		if (shiftByOneHour == null) {
			shiftByOneHour = new JCheckBox();
			shiftByOneHour.setText("Shift by one Hour");
			shiftByOneHour.setSelected(Options.isShiftByOneHour());

			shiftByOneHour.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Options.setShiftByOneHour(shiftByOneHour.isSelected());

				}
			});
		}
		return shiftByOneHour;
	}

	private void initialize() {
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("pref, 4dlu, fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		builder.append(getShiftByOneHourCheckBox(), 3);
		builder.append("Sample Rate");
		builder.append(getSampleSpinner());
		// builder.append("Shift by one Hour");

		setPreferredSize(builder.getPanel().getPreferredSize());
		Dimension prefSize = builder.getPanel().getPreferredSize();

		prefSize.height = prefSize.height + 25;
		prefSize.width = prefSize.width + 15;

		setSize(prefSize);
		add(builder.getPanel(), BorderLayout.CENTER);

	}

	public static class Options {

		static boolean shiftByOneHour = false;
		static int sampleRate = 10;
		static File inputFolder = new File("input");
		static File outputFolder = new File("output");
		static File temperatureInputFolder = new File(inputFolder, "temp");

		public static int getSampleRate() {
			return sampleRate;
		}

		public static void setSampleRate(int count) {
			sampleRate = count;
		}

		public static boolean isShiftByOneHour() {
			return shiftByOneHour;
		}

		public static void setShiftByOneHour(boolean shiftByOneHour) {
			Options.shiftByOneHour = shiftByOneHour;
		}

		public static File getInputFolder() {
			return inputFolder;
		}

		public static void setInputFolder(File inputFolder) {
			Options.inputFolder = inputFolder;
		}

		public static File getOutputFolder() {
			return outputFolder;
		}

		public static void setOutputFolder(File outputFolder) {
			Options.outputFolder = outputFolder;
		}

		public static File getTemperatureInputFolder() {
			return temperatureInputFolder;
		}

		public static void setTemperatureInputFolder(File temperatureInputFolder) {
			Options.temperatureInputFolder = temperatureInputFolder;
		}
	}

}
