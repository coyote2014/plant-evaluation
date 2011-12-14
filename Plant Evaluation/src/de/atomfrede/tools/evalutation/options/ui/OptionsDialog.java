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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.ui.res.Messages;

public class OptionsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2017237494464526876L;
	JCheckBox shiftByOneHourCheckBox, recordReferenceChambersCheckbox;
	JSpinner sampleSpinner;
	JButton okButton;

	double sampleRate = Options.getSampleRate();

	public OptionsDialog(JFrame parent) {
		super();

		setModalityType(ModalityType.APPLICATION_MODAL);

		initialize();

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
					// TODO Auto-generated method stub
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

			// recordReferenceChambersCheckbox
			// .addActionListener(new ActionListener() {
			//
			// @Override
			// public void actionPerformed(ActionEvent e) {
			// Options.setRecordReferenceChambers(recordReferenceChambersCheckbox
			// .isSelected());
			// }
			// });
		}
		return recordReferenceChambersCheckbox;
	}

	private JCheckBox getShiftByOneHourCheckBox() {
		if (shiftByOneHourCheckBox == null) {
			shiftByOneHourCheckBox = new JCheckBox();
			shiftByOneHourCheckBox.setText(Messages.getString("OptionsDialog.1")); //$NON-NLS-1$
			shiftByOneHourCheckBox.setSelected(Options.isShiftByOneHour());

			// shiftByOneHourCheckBox.addActionListener(new ActionListener() {
			//
			// @Override
			// public void actionPerformed(ActionEvent e) {
			// // TODO Auto-generated method stub
			// Options.setShiftByOneHour(shiftByOneHourCheckBox
			// .isSelected());
			//
			// }
			// });
		}
		return shiftByOneHourCheckBox;
	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton("OK");

			okButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					saveOptionsAndCloseDialog();

				}
			});
		}
		return okButton;
	}

	private void saveOptionsAndCloseDialog() {
		Options.setShiftByOneHour(shiftByOneHourCheckBox.isSelected());

		Options.setRecordReferenceChambers(recordReferenceChambersCheckbox.isSelected());

		Options.setRecordReferenceChambers(recordReferenceChambersCheckbox.isSelected());

		Options.setSampleRate(sampleRate);

		this.dispose();

	}

	private void initialize() {
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("pref, 4dlu, fill:pref:grow"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		builder.append(getShiftByOneHourCheckBox(), 3);
		builder.append(getRecordReferenceChambersCheckbox(), 3);
		builder.append(Messages.getString("OptionsDialog.3")); //$NON-NLS-1$
		builder.append(getSampleSpinner());

		builder.append(ButtonBarFactory.buildOKBar(getOkButton()), 3);

		setPreferredSize(builder.getPanel().getPreferredSize());
		Dimension prefSize = builder.getPanel().getPreferredSize();

		prefSize.height = prefSize.height + 25;
		prefSize.width = prefSize.width + 25;

		setSize(prefSize);
		add(builder.getPanel(), BorderLayout.CENTER);

	}

}
