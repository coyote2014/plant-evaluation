/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	ReduceDatasetEvaluator.java is part of Plant Evaluation.
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.stat.StatUtils;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.evaluator.SingleInputFileEvaluator;
import de.atomfrede.tools.evalutation.tools.plot.SimplePlot;
import de.atomfrede.tools.evalutation.tools.plot.TimePlot;
import de.atomfrede.tools.evalutation.util.DialogUtil;

/**
 * Evaluator that reduces the whole dataset by taking 60 lines, computing the
 * means for neccessary columns and writing the resulting line into the
 * outputfile. <br>
 * Instead of having datasets for every second, we now have datasets for about
 * each minute (with means).<br>
 * <br>
 * Neccessary Values are:<br>
 * 
 * ->CO2Absolute<br>
 * ->12CO2<br>
 * ->13CO2<br>
 * ->12CO2_DRY<br>
 * ->13CO2_DRY<br>
 * ->DeltaRaw
 */
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
			// first write the header again
			writer.writeNext(allLines.get(0));

			int i = 1;
			while (i < allLines.size()) {
				// the endindex is always the minimum of startIndex + 60 (so 60
				// lines) and the size of all lines to avoid
				// out of bounce exceptions
				int endIndex = Math.min(i + 60, allLines.size());
				List<String[]> meanLines = getLinesForMeanComputation(i, endIndex, allLines);
				// compute the means for all necessary values
				String[] meanLine = computeMeanLine(meanLines);
				writer.writeNext(meanLine);
				i = Math.min(i + 60 + 1, allLines.size());
				progressBar.setValue((int) ((i * 1.0 / allLines.size()) * 100.0));
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

		try {
			new TimePlot(outputFile).plot();
			new SimplePlot(outputFile).plot();
		} catch (Exception e) {
			log.error("Error during plot.", e);
			DialogUtil.getInstance().showError(e);

		}
		log.info("Reduce Dataset Evaluator done.");
		return true;
	}

	/**
	 * Collects all values, computes their means and writes them into a new data
	 * line for the csv file. Besides the computed means the line stays
	 * unchanged.
	 * 
	 * @param meanLines
	 * @return
	 */
	private String[] computeMeanLine(List<String[]> meanLines) {
		// create a new empty line
		String[] meanLine = new String[meanLines.get(0).length];
		// take the last line of all mean lines as line that will be written in
		// the new file
		meanLine = meanLines.get(meanLines.size() - 1);
		// List for collection all values to compute means of
		List<Double> co2AbsoluteValues = new ArrayList<Double>();
		List<Double> _12Co2Values = new ArrayList<Double>();
		List<Double> _13Co2Values = new ArrayList<Double>();
		List<Double> _12Co2DryValues = new ArrayList<Double>();
		List<Double> _13Co2DryValues = new ArrayList<Double>();
		List<Double> deltaRawValues = new ArrayList<Double>();

		for (int i = 0; i < meanLines.size(); i++) {
			String[] currentMeanLine = meanLines.get(i);
			_12Co2Values.add(parseDoubleValue(currentMeanLine, InputFileConstants._12CO2));
			_13Co2Values.add(parseDoubleValue(currentMeanLine, InputFileConstants._13CO2));
			_12Co2DryValues.add(parseDoubleValue(currentMeanLine, InputFileConstants._12CO2_DRY));
			_13Co2DryValues.add(parseDoubleValue(currentMeanLine, InputFileConstants._13CO2_DRY));
			deltaRawValues.add(parseDoubleValue(currentMeanLine, InputFileConstants.DELTA_RAW));
			co2AbsoluteValues.add(parseDoubleValue(currentMeanLine, meanLine.length - 1));

		}
		// now compute the means
		double mean12Co2 = StatUtils.mean(list2DoubleArray(_12Co2Values));
		double mean13Co2 = StatUtils.mean(list2DoubleArray(_13Co2Values));
		double mean12Co2Dry = StatUtils.mean(list2DoubleArray(_12Co2DryValues));
		double mean13Co2Dry = StatUtils.mean(list2DoubleArray(_13Co2DryValues));
		double meanDeltaRaw = StatUtils.mean(list2DoubleArray(deltaRawValues));
		double meanCo2Absolute = StatUtils.mean(list2DoubleArray(co2AbsoluteValues));
		// add them to the new line that is written to the outputfile
		meanLine[InputFileConstants._12CO2] = mean12Co2 + "";
		meanLine[InputFileConstants._13CO2] = mean13Co2 + "";
		meanLine[InputFileConstants._12CO2_DRY] = mean12Co2Dry + "";
		meanLine[InputFileConstants._13CO2_DRY] = mean13Co2Dry + "";
		meanLine[InputFileConstants.DELTA_RAW] = meanDeltaRaw + "";
		meanLine[meanLine.length - 1] = meanCo2Absolute + "";
		return meanLine;
	}

	/**
	 * Returns a list of data lines between startIndex and endIndex
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param allLines
	 * @return
	 */
	private List<String[]> getLinesForMeanComputation(int startIndex, int endIndex, List<String[]> allLines) {
		List<String[]> lines = new ArrayList<String[]>();
		for (int i = startIndex; i < endIndex; i++) {
			lines.add(allLines.get(i));
		}
		return lines;
	}
}
