/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	PickDatasetEvaluator.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.evaluator.SingleInputFileEvaluator;
import de.atomfrede.tools.evalutation.util.DialogUtil;

public class PickDatasetEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(PickDatasetEvaluator.class);

	public PickDatasetEvaluator(File inputFile) {
		super("Pick DataSets", inputFile, null);
		this.name = "Pick DataSets";
	}

	@Override
	public boolean evaluate() throws Exception {
		CSVWriter writer = null;
		try {
			outputFile = new File(outputFolder, "pick-datasets.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;
			writer = getCsvWriter(outputFile);

			List<String[]> allLines = readAllLinesInFile(inputFile);

			writer.writeNext(allLines.get(0));

			int i = 1;
			// if the first (and only) switch index is equal to the lenght of the input file we just copy the file, otherwise we will get only one line
			if (getSwitchIndex(i, allLines) == allLines.size()) {
				// write the content write by line
				for (int j = 1; j < allLines.size(); j++) {
					writer.writeNext(allLines.get(j));
					progressBar.setValue((int) ((j * 1.0 / allLines.size()) * 100.0));
				}
			} else {
				while (i < allLines.size()) {
					int index = getSwitchIndex(i, allLines);
					writer.writeNext(allLines.get(index - 1));
					i = index;
					progressBar.setValue((int) ((i * 1.0 / allLines.size()) * 100.0));
				}
			}

			progressBar.setValue(100);
		} catch (Exception e) {
			log.error(e);
			DialogUtil.getInstance().showError(e);
			return false;
		} finally {
			if (writer != null)
				writer.close();
		}
		return true;
	}

	private int getSwitchIndex(int startIndex, List<String[]> allLines) {
		double startSolenoidValve = parseDoubleValue(allLines.get(startIndex), InputFileConstants.SOLENOID_VALVE_INPUT);
		for (int i = startIndex; i < allLines.size(); i++) {
			String[] currentLine = allLines.get(i);
			double currentSolenoid = parseDoubleValue(currentLine, InputFileConstants.SOLENOID_VALVE_INPUT);
			if (currentSolenoid != startSolenoidValve) {
				// detected a switch
				return i;
			}
		}
		return allLines.size();
	}
}
