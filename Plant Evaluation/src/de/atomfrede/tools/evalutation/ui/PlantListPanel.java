/**
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

package de.atomfrede.tools.evalutation.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.Plant;

public class PlantListPanel extends JPanel {

	static final ImageIcon IC_ADD = new ImageIcon(
			PlantDatesInputPanel.class.getResource("res/list-add.png"));

	List<Plant> plantList;
	JButton addButton, evaluateButton;
	JProgressBar evaluationProgressBar;

	public PlantListPanel() {
		this(new ArrayList<Plant>());
	}

	public PlantListPanel(List<Plant> plantList) {
		this.plantList = plantList;
		initialize();
	}

	private void initialize() {
		removeAll();
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.appendSeparator("Plants");

		int index = -1;
		for (Plant plant : plantList) {
			index++;
			builder.append(getPlantInputPanel(plant, index));
		}

		builder.append(ButtonBarFactory.buildOKCancelBar(getEvaluateButton(),
				getAddButton()));
		builder.append("");

		add(builder.getPanel(), BorderLayout.CENTER);

		add(getEvaluationProgressBar(), BorderLayout.SOUTH);

	}

	private PlantDatesInputPanel getPlantInputPanel(Plant plant, final int index) {
		PlantDatesInputPanel inputPanel = new PlantDatesInputPanel(plant);

		if (plantList.size() == 1)
			inputPanel.deleteButton.setEnabled(false);

		inputPanel.deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				removePlant(index);

			}
		});
		return inputPanel;
	}

	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					addPlant(new Plant());

				}
			});
			addButton.setIcon(IC_ADD);
			addButton.setMaximumSize(addButton.getPreferredSize());
		}
		return addButton;
	}

	private JButton getEvaluateButton() {
		if (evaluateButton == null) {
			evaluateButton = new JButton("Evaluate");
			// evaluateButton.setIcon(IC_ADD);

			evaluateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					invalidate();
					getEvaluationProgressBar().setVisible(true);
					revalidate();

				}
			});
		}
		return evaluateButton;
	}

	private JProgressBar getEvaluationProgressBar() {
		if (evaluationProgressBar == null) {
			evaluationProgressBar = new JProgressBar();
			evaluationProgressBar.setIndeterminate(true);
			evaluationProgressBar.setVisible(false);
		}
		return evaluationProgressBar;
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
		plantList.remove(index);
		rebuild();
	}

	public void addPlant(Plant plant) {
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

}
