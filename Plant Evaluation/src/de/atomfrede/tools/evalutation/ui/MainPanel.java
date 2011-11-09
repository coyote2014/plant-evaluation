/**
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

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.atomfrede.tools.evalutation.main.PlantHelper;

public class MainPanel extends JPanel {

	PlantListPanel plantListPanel;
	FolderSelectionPanel folderSelectionPanel;
	JFrame parent;

	public MainPanel(JFrame parent) {
		super();
		this.parent = parent;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());

		add(getFolderSelectionPanel(), BorderLayout.NORTH);
		add(getPlantListPanel(), BorderLayout.CENTER);
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
