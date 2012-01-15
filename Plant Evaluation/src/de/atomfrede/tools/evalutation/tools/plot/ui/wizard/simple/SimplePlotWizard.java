/**
 *  Copyright 2012 Frederik Hahne
 *
 * 	SimplePlotWizard.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard.simple;

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
import de.atomfrede.tools.evalutation.tools.plot.custom.CustomSimplePlot;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.PlotWizard;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.DatasetSelectionWizardPage;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.FileSelectionWizardPage;
import de.atomfrede.tools.evalutation.tools.plot.wrapper.XYDatasetWrapper;
import de.atomfrede.tools.evalutation.ui.BusyDialog;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;
import de.atomfrede.tools.evalutation.util.DialogUtil;

@SuppressWarnings("serial")
public class SimplePlotWizard extends PlotWizard {

	private final Log log = LogFactory.getLog(SimplePlotWizard.class);

	List<XYDatasetWrapper> datasetWrappers;

	public SimplePlotWizard() {
		super();
		setType(PlotType.TIME);
		getWizardPages();
		datasetWrappers = new ArrayList<XYDatasetWrapper>();
		wizardContainer = new WizardContainer(new SimplePlotPageFactory(), new TitledPageTemplate(), new StackWizardSettings());

		wizardContainer.addWizardListener(this);
		wizardContainer.setNextEnabled(false);

		this.setTitle("Simple Plot Wizard");
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
			pages.add(new FileSelectionWizardPage(this));
			pages.add(new DatasetSelectionWizardPage(this, null));
		}
		return pages;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	@Override
	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
		if (dataFile != null) {
			wizardContainer.setNextEnabled(true);
		}
	}

	private class SimplePlotPageFactory implements PageFactory {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.ciscavate.cjwizard.PageFactory#createPage(java.util.List,
		 * org.ciscavate.cjwizard.WizardSettings)
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
				// if (((FileSelectionPage) pages.get(0)).getDataFile() == null)
				// return null;
				((DatasetSelectionWizardPage) pages.get(1)).setDatafile(((FileSelectionWizardPage) pages.get(0)).getDataFile());
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
		this.setVisible(false);

		busyDialog = new BusyDialog("Please wait while plots are generated...");
		busyDialog.setLocationRelativeTo(DialogUtil.getInstance().getFrame());
		DialogUtil.getInstance().showStandardDialog(busyDialog);

		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				StringBuilder fileNameBuilder = new StringBuilder();
				// first collect all datasets
				List<XYDatasetWrapper> wrappers = ((DatasetSelectionWizardPage) pages.get(1)).getDatasetWrappers();
				XYDatasetWrapper[] wrappersArray = new XYDatasetWrapper[wrappers.size()];

				int width = ((FileSelectionWizardPage) pages.get(0)).getEnteredWidth();
				int height = ((FileSelectionWizardPage) pages.get(0)).getEnteredHeight();

				int i = 0;
				for (XYDatasetWrapper wrapper : wrappers) {
					wrappersArray[i] = wrapper;
					if (i == 0)
						fileNameBuilder.append(wrapper.getSeriesName());
					else
						fileNameBuilder.append("-" + wrapper.getSeriesName());
					i++;
				}

				String date = DateFormat.getDateInstance().format(new Date());

				String fileName = date + "-" + fileNameBuilder.toString();
				CustomSimplePlot timePlot = new CustomSimplePlot(dataFile, fileName, width, height, wrappersArray);
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
