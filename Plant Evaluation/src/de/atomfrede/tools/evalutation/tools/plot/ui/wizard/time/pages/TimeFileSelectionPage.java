/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	FileSelectionPage.java is part of Plant Evaluation.
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import org.apache.commons.lang3.ArrayUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBorderLayout;

import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.FileSelectionWizardPage;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.TimePlotWizard;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;
import de.atomfrede.tools.evalutation.util.ColumnCheckUtil;

@SuppressWarnings("serial")
public class TimeFileSelectionPage extends FileSelectionWizardPage {

	JLabel errorLabel, correctLabel;

	/**
	 * @param title
	 * @param description
	 */
	public TimeFileSelectionPage(String title, String description, TimePlotWizard parent) {
		super(title, description, parent);
		addContent();
	}

	public TimeFileSelectionPage(TimePlotWizard parent) {
		this("Select Data File", "Select the file which contains the data you like to plot. And the size of the generated PDF.", parent);
	}

	@Override
	protected void addContent() {
		setLayout(new JideBorderLayout());

		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow, 4dlu, pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		builder.append("Input File", getInputFileTextField());
		builder.append(getSelectInputFileButton());
		builder.appendSeparator("Size of generated PDF");
		builder.append("Width", getWidthSpinner(), 3);
		builder.append("Height", getHeightSpinner(), 3);

		add(builder.getPanel(), JideBorderLayout.CENTER);
	}

	@Override
	public JButton getSelectInputFileButton() {
		if (selectInputFileButton == null) {
			selectInputFileButton = new JButton(Icons.IC_MIME_CSV_SMALL);
			selectInputFileButton.setText("Select Data File");

			selectInputFileButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					JFileChooser fc = getFileChooser();
					if (dataFile != null)
						fc.setCurrentDirectory(dataFile.getParentFile());
					int returnValue = fc.showOpenDialog(parent);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						dataFile = fc.getSelectedFile();
						getInputFileTextField().setText(dataFile.getName());
						// TODO check if that file contains a time column
						if (checkFile()) {
							plotWizard.setDataFile(dataFile);
						} else {
							plotWizard.setDataFile(null);
						}
					}
				}
			});
		}
		return selectInputFileButton;
	}

	private boolean checkFile() {
		boolean fileContainsTimeColumn = false;
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(dataFile));
			String[] header = reader.readNext();
			if (ArrayUtils.contains(header, InputFileConstants.HEADER_EPOCH_TIME)) {
				fileContainsTimeColumn = true;
				ColumnCheckUtil.checkInputFileHeader(header);
				((TimePlotWizard) plotWizard).setTimeColumn(InputFileConstants.EPOCH_TIME);
			}
		} catch (Exception ioe) {

		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {

			}
		}
		return fileContainsTimeColumn;
	}
}
