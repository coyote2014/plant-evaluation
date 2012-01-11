/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	FilePropertiesPage.java is part of Plant Evaluation.
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

import javax.swing.JTextField;

import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.TimePlotWizard;

public class FilePropertiesPage extends TimePlotWizardPage {

	JTextField widthInputField, heightInputField;

	/**
	 * @param title
	 * @param description
	 * @param parent
	 */
	public FilePropertiesPage(String title, String description, TimePlotWizard parent) {
		super(title, description, parent);
		// TODO Auto-generated constructor stub
	}

	public JTextField getWidthInputField() {
		if (widthInputField == null) {
			widthInputField = new JTextField();
		}
		return widthInputField;
	}

	public JTextField getHeightInputField() {
		if (heightInputField == null) {
			heightInputField = new JTextField();
		}
		return heightInputField;
	}

}
