/**
 *  Copyright 2012 Frederik Hahne
 *
 * 	DatasetInputPanel.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

@SuppressWarnings("serial")
public class DatasetInputPanel extends JPanel {

	JLabel colorLabel;
	JSpinner minimumSpinner, maximumSpinner;
	JCheckBox enableAutoscaleCheckbox;
	JButton colorChooseButton, deleteDatasetButton;
	JTextField datasetNameTextField;
	JComboBox datasetCombobox;
	File dataFile;
	Color graphColor;
	boolean isDeleteAllowed;

	public DatasetInputPanel(File dataFile, Color initialColor, boolean isDeleteAllowed) {
		super();
		this.dataFile = dataFile;
		this.graphColor = initialColor;
		this.isDeleteAllowed = isDeleteAllowed;

		addContent();
	}

	void addContent() {
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow, 4dlu, pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("Name", getDatasetNameTextField(), 3);
		builder.nextLine();
		builder.append("Dataset", getDatasetCombobox(), 3);

		builder.append("Color", getColorLabel());
		builder.append(getColorChooseButton());

		builder.nextLine();

		builder.appendSeparator("Scale");

		builder.append("Minimum", getMinimumSpinner(), 3);
		builder.append("Maximim", getMaximumSpinner(), 3);

		add(builder.getPanel(), BorderLayout.CENTER);

	}

	public JLabel getColorLabel() {
		if (colorLabel == null) {
			colorLabel = new JLabel();
			colorLabel.setText(" ");
			colorLabel.setOpaque(true);
			colorLabel.setBackground(graphColor);
			colorLabel.setForeground(graphColor);
		}
		return colorLabel;
	}

	public JSpinner getMinimumSpinner() {
		if (minimumSpinner == null) {
			minimumSpinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 10));
			minimumSpinner.setEnabled(false);
		}
		return minimumSpinner;
	}

	public JSpinner getMaximumSpinner() {
		if (maximumSpinner == null) {
			maximumSpinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 10));
			maximumSpinner.setEnabled(false);
		}
		return maximumSpinner;
	}

	public JCheckBox getEnableAutoscaleCheckbox() {
		if (enableAutoscaleCheckbox == null) {
			enableAutoscaleCheckbox = new JCheckBox("Autoscale");
			enableAutoscaleCheckbox.setSelected(true);

		}
		return enableAutoscaleCheckbox;
	}

	public JButton getColorChooseButton() {
		if (colorChooseButton == null) {
			colorChooseButton = new JButton("Choose...");

		}
		return colorChooseButton;
	}

	public JButton getDeleteDatasetButton() {
		if (deleteDatasetButton == null) {
			deleteDatasetButton = new JButton(Icons.IC_DELETE_SMALL);
			deleteDatasetButton.setText("Delete");
		}
		return deleteDatasetButton;
	}

	public JTextField getDatasetNameTextField() {
		if (datasetNameTextField == null) {
			datasetNameTextField = new JTextField();
		}
		return datasetNameTextField;
	}

	public JComboBox getDatasetCombobox() {
		if (datasetCombobox == null) {
			datasetCombobox = new JComboBox();
		}
		return datasetCombobox;
	}

	public File getDataFile() {
		return dataFile;
	}

	public Color getGraphColor() {
		return graphColor;
	}

	public boolean isDeleteAllowed() {
		return isDeleteAllowed;
	}

}
