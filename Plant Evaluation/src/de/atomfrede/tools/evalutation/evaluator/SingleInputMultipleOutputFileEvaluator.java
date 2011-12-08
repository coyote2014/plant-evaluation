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

package de.atomfrede.tools.evalutation.evaluator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluator that takes one file as input and produces multiple files as output.
 */
public abstract class SingleInputMultipleOutputFileEvaluator extends
		AbstractEvaluator {

	protected File inputFile, standardDeviationInputFile;
	protected List<File> outputFiles, standardDeviationOutputFiles;

	public SingleInputMultipleOutputFileEvaluator(String outputFolderName,
			File inputFile, File standardDeviationInputFile) {
		super(outputFolderName);
		this.inputFile = inputFile;
		this.standardDeviationInputFile = standardDeviationInputFile;
		this.outputFiles = new ArrayList<File>();
		this.standardDeviationOutputFiles = new ArrayList<File>();
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

	public List<File> getOutputFiles() {
		return outputFiles;
	}

	public void setOutputFiles(List<File> outputFiles) {
		this.outputFiles = outputFiles;
	}

	public List<File> getStandardDeviationOutputFiles() {
		return standardDeviationOutputFiles;
	}

	public void setStandardDeviationOutputFiles(
			List<File> standardDeviationOutputFiles) {
		this.standardDeviationOutputFiles = standardDeviationOutputFiles;
	}

	@Override
	public abstract boolean evaluate() throws Exception;

}
