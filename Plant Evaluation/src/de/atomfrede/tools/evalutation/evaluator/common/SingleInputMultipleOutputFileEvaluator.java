/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	SingleInputMultipleOutputFileEvaluator.java is part of Plant Evaluation.
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
import java.util.ArrayList;
import java.util.List;

public abstract class SingleInputMultipleOutputFileEvaluator extends
		AbstractEvaluator {

	protected File inputFile, standardDerivationInputFile;
	protected List<File> outputFiles, standardDerivationOutputFiles;

	public SingleInputMultipleOutputFileEvaluator(String outputFolderName,
			File inputFile, File standardDerivationInputFile) {
		super(outputFolderName);
		this.inputFile = inputFile;
		this.standardDerivationInputFile = standardDerivationInputFile;
		this.outputFiles = new ArrayList<File>();
		this.standardDerivationOutputFiles = new ArrayList<File>();
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

	public List<File> getOutputFiles() {
		return outputFiles;
	}

	public void setOutputFiles(List<File> outputFiles) {
		this.outputFiles = outputFiles;
	}

	public List<File> getStandardDerivationOutputFiles() {
		return standardDerivationOutputFiles;
	}

	public void setStandardDerivationOutputFiles(
			List<File> standardDerivationOutputFiles) {
		this.standardDerivationOutputFiles = standardDerivationOutputFiles;
	}

	@Override
	public abstract boolean evaluate() throws Exception;

}
