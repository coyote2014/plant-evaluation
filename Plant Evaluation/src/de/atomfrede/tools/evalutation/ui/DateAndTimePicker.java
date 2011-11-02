/**
 * 	DateAndTimePicker.java is part of Plant Evaluation.
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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.jdesktop.swingx.JXDatePicker;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class DateAndTimePicker extends JPanel {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yy HH:mm:ss");

	private JXDatePicker datePicker = null;
	private JSpinner timeSpinner = null;

	public DateAndTimePicker() {
		initialize();
	}

	public void setDate(Date date) {
		getTimeSpinner().setValue(date);
		getDatePicker().setDate(date);
	}

	private void initialize() {
		setLayout(new BorderLayout());
		FormLayout layout = new FormLayout("pref, 4dlu, pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append(getDatePicker());
		builder.append(getTimeSpinner());

		add(builder.getPanel(), BorderLayout.CENTER);

	}

	private JSpinner getTimeSpinner() {
		if (timeSpinner == null) {
			timeSpinner = new JSpinner(new SpinnerDateModel());
			JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(
					timeSpinner, "HH:mm:ss");
			timeSpinner.setEditor(timeEditor);
		}
		return timeSpinner;
	}

	private JXDatePicker getDatePicker() {
		if (datePicker == null) {
			datePicker = new JXDatePicker();
		}
		return datePicker;
	}
}
