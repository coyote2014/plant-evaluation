/**
u *  Copyright 2011 Frederik Hahne
 *  
 * 	PlantDatesInputPanel.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.ui.plant;

import java.awt.BorderLayout;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.Plant;
import de.atomfrede.tools.evalutation.ui.dateTime.DateAndTimePicker;
import de.atomfrede.tools.evalutation.ui.res.Messages;

public class PlantDataInputPanel extends JPanel {

	static final ImageIcon IC_DELETE = new ImageIcon(
			PlantDataInputPanel.class.getResource("../res/list-remove.png")); //$NON-NLS-1$

	Plant plant;

	DateAndTimePicker startDatePicker, endDatePicker;
	JButton deleteButton;
	JSpinner lowerLeafAreaSpinner, upperLeafAreaSpinner;

	public PlantDataInputPanel() {
		this(new Plant());
	}

	public PlantDataInputPanel(Plant plant) {
		this.plant = plant;
		initialize();
		getStartPicker().setDate(plant.getStartDate());
		getEndPicker().setDate(plant.getEndDate());
		getLowerLeafAreaSpinner().setValue(plant.getLowerLeafArea());
		getUpperLeafAreaSpinner().setValue(plant.getUpperLeafArea());
	}

	private void initialize() {
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(2);

		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout(
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append(
				Messages.getString("PlantDataInputPanel.2"), getStartPicker()); //$NON-NLS-1$
		builder.append(
				Messages.getString("PlantDataInputPanel.3"), getEndPicker()); //$NON-NLS-1$
		builder.append(getDeleteButton());

		builder.append(Messages.getString("PlantDataInputPanel.4")); //$NON-NLS-1$
		builder.append(getLowerLeafAreaSpinner());

		builder.append(Messages.getString("PlantDataInputPanel.5")); //$NON-NLS-1$
		builder.append(getUpperLeafAreaSpinner());
		add(builder.getPanel(), BorderLayout.CENTER);

	}

	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButton.setIcon(IC_DELETE);
		}
		return deleteButton;
	}

	private DateAndTimePicker getStartPicker() {
		if (startDatePicker == null) {
			startDatePicker = new DateAndTimePicker();
		}
		return startDatePicker;
	}

	private DateAndTimePicker getEndPicker() {
		if (endDatePicker == null) {
			endDatePicker = new DateAndTimePicker();
		}
		return endDatePicker;
	}

	private JSpinner getLowerLeafAreaSpinner() {
		if (lowerLeafAreaSpinner == null) {
			lowerLeafAreaSpinner = new JSpinner(new SpinnerNumberModel(0.0,
					0.0, 1.0, 0.00000000001));

			JSpinner.NumberEditor leafAreaEditor = new JSpinner.NumberEditor(
					lowerLeafAreaSpinner, "0.0000000000###"); //$NON-NLS-1$

			lowerLeafAreaSpinner.setEditor(leafAreaEditor);

			lowerLeafAreaSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					if (lowerLeafAreaSpinner != null) {
						SpinnerNumberModel model = (SpinnerNumberModel) lowerLeafAreaSpinner
								.getModel();
						getPlant().setLowerLeafArea(
								model.getNumber().doubleValue());
					}

				}
			});
		}
		return lowerLeafAreaSpinner;
	}

	private JSpinner getUpperLeafAreaSpinner() {
		if (upperLeafAreaSpinner == null) {
			upperLeafAreaSpinner = new JSpinner(new SpinnerNumberModel(0.0,
					0.0, 1.0, 0.00000000001));

			JSpinner.NumberEditor leafAreaEditor = new JSpinner.NumberEditor(
					upperLeafAreaSpinner, "0.0000000000###"); //$NON-NLS-1$

			upperLeafAreaSpinner.setEditor(leafAreaEditor);

			upperLeafAreaSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					if (upperLeafAreaSpinner != null) {
						SpinnerNumberModel model = (SpinnerNumberModel) upperLeafAreaSpinner
								.getModel();
						getPlant().setUpperLeafArea(
								model.getNumber().doubleValue());
					}

				}
			});
		}
		return upperLeafAreaSpinner;
	}

	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
	}

	public Date getStartDate() {
		return getStartPicker().getDate();
	}

	public Date getEndDate() {
		return getEndPicker().getDate();
	}
}
