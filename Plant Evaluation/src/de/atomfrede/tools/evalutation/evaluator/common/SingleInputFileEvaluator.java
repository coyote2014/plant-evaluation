/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	AbstractSingleInputFileEvaluator.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.evaluator.common;

import java.io.File;

/**
 * Evaluator that takes one file as input (and possible a standard deviation
 * input file) and produces one file as output (and possible one file for
 * standard deviation).
 */
public abstract class SingleInputFileEvaluator extends AbstractEvaluator {

	protected File inputFile, standardDeviationInputFile, outputFile,
			standardDeviationOutputFile;

	public SingleInputFileEvaluator(String outputFolderName, File inputFile,
			File standardDeviationInputFile) {
		super(outputFolderName);
		this.inputFile = inputFile;
		this.standardDeviationInputFile = standardDeviationInputFile;

	}

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public File getStandardDeviationInputFile() {
		return standardDeviationInputFile;
	}

	public void setStandardDeviationInputFile(File standardDeviationInputFile) {
		this.standardDeviationInputFile = standardDeviationInputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public File getStandardDeviationOutputFile() {
		return standardDeviationOutputFile;
	}

	public void setStandardDeviationOutputFile(
			File standardDeviationOutputFile) {
		this.standardDeviationOutputFile = standardDeviationOutputFile;
	}

	@Override
	public abstract boolean evaluate() throws Exception;

}
