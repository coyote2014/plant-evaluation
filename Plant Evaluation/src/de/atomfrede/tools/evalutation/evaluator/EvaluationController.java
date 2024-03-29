/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	EvaluationController.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.evaluator;

import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.evaluator.evaluation.CO2AbsoluteOnlyEvaluation;
import de.atomfrede.tools.evalutation.evaluator.evaluation.TypeAevaluation;
import de.atomfrede.tools.evalutation.evaluator.evaluation.TypeBevaluation;
import de.atomfrede.tools.evalutation.ui.MainPanel;
import de.atomfrede.tools.evalutation.ui.plant.PlantListPanel;

/**
 * Controller class that starts and setups each evaluation process
 */
public class EvaluationController {

	private final Log log = LogFactory.getLog(EvaluationController.class);

	PlantListPanel plantListPanel;
	MainPanel mainPanel;

	public EvaluationController(PlantListPanel plantListPanel, MainPanel mainPanel) {
		this.plantListPanel = plantListPanel;
		this.mainPanel = mainPanel;
	}

	public PlantListPanel getPlantListPanel() {
		return plantListPanel;
	}

	public void setPlantListPanel(PlantListPanel plantListPanel) {
		this.plantListPanel = plantListPanel;
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public void startStandardEvaluation() {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				disableButtons();
				setupStandardEvaluation();
				return null;
			}

			@Override
			protected void done() {
				enableButtons();
				mainPanel.removeProgressPanel();
			}
		}.execute();
	}

	private void disableButtons() {
		mainPanel.disableButtons();
	}

	private void enableButtons() {
		mainPanel.enableButtons();
	}

	public void startCo2AbsoluteOnlyEvaluation() {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				disableButtons();
				setupCO2OnlyEvaluation();
				return null;
			}

			@Override
			protected void done() {
				enableButtons();
				mainPanel.removeProgressPanel();
			}
		}.execute();
	}

	public void startIngoEvaluation() {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				disableButtons();
				setupIngoEvaluation();
				return null;
			}

			@Override
			protected void done() {
				enableButtons();
				mainPanel.removeProgressPanel();
			}
		}.execute();
	}

	private void setupStandardEvaluation() {
		plantListPanel.updatePlants();
		TypeAevaluation evaluation = new TypeAevaluation();
		mainPanel.addProgressBars(evaluation.getEvaluators());
		try {
			evaluation.evaluate();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void setupCO2OnlyEvaluation() {
		CO2AbsoluteOnlyEvaluation evaluation = new CO2AbsoluteOnlyEvaluation();
		mainPanel.addProgressBars(evaluation.getEvaluators());
		try {
			evaluation.evaluate();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void setupIngoEvaluation() {
		TypeBevaluation evaluation = new TypeBevaluation();
		mainPanel.addProgressBars(evaluation.getEvaluators());
		try {
			evaluation.evaluate();
		} catch (Exception e) {
			log.error(e);
		}
	}

}
