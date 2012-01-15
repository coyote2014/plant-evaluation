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

import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;

import org.ciscavate.cjwizard.WizardContainer;
import org.ciscavate.cjwizard.WizardListener;
import org.ciscavate.cjwizard.WizardPage;

import de.atomfrede.tools.evalutation.tools.plot.AbstractPlot.PlotType;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.DatasetSelectionWizardPage;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.FileSelectionWizardPage;
import de.atomfrede.tools.evalutation.ui.BusyDialog;
import de.atomfrede.tools.evalutation.util.DialogUtil;

@SuppressWarnings("serial")
public abstract class PlotWizard extends JDialog implements WizardListener {

	protected PlotType type;
	protected WizardContainer wizardContainer;
	protected File dataFile;
	protected List<WizardPage> pages;
	protected BusyDialog busyDialog;

	protected Dimension lastSize = null;

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

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
		if (dataFile != null) {
			wizardContainer.setNextEnabled(true);
		}
	}

	public BusyDialog getBusyDialog() {
		return busyDialog;
	}

	public void setBusyDialog(BusyDialog busyDialog) {
		this.busyDialog = busyDialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ciscavate.cjwizard.WizardListener#onPageChanged(org.ciscavate.cjwizard
	 * .WizardPage, java.util.List)
	 */
	@Override
	public void onPageChanged(WizardPage page, List<WizardPage> pageList) {
		if (page instanceof FileSelectionWizardPage) {
			// put the wizard back to smaller scale
			if (lastSize != null) {
				setSize(lastSize);
				setLocationRelativeTo(DialogUtil.getInstance().getFrame());
			}
		} else if (page instanceof DatasetSelectionWizardPage) {
			lastSize = getSize();
			// 800x600 looks to be a good size
			setSize(800, 600);
			setLocationRelativeTo(DialogUtil.getInstance().getFrame());
		}

	}
}
