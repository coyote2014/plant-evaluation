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

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jidesoft.swing.JideBorderLayout;

import de.atomfrede.tools.evalutation.main.PlantHelper;
import de.atomfrede.tools.evalutation.ui.plant.PlantListPanel;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6119526095319839746L;
	PlantListPanel plantListPanel;
	FolderSelectionPanel folderSelectionPanel;
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

}
