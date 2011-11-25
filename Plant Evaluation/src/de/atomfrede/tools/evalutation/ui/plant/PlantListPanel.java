/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	PlantListPanel.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.ui.plant;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.Plant;
import de.atomfrede.tools.evalutation.evaluator.CO2AbsoluteOnlyEvaluation;
import de.atomfrede.tools.evalutation.evaluator.StandardEvaluation;
import de.atomfrede.tools.evalutation.evaluator.common.AbstractEvaluator;
import de.atomfrede.tools.evalutation.main.PlantHelper;
import de.atomfrede.tools.evalutation.ui.res.Messages;

public class PlantListPanel extends JPanel {

	private final Log log = LogFactory.getLog(PlantListPanel.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -2045646288488329282L;
	List<Plant> plantList;
	List<PlantDataInputPanel> plantDataInputPanelList;

	public PlantListPanel() {
		this(new ArrayList<Plant>());
	}

	public PlantListPanel(List<Plant> plantList) {
		this.plantList = plantList;
		this.plantDataInputPanelList = new ArrayList<PlantDataInputPanel>();
		initialize();
	}

	private void initialize() {
		removeAll();
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("fill:pref:grow"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("");
		builder.appendSeparator(Messages.getString("PlantListPanel.2")); //$NON-NLS-1$

		int index = -1;
		for (Plant plant : plantList) {
			index++;
			builder.appendSeparator(Messages.getString("PlantListPanel.3") + (index + 1)); //$NON-NLS-1$
			builder.append(getPlantInputPanel(plant, index));

		}

		builder.append(""); //$NON-NLS-1$

		add(builder.getPanel(), BorderLayout.CENTER);

	}

	public void standardEvaluation(final JButton button, final JButton addButton) {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				button.setEnabled(false);
				addButton.setEnabled(false);
				setupStandardEvaluation();
				return null;
			}

			@Override
			protected void done() {
				invalidate();
				button.setEnabled(true);
				addButton.setEnabled(true);
				rebuild();
				revalidate();
			}
		}.execute();
	}

	public void co2AbsoluteOnlyEvaluation(final JButton button,
			final JButton addButton) {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				button.setEnabled(false);
				addButton.setEnabled(false);
				setupCO2OnlyEvaluation();
				return null;
			}

			@Override
			protected void done() {
				invalidate();
				button.setEnabled(true);
				addButton.setEnabled(true);
				rebuild();
				revalidate();
			}
		}.execute();
	}

	private PlantDataInputPanel getPlantInputPanel(Plant plant, final int index) {
		PlantDataInputPanel inputPanel = new PlantDataInputPanel(plant);

		if (plantList.size() == 1)
			inputPanel.deleteButton.setEnabled(false);

		plantDataInputPanelList.add(index, inputPanel);

		inputPanel.deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removePlant(index);
			}
		});
		return inputPanel;
	}

	private void rebuild() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					invalidate();
					initialize();
					revalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void removePlant(int index) {
		updatePlants();
		plantList.remove(index);
		rebuild();
	}

	public void addPlant(Plant plant) {
		updatePlants();
		plantList.add(plant);
		rebuild();

	}

	public List<Plant> getPlantList() {
		if (plantList == null)
			plantList = new ArrayList<Plant>();

		return plantList;
	}

	public void setPlantList(List<Plant> plantList) {
		this.plantList = plantList;
	}

	private void setupStandardEvaluation() {
		updatePlants();
		StandardEvaluation evaluation = new StandardEvaluation();
		addProgressBars(evaluation.getEvaluators());
		try {
			evaluation.evaluate();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void setupCO2OnlyEvaluation() {
		CO2AbsoluteOnlyEvaluation evaluation = new CO2AbsoluteOnlyEvaluation();
		addProgressBars(evaluation.getEvaluators());
		try {
			evaluation.evaluate();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void addProgressBars(List<AbstractEvaluator> evaluators) {
		invalidate();
		FormLayout layout = new FormLayout("pref, 4dlu,fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		for (AbstractEvaluator eval : evaluators) {
			builder.append(eval.getName());
			builder.append(eval.getProgressBar());
		}

		add(builder.getPanel(), BorderLayout.NORTH);
		revalidate();
	}

	private void updatePlants() {
		int i = -1;
		for (Plant p : PlantHelper.getDefaultPlantList()) {
			i++;
			p.setStartDate(plantDataInputPanelList.get(i).getStartDate());
			p.setEndDate(plantDataInputPanelList.get(i).getEndDate());
		}
	}

}
