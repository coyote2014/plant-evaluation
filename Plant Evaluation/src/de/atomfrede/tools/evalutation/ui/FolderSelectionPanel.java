/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	FolderSelectionPanel.java is part of Plant Evaluation.
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.JideBorderLayout;

import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.ui.res.Messages;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

/**
 * Panel that contains interface to select all necessary folders to run a complete evaluation.
 */
public class FolderSelectionPanel extends JPanel {

	private static final long serialVersionUID = 6734919047205211900L;
	JTextField inputFolderTextField, outputFolderTextField, temperatureTextField;
	JFileChooser inputFolderFileChooser, outputFolderFileChooser, temperatureFileChooser;
	JButton inputFolderButton, outputFolderButton, temperatureButton;

	File inputFolder, outputFolder, temperatureFolder;

	JFrame parent;

	public FolderSelectionPanel(JFrame parent) {
		super();
		this.parent = parent;
		inputFolder = Options.getInputFolder();
		outputFolder = Options.getOutputFolder();
		temperatureFolder = Options.getTemperatureInputFolder();
		initialize();
	}

	private void initialize() {
		setLayout(new JideBorderLayout());
		FormLayout layout = new FormLayout("pref, 4dlu, fill:pref:grow, 4dlu, pref"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.appendSeparator(Messages.getString("FolderSelectionPanel.1")); //$NON-NLS-1$
		builder.append(Messages.getString("FolderSelectionPanel.2"), getInputFolderTextField()); //$NON-NLS-1$
		builder.append(getInputFolderButton());

		builder.append(Messages.getString("FolderSelectionPanel.3"), getTemperatureTextField()); //$NON-NLS-1$
		builder.append(getTemperatureFolderButton());

		builder.append(Messages.getString("FolderSelectionPanel.4"), getOutputFolderTextField()); //$NON-NLS-1$
		builder.append(getOutputFolderButton());
		add(builder.getPanel(), JideBorderLayout.CENTER);
	}

	private JButton getOutputFolderButton() {
		if (outputFolderButton == null) {
			outputFolderButton = new JButton(Messages.getString("FolderSelectionPanel.5")); //$NON-NLS-1$
			outputFolderButton.setIcon(Icons.IC_FOLDER_OPEN_SMALL);
			outputFolderButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = getFileChooser();
					fc.setCurrentDirectory(outputFolder);
					int returnValue = fc.showDialog(parent, Messages.getString("FolderSelectionPanel.6")); //$NON-NLS-1$

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						outputFolder = fc.getSelectedFile();
						Options.setOutputFolder(outputFolder);
						getOutputFolderTextField().setText(outputFolder.getAbsolutePath());
					}

				}
			});
		}
		return outputFolderButton;
	}

	private JButton getInputFolderButton() {
		if (inputFolderButton == null) {
			inputFolderButton = new JButton(Messages.getString("FolderSelectionPanel.7")); //$NON-NLS-1$
			inputFolderButton.setIcon(Icons.IC_FOLDER_OPEN_SMALL);

			inputFolderButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = getFileChooser();
					fc.setCurrentDirectory(inputFolder);
					int returnValue = fc.showDialog(parent, Messages.getString("FolderSelectionPanel.8")); //$NON-NLS-1$

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						inputFolder = fc.getSelectedFile();
						Options.setInputFolder(inputFolder);
						getInputFolderTextField().setText(inputFolder.getAbsolutePath());
					}

				}
			});
		}
		return inputFolderButton;
	}

	private JButton getTemperatureFolderButton() {
		if (temperatureButton == null) {
			temperatureButton = new JButton(Messages.getString("FolderSelectionPanel.9")); //$NON-NLS-1$
			temperatureButton.setIcon(Icons.IC_FOLDER_OPEN_SMALL);
			temperatureButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = getFileChooser();
					fc.setCurrentDirectory(temperatureFolder);
					int returnValue = fc.showDialog(parent, Messages.getString("FolderSelectionPanel.10")); //$NON-NLS-1$

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						temperatureFolder = fc.getSelectedFile();
						Options.setTemperatureInputFolder(temperatureFolder);
						getTemperatureTextField().setText(temperatureFolder.getAbsolutePath());
					}
				}
			});
		}
		return temperatureButton;
	}

	private JTextField getInputFolderTextField() {
		if (inputFolderTextField == null) {
			inputFolderTextField = new JTextField(inputFolder.getAbsolutePath());
			inputFolderTextField.setEnabled(false);

		}
		return inputFolderTextField;
	}

	private JTextField getOutputFolderTextField() {
		if (outputFolderTextField == null) {
			outputFolderTextField = new JTextField(outputFolder.getAbsolutePath());
			outputFolderTextField.setEnabled(false);
		}
		return outputFolderTextField;
	}

	private JTextField getTemperatureTextField() {
		if (temperatureTextField == null) {
			temperatureTextField = new JTextField(temperatureFolder.getAbsolutePath());
			temperatureTextField.setEnabled(false);
		}
		return temperatureTextField;
	}

	private JFileChooser getFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		return fc;
	}

	@SuppressWarnings("unused")
	private FolderChooser getFolderChooser() {
		FolderChooser fc = new FolderChooser();
		fc.setFileHidingEnabled(true);
		fc.updateUI();
		// fc.setAvailableButtons(arg0)

		return fc;
	}
}
