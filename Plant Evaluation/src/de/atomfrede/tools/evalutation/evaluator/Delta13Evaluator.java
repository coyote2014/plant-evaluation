/**
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

package de.atomfrede.tools.evalutation.evaluator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.WriteUtils;

public class Delta13Evaluator extends AbstractEvaluator {

	public static int DELTA_FIVE_MINUTES = 4;
	public static int SOLENOID_VALUE = 6;
	public static int CO2_ABS_VALUE = 7;
	public static int TIME_VALUE = 8;

	File inputFile;
	File outputFile;

	File standardDerivationInputFile;
	File standardDerivationOutputFile;

	public Delta13Evaluator(File inputFile, File standardDerivationInputFile) {
		super("delta13");
		this.inputFile = inputFile;
		this.standardDerivationInputFile = standardDerivationInputFile;
		boolean done = evaluate();
		if (done)
			new TemperatureEvaluator(outputFile, standardDerivationOutputFile);
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

				List<Integer> referenceLines = findAllReferenceChambers(
						allLines, SOLENOID_VALUE);

				for (int i = 1; i < allLines.size(); i++) {
					String[] currentLine = allLines.get(i);
					if (i == 33)
						System.out.println("Before value");
					if (!referenceLines.contains(Integer.valueOf(i))) {
						// only for non reference lines compute the values
						int refLine2Use = getReferenceLineToUse(currentLine,
								allLines, referenceLines, TIME_VALUE);
						String[] refLine = allLines.get(refLine2Use);
						double delta13 = computeDelta13(currentLine, refLine);
						writeDelta13Values(writer, currentLine, delta13);
					} else {
						writeDelta13Values(writer, currentLine, 0.0);
					}
				}
			}
			writer.close();
			System.out.println("Delta13 for mean values done.");
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

				List<Integer> referenceLines = findAllReferenceChambers(
						allLines, SOLENOID_VALUE);
				System.out.println("Delta 13: Reference Lines "
						+ referenceLines.size());
				for (int i = 1; i < allLines.size(); i++) {
					String[] currentLine = allLines.get(i);
					if (!referenceLines.contains(Integer.valueOf(i))) {
						// only for non reference lines compute the values
						int refLine2Use = getReferenceLineToUse(currentLine,
								allLines, referenceLines, TIME_VALUE);
						String[] refLine = allLines.get(refLine2Use);
						double delta13 = computeDelta13(currentLine, refLine);
						writeDelta13Values(standardDerivationWriter,
								currentLine, delta13);
					} else {
						writeDelta13Values(standardDerivationWriter,
								currentLine, 0.0);
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOException " + ioe.getMessage());
			return false;
		} catch (ParseException pe) {
			System.out.println("ParseException " + pe.getMessage());
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

		System.out.println("Delta13 done.");
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
		double co2abs = parseDoubleValue(currentLine, CO2_ABS_VALUE);
		double co2absRef = parseDoubleValue(refLine, CO2_ABS_VALUE);
		double delta5Minutes = parseDoubleValue(currentLine, DELTA_FIVE_MINUTES);
		double delta5MinutesRef = parseDoubleValue(refLine, DELTA_FIVE_MINUTES);

		double delta13 = ((co2abs * delta5Minutes) - (co2absRef * delta5MinutesRef))
				/ (co2abs - co2absRef);
		return delta13;
	}
}
