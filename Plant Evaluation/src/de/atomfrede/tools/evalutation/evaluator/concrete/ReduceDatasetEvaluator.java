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
import org.apache.commons.math.stat.StatUtils;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.evaluator.common.SingleInputFileEvaluator;

public class ReduceDatasetEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(ReduceDatasetEvaluator.class);

	public ReduceDatasetEvaluator(File inputFile) {
		super("Reduced Datasets", inputFile, null);
		this.name = "Reducing Dataset";
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
				int endIndex = Math.min(i + 60, allLines.size());
				List<String[]> meanLines = getLinesForMeanComputation(i,
						endIndex, allLines);

				String[] meanLine = computeMeanLine(meanLines);
				writer.writeNext(meanLine);
				i = Math.min(i + 60 + 1, allLines.size());
				progressBar
						.setValue((int) ((i * 1.0 / allLines.size()) * 100.0));
			}

			progressBar.setValue(100);
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
		meanLine = meanLines.get(meanLine.length - 1);

		List<Double> co2AbsoluteValues = new ArrayList<Double>();
		List<Double> _12Co2Values = new ArrayList<Double>();
		List<Double> _13Co2Values = new ArrayList<Double>();
		List<Double> _12Co2DryValues = new ArrayList<Double>();
		List<Double> _13Co2DryValues = new ArrayList<Double>();
		List<Double> deltaRawValues = new ArrayList<Double>();

		for (int i = 0; i < meanLines.size(); i++) {
			String[] currentMeanLine = meanLines.get(i);
			_12Co2Values
					.add(parseDoubleValue(currentMeanLine, Constants._12CO2));
			_13Co2Values
					.add(parseDoubleValue(currentMeanLine, Constants._13CO2));
			_12Co2DryValues.add(parseDoubleValue(currentMeanLine,
					Constants._12CO2_DRY));
			_13Co2DryValues.add(parseDoubleValue(currentMeanLine,
					Constants._13CO2_DRY));
			deltaRawValues.add(parseDoubleValue(currentMeanLine,
					Constants.DELTA));
			co2AbsoluteValues.add(parseDoubleValue(currentMeanLine,
					meanLine.length - 1));

		}
		double mean12Co2 = StatUtils.mean(list2DoubleArray(_12Co2Values));
		double mean13Co2 = StatUtils.mean(list2DoubleArray(_13Co2Values));
		double mean12Co2Dry = StatUtils.mean(list2DoubleArray(_12Co2DryValues));
		double mean13Co2Dry = StatUtils.mean(list2DoubleArray(_13Co2DryValues));
		double meanDeltaRaw = StatUtils.mean(list2DoubleArray(deltaRawValues));
		double meanCo2Absolute = StatUtils
				.mean(list2DoubleArray(co2AbsoluteValues));
		meanLine[Constants._12CO2] = mean12Co2 + "";
		meanLine[Constants._13CO2] = mean13Co2 + "";
		meanLine[Constants._12CO2_DRY] = mean12Co2Dry + "";
		meanLine[Constants._13CO2_DRY] = mean13Co2Dry + "";
		meanLine[Constants.DELTA] = meanDeltaRaw + "";
		meanLine[meanLine.length - 1] = meanCo2Absolute + "";
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
