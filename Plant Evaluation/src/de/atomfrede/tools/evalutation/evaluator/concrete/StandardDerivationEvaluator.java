/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	StandardDerivationEvaluator.java is part of Plant Evaluation.
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
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.StatUtils;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.evaluator.common.AbstractEvaluator;

public class StandardDerivationEvaluator extends AbstractEvaluator {

	// static int TIME = 8;
	// static int PSR = 12;
	// static int delta13 = 10;
	// static int SOLENOID = 6;

	List<File> outputFiles;

	List<File> inputFiles;
	List<File> standardDerivationInputFiles;

	int currentPlant;

	List<String[]> currentMeanDataLines;
	List<String[]> currentStandardDerivationLines;

	public StandardDerivationEvaluator(List<File> inputFiles,
			List<File> standardDerivationInputFiles) {
		super("standard-derivation");
		this.inputFiles = inputFiles;
		this.standardDerivationInputFiles = standardDerivationInputFiles;
		outputFiles = new ArrayList<File>();
		Collections.sort(inputFiles);
		Collections.sort(standardDerivationInputFiles);
		evaluate();

	}

	@Override
	public boolean evaluate() {
		currentPlant = -1;
		try {
			for (int i = 0; i < inputFiles.size(); i++) {
				System.out.println("Input File " + inputFiles.get(i));
				currentPlant = i;

				currentMeanDataLines = readAllLinesInFile(inputFiles.get(i));
				File currentSdFile = getCorrespondingStandardDerivationFile(currentPlant);
				System.out.println("Using SD File " + currentSdFile);
				currentStandardDerivationLines = readAllLinesInFile(currentSdFile);

				File outputFile = new File(outputFolder, "psr-sd-0"
						+ (currentPlant) + ".csv");

				CSVWriter writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				// for each line compute the standard derivation
				for (int j = 1; j < currentMeanDataLines.size(); j++) {
					String[] currentLine = currentMeanDataLines.get(j);
					Map<Integer, double[]> mapping = getPSRandDelta13Value(currentLine);
					double[] psrValues = mapping.get(Constants.PSR);
					double[] delta13Values = mapping.get(Constants.DELTA13);

					double psrMean = parseDoubleValue(currentLine,
							Constants.PSR);
					double delta13Mean = parseDoubleValue(currentLine,
							Constants.DELTA13);

					// double psrStandardDerivation = getStandardDerivation(
					// psrValues, psrMean);
					// double delta13StandardDerivation = getStandardDerivation(
					// delta13Values, delta13Mean);

					double psrStandardDerivation = getStandardDerivation(psrValues);
					double delta13StandardDerivation = getStandardDerivation(delta13Values);
					if (psrMean == 0.0) {
						psrStandardDerivation = 0.0;
						delta13StandardDerivation = 0.0;
					}
					writeLineWithStandardDerivation(writer, currentLine,
							psrStandardDerivation, delta13StandardDerivation);

				}
				writer.close();
				outputFiles.add(outputFile);

			}
		} catch (IOException ioe) {
			System.out.println("IOException " + ioe);
			return false;
		} catch (ParseException pe) {
			System.out.println("Parse Exception " + pe);
			return false;
		}
		System.out.println("StandarDerivation Evaluator done.");
		return true;
	}

	double getStandardDerivation(double[] values, double mean) {
		return Math.sqrt(StatUtils.variance(values, mean));
	}

	double getStandardDerivation(double[] values) {
		if (values != null && values.length >= 1) {
			return Math.sqrt(StatUtils.variance(values));
		} else
			return 0.0;
		// return ((1.0 / values.length) * StatUtils.sum(values));
		// double sum = 0;
		// for (int i = 0; i < values.length; i++) {
		// sum += values[i];
		// }
		// double v = 1.0 / values.length;
		// return v * sum;
	}

	File getCorrespondingStandardDerivationFile(int currentDataFile) {
		for (File file : standardDerivationInputFiles) {
			if (file.getName().endsWith(currentDataFile + ".csv")) {
				return file;
			}
		}
		return null;
	}

	void writeLineWithStandardDerivation(CSVWriter writer, String[] line,
			double psrStandardDerivation, double delta13StandardDerivation) {
		String[] newLine = new String[line.length + 2];
		int i = 0;
		for (i = 0; i < line.length; i++) {
			newLine[i] = line[i];
		}
		newLine[i] = psrStandardDerivation + "";
		newLine[i + 1] = delta13StandardDerivation + "";
		writer.writeNext(newLine);

	}

	Map<Integer, double[]> getPSRandDelta13Value(String[] meanDataLine)
			throws ParseException {
		Map<Integer, double[]> dataMapping = new HashMap<Integer, double[]>();
		List<Double> allPSRValues = new ArrayList<Double>();
		List<Double> allDelta13Values = new ArrayList<Double>();
		double solenoid = parseDoubleValue(meanDataLine,
				Constants.SOLENOID_VALVES);
		Date meanDate = dateFormat.parse(meanDataLine[Constants.DATE_AND_TIME]);
		List<Integer> allLinesForCurrentMeanDate = getAllLinesToComputeStandardDerivation(
				meanDate, solenoid);

		for (Integer lineIndex : allLinesForCurrentMeanDate) {
			if (lineIndex >= 1) {
				String[] currentLine = currentStandardDerivationLines
						.get(lineIndex);
				double psrValue = parseDoubleValue(currentLine, Constants.PSR);
				double delta13Value = parseDoubleValue(currentLine,
						Constants.DELTA13);
				allPSRValues.add(psrValue);
				allDelta13Values.add(delta13Value);
			}
		}
		dataMapping.put(Constants.PSR, list2DoubleArray(allPSRValues));
		dataMapping.put(Constants.DELTA13, list2DoubleArray(allDelta13Values));

		return dataMapping;
	}

	List<Integer> getAllLinesToComputeStandardDerivation(Date meanDate,
			double solenoidToEvaluate) throws ParseException {
		List<Integer> standardDerivationLines = new ArrayList<Integer>();
		int startIndex = getStartIndex(meanDate);
		if (startIndex == -1)
			System.out.println("StartIndex for " + meanDate + " is "
					+ startIndex);
		Date currentDate = meanDate;
		int currentIndex = startIndex;
		// standardDerivationLines.add(startIndex);
		while (Math.abs(meanDate.getTime() - currentDate.getTime()) <= Constants.fiveMinutes
				&& currentIndex >= 1
				&& currentIndex < currentStandardDerivationLines.size()) {
			String[] possibleLine = currentStandardDerivationLines
					.get(currentIndex);
			double solenoid = parseDoubleValue(possibleLine,
					Constants.SOLENOID_VALVES);
			if (solenoid == solenoidToEvaluate) {
				standardDerivationLines.add(currentIndex);
				currentDate = dateFormat
						.parse(possibleLine[Constants.DATE_AND_TIME]);
			}
			currentIndex++;

		}
		return standardDerivationLines;
	}

	int getStartIndex(Date meanDate) throws ParseException {
		int startIndex = -1;
		long shortestedDistance = Long.MAX_VALUE;
		for (int i = 1; i < currentStandardDerivationLines.size(); i++) {
			String[] currentStandardDerivationLine = currentStandardDerivationLines
					.get(i);
			Date standardDerivationDate = dateFormat
					.parse(currentStandardDerivationLine[Constants.DATE_AND_TIME]);

			long difference = Math.abs(meanDate.getTime()
					- standardDerivationDate.getTime());
			if (shortestedDistance > difference) {
				shortestedDistance = difference;
				startIndex = i;
			}
		}
		return startIndex;
	}
}
