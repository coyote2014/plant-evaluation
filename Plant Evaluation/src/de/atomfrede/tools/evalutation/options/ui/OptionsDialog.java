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
	// type A (= Juliane) Options
	JCheckBox shiftByOneHourCheckBox, recordReferenceValveCheckbox;
	JSpinner sampleSpinner;

	JButton okButton, cancelButton;

	// General Options (=Copy Evaluator)
	JCheckBox allSolenoidValvesCheckbox, solenoidValveOneCheckBox, solenoidValveTwoCheckBox, solenoidValveFourCheckBox, solenoidValveEightCheckBox,
			solenoidValveSixteenCheckBox;

	// CO2 Absolute only Evaluation
	JCheckBox co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox, co2Absolute_isCo2AbsoluteAutoscaleCheckbox;
	JSpinner co2Absolute_co2AbsoluteMinimumSpinner, co2Absolute_co2AbsoluteMaximumSpinner, co2Absolute_deltaFiveMinutesMinimumSpinner,
			co2Absolute_deltaFiveMinutesMaximumSpinner;

	JTabbedPane tabs;
	JPanel firstTab, secondTab, thirdTab, fourthTab;

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
		if (recordReferenceValveCheckbox == null) {
			recordReferenceValveCheckbox = new JCheckBox();
			recordReferenceValveCheckbox.setText(Messages.getString("OptionsDialog.2")); //$NON-NLS-1$
			recordReferenceValveCheckbox.setSelected(Options.isRecordReferenceChambers());
		}
		return recordReferenceValveCheckbox;
	}

	private JCheckBox getShiftByOneHourCheckBox() {
		if (shiftByOneHourCheckBox == null) {
			shiftByOneHourCheckBox = new JCheckBox();
			shiftByOneHourCheckBox.setText(Messages.getString("OptionsDialog.1")); //$NON-NLS-1$
			shiftByOneHourCheckBox.setSelected(Options.isShiftByOneHour());
		}
		return shiftByOneHourCheckBox;
	}

	private JCheckBox getCo2AbsoluteDeltaCheckBox() {
		if (co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox == null) {
			co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox = new JCheckBox();
			co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox.setText("Enable Autoscale");
			co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox.setSelected(Options.isAutoScaleDeltaFiveMinutes());

			co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Autoscale is disabled enable minimum and maximum spinners
					getCo2Absolute_deltaFiveMinutesMinimumSpinner().setEnabled(!co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox.isSelected());
					getCo2Absolute_deltaFiveMinutesMaximumSpinner().setEnabled(!co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox.isSelected());
				}
			});
		}
		return co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox;
	}

	private JCheckBox getCo2AbsoluteCo2AbsoluteCheckBox() {
		if (co2Absolute_isCo2AbsoluteAutoscaleCheckbox == null) {
			co2Absolute_isCo2AbsoluteAutoscaleCheckbox = new JCheckBox();
			co2Absolute_isCo2AbsoluteAutoscaleCheckbox.setText("Enable Autoscale");
			co2Absolute_isCo2AbsoluteAutoscaleCheckbox.setSelected(Options.isAutoScaleCO2Absolute());

			co2Absolute_isCo2AbsoluteAutoscaleCheckbox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Autoscale is disabled enable minimum and maximum spinners
					getCo2Absolute_Co2AbsoluteMinimumSpinner().setEnabled(!co2Absolute_isCo2AbsoluteAutoscaleCheckbox.isSelected());
					getCo2Absolute_Co2AbsoluteMaximumSpinner().setEnabled(!co2Absolute_isCo2AbsoluteAutoscaleCheckbox.isSelected());
				}
			});
		}
		return co2Absolute_isCo2AbsoluteAutoscaleCheckbox;
	}

	private JSpinner getCo2Absolute_Co2AbsoluteMinimumSpinner() {
		if (co2Absolute_co2AbsoluteMinimumSpinner == null) {
			co2Absolute_co2AbsoluteMinimumSpinner = new JSpinner(new SpinnerNumberModel(Options.co2AbsoluteOnly_getCo2AbsoluteDatasetMinimum(), Integer.MIN_VALUE,
					Integer.MAX_VALUE, 10));
			co2Absolute_co2AbsoluteMinimumSpinner.setEnabled(!Options.isAutoScaleCO2Absolute());
		}
		return co2Absolute_co2AbsoluteMinimumSpinner;
	}

	private JSpinner getCo2Absolute_Co2AbsoluteMaximumSpinner() {
		if (co2Absolute_co2AbsoluteMaximumSpinner == null) {
			co2Absolute_co2AbsoluteMaximumSpinner = new JSpinner(new SpinnerNumberModel(Options.co2AbsoluteOnly_getCo2AbsoluteDatasetMaximum(), Integer.MIN_VALUE,
					Integer.MAX_VALUE, 10));
			co2Absolute_co2AbsoluteMaximumSpinner.setEnabled(!Options.isAutoScaleCO2Absolute());
		}
		return co2Absolute_co2AbsoluteMaximumSpinner;
	}

	private JSpinner getCo2Absolute_deltaFiveMinutesMinimumSpinner() {
		if (co2Absolute_deltaFiveMinutesMinimumSpinner == null) {
			co2Absolute_deltaFiveMinutesMinimumSpinner = new JSpinner(new SpinnerNumberModel(Options.co2AbsoluteOnly_getDeltaFiveMinutesMinimum(), Integer.MIN_VALUE,
					Integer.MAX_VALUE, 10));
			co2Absolute_deltaFiveMinutesMinimumSpinner.setEnabled(!Options.isAutoScaleDeltaFiveMinutes());
		}
		return co2Absolute_deltaFiveMinutesMinimumSpinner;
	}

	private JSpinner getCo2Absolute_deltaFiveMinutesMaximumSpinner() {
		if (co2Absolute_deltaFiveMinutesMaximumSpinner == null) {
			co2Absolute_deltaFiveMinutesMaximumSpinner = new JSpinner(new SpinnerNumberModel(Options.co2AbsoluteOnly_getDeltaFiveMinutesMaximum(), Integer.MIN_VALUE,
					Integer.MAX_VALUE, 10));
			co2Absolute_deltaFiveMinutesMaximumSpinner.setEnabled(!Options.isAutoScaleDeltaFiveMinutes());
		}
		return co2Absolute_deltaFiveMinutesMaximumSpinner;
	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton(Messages.getString("OptionsDialog.4")); //$NON-NLS-1$

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
			cancelButton = new JButton(Messages.getString("OptionsDialog.5")); //$NON-NLS-1$

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
			allSolenoidValvesCheckbox = new JCheckBox(Messages.getString("OptionsDialog.6")); //$NON-NLS-1$

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
			solenoidValveOneCheckBox = new JCheckBox("1.0"); //$NON-NLS-1$

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
			solenoidValveTwoCheckBox = new JCheckBox("2.0"); //$NON-NLS-1$

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
			solenoidValveFourCheckBox = new JCheckBox("4.0"); //$NON-NLS-1$

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
			solenoidValveEightCheckBox = new JCheckBox("8.0"); //$NON-NLS-1$

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
			solenoidValveSixteenCheckBox = new JCheckBox("16.0"); //$NON-NLS-1$

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

	private JTabbedPane getTabs() {
		if (tabs == null) {
			tabs = new JTabbedPane();
			tabs.setTabPlacement(JTabbedPane.LEFT);
		}
		return tabs;
	}

	private void saveOptionsAndCloseDialog() {
		Options.setShiftByOneHour(shiftByOneHourCheckBox.isSelected());

		Options.setRecordReferenceChambers(recordReferenceValveCheckbox.isSelected());

		Options.setRecordReferenceChambers(recordReferenceValveCheckbox.isSelected());

		Options.setSampleRate(sampleRate);

		Options.setSolenoidValvesOfInterest(solenoidValvesOfInterest);

		// CO2 Absolute Only Evaluation Options
		Options.setAutoScaleCO2Absolute(co2Absolute_isCo2AbsoluteAutoscaleCheckbox.isSelected());
		Options.setAutoScaleDeltaFiveMinutes(co2Absolute_isDeltaFiveMinutesAutoscaleCheckbox.isSelected());
		Options.co2AbsoluteOnly_setCo2AbsoluteDatasetMinimum(Double.valueOf(getCo2Absolute_Co2AbsoluteMinimumSpinner().getValue() + ""));
		Options.co2AbsoluteOnly_setCo2AbsoluteDatasetMaximum(Double.valueOf(getCo2Absolute_Co2AbsoluteMaximumSpinner().getValue() + ""));
		Options.co2AbsoluteOnly_setDeltaFiveMinutesMinimum(Double.valueOf(getCo2Absolute_deltaFiveMinutesMinimumSpinner().getValue() + ""));
		Options.co2AbsoluteOnly_setDeltaFiveMinutesMaximum(Double.valueOf(getCo2Absolute_deltaFiveMinutesMaximumSpinner().getValue() + ""));

		this.dispose();

	}

	private void closeDialog() {
		this.dispose();
	}

	private JPanel getFirstTab() {
		if (firstTab == null) {
			firstTab = new JPanel();
			firstTab.setLayout(new BorderLayout());

			FormLayout layout = new FormLayout("pref, 4dlu, pref, 4dlu,fill:pref:grow"); //$NON-NLS-1$
			DefaultFormBuilder builder = new DefaultFormBuilder(layout);
			builder.setDefaultDialogBorder();

			// solenoid valves of interest
			builder.appendSeparator(Messages.getString("OptionsDialog.12")); //$NON-NLS-1$

			builder.append(getAllSolenoidValveCheckBox(), 5);
			builder.append(getSolenoidValveOneCheckBox());
			builder.append(getSolenoidValveTwoCheckBox());
			builder.append(getSolenoidValveFourCheckBox());
			builder.append(getSolenoidValveEightCheckBox());
			builder.append(getSolenoidValveSixteenCheckBox());

			firstTab.add(builder.getPanel(), BorderLayout.CENTER);

		}
		return firstTab;
	}

	private JPanel getSecondTab() {
		if (secondTab == null) {
			secondTab = new JPanel();
			secondTab.setLayout(new BorderLayout());

			FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow"); //$NON-NLS-1$
			DefaultFormBuilder builder = new DefaultFormBuilder(layout);
			builder.setDefaultDialogBorder();

			builder.appendSeparator("Plotting");
			builder.appendSeparator("CO2 Absolute");
			builder.append(getCo2AbsoluteCo2AbsoluteCheckBox(), 3);
			builder.append("Minimum", getCo2Absolute_Co2AbsoluteMinimumSpinner());
			builder.append("Maximum", getCo2Absolute_Co2AbsoluteMaximumSpinner());
			builder.nextLine();

			builder.appendSeparator("Delta 5 Minutes");
			builder.append(getCo2AbsoluteDeltaCheckBox(), 3);
			builder.append("Minimum", getCo2Absolute_deltaFiveMinutesMinimumSpinner());
			builder.append("Maximum", getCo2Absolute_deltaFiveMinutesMaximumSpinner());
			//builder.append("Options for CO2-Absolute Only Evalution"); //$NON-NLS-1$

			secondTab.add(builder.getPanel(), BorderLayout.CENTER);

		}

		return secondTab;
	}

	private JPanel getThirdTab() {
		if (thirdTab == null) {
			thirdTab = new JPanel();
			thirdTab.setLayout(new BorderLayout());

			FormLayout layout = new FormLayout("pref, 4dlu, pref, 4dlu,fill:pref:grow"); //$NON-NLS-1$
			DefaultFormBuilder builder = new DefaultFormBuilder(layout);
			builder.setDefaultDialogBorder();

			builder.append(getShiftByOneHourCheckBox(), 5);
			builder.append(getRecordReferenceChambersCheckbox(), 5);
			builder.append(Messages.getString("OptionsDialog.3")); //$NON-NLS-1$
			builder.append(getSampleSpinner(), 3);

			thirdTab.add(builder.getPanel(), BorderLayout.CENTER);
		}
		return thirdTab;
	}

	private JPanel getFourthTab() {
		if (fourthTab == null) {
			fourthTab = new JPanel();
			fourthTab.setLayout(new BorderLayout());

			FormLayout layout = new FormLayout("fill:pref:grow"); //$NON-NLS-1$
			DefaultFormBuilder builder = new DefaultFormBuilder(layout);
			builder.setDefaultDialogBorder();

			builder.append("Options for Ingo's Evaluation"); //$NON-NLS-1$

			fourthTab.add(builder.getPanel(), BorderLayout.CENTER);

		}
		return fourthTab;
	}

	private void initialize() {
		setLayout(new BorderLayout());

		getTabs().addTab(Messages.getString("OptionsDialog.13"), getFirstTab()); //$NON-NLS-1$
		getTabs().addTab(Messages.getString("OptionsDialog.14"), getSecondTab()); //$NON-NLS-1$
		getTabs().addTab(Messages.getString("OptionsDialog.15"), getThirdTab()); //$NON-NLS-1$
		getTabs().addTab(Messages.getString("OptionsDialog.9"), getFourthTab()); //$NON-NLS-1$

		FormLayout layout = new FormLayout("fill:pref:grow"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.append(getTabs());
		builder.append(ButtonBarFactory.buildOKCancelBar(getOkButton(), getCancelButton()));

		setPreferredSize(builder.getPanel().getPreferredSize());

		Dimension prefSize = builder.getPanel().getPreferredSize();
		prefSize.height = prefSize.height + 35;
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
		log.info(Messages.getString("OptionsDialog.17") + count); //$NON-NLS-1$
		if (count >= allCount) {
			getAllSolenoidValveCheckBox().setSelected(true);
			selectAllCheckBox();
		}
	}
}
