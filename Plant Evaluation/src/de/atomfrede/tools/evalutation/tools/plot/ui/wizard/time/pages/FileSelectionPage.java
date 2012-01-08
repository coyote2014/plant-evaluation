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
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBorderLayout;

import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.AbstractWizardPage;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

@SuppressWarnings("serial")
public class FileSelectionPage extends AbstractWizardPage {

	File dataFile;
	JButton selectInputFileButton;
	JTextField fileNameTextField;

	/**
	 * @param title
	 * @param description
	 */
	public FileSelectionPage(String title, String description, JDialog parent) {
		super(title, description, parent);
		addContent();
	}

	public FileSelectionPage(JDialog parent) {
		this("Select Data File", "Select the file which contains the data you like to plot.", parent);
	}

	void addContent() {
		setLayout(new JideBorderLayout());

		FormLayout layout = new FormLayout("fill:pref:grow, 4dlu, pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		builder.append(getInputFileTextField());
		builder.append(getSelectInputFileButton());

		add(builder.getPanel(), JideBorderLayout.CENTER);
	}

	public File getDataFile() {
		return dataFile;
	}

	public JTextField getInputFileTextField() {
		if (fileNameTextField == null) {
			fileNameTextField = new JTextField();
			fileNameTextField.setName("filename");
			fileNameTextField.setEnabled(false);
		}
		return fileNameTextField;
	}

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
						getInputFileTextField().setText(dataFile.getAbsolutePath());
					}
				}
			});
		}
		return selectInputFileButton;
	}

	public JFileChooser getFileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv", "CSV"));
		return chooser;
	}

}
