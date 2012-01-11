/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	DatasetSelectionWizardPage.java is part of Plant Evaluation.
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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.tools.plot.TimeDatasetWrapper;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.TimePlotWizard;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

@SuppressWarnings("serial")
public class DatasetSelectionWizardPage extends TimePlotWizardPage {

	File datafile;
	int timeColumn;
	List<DatasetInputPanel> datasetInputPanels;
	JButton addDatasetButton;

	public DatasetSelectionWizardPage(TimePlotWizard parent, File datafile) {
		this("Setup Datasets", "Setup and configure the desired datasets.", parent);
		this.datafile = datafile;
		datasetInputPanels = new ArrayList<DatasetInputPanel>();
	}

	/**
	 * @param title
	 * @param description
	 * @param parent
	 */
	public DatasetSelectionWizardPage(String title, String description, TimePlotWizard parent) {
		super(title, description, parent);
	}

	public void addContent() {
		setLayout(new BorderLayout());
		FormLayout layout = new FormLayout("fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		datasetInputPanels.add(new DatasetInputPanel(timePlotWizard.getDataFile(), Color.ORANGE, false, this));
		builder.append(datasetInputPanels.get(0));
		add(new JScrollPane(builder.getPanel()), BorderLayout.CENTER);

		add(getAddButtonBarPanel(), BorderLayout.SOUTH);
	}

	JPanel getAddButtonBarPanel() {
		FormLayout layout = new FormLayout("fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.append(ButtonBarFactory.buildRightAlignedBar(getAddButton()));

		return builder.getPanel();
	}

	void updateContent() {

		removeAll();

		setLayout(new BorderLayout());
		FormLayout layout = new FormLayout("fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		for (DatasetInputPanel inputPanel : datasetInputPanels) {
			builder.append(inputPanel);
		}
		add(new JScrollPane(builder.getPanel()), BorderLayout.CENTER);

		add(getAddButtonBarPanel(), BorderLayout.SOUTH);
	}

	public void removeDataset(DatasetInputPanel panelToRemove) {
		datasetInputPanels.remove(panelToRemove);
		updateContent();
		this.revalidate();
	}

	public void addDataset() {
		DatasetInputPanel panelToAdd = new DatasetInputPanel(timePlotWizard.getDataFile(), Color.ORANGE, true, this);
		datasetInputPanels.add(panelToAdd);
		updateContent();
		this.revalidate();
	}

	JButton getAddButton() {
		if (addDatasetButton == null) {
			addDatasetButton = new JButton(Icons.IC_ADD_SMALL);
			addDatasetButton.setText("Add Dataset");

			addDatasetButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addDataset();
				}
			});
		}
		return addDatasetButton;
	}

	public File getDatafile() {
		return datafile;
	}

	public void setDatafile(File datafile) {
		this.datafile = datafile;
	}

	public int getTimeColumn() {
		return timePlotWizard.getTimeColumn();
	}

	public void setTimeColumn(int timeColumn) {
		this.timeColumn = timeColumn;
	}

	public List<TimeDatasetWrapper> getDatasetWrappers() {
		List<TimeDatasetWrapper> wrappers = new ArrayList<TimeDatasetWrapper>();
		for (DatasetInputPanel panel : datasetInputPanels) {
			wrappers.add(panel.getTimeDatasetWrapper());
		}
		return wrappers;
	}

}
