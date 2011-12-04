/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	MeanEvaluator.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.evaluator.concrete;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.evaluator.common.SingleInputFileEvaluator;

public class ReduceDatasetEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(ReduceDatasetEvaluator.class);

	public ReduceDatasetEvaluator(File inputFile) {
		super("Reduced Datasets", inputFile, null);
	}

	@Override
	public boolean evaluate() throws Exception {
		CSVWriter writer = null;
		try {
			outputFile = new File(outputFolder, "reduced-datasets.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;
			writer = getCsvWriter(outputFile);

			List<String[]> allLines = readAllLinesInFile(inputFile);

			writer.writeNext(allLines.get(0));

			int i = 1;
			while (i < allLines.size()) {
				int endIndex = Math.min(i += 60, allLines.size());
				List<String[]> meanLines = getLinesForMeanComputation(i,
						endIndex, allLines);

				String[] meanLine = computeMeanLine(meanLines);
				writer.writeNext(meanLine);
				i = Math.min(i += 60, allLines.size());
			}

		} catch (Exception e) {
			log.error(e);
			return false;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return true;
	}

	private String[] computeMeanLine(List<String[]> meanLines) {
		String[] meanLine = new String[meanLines.get(0).length];

		meanLine = meanLines.get(0);
		return meanLine;
	}

	private List<String[]> getLinesForMeanComputation(int startIndex,
			int endIndex, List<String[]> allLines) {
		List<String[]> lines = new ArrayList<String[]>();
		for (int i = startIndex; i < endIndex; i++) {
			lines.add(allLines.get(i));
		}
		return lines;
	}
}
