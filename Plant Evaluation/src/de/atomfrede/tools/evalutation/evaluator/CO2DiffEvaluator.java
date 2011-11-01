/**
 * 	SecondStepEvaluator.java is part of Plant Evaluation.
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
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.WriteUtils;

public class CO2DiffEvaluator extends AbstractEvaluator {

	public static int SOLENOID_VALUE = 6;
	public static int CO2_ABS_VALUE = 7;
	public static int TIME_VALUE = 8;

	File inputFile;
	File outputFile;
	File standardDerivationInputFile;
	File standardDerivationOutputFile;

	public CO2DiffEvaluator(File inputFile, File standardDerivationInputFile) {
		super("co2diff");
		this.inputFile = inputFile;
		this.standardDerivationInputFile = standardDerivationInputFile;
		boolean done = evaluate();
		if (done)
			new Delta13Evaluator(outputFile, standardDerivationOutputFile);
	}

	@Override
	public boolean evaluate() {
		CSVWriter writer = null;
		CSVWriter standardDerivationWriter = null;
		try {
			{
				if (!inputFile.exists())
					return false;

				outputFile = new File(outputFolder, "co2diff.csv");

				outputFile.createNewFile();
				if (!outputFile.exists())
					return false;

				writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				List<String[]> lines = readAllLinesInFile(inputFile);

				List<Integer> referenceLines = findAllReferenceChambers(lines,
						SOLENOID_VALUE);

				for (int i = 1; i < lines.size(); i++) {
					String[] currentLine = lines.get(i);
					double co2Diff = parseDoubleValue(currentLine,
							CO2_ABS_VALUE)
							- getCO2DiffForLine(currentLine, lines,
									referenceLines);
					writeCO2Diff(writer, currentLine, co2Diff);
				}
			}
			System.out.println("CO2 Diff for Data Values done.");
			writer.close();
			{
				// now compute needed valus for standard derivation file
				if (!standardDerivationInputFile.exists())
					return false;

				standardDerivationOutputFile = new File(outputFolder,
						"standard-derivation-co2diff.csv");

				standardDerivationOutputFile.createNewFile();
				if (!standardDerivationOutputFile.exists())
					return false;

				standardDerivationWriter = getCsvWriter(standardDerivationOutputFile);
				WriteUtils.writeHeader(standardDerivationWriter);

				List<String[]> lines = readAllLinesInFile(standardDerivationInputFile);

				List<Integer> referenceLines = findAllReferenceChambers(lines,
						SOLENOID_VALUE);

				System.out
						.println("Reference Chambers for Standard Derivation found. Size "
								+ referenceLines.size());

				for (int i = 1; i < lines.size(); i++) {
					if (i % 1000 == 0)
						System.out.println("Writing Standard Derivation Line "
								+ i);
					String[] currentLine = lines.get(i);
					double co2Diff = parseDoubleValue(currentLine,
							CO2_ABS_VALUE)
							- getCO2DiffForLine(currentLine, lines,
									referenceLines);
					writeCO2Diff(standardDerivationWriter, currentLine, co2Diff);
				}
			}
			System.out.println("CO2 Diff for StandardDerivation Values done.");
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("CO2Diff done");
		return true;
	}

	void writeCO2Diff(CSVWriter writer, String[] currentLine, double co2Diff) {
		// first reuse the old values
		String[] newLine = new String[currentLine.length + 1];
		int i = 0;
		for (i = 0; i < currentLine.length; i++) {
			newLine[i] = currentLine[i];
		}
		newLine[i] = co2Diff + "";
		writer.writeNext(newLine);
	}

	double getCO2DiffForLine(String[] line, List<String[]> allLines,
			List<Integer> referenceLines) throws ParseException {
		if (parseDoubleValue(line, SOLENOID_VALUE) != referenceChamberValue) {
			Date date = dateFormat.parse(line[TIME_VALUE]);
			long shortestedDistance = Long.MAX_VALUE;
			int refIndex2Use = -1;
			for (Integer refLineIndex : referenceLines) {
				Date refDate = dateFormat
						.parse(allLines.get(refLineIndex)[TIME_VALUE]);
				long difference = Math.abs(date.getTime() - refDate.getTime());
				if (shortestedDistance > difference) {
					shortestedDistance = difference;
					refIndex2Use = refLineIndex;
				}
			}

			return parseDoubleValue(allLines.get(refIndex2Use), CO2_ABS_VALUE);

		}
		// TODO return the value of that reference chamber
		return parseDoubleValue(line, CO2_ABS_VALUE);
	}
}
