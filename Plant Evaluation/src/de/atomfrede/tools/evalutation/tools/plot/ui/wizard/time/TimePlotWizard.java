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
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciscavate.cjwizard.*;
import org.ciscavate.cjwizard.pagetemplates.TitledPageTemplate;

import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.tools.plot.AbstractPlot.PlotType;
import de.atomfrede.tools.evalutation.tools.plot.custom.CustomTimePlot;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.PlotWizard;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.DatasetSelectionWizardPage;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.pages.TimeFileSelectionPage;
import de.atomfrede.tools.evalutation.tools.plot.wrapper.TimeDatasetWrapper;
import de.atomfrede.tools.evalutation.ui.BusyDialog;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;
import de.atomfrede.tools.evalutation.util.DialogUtil;

@SuppressWarnings("serial")
public class TimePlotWizard extends PlotWizard {

	private final Log log = LogFactory.getLog(TimePlotWizard.class);

	List<TimeDatasetWrapper> datasetWrappers;

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
		Dimension dim0 = wizardContainer.getPreferredSize();
		Dimension dim = pages.get(0).getPreferredSize();
		Dimension size = new Dimension(dim0.width + 80, dim0.height + dim.height + 50);
		setPreferredSize(size);
		this.pack();
	}

	public List<WizardPage> getWizardPages() {
		if (pages == null) {
			pages = new ArrayList<WizardPage>();
			pages.add(new TimeFileSelectionPage(this));
			DatasetSelectionWizardPage datasetSelectionWizardPage = new DatasetSelectionWizardPage(this, null) {
				@Override
				public int getTimeColumn() {
					return ((TimePlotWizard) plotWizard).timeColumn;
				}
			};
			pages.add(datasetSelectionWizardPage);
		}
		return pages;
	}

	public List<TimeDatasetWrapper> getDatasetWrappers() {
		return datasetWrappers;
	}

	public void setDatasetWrappers(List<TimeDatasetWrapper> datasetWrappers) {
		this.datasetWrappers = datasetWrappers;
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
		 * @see org.ciscavate.cjwizard.PageFactory#createPage(java.util.List, org.ciscavate.cjwizard.WizardSettings)
		 */
		@Override
		public WizardPage createPage(List<WizardPage> path, WizardSettings settings) {
			return buildPage(path.size(), settings);
		}

		private WizardPage buildPage(int pageCount, WizardSettings settings) {
			switch (pageCount) {
			case 0:
				return pages.get(0);
			case 1: {
				if (((TimeFileSelectionPage) pages.get(0)).getDataFile() == null)
					return null;
				((DatasetSelectionWizardPage) pages.get(1)).setDatafile(((TimeFileSelectionPage) pages.get(0)).getDataFile());
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
	 * @see org.ciscavate.cjwizard.WizardListener#onCanceled(java.util.List, org.ciscavate.cjwizard.WizardSettings)
	 */
	@Override
	public void onCanceled(List<WizardPage> arg0, WizardSettings arg1) {
		dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ciscavate.cjwizard.WizardListener#onFinished(java.util.List, org.ciscavate.cjwizard.WizardSettings)
	 */
	@Override
	public void onFinished(List<WizardPage> arg0, WizardSettings arg1) {
		this.setVisible(false);

		busyDialog = new BusyDialog("Please wait while plots are generated...");
		busyDialog.setLocationRelativeTo(DialogUtil.getInstance().getFrame());
		DialogUtil.getInstance().showStandardDialog(busyDialog);

		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				StringBuilder fileNameBuilder = new StringBuilder();
				// first collect all datasets
				List<TimeDatasetWrapper> wrappers = ((DatasetSelectionWizardPage) pages.get(1)).getTimeDatasetWrappers();
				TimeDatasetWrapper[] wrappersArray = new TimeDatasetWrapper[wrappers.size()];

				int width = ((TimeFileSelectionPage) pages.get(0)).getEnteredWidth();
				int height = ((TimeFileSelectionPage) pages.get(0)).getEnteredHeight();

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
					log.error("The requested data could not be plotted", e);
				}
				try {
					Desktop.getDesktop().open(new File(Options.getOutputFolder(), fileName + ".pdf"));
				} catch (IOException e) {
					log.error("Generated file could not be opened.", e);
				}

				return null;
			}

			@Override
			protected void done() {
				busyDialog.dispose();
				dispose();
			}

		};

		worker.execute();
	}
}
