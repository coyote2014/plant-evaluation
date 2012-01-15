/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	DialogUtil.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.util;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.tools.plot.AbstractPlot.PlotType;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.simple.SimplePlotWizard;
import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.TimePlotWizard;
import de.atomfrede.tools.evalutation.ui.ExceptionDialog;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

public class DialogUtil {

	private static DialogUtil instance;

	private final Log log = LogFactory.getLog(DialogUtil.class);

	JFrame frame;

	private DialogUtil() {
	};

	public static synchronized DialogUtil getInstance() {
		if (instance == null)
			instance = new DialogUtil();
		return instance;
	}

	public void setMainFrame(JFrame frame) {
		this.frame = frame;
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public void showStandardDialog(final JDialog dialogToShow) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dialogToShow.setVisible(true);
			}
		});
	}

	public void showError(final Exception ex) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ExceptionDialog(frame, ex).setVisible(true);
			}
		});
	}

	public void showWizardDialog(final JDialog wizardDialog) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				wizardDialog.setVisible(true);
			}
		});
	}

	public void showPlotTypeSelection() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Object[] types = PlotType.values();

				PlotType selectedType = (PlotType) JOptionPane.showInputDialog(frame, "Select type of plot you want to create.", "Select Plot Type",
						JOptionPane.QUESTION_MESSAGE, Icons.IC_INFORMATION_LARGE, types, types[0]);

				if (selectedType != null) {
					log.debug("Plot " + selectedType + " should be created.");
					switch (selectedType) {
					case SIMPLE: {
						SimplePlotWizard wizard = new SimplePlotWizard();
						// wizard.setSize(600, 800);
						wizard.setSize(wizard.getPreferredSize());
						wizard.setLocationRelativeTo(frame);
						wizard.setModal(true);
						showWizardDialog(wizard);
						break;
					}
					case TIME: {
						TimePlotWizard wizard = new TimePlotWizard();
						// wizard.setSize(600, 800);
						wizard.setSize(wizard.getPreferredSize());
						wizard.setLocationRelativeTo(frame);
						wizard.setModal(true);
						showWizardDialog(wizard);
						break;
					}
					default:
						break;
					}
				}
			}
		});
	}
}
