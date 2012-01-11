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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.constants.OutputFileConstants;
import de.atomfrede.tools.evalutation.tools.plot.TimeDatasetWrapper;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;
import de.atomfrede.tools.evalutation.util.CSVUtil;

@SuppressWarnings("serial")
public class DatasetInputPanel extends JPanel {

	DatasetSelectionWizardPage wizardPage;
	JLabel colorLabel;
	JSpinner minimumSpinner, maximumSpinner;
	JCheckBox enableAutoscaleCheckbox;
	JButton colorChooseButton, deleteDatasetButton;
	JTextField datasetNameTextField;
	JComboBox datasetCombobox;
	File dataFile;
	Color graphColor;
	boolean isDeleteAllowed;

	List<String> possibleDatasetColumns;

	Map<String, Integer> headerToColumnNumber;

	public DatasetInputPanel(File dataFile, Color initialColor, boolean isDeleteAllowed, DatasetSelectionWizardPage wizardPage) {
		super();
		this.dataFile = dataFile;
		this.graphColor = initialColor;
		this.isDeleteAllowed = isDeleteAllowed;
		this.wizardPage = wizardPage;
		headerToColumnNumber = new HashMap<String, Integer>();

		possibleDatasetColumns = getPossibleDatasetColumns();
		addContent();
	}

	List<String> getPossibleDatasetColumns() {
		String[] header = CSVUtil.getHeader(dataFile);

		List<String> dataColumns = new ArrayList<String>();
		// now read all possible headers that might contain interesting data
		int i = 0;
		for (String head : header) {
			if (head.equals(InputFileConstants.HEADER_12_CO2) || head.equals(InputFileConstants.HEADER_12_CO2_DRY)
					|| head.equals(InputFileConstants.HEADER_13_CO2) || head.equals(InputFileConstants.HEADER_13_CO2_DRY)
					|| head.equals(InputFileConstants.HEADER_DELTA_5_MINUTES) || head.equals(InputFileConstants.HEADER_DELTA_RAW)
					|| head.equals(InputFileConstants.HEADER_H2O) || head.equals(OutputFileConstants.HEADER_DELTA_13)
					|| head.equals(OutputFileConstants.HEADER_CO2_ABSOLUTE)) {
				// if any of that interesting columns is found use it
				headerToColumnNumber.put(head, Integer.valueOf(i));
				dataColumns.add(head);
			}
			i++;
		}

		return dataColumns;
	}

	List<String[]> getAllDataLines() {
		return CSVUtil.getAllDataLines(dataFile);
	}

	int getDataColumn() {
		return headerToColumnNumber.get(getDatasetCombobox().getSelectedItem());
	}

	public TimeDatasetWrapper getTimeDatasetWrapper() {
		TimeDatasetWrapper wrapper = new TimeDatasetWrapper(getDatasetNameTextField().getText(), getAllDataLines(), getDataColumn(), wizardPage.getTimeColumn());
		wrapper.createDataset();
		wrapper.setSeriesColor(graphColor);
		if (!getEnableAutoscaleCheckbox().isSelected()) {
			// if autoscale is not enabled we must set custom minimum an maximum
			wrapper.setMinimum(Double.valueOf(getMinimumSpinner().getValue() + ""));
			wrapper.setMaximum(Double.valueOf(getMaximumSpinner().getValue() + ""));
		}
		return wrapper;
	}

	void addContent() {
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow, 4dlu, pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.appendSeparator("Dataset");
		builder.append("Name", getDatasetNameTextField(), 3);
		builder.nextLine();
		builder.append("Dataset", getDatasetCombobox(), 3);

		builder.append("Color", getColorLabel());
		builder.append(getColorChooseButton());

		builder.nextLine();

		builder.appendSeparator("Scale");
		builder.append(getEnableAutoscaleCheckbox(), 4);
		builder.nextLine();
		builder.append("Minimum", getMinimumSpinner(), 3);
		builder.append("Maximim", getMaximumSpinner(), 3);

		builder.append(ButtonBarFactory.buildRightAlignedBar(getDeleteDatasetButton()), 5);

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

			colorChooseButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					graphColor = JColorChooser.showDialog(null, "Select Graph Color", graphColor);
					getColorLabel().setBackground(graphColor);
					getColorLabel().setForeground(graphColor);
					getColorLabel().repaint();
				}
			});

		}
		return colorChooseButton;
	}

	public JButton getDeleteDatasetButton() {
		if (deleteDatasetButton == null) {
			deleteDatasetButton = new JButton(Icons.IC_DELETE_SMALL);
			deleteDatasetButton.setText("Delete");
			deleteDatasetButton.setEnabled(isDeleteAllowed);

			deleteDatasetButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					removeSelf();
				}
			});
		}
		return deleteDatasetButton;
	}

	private void removeSelf() {
		wizardPage.removeDataset(this);
	}

	public JTextField getDatasetNameTextField() {
		if (datasetNameTextField == null) {
			datasetNameTextField = new JTextField();
		}
		return datasetNameTextField;
	}

	public JComboBox getDatasetCombobox() {
		if (datasetCombobox == null) {
			datasetCombobox = new JComboBox(possibleDatasetColumns.toArray());

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
