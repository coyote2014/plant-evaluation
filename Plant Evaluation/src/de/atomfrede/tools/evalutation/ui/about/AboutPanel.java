/**
 * 	Copyright 2011 Frederik Hahne
 * 
 * 	AboutPanel.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.ui.about;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import de.atomfrede.tools.evalutation.ui.res.Messages;

public class AboutPanel extends JPanel {
	private JLabel lblPlantEvaluationTool;
	private JLabel lblVersion;
	private JLabel lblCopyright;
	private JLabel lblFrederikHahne;
	private JLabel lblLicensedUnderGpl;
	private JButton btnNewButton;

	private final JDialog parentDialog;
	private JButton btnSourceCode;
	URI uri;

	/**
	 * Create the panel.
	 * 
	 * @throws URISyntaxException
	 */
	public AboutPanel(JDialog parentDialog) throws URISyntaxException {
		uri = new URI(
				Messages.getString("AboutPanel.sourceCode")); //$NON-NLS-1$
		this.parentDialog = parentDialog;
		setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));
		add(getLblPlantEvaluationTool(), "2, 2, left, default"); //$NON-NLS-1$
		add(getLblVersion(), "2, 4"); //$NON-NLS-1$
		add(getLblCopyright(), "2, 8"); //$NON-NLS-1$
		add(getLblFrederikHahne(), "2, 10"); //$NON-NLS-1$
		add(getLblLicensedUnderGpl(), "2, 14"); //$NON-NLS-1$
		add(getBtnSourceCode(), "2, 16"); //$NON-NLS-1$
		add(getBtnNewButton(), "2, 18, center, default"); //$NON-NLS-1$

	}

	private JLabel getLblPlantEvaluationTool() {
		if (lblPlantEvaluationTool == null) {
			lblPlantEvaluationTool = new JLabel(Messages.getString("AboutPanel.8")); //$NON-NLS-1$
			lblPlantEvaluationTool.setFont(new Font("Dialog", Font.BOLD, 20)); //$NON-NLS-1$
		}
		return lblPlantEvaluationTool;
	}

	private JLabel getLblVersion() {
		if (lblVersion == null) {
			lblVersion = new JLabel(Messages.getString("AboutPanel.version")); //$NON-NLS-1$
		}
		return lblVersion;
	}

	private JLabel getLblCopyright() {
		if (lblCopyright == null) {
			lblCopyright = new JLabel(Messages.getString("AboutPanel.11")); //$NON-NLS-1$
		}
		return lblCopyright;
	}

	private JLabel getLblFrederikHahne() {
		if (lblFrederikHahne == null) {
			lblFrederikHahne = new JLabel(Messages.getString("AboutPanel.12")); //$NON-NLS-1$
		}
		return lblFrederikHahne;
	}

	private JLabel getLblLicensedUnderGpl() {
		if (lblLicensedUnderGpl == null) {
			lblLicensedUnderGpl = new JLabel(Messages.getString("AboutPanel.13")); //$NON-NLS-1$
		}
		return lblLicensedUnderGpl;
	}

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton(Messages.getString("AboutPanel.14")); //$NON-NLS-1$
			btnNewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					parentDialog.dispose();
				}
			});
		}
		return btnNewButton;
	}

	private JButton getBtnSourceCode() {
		if (btnSourceCode == null) {
			String link = "<HTML><FONT color=\"#000099\"><U>Source Code</U></FONT></HTML>"; //$NON-NLS-1$
			btnSourceCode = new JButton(link);
			btnSourceCode.setHorizontalAlignment(SwingConstants.CENTER);
			btnSourceCode.setBorderPainted(false);
			btnSourceCode.setOpaque(false);
			btnSourceCode.setBackground(Color.WHITE);
			btnSourceCode.setRolloverEnabled(false);
			btnSourceCode.setBorder(null);
			btnSourceCode.setToolTipText(uri.toString());
			btnSourceCode.setFocusPainted(false);

			btnSourceCode.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					open(uri);
				}
			});
		}
		return btnSourceCode;
	}

	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(uri);
			} catch (IOException e) {
				// TODO: error handling
			}
		} else {
			// TODO: error handling
		}
	}

}
