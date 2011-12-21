/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	TimePlotWizard.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time;

import java.util.ArrayList;
import java.util.List;

import org.ciscavate.cjwizard.*;
import org.ciscavate.cjwizard.pagetemplates.TitledPageTemplate;

import de.atomfrede.tools.evalutation.tools.plot.AbstractPlot.PlotType;
import de.atomfrede.tools.evalutation.tools.plot.TimeDatasetWrapper;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.PlotWizard;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.pages.FileSelectionPage;

@SuppressWarnings("serial")
public class TimePlotWizard extends PlotWizard {

	List<TimeDatasetWrapper> datasetWrappers;

	public TimePlotWizard() {
		super();
		setType(PlotType.TIME);
		datasetWrappers = new ArrayList<TimeDatasetWrapper>();
		wizardContainer = new WizardContainer(new TimePlotPageFactory(), new TitledPageTemplate(), new StackWizardSettings());
	}

	public List<TimeDatasetWrapper> getDatasetWrappers() {
		return datasetWrappers;
	}

	public void setDatasetWrappers(List<TimeDatasetWrapper> datasetWrappers) {
		this.datasetWrappers = datasetWrappers;
	}

	private class TimePlotPageFactory implements PageFactory {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.ciscavate.cjwizard.PageFactory#createPage(java.util.List,
		 * org.ciscavate.cjwizard.WizardSettings)
		 */
		@Override
		public WizardPage createPage(List<WizardPage> path, WizardSettings settings) {
			// TODO Auto-generated method stub
			return buildPage(path.size(), settings);
		}

		private WizardPage buildPage(int pageCount, WizardSettings settings) {
			switch (pageCount) {
			case 0:
				return new FileSelectionPage();

			default:
				break;
			}
			return null;
		}
	}

}
