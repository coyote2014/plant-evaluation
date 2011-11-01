/**
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

package de.atomfrede.tools.evalutation.evaluator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math.stat.StatUtils;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.WriteUtils;

public class StandardDerivationEvaluator extends AbstractEvaluator {

	static int TIME = 8;
	static int PSR = 12;
	static int delta13 = 10;

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

	}

	@Override
	public boolean evaluate() {
		currentPlant = -1;
		try {
			for (int i = 0; i < inputFiles.size(); i++) {

				currentPlant = i;

				currentMeanDataLines = readAllLinesInFile(inputFiles.get(i));
				currentStandardDerivationLines = readAllLinesInFile(standardDerivationInputFiles
						.get(i));

				File outputFile = new File(outputFolder, "psr-sd-0"
						+ (currentPlant) + ".csv");

				CSVWriter writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				// for each line compute the standard derivation
				for (int j = 1; i < currentMeanDataLines.size(); j++) {
					String[] currentLine = currentMeanDataLines.get(j);
					double[] psrValues = getAllPSRValues(currentLine);
					double psrStandardDerivation = Math.sqrt(StatUtils
							.variance(psrValues,
									parseDoubleValue(currentLine, PSR)));

					writeLineWithStandardDerivation(writer, currentLine,
							psrStandardDerivation);

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

	void writeLineWithStandardDerivation(CSVWriter writer, String[] line,
			double psrStandardDerivation) {

	}

	double[] getAllPSRValues(String[] meanDataLine) throws ParseException {
		Date meanDate = dateFormat.parse(meanDataLine[TIME]);
		List<Integer> allLinesForCurrentMeanDate = getAllLinesToComputeStandardDerivation(meanDate);
		// now we have all lines needed for computation of the standard
		// derivation
		List<Double> allPSRValues = new ArrayList<Double>();
		for (Integer lineIndex : allLinesForCurrentMeanDate) {
			String[] currentLine = currentStandardDerivationLines
					.get(lineIndex);
			double psrValue = parseDoubleValue(currentLine, PSR);
			allPSRValues.add(psrValue);
		}
		return list2DoubleArray(allPSRValues);
	}

	List<Integer> getAllLinesToComputeStandardDerivation(Date meanDate)
			throws ParseException {
		List<Integer> standardDerivationLines = new ArrayList<Integer>();
		int startIndex = getStartIndex(meanDate);

		Date currentDate = meanDate;
		int currentIndex = startIndex;
		standardDerivationLines.add(startIndex);
		while (Math.abs(meanDate.getTime() - currentDate.getTime()) <= Constants.fiveMinutes
				&& currentIndex >= 1) {
			currentIndex++;
			standardDerivationLines.add(currentIndex);

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
					.parse(currentStandardDerivationLine[TIME]);
			long difference = meanDate.getTime()
					- standardDerivationDate.getTime();
			if (difference < 0)
				return startIndex;
			if (shortestedDistance > difference) {
				shortestedDistance = difference;
			}
		}
		return startIndex;
	}
}
