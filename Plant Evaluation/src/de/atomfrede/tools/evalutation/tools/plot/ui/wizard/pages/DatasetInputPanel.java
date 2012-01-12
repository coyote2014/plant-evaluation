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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.constants.OutputFileConstants;
import de.atomfrede.tools.evalutation.tools.plot.wrapper.TimeDatasetWrapper;
import de.atomfrede.tools.evalutation.tools.plot.wrapper.XYDatasetWrapper;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;
import de.atomfrede.tools.evalutation.util.CSVUtil;

@SuppressWarnings("serial")
public class DatasetInputPanel extends JPanel {

	IDatasetSelectionWizardPage wizardPage;
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

	public DatasetInputPanel(File dataFile, Color initialColor, boolean isDeleteAllowed, IDatasetSelectionWizardPage wizardPage) {
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

	public XYDatasetWrapper getSimpleDatasetWrapper() {
		XYDatasetWrapper wrapper = new XYDatasetWrapper(getDatasetNameTextField().getText(), getAllDataLines(), getDataColumn());
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

		// FormLayout layout = new
		// FormLayout("left:pref, 4dlu, fill:pref:grow, 4dlu, pref");
		// FormLayout layout = new
		// FormLayout("left:pref, 4dlu, fill:pref:grow, 4dlu, left:pref, 4dlu, fill:pref:grow");
		FormLayout layout = new FormLayout("left:pref, 4dlu, left:pref, 4dlu, fill:pref:grow, 4dlu, left:pref, 4dlu, fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		TitledBorder title;
		Border raisedBevel = BorderFactory.createRaisedBevelBorder();
		title = BorderFactory.createTitledBorder(raisedBevel, "Dataset");

		// builder.appendSeparator("Dataset");
		builder.append("Name", getDatasetNameTextField(), 3);
		builder.append("Column", getDatasetCombobox());
		builder.nextLine();

		builder.append("Color", getColorLabel(), 3);
		builder.append(getColorChooseButton());

		builder.nextLine();

		// builder.appendSeparator("Scale");
		builder.append(getEnableAutoscaleCheckbox());
		builder.append("Minimum", getMinimumSpinner());
		builder.append("Maximum", getMaximumSpinner());

		builder.append(ButtonBarFactory.buildRightAlignedBar(getDeleteDatasetButton()), 9);

		// builder.getPanel().setBorder(title);
		add(builder.getPanel(), BorderLayout.CENTER);
		setBorder(title);

	}

	public JLabel getColorLabel() {
		if (colorLabel == null) {
			colorLabel = new JLabel();
			colorLabel.setText(" ");
			colorLabel.setOpaque(true);
			colorLabel.setBackground(graphColor);
			colorLabel.setForeground(graphColor);

			colorLabel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					getColorChooseButton().doClick();
				}
			});
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

			enableAutoscaleCheckbox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					getMaximumSpinner().setEnabled(!enableAutoscaleCheckbox.isSelected());
					getMinimumSpinner().setEnabled(!enableAutoscaleCheckbox.isSelected());
				}
			});

		}
		return enableAutoscaleCheckbox;
	}

	public JButton getColorChooseButton() {
		if (colorChooseButton == null) {
			colorChooseButton = new JButton("Choose...");

			colorChooseButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Color tempGraphColor = JColorChooser.showDialog(null, "Select Graph Color", graphColor);
					if (tempGraphColor != null) {
						graphColor = tempGraphColor;
						getColorLabel().setBackground(tempGraphColor);
						getColorLabel().setForeground(tempGraphColor);
						getColorLabel().repaint();
					}
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
