/**
 *  Copyright 2011 Frederik Hahne
 *  
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

package de.atomfrede.tools.evalutation.options.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.ui.res.Messages;

public class OptionsDialog extends JDialog {

	private final Log log = LogFactory.getLog(OptionsDialog.class);
	private static final long serialVersionUID = 2017237494464526876L;
	JCheckBox shiftByOneHourCheckBox, recordReferenceChambersCheckbox;
	JSpinner sampleSpinner;
	JButton okButton, cancelButton;
	JCheckBox allSolenoidValvesCheckbox, solenoidValveOneCheckBox, solenoidValveTwoCheckBox, solenoidValveFourCheckBox, solenoidValveEightCheckBox,
			solenoidValveSixteenCheckBox;

	double sampleRate = Options.getSampleRate();

	List<Double> solenoidValvesOfInterest = new ArrayList<Double>();
	List<Double> allSolenoidValves = new ArrayList<Double>();

	public OptionsDialog(JFrame parent) {
		super();

		setModalityType(ModalityType.APPLICATION_MODAL);

		allSolenoidValves = Options.getAllSolenoidValves();

		solenoidValvesOfInterest = new ArrayList<Double>();
		for (Object valve : Options.getSolenoidValvesOfInterest()) {
			if (valve instanceof Double)
				solenoidValvesOfInterest.add((Double) valve);
			else
				solenoidValvesOfInterest.add(Double.valueOf((String) valve));
		}

		initialize();

		selectSolenoidValves();
		setLocationRelativeTo(parent);
		setResizable(false);

		setTitle(Messages.getString("OptionsDialog.0")); //$NON-NLS-1$
		setVisible(true);
	}

	private JSpinner getSampleSpinner() {
		if (sampleSpinner == null) {
			sampleSpinner = new JSpinner();
			SpinnerNumberModel numberModel = new SpinnerNumberModel(Options.getSampleRate(), 1.0, 10.0, 1.0);
			sampleSpinner.setModel(numberModel);

			sampleSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					SpinnerNumberModel model = (SpinnerNumberModel) sampleSpinner.getModel();
					double selectedValue = model.getNumber().doubleValue();
					sampleRate = selectedValue;

				}
			});
		}
		return sampleSpinner;
	}

	private JCheckBox getRecordReferenceChambersCheckbox() {
		if (recordReferenceChambersCheckbox == null) {
			recordReferenceChambersCheckbox = new JCheckBox();
			recordReferenceChambersCheckbox.setText("Record Reference Chambers");
			recordReferenceChambersCheckbox.setSelected(Options.isRecordReferenceChambers());
		}
		return recordReferenceChambersCheckbox;
	}

	private JCheckBox getShiftByOneHourCheckBox() {
		if (shiftByOneHourCheckBox == null) {
			shiftByOneHourCheckBox = new JCheckBox();
			shiftByOneHourCheckBox.setText(Messages.getString("OptionsDialog.1")); //$NON-NLS-1$
			shiftByOneHourCheckBox.setSelected(Options.isShiftByOneHour());
		}
		return shiftByOneHourCheckBox;
	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton("OK");

			okButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					saveOptionsAndCloseDialog();
				}
			});
		}
		return okButton;
	}

	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton("Cancel");

			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					closeDialog();
				}
			});
		}
		return cancelButton;
	}

	private JCheckBox getAllSolenoidValveCheckBox() {
		if (allSolenoidValvesCheckbox == null) {
			allSolenoidValvesCheckbox = new JCheckBox("All Valves");

			allSolenoidValvesCheckbox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					selectAllCheckBox();
				}
			});
		}
		return allSolenoidValvesCheckbox;
	}

	private JCheckBox getSolenoidValveOneCheckBox() {
		if (solenoidValveOneCheckBox == null) {
			solenoidValveOneCheckBox = new JCheckBox("1.0");

			solenoidValveOneCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (solenoidValveOneCheckBox.isSelected())
						solenoidValvesOfInterest.add(Double.valueOf(1.0));
					else
						solenoidValvesOfInterest.remove(Double.valueOf(1.0));
				}
			});
		}
		return solenoidValveOneCheckBox;
	}

	private JCheckBox getSolenoidValveTwoCheckBox() {
		if (solenoidValveTwoCheckBox == null) {
			solenoidValveTwoCheckBox = new JCheckBox("2.0");

			solenoidValveTwoCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (solenoidValveTwoCheckBox.isSelected())
						solenoidValvesOfInterest.add(Double.valueOf(2.0));
					else
						solenoidValvesOfInterest.remove(Double.valueOf(2.0));
				}
			});
		}
		return solenoidValveTwoCheckBox;
	}

	private JCheckBox getSolenoidValveFourCheckBox() {
		if (solenoidValveFourCheckBox == null) {
			solenoidValveFourCheckBox = new JCheckBox("4.0");

			solenoidValveFourCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (solenoidValveFourCheckBox.isSelected())
						solenoidValvesOfInterest.add(Double.valueOf(4.0));
					else
						solenoidValvesOfInterest.remove(Double.valueOf(4.0));
				}
			});
		}
		return solenoidValveFourCheckBox;
	}

	private JCheckBox getSolenoidValveEightCheckBox() {
		if (solenoidValveEightCheckBox == null) {
			solenoidValveEightCheckBox = new JCheckBox("8.0");

			solenoidValveEightCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (solenoidValveEightCheckBox.isSelected())
						solenoidValvesOfInterest.add(Double.valueOf(8.0));
					else
						solenoidValvesOfInterest.remove(Double.valueOf(8.0));
				}
			});
		}
		return solenoidValveEightCheckBox;
	}

	private JCheckBox getSolenoidValveSixteenCheckBox() {
		if (solenoidValveSixteenCheckBox == null) {
			solenoidValveSixteenCheckBox = new JCheckBox("16.0");

			solenoidValveSixteenCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (solenoidValveSixteenCheckBox.isSelected())
						solenoidValvesOfInterest.add(Double.valueOf(16.0));
					else
						solenoidValvesOfInterest.remove(Double.valueOf(16.0));
				}
			});

		}
		return solenoidValveSixteenCheckBox;
	}

	private void saveOptionsAndCloseDialog() {
		Options.setShiftByOneHour(shiftByOneHourCheckBox.isSelected());

		Options.setRecordReferenceChambers(recordReferenceChambersCheckbox.isSelected());

		Options.setRecordReferenceChambers(recordReferenceChambersCheckbox.isSelected());

		Options.setSampleRate(sampleRate);

		Options.setSolenoidValvesOfInterest(solenoidValvesOfInterest);

		this.dispose();

	}

	private void closeDialog() {
		this.dispose();
	}

	private void initialize() {
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("pref, 4dlu, pref, 4dlu,fill:pref:grow"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		builder.append(getShiftByOneHourCheckBox(), 5);
		builder.append(getRecordReferenceChambersCheckbox(), 5);
		builder.append(Messages.getString("OptionsDialog.3")); //$NON-NLS-1$
		builder.append(getSampleSpinner(), 3);

		builder.nextLine();

		// solenoid valves of interest
		builder.appendSeparator("Solenoid Valves of Interest");

		builder.append(getAllSolenoidValveCheckBox(), 5);
		builder.append(getSolenoidValveOneCheckBox());
		builder.append(getSolenoidValveTwoCheckBox());
		builder.append(getSolenoidValveFourCheckBox());
		builder.append(getSolenoidValveEightCheckBox());
		builder.append(getSolenoidValveSixteenCheckBox());

		builder.nextLine();
		builder.append(ButtonBarFactory.buildOKCancelBar(getOkButton(), getCancelButton()), 5);

		setPreferredSize(builder.getPanel().getPreferredSize());
		Dimension prefSize = builder.getPanel().getPreferredSize();

		prefSize.height = prefSize.height + 25;
		prefSize.width = prefSize.width + 25;

		setSize(prefSize);
		add(builder.getPanel(), BorderLayout.CENTER);

	}

	private void selectAllCheckBox() {
		if (allSolenoidValvesCheckbox.isSelected()) {
			getSolenoidValveOneCheckBox().setEnabled(false);
			getSolenoidValveTwoCheckBox().setEnabled(false);
			getSolenoidValveFourCheckBox().setEnabled(false);
			getSolenoidValveEightCheckBox().setEnabled(false);
			getSolenoidValveSixteenCheckBox().setEnabled(false);

			getSolenoidValveOneCheckBox().setSelected(true);
			getSolenoidValveTwoCheckBox().setSelected(true);
			getSolenoidValveFourCheckBox().setSelected(true);
			getSolenoidValveEightCheckBox().setSelected(true);
			getSolenoidValveSixteenCheckBox().setSelected(true);

			solenoidValvesOfInterest.clear();
			solenoidValvesOfInterest.addAll(allSolenoidValves);

		} else {
			getSolenoidValveOneCheckBox().setEnabled(true);
			getSolenoidValveTwoCheckBox().setEnabled(true);
			getSolenoidValveFourCheckBox().setEnabled(true);
			getSolenoidValveEightCheckBox().setEnabled(true);
			getSolenoidValveSixteenCheckBox().setEnabled(true);
		}
	}

	/**
	 * Selecting the solenoid valves corresponding to the currently selected
	 * ones in the properties file
	 */
	private void selectSolenoidValves() {
		int allCount = 5;
		int count = -1;

		if (solenoidValvesOfInterest.size() == allCount) {
			getAllSolenoidValveCheckBox().setSelected(true);
			selectAllCheckBox();
			return;
		} else {
			for (Double valve : solenoidValvesOfInterest) {
				count++;
				if (valve.doubleValue() == 1.0)
					getSolenoidValveOneCheckBox().setSelected(true);
				if (valve.doubleValue() == 2.0)
					getSolenoidValveTwoCheckBox().setSelected(true);
				if (valve.doubleValue() == 4.0)
					getSolenoidValveFourCheckBox().setSelected(true);
				if (valve.doubleValue() == 8.0)
					getSolenoidValveEightCheckBox().setSelected(true);
				if (valve.doubleValue() == 16.0)
					getSolenoidValveSixteenCheckBox().setSelected(true);
			}
		}
		log.info("Count = " + count);
		if (count >= allCount) {
			getAllSolenoidValveCheckBox().setSelected(true);
			selectAllCheckBox();
		}
	}
}
