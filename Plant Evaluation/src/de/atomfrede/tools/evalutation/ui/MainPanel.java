/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	MainPanel.java is part of Plant Evaluation.
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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBorderLayout;

import de.atomfrede.tools.evalutation.evaluator.AbstractEvaluator;
import de.atomfrede.tools.evalutation.evaluator.EvaluationController;
import de.atomfrede.tools.evalutation.evaluator.evaluation.AbstractEvaluation;
import de.atomfrede.tools.evalutation.evaluator.evaluation.AbstractEvaluation.EvaluationType;
import de.atomfrede.tools.evalutation.plant.Plant;
import de.atomfrede.tools.evalutation.plant.PlantHelper;
import de.atomfrede.tools.evalutation.ui.plant.PlantListPanel;
import de.atomfrede.tools.evalutation.ui.res.Messages;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

public class MainPanel extends JPanel {

	private final Log log = LogFactory.getLog(MainPanel.class);

	private static final long serialVersionUID = 6119526095319839746L;

	PlantListPanel plantListPanel;
	JPanel centerPanel, progressPanel;
	FolderSelectionPanel folderSelectionPanel;
	JComboBox evaluationTypeCombobox;
	JButton addButton, evaluateButton;
	JFrame parent;
	EvaluationController controller;

	public MainPanel(JFrame parent) {
		super();
		this.parent = parent;
		initialize();
		controller = new EvaluationController(getPlantListPanel(), this);
	}

	private void initialize() {
		setLayout(new JideBorderLayout());

		add(getFolderSelectionPanel(), JideBorderLayout.NORTH);
		add(getCenterPanel());
		FormLayout layout = new FormLayout("right:pref:grow, 4dlu, right:pref, 4dlu, right:pref"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append("Evaluation Type", getEvaluationTypeComboBox());
		builder.append(ButtonBarFactory.buildOKCancelBar(getEvaluateButton(), getAddButton()));
		add(builder.getPanel(), BorderLayout.SOUTH);

		log.trace("Main Panel initialized");

	}

	private PlantListPanel getPlantListPanel() {
		if (plantListPanel == null) {
			plantListPanel = new PlantListPanel(PlantHelper.getDefaultPlantList());
		}
		return plantListPanel;
	}

	private JPanel getCenterPanel() {
		centerPanel = new JPanel(new JideBorderLayout());
		centerPanel.add(getPlantListPanel(), JideBorderLayout.CENTER);

		return centerPanel;
	}

	private FolderSelectionPanel getFolderSelectionPanel() {
		if (folderSelectionPanel == null) {
			folderSelectionPanel = new FolderSelectionPanel(parent);
		}
		return folderSelectionPanel;
	}

	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getPlantListPanel().addPlant(new Plant());
				}
			});
			addButton.setIcon(Icons.IC_ADD_SMALL);
			addButton.setText("Add Plant");
			// addButton.setMaximumSize(addButton.getPreferredSize());
		}
		return addButton;
	}

	private JButton getEvaluateButton() {
		if (evaluateButton == null) {
			evaluateButton = new JButton(Messages.getString("PlantListPanel.5")); //$NON-NLS-1$

			evaluateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					AbstractEvaluation.EvaluationType type = (EvaluationType) getEvaluationTypeComboBox().getSelectedItem();

					switch (type) {
					case JULIANE:
						// getPlantListPanel().standardEvaluation(evaluateButton,
						// addButton);
						controller.startStandardEvaluation();
						break;
					case CO2ABSOLUTE:
						// getPlantListPanel().co2AbsoluteOnlyEvaluation(
						// evaluateButton, addButton);
						controller.startCo2AbsoluteOnlyEvaluation();
						break;
					case INGO:
						controller.startIngoEvaluation();
						break;
					}
				}
			});
		}
		return evaluateButton;
	}

	private JComboBox getEvaluationTypeComboBox() {
		if (evaluationTypeCombobox == null) {
			evaluationTypeCombobox = new JComboBox(AbstractEvaluation.EvaluationType.values());

			evaluationTypeCombobox.setRenderer(new EvaluationTypeRender());

			evaluationTypeCombobox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					AbstractEvaluation.EvaluationType type = (EvaluationType) evaluationTypeCombobox.getSelectedItem();
					switch (type) {
					case JULIANE:
						plantListPanel.setVisible(true);
						getAddButton().setVisible(true);
						revalidate();
						break;

					default:
						plantListPanel.setVisible(false);
						getAddButton().setVisible(false);
						revalidate();
						break;
					}
				}
			});

			evaluationTypeCombobox.setSelectedItem(EvaluationType.JULIANE);
		}
		return evaluationTypeCombobox;
	}

	public void disableButtons() {
		getEvaluateButton().setEnabled(false);
		getAddButton().setEnabled(false);

		getFolderSelectionPanel().inputFolderButton.setEnabled(false);
		getFolderSelectionPanel().temperatureButton.setEnabled(false);
		getFolderSelectionPanel().outputFolderButton.setEnabled(false);

		getPlantListPanel().setEnabled(false);
	}

	public void enableButtons() {
		getEvaluateButton().setEnabled(true);
		getAddButton().setEnabled(true);
		getFolderSelectionPanel().inputFolderButton.setEnabled(true);
		getFolderSelectionPanel().temperatureButton.setEnabled(true);
		getFolderSelectionPanel().outputFolderButton.setEnabled(true);

		getPlantListPanel().setEnabled(true);
	}

	/**
	 * Adds progressbars for each evaluator in the given list to this panel
	 * 
	 * @param evaluators
	 */
	public void addProgressBars(List<AbstractEvaluator> evaluators) {
		invalidate();
		FormLayout layout = new FormLayout("pref, 4dlu,fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();

		for (AbstractEvaluator eval : evaluators) {
			builder.append(eval.getName());
			builder.append(eval.getProgressBar());
		}

		progressPanel = builder.getPanel();
		centerPanel.add(progressPanel, BorderLayout.NORTH);
		revalidate();
	}

	public void removeProgressPanel() {
		invalidate();
		centerPanel.remove(progressPanel);
		revalidate();
	}

	/**
	 * Custom Renderer to display nice human readable strings for
	 * {@link EvaluationType}s
	 */
	@SuppressWarnings("serial")
	private static class EvaluationTypeRender extends JLabel implements ListCellRenderer {

		public EvaluationTypeRender() {
			super();
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			EvaluationType selectedType = (EvaluationType) value;
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			switch (selectedType) {
			case CO2ABSOLUTE:
				setText("<html>CO<sub>2</sub>-Absolute");
				break;

			case INGO:
				setText("Type B (Ingo)");
				break;
			case JULIANE:
				setText("Type A (Juliane)");
				break;
			default:
				setText(selectedType.toString());
				break;
			}
			return this;
		}
	}

}
