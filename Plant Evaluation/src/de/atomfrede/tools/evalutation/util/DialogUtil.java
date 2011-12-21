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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.atomfrede.tools.evalutation.ui.ExceptionDialog;

public class DialogUtil {

	private static DialogUtil instance;

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

	public void showError(final Exception ex) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ExceptionDialog(frame, ex).setVisible(true);
			}
		});

	}
}