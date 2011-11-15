/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	ThirdStepEvaluator.java is part of Plant Evaluation.
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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.evaluator.common.SingleInputFileEvaluator;

public class Delta13Evaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(Delta13Evaluator.class);

	public Delta13Evaluator(File inputFile, File standardDerivationInputFile) {
		super("delta13", inputFile, standardDerivationInputFile);
		this.name = "Delta 13";
	}

	@Override
	public boolean evaluate() {
		CSVWriter writer = null;
		CSVWriter standardDerivationWriter = null;
		try {
			{
				// first the mean values
				if (!inputFile.exists())
					return false;

				outputFile = new File(outputFolder, "delta13.csv");

				outputFile.createNewFile();
				if (!outputFile.exists())
					return false;

				writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				List<String[]> allLines = readAllLinesInFile(inputFile);

				// List<Integer> referenceLines = findAllReferenceChambers(
				// allLines, SOLENOID_VALUE);
				allReferenceLines = findAllReferenceLines(allLines,
						Constants.SOLENOID_VALVES);

				for (int i = 1; i < allLines.size(); i++) {
					String[] currentLine = allLines.get(i);
					double solenoid = parseDoubleValue(currentLine,
							Constants.SOLENOID_VALVES);
					if (solenoid != 1.0) {
						// only for non reference lines compute the values
						String[] refLine2Use = getReferenceLineToUse(
								currentLine, allLines, allReferenceLines,
								Constants.DATE_AND_TIME);
						// System.out.println("Current Line " + i);
						// System.out.println("RefLine " + refLine2Use);
						// String[] refLine = allLines.get(refLine2Use);
						double delta13 = computeDelta13(currentLine,
								refLine2Use);
						writeDelta13Values(writer, currentLine, delta13);
					} else {
						writeDelta13Values(writer, currentLine, 0.0);
					}
					progressBar.setValue((int) (i * 1.0 / allLines.size()
							* 100.0 * 0.5));
				}
			}
			writer.close();
			log.info("Delta13 for mean values done.");
			progressBar.setValue(50);
			{
				// first the mean values
				if (!standardDerivationInputFile.exists())
					return false;

				standardDerivationOutputFile = new File(outputFolder,
						"standard-derivation-delta13.csv");

				standardDerivationOutputFile.createNewFile();
				if (!standardDerivationOutputFile.exists())
					return false;

				standardDerivationWriter = getCsvWriter(standardDerivationOutputFile);
				WriteUtils.writeHeader(standardDerivationWriter);

				List<String[]> allLines = readAllLinesInFile(standardDerivationInputFile);

				// List<Integer> referenceLines = findAllReferenceChambers(
				// allLines, SOLENOID_VALUE);

				for (int i = 1; i < allLines.size(); i++) {
					String[] currentLine = allLines.get(i);
					double solenoid = parseDoubleValue(currentLine,
							Constants.SOLENOID_VALVES);
					if (solenoid != 1.0) {
						// only for non reference lines compute the values
						String[] refLine2Use = getReferenceLineToUse(
								currentLine, allLines, allReferenceLines,
								Constants.DATE_AND_TIME);
						// String[] refLine = allLines.get(refLine2Use);
						// System.out.println("CurrentLine " + i);
						// System.out.println("RefLine2Use " + refLine2Use);
						double delta13 = computeDelta13(currentLine,
								refLine2Use);
						writeDelta13Values(standardDerivationWriter,
								currentLine, delta13);
					} else {
						writeDelta13Values(standardDerivationWriter,
								currentLine, 0.0);
					}
					progressBar.setValue((int) ((i * 1.0 / allLines.size()
							* 100.0 * 0.5) + 50.0));
				}
			}
		} catch (IOException ioe) {
			log.error("IOException " + ioe.getMessage());
			return false;
		} catch (ParseException pe) {
			log.error("ParseException " + pe.getMessage());
			return false;
		} finally {
			try {
				if (writer != null)
					writer.close();
				if (standardDerivationWriter != null)
					standardDerivationWriter.close();
			} catch (IOException ioe) {
				System.out.println("IOException when trying to close writers.");
			}
		}

		log.info("Delta13 done.");
		progressBar.setValue(100);
		return true;
	}

	void writeDelta13Values(CSVWriter writer, String[] currentLine,
			double delta13) {
		String[] newLine = new String[currentLine.length + 1];
		int i = 0;
		for (i = 0; i < currentLine.length; i++) {
			newLine[i] = currentLine[i];
		}
		newLine[i] = delta13 + "";
		writer.writeNext(newLine);
	}

	double computeDelta13(String[] currentLine, String[] refLine) {
		double co2abs = parseDoubleValue(currentLine, Constants.CO2_ABS);
		double co2absRef = parseDoubleValue(refLine, Constants.CO2_ABS);
		double delta5Minutes = parseDoubleValue(currentLine,
				Constants.MEAN_DELTA_5_MINUTES);
		double delta5MinutesRef = parseDoubleValue(refLine,
				Constants.MEAN_DELTA_5_MINUTES);

		// double a = co2abs * delta5Minutes;
		// double b = co2absRef * delta5MinutesRef;
		// double c = a - b;
		// double d = co2abs - co2absRef;
		// double delta13 = c / d;
		double delta13 = ((co2abs * delta5Minutes) - (co2absRef * delta5MinutesRef))
				/ (co2abs - co2absRef);
		return delta13;
	}
}
