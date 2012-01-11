/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	SimplePlotWizardPage.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard.simple.pages;

import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.AbstractWizardPage;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.simple.SimplePlotWizard;

@SuppressWarnings("serial")
public class SimplePlotWizardPage extends AbstractWizardPage {

	SimplePlotWizard simplePlotWizard;

	/**
	 * @param title
	 * @param description
	 * @param parent
	 */
	public SimplePlotWizardPage(String title, String description, SimplePlotWizard parent) {
		super(title, description, parent);
		simplePlotWizard = parent;
	}

	public SimplePlotWizard getSimplePlotWizard() {
		return simplePlotWizard;
	}

	public void setSimplePlotWizard(SimplePlotWizard simplePlotWizard) {
		this.simplePlotWizard = simplePlotWizard;
	}

}
