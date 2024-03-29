/**
 *  Copyright 2011 Frederik Hahne
 *  
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

package de.atomfrede.tools.evalutation.ui.dateTime;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

/**
 * Combines a DatePicker with a spinner to change the time of the selected date.
 */
public class DateAndTimePicker extends JPanel {

	private static final long serialVersionUID = 7832437980182475053L;

	private JXDatePicker datePicker = null;
	private JSpinner timeSpinner = null;
	private Calendar calendar = Calendar.getInstance();

	public DateAndTimePicker() {
		initialize();
	}

	public void setDate(Date date) {
		getTimeSpinner().setValue(date);
		getDatePicker().setDate(date);

		calendar.setTime(date);
	}

	private void initialize() {
		setLayout(new BorderLayout());
		FormLayout layout = new FormLayout("fill:pref:grow, 4dlu, fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append(getDatePicker());
		builder.append(getTimeSpinner());

		add(builder.getPanel(), BorderLayout.CENTER);

	}

	public JSpinner getTimeSpinner() {
		if (timeSpinner == null) {
			timeSpinner = new JSpinner(new SpinnerDateModel());
			JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
			timeSpinner.setEditor(timeEditor);

			timeSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					SpinnerDateModel model = (SpinnerDateModel) timeSpinner.getModel();

					Calendar cal = Calendar.getInstance();
					cal.setTime(model.getDate());

					int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
					int minute = cal.get(Calendar.MINUTE);
					int second = cal.get(Calendar.SECOND);
					int millisecond = cal.get(Calendar.MILLISECOND);

					getCalendar().set(Calendar.HOUR_OF_DAY, hourOfDay);
					getCalendar().set(Calendar.MINUTE, minute);
					getCalendar().set(Calendar.SECOND, second);
					getCalendar().set(Calendar.MILLISECOND, millisecond);

				}
			});
		}
		return timeSpinner;
	}

	public JXDatePicker getDatePicker() {
		if (datePicker == null) {
			datePicker = new JXDatePicker();
			datePicker.setUI(new CustomDatePickerUI());

			datePicker.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Date date = datePicker.getDate();
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH);
					int day = cal.get(Calendar.DATE);
					getCalendar().set(year, month, day);

				}
			});
		}
		return datePicker;
	}

	public Date getDate() {
		return getCalendar().getTime();
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	/**
	 * Custom UI that uses an Tango Icon as calendar icon
	 */
	private static class CustomDatePickerUI extends BasicDatePickerUI {

		@Override
		protected JButton createPopupButton() {
			JButton b = new JButton();
			b.setName("popupButton");
			b.setRolloverEnabled(false);
			b.setMargin(new Insets(0, 0, 0, 0));

			b.setBackground(null);

			Icon icon = Icons.IC_OFFICE_CALENDAR_SMALL;
			if (icon == null) {
				icon = (Icon) UIManager.get("Tree.expandedIcon");
			}
			b.setIcon(icon);
			b.setFocusable(false);
			return b;
		}
	}
}
