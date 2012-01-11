/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	PlotWizard.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard;

import javax.swing.JDialog;

import org.ciscavate.cjwizard.WizardContainer;
import org.ciscavate.cjwizard.WizardListener;

import de.atomfrede.tools.evalutation.tools.plot.AbstractPlot.PlotType;

@SuppressWarnings("serial")
public abstract class PlotWizard extends JDialog implements WizardListener {

	protected PlotType type;
	protected WizardContainer wizardContainer;

	public PlotType getType() {
		return type;
	}

	public void setType(PlotType type) {
		this.type = type;
	}

	public WizardContainer getWizardContainer() {
		return wizardContainer;
	}

	public void setWizardContainer(WizardContainer wizardContainer) {
		this.wizardContainer = wizardContainer;
	}
}
