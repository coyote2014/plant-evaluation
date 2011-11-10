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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.ui.OptionsDialog.Options;

public class FolderSelectionPanel extends JPanel {

	JTextField inputFolderTextField, outputFolderTextField,
			temperatureTextField;
	JFileChooser inputFolderFileChooser, outputFolderFileChooser,
			temperatureFileChooser;
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
		setLayout(new BorderLayout());
		FormLayout layout = new FormLayout(
				"pref, 4dlu, fill:pref:grow, 4dlu, pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.appendSeparator("Folders");
		builder.append("Input", getInputFolderTextField());
		builder.append(getInputFolderButton());

		builder.append("Temperature", getTemperatureTextField());
		builder.append(getTemperatureFolderButton());

		builder.append("Output", getOutputFolderTextField());
		builder.append(getOutputFolderButton());
		add(builder.getPanel());
	}

	private JButton getOutputFolderButton() {
		if (outputFolderButton == null) {
			outputFolderButton = new JButton("Choose...");

			outputFolderButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = getFileChooser();
					fc.setCurrentDirectory(outputFolder);
					int returnValue = fc.showDialog(parent,
							"Select Output Folder");

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						outputFolder = fc.getSelectedFile();
					}

				}
			});
		}
		return outputFolderButton;
	}

	private JButton getInputFolderButton() {
		if (inputFolderButton == null) {
			inputFolderButton = new JButton("Choose...");

			inputFolderButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = getFileChooser();
					fc.setCurrentDirectory(inputFolder);
					int returnValue = fc.showDialog(parent,
							"Select Input Folder");

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						inputFolder = fc.getSelectedFile();
					}

				}
			});
		}
		return inputFolderButton;
	}

	private JButton getTemperatureFolderButton() {
		if (temperatureButton == null) {
			temperatureButton = new JButton("Choose...");

			temperatureButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = getFileChooser();
					fc.setCurrentDirectory(temperatureFolder);
					int returnValue = fc.showDialog(parent,
							"Select Temperature Folder");

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						temperatureFolder = fc.getSelectedFile();
					}
				}
			});
		}
		return temperatureButton;
	}

	private JTextField getInputFolderTextField() {
		if (inputFolderTextField == null) {
			inputFolderTextField = new JTextField(inputFolder.getPath());
			inputFolderTextField.setEnabled(false);

		}
		return inputFolderTextField;
	}

	private JTextField getOutputFolderTextField() {
		if (outputFolderTextField == null) {
			outputFolderTextField = new JTextField(outputFolder.getPath());
			outputFolderTextField.setEnabled(false);
		}
		return outputFolderTextField;
	}

	private JTextField getTemperatureTextField() {
		if (temperatureTextField == null) {
			temperatureTextField = new JTextField(temperatureFolder.getPath());
			temperatureTextField.setEnabled(false);
		}
		return temperatureTextField;
	}

	private JFileChooser getFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		return fc;
	}
}
