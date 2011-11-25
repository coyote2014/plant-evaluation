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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBorderLayout;

import de.atomfrede.tools.evalutation.Plant;
import de.atomfrede.tools.evalutation.main.PlantHelper;
import de.atomfrede.tools.evalutation.ui.plant.PlantListPanel;
import de.atomfrede.tools.evalutation.ui.res.Messages;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6119526095319839746L;
	PlantListPanel plantListPanel;
	FolderSelectionPanel folderSelectionPanel;
	JCheckBox co2AbsOnlyCheckbox;
	JButton addButton, evaluateButton;
	JFrame parent;

	public MainPanel(JFrame parent) {
		super();
		this.parent = parent;
		initialize();
	}

	private void initialize() {
		setLayout(new JideBorderLayout());

		add(getFolderSelectionPanel(), JideBorderLayout.NORTH);
		add(getPlantListPanel(), JideBorderLayout.CENTER);

		FormLayout layout = new FormLayout("right:pref:grow, 4dlu, right:pref"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.append(getCo2AbsoluteCheckBox());
		builder.append(ButtonBarFactory.buildOKCancelBar(getEvaluateButton(),
				getAddButton()));
		add(builder.getPanel(), BorderLayout.SOUTH);
	}

	private PlantListPanel getPlantListPanel() {
		if (plantListPanel == null) {
			plantListPanel = new PlantListPanel(
					PlantHelper.getDefaultPlantList());
		}
		return plantListPanel;
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
			addButton.setMaximumSize(addButton.getPreferredSize());
		}
		return addButton;
	}

	private JButton getEvaluateButton() {
		if (evaluateButton == null) {
			evaluateButton = new JButton(Messages.getString("PlantListPanel.5")); //$NON-NLS-1$

			evaluateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					getPlantListPanel().evaluate(evaluateButton, addButton);
				}
			});
		}
		return evaluateButton;
	}

	private JCheckBox getCo2AbsoluteCheckBox() {
		if (co2AbsOnlyCheckbox == null) {
			co2AbsOnlyCheckbox = new JCheckBox(
					Messages.getString("MainPanel.1")); //$NON-NLS-1$
		}
		return co2AbsOnlyCheckbox;
	}

}
