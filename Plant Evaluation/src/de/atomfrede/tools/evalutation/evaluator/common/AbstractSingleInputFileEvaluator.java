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

public abstract class AbstractSingleInputFileEvaluator extends
		AbstractEvaluator {

	protected File inputFile, standardDerivationInputFile, outputFile,
			standardDerivationOutputFile;

	public AbstractSingleInputFileEvaluator(String outputFolderName,
			File inputFile, File standardDerivationInputFile) {
		super(outputFolderName);
		this.inputFile = inputFile;
		this.standardDerivationInputFile = standardDerivationInputFile;

	}

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public File getStandardDerivationInputFile() {
		return standardDerivationInputFile;
	}

	public void setStandardDerivationInputFile(File standardDerivationInputFile) {
		this.standardDerivationInputFile = standardDerivationInputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public File getStandardDerivationOutputFile() {
		return standardDerivationOutputFile;
	}

	public void setStandardDerivationOutputFile(
			File standardDerivationOutputFile) {
		this.standardDerivationOutputFile = standardDerivationOutputFile;
	}

	@Override
	public abstract boolean evaluate() throws Exception;

}
