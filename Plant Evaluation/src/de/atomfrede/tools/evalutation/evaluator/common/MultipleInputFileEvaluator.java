/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	MultipleInputFileEvaluator.java is part of Plant Evaluation.
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

public abstract class MultipleInputFileEvaluator extends AbstractEvaluator {

	protected List<File> inputFiles, standardDerivationInputFiles, outputFiles,
			standardDerivationOutputFiles;

	public MultipleInputFileEvaluator(String outputFolderName,
			List<File> inputFiles, List<File> standardDerivationInputFiles) {
		super(outputFolderName);
		this.inputFiles = inputFiles;
		this.standardDerivationInputFiles = standardDerivationInputFiles;
		this.outputFiles = new ArrayList<File>();
		this.standardDerivationOutputFiles = new ArrayList<File>();
	}

	public List<File> getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}

	public List<File> getStandardDerivationInputFiles() {
		return standardDerivationInputFiles;
	}

	public void setStandardDerivationInputFiles(
			List<File> standardDerivationInputFiles) {
		this.standardDerivationInputFiles = standardDerivationInputFiles;
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