/**
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

package de.atomfrede.tools.evalutation.ui;

import java.awt.BorderLayout;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class PlantDatesInputPanel extends JPanel {

	DateAndTimePicker startDatePicker, endDatePicker;
	JButton deleteButton;
	int plantNumber;

	public PlantDatesInputPanel() {
		this(1);
	}

	public PlantDatesInputPanel(int plantNumber) {
		this.plantNumber = plantNumber;
		initialize();
	}

	private void initialize() {
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(2);

		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout(
				"pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.appendSeparator("Plant No " + nf.format(plantNumber));
		builder.append("Start ", getStartPicker());
		builder.append("End ", getEndPicker());
		builder.append(getDeleteButton());
		add(builder.getPanel(), BorderLayout.CENTER);

	}

	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton("-");
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
}
