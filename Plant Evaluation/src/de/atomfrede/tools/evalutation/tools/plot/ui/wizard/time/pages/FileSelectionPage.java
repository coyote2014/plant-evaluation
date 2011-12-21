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

import org.ciscavate.cjwizard.WizardPage;

@SuppressWarnings("serial")
public class FileSelectionPage extends WizardPage {

	/**
	 * @param title
	 * @param description
	 */
	public FileSelectionPage(String title, String description) {
		super(title, description);
	}

	public FileSelectionPage() {
		this("Select Data File", "Select the file which contains the data you like to plot.");
	}

	void addContent() {

	}

}
