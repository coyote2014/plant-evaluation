/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	DatasetSelectionWizardPage.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard.time.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JDialog;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages.AbstractWizardPage;

@SuppressWarnings("serial")
public class DatasetSelectionWizardPage extends AbstractWizardPage {

	File datafile;

	public DatasetSelectionWizardPage(JDialog parent, File datafile) {
		this("Setup Datasets", "Setup and configure the desired datasets.", parent);
		this.datafile = datafile;

	}

	public void addContent() {
		setLayout(new BorderLayout());
		FormLayout layout = new FormLayout("fill:pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.append(new DatasetInputPanel(datafile, Color.ORANGE, false));
		add(builder.getPanel(), BorderLayout.CENTER);

	}

	/**
	 * @param title
	 * @param description
	 * @param parent
	 */
	public DatasetSelectionWizardPage(String title, String description, JDialog parent) {
		super(title, description, parent);
	}

	public File getDatafile() {
		return datafile;
	}

	public void setDatafile(File datafile) {
		this.datafile = datafile;
	}

}
