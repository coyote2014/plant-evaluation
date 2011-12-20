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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.stat.StatUtils;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.CommonConstants;
import de.atomfrede.tools.evalutation.OutputFileConstants;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.evaluator.MultipleInputFileEvaluator;

public class StandardDeviationEvaluator extends MultipleInputFileEvaluator {

	private final Log log = LogFactory.getLog(StandardDeviationEvaluator.class);

	int currentPlant;

	List<String[]> currentMeanDataLines;
	List<String[]> currentStandardDeviationLines;

	public StandardDeviationEvaluator(List<File> inputFiles, List<File> standardDeviationInputFiles) {
		super("standard-derivation", inputFiles, standardDeviationInputFiles);
		this.name = "Standard Derivation";
		Collections.sort(inputFiles);
		Collections.sort(standardDeviationInputFiles);
	}

	@Override
	public boolean evaluate() {
		currentPlant = -1;
		try {
			for (int i = 0; i < inputFiles.size(); i++) {
				log.debug("Input File " + inputFiles.get(i));
				currentPlant = i;

				currentMeanDataLines = readAllLinesInFile(inputFiles.get(i));
				File currentSdFile = getCorrespondingStandardDeviationFile(currentPlant);
				System.out.println("Using SD File " + currentSdFile);
				currentStandardDeviationLines = readAllLinesInFile(currentSdFile);

				File outputFile = new File(outputFolder, "psr-sd-0" + (currentPlant) + ".csv");

				CSVWriter writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				// for each line compute the standard derivation
				for (int j = 1; j < currentMeanDataLines.size(); j++) {
					String[] currentLine = currentMeanDataLines.get(j);
					Map<Integer, double[]> mapping = getPSRandDelta13Value(currentLine);
					double[] psrValues = mapping.get(OutputFileConstants.PSR);
					double[] delta13Values = mapping.get(OutputFileConstants.DELTA13);

					double psrMean = parseDoubleValue(currentLine, OutputFileConstants.PSR);
					double delta13Mean = parseDoubleValue(currentLine, OutputFileConstants.DELTA13);

					double psrStandardDerivation = getStandardDeviation(psrValues);
					double delta13StandardDerivation = getStandardDeviation(delta13Values);
					if (psrMean == 0.0) {
						psrStandardDerivation = 0.0;
						delta13StandardDerivation = 0.0;
					}
					if (psrMean != 0.0) {
						psrMean = StatUtils.mean(psrValues);
						currentLine[OutputFileConstants.PSR] = psrMean + "";
					}
					if (delta13Mean != 0.0) {
						delta13Mean = StatUtils.mean(delta13Values);
						currentLine[OutputFileConstants.DELTA13] = delta13Mean + "";
					}

					writeLineWithStandardDeviation(writer, currentLine, psrStandardDerivation, delta13StandardDerivation);

					progressBar.setValue((int) (j * 1.0 / currentMeanDataLines.size() * 100.0 * 1.0 / inputFiles.size() * 1.0));

				}
				writer.close();
				outputFiles.add(outputFile);
				progressBar.setValue(100);

			}
		} catch (IOException ioe) {
			log.error(ioe);
			return false;
		} catch (ParseException pe) {
			log.error(pe);
			return false;
		}
		log.info("StandardDeviation Evaluator done.");
		return true;
	}

	double getStandardDeviation(double[] values, double mean) {
		return Math.sqrt(StatUtils.variance(values, mean));
	}

	double getStandardDeviation(double[] values) {

		return Math.sqrt(StatUtils.variance(values));

		// double mean = StatUtils.mean(values);
		//
		// double sum = 0.0;
		// for (int i = 0; i < values.length; i++) {
		// sum += Math.pow((values[i] - mean), 2);
		// }
		// double divider = 1.0 / (values.length - 1.0);
		// double value = divider * sum;
		// double sd = Math.sqrt(value);
		// return sd;
	}

	File getCorrespondingStandardDeviationFile(int currentDataFile) {
		for (File file : standardDeviationInputFiles) {
			if (file.getName().endsWith(currentDataFile + ".csv")) {
				return file;
			}
		}
		return null;
	}

	void writeLineWithStandardDeviation(CSVWriter writer, String[] line, double psrStandardDerivation, double delta13StandardDerivation) {
		String[] newLine = new String[line.length + 2];
		int i = 0;
		for (i = 0; i < line.length; i++) {
			newLine[i] = line[i];
		}
		newLine[i] = psrStandardDerivation + "";
		newLine[i + 1] = delta13StandardDerivation + "";
		writer.writeNext(newLine);

	}

	Map<Integer, double[]> getPSRandDelta13Value(String[] meanDataLine) throws ParseException {
		Map<Integer, double[]> dataMapping = new HashMap<Integer, double[]>();
		List<Double> allPSRValues = new ArrayList<Double>();
		List<Double> allDelta13Values = new ArrayList<Double>();
		double solenoid = parseDoubleValue(meanDataLine, OutputFileConstants.SOLENOID_VALVES);
		Date meanDate = dateFormat.parse(meanDataLine[OutputFileConstants.DATE_AND_TIME]);
		List<Integer> allLinesForCurrentMeanDate = getAllLinesToComputeStandardDeviation(meanDate, solenoid);

		for (Integer lineIndex : allLinesForCurrentMeanDate) {
			if (lineIndex >= 1) {
				String[] currentLine = currentStandardDeviationLines.get(lineIndex);
				double psrValue = parseDoubleValue(currentLine, OutputFileConstants.PSR);
				double delta13Value = parseDoubleValue(currentLine, OutputFileConstants.DELTA13);
				allPSRValues.add(psrValue);
				allDelta13Values.add(delta13Value);
			}
		}
		dataMapping.put(OutputFileConstants.PSR, list2DoubleArray(allPSRValues));
		dataMapping.put(OutputFileConstants.DELTA13, list2DoubleArray(allDelta13Values));

		return dataMapping;
	}

	List<Integer> getAllLinesToComputeStandardDeviation(Date meanDate, double solenoidToEvaluate) throws ParseException {
		List<Integer> standardDeviationLines = new ArrayList<Integer>();
		int startIndex = getStartIndex(meanDate);
		if (startIndex == -1)
			System.out.println("StartIndex for " + meanDate + " is " + startIndex);
		Date currentDate = meanDate;
		int currentIndex = startIndex;
		// standardDerivationLines.add(startIndex);
		while (Math.abs(meanDate.getTime() - currentDate.getTime()) <= CommonConstants.fiveMinutes && currentIndex >= 1
				&& currentIndex < currentStandardDeviationLines.size()) {
			String[] possibleLine = currentStandardDeviationLines.get(currentIndex);
			double solenoid = parseDoubleValue(possibleLine, OutputFileConstants.SOLENOID_VALVES);
			if (solenoid == solenoidToEvaluate) {
				standardDeviationLines.add(currentIndex);
				currentDate = dateFormat.parse(possibleLine[OutputFileConstants.DATE_AND_TIME]);
			}
			currentIndex--;

		}
		return standardDeviationLines;
	}

	int getStartIndex(Date meanDate) throws ParseException {
		int startIndex = -1;
		long shortestedDistance = Long.MAX_VALUE;
		for (int i = 1; i < currentStandardDeviationLines.size(); i++) {
			String[] currentStandardDeviationLine = currentStandardDeviationLines.get(i);
			Date standardDeviationDate = dateFormat.parse(currentStandardDeviationLine[OutputFileConstants.DATE_AND_TIME]);

			long difference = Math.abs(meanDate.getTime() - standardDeviationDate.getTime());
			if (shortestedDistance > difference) {
				shortestedDistance = difference;
				startIndex = i;
			}
		}
		return startIndex;
	}
}
