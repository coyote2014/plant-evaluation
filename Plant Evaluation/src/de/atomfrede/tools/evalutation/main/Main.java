/**
 * 	Main.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.main;

import java.io.File;

import de.atomfrede.tools.evalutation.evaluator.CopyEvaluator;

public class Main {

	static File outputFolder = new File("output");
	static File photosynthesisFolder = new File(outputFolder, "photosythesis/");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CopyEvaluator evaluator = new CopyEvaluator();

		// List<File> inputFiles = new ArrayList<File>();
		// List<File> sdFiles = new ArrayList<File>();
		//
		// File[] allFiles = photosynthesisFolder.listFiles();
		// for (File file : allFiles) {
		// if (file.isFile()) {
		// if (file.getName().startsWith("psr"))
		// inputFiles.add(file);
		// else
		// sdFiles.add(file);
		// }
		// }
		//
		// StandardDerivationEvaluator sdEvaluator = new
		// StandardDerivationEvaluator(
		// inputFiles, sdFiles);

	}
}
