/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	BusyPanel.java is part of Plant Evaluation.
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

import javax.swing.JDialog;

import org.jdesktop.swingx.JXBusyLabel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class BusyDialog extends JDialog {

	JXBusyLabel busyLabel;
	String busyText;

	public BusyDialog() {
		this("Please Wait...");
	}

	public BusyDialog(String busyText) {
		this.busyText = busyText;
		setTitle("Please Wait");
		setModal(true);
		setResizable(false);
		addContent();
	}

	void addContent() {
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		builder.append(getBusyLabel());
		builder.append(busyText);

		Dimension dim = builder.getPanel().getPreferredSize();
		dim.height = dim.height + 45;
		setSize(dim);
		add(builder.getPanel(), BorderLayout.CENTER);
	}

	JXBusyLabel getBusyLabel() {
		if (busyLabel == null) {
			busyLabel = new JXBusyLabel(new Dimension(40, 40));
			busyLabel.setBusy(true);
			busyLabel.getBusyPainter().setPoints(12);
			busyLabel.getBusyPainter().setTrailLength(10);
			busyLabel.setDelay(80);

		}

		return busyLabel;
	}
}
