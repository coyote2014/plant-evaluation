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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciscavate.cjwizard.*;
import org.ciscavate.cjwizard.pagetemplates.TitledPageTemplate;

import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.tools.plot.AbstractPlot.PlotType;
import de.atomfrede.tools.evalutation.tools.plot.CustomTimePlot;
import de.atomfrede.tools.evalutation.tools.plot.TimeDatasetWrapper;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.PlotWizard;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.pages.DatasetSelectionWizardPage;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.pages.FileSelectionPage;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

@SuppressWarnings("serial")
public class TimePlotWizard extends PlotWizard {

	private final Log log = LogFactory.getLog(TimePlotWizard.class);

	List<TimeDatasetWrapper> datasetWrappers;
	List<WizardPage> pages;

	File dataFile;
	int timeColumn;

	public TimePlotWizard() {
		super();
		setType(PlotType.TIME);
		getWizardPages();
		datasetWrappers = new ArrayList<TimeDatasetWrapper>();
		wizardContainer = new WizardContainer(new TimePlotPageFactory(), new TitledPageTemplate(), new StackWizardSettings());

		wizardContainer.addWizardListener(this);
		wizardContainer.setNextEnabled(false);

		this.setTitle("Timeplot Wizard");
		this.setIconImage(Icons.IC_TOOL_PLOT_LARGE.getImage());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.add(wizardContainer);

		this.pack();
	}

	public List<WizardPage> getWizardPages() {
		if (pages == null) {
			pages = new ArrayList<WizardPage>();
			pages.add(new FileSelectionPage(this));
			pages.add(new DatasetSelectionWizardPage(this, null));
		}
		return pages;
	}

	public List<TimeDatasetWrapper> getDatasetWrappers() {
		return datasetWrappers;
	}

	public void setDatasetWrappers(List<TimeDatasetWrapper> datasetWrappers) {
		this.datasetWrappers = datasetWrappers;
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

	public int getTimeColumn() {
		return timeColumn;
	}

	public void setTimeColumn(int timeColumn) {
		this.timeColumn = timeColumn;
	}

	private class TimePlotPageFactory implements PageFactory {

		public TimePlotPageFactory() {
			super();
		}

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
				return pages.get(0);
			case 1: {
				if (((FileSelectionPage) pages.get(0)).getDataFile() == null)
					return null;
				((DatasetSelectionWizardPage) pages.get(1)).setDatafile(((FileSelectionPage) pages.get(0)).getDataFile());
				((DatasetSelectionWizardPage) pages.get(1)).addContent();
				wizardContainer.setFinishEnabled(true);
				wizardContainer.setNextEnabled(false);
				return pages.get(1);
			}
			default:
				break;
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ciscavate.cjwizard.WizardListener#onCanceled(java.util.List,
	 * org.ciscavate.cjwizard.WizardSettings)
	 */
	@Override
	public void onCanceled(List<WizardPage> arg0, WizardSettings arg1) {
		// TODO Auto-generated method stub
		dispose();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ciscavate.cjwizard.WizardListener#onFinished(java.util.List,
	 * org.ciscavate.cjwizard.WizardSettings)
	 */
	@Override
	public void onFinished(List<WizardPage> arg0, WizardSettings arg1) {
		log.info("Finished!");
		this.setVisible(false);

		StringBuilder fileNameBuilder = new StringBuilder();
		// first collect all datasets
		List<TimeDatasetWrapper> wrappers = ((DatasetSelectionWizardPage) pages.get(1)).getDatasetWrappers();
		TimeDatasetWrapper[] wrappersArray = new TimeDatasetWrapper[wrappers.size()];

		int width = ((FileSelectionPage) pages.get(0)).getEnteredWidth();
		int height = ((FileSelectionPage) pages.get(0)).getEnteredHeight();

		int i = 0;
		for (TimeDatasetWrapper wrapper : wrappers) {
			wrappersArray[i] = wrapper;
			if (i == 0)
				fileNameBuilder.append(wrapper.getSeriesName());
			else
				fileNameBuilder.append("-" + wrapper.getSeriesName());
			i++;
		}

		String date = DateFormat.getDateInstance().format(new Date());

		String fileName = date + "-" + fileNameBuilder.toString();
		CustomTimePlot timePlot = new CustomTimePlot(dataFile, fileName, width, height, wrappersArray);
		try {
			timePlot.plot();
		} catch (Exception e) {

		}
		try {
			Desktop.getDesktop().open(new File(Options.getOutputFolder(), fileName + ".pdf"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ciscavate.cjwizard.WizardListener#onPageChanged(org.ciscavate.cjwizard
	 * .WizardPage, java.util.List)
	 */
	@Override
	public void onPageChanged(WizardPage arg0, List<WizardPage> arg1) {
		// TODO Auto-generated method stub

	}

}
