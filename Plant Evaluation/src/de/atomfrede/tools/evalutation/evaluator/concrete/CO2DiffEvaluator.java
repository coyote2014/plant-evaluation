/**
 *  Copyright 2011 Frederik Hahne
 *  
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

package de.atomfrede.tools.evalutation.evaluator.concrete;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.evaluator.SingleInputFileEvaluator;

/**
 * Appends the CO2 Difference as a new column to the given input file. One file
 * is produces as output.
 */
public class CO2DiffEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(CO2DiffEvaluator.class);

	public CO2DiffEvaluator(File inputFile, File standardDerivationInputFile) {
		super("co2diff", inputFile, standardDerivationInputFile);
		this.name = "CO2 Diff";
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

				allReferenceLines = findAllReferenceLines(lines,
						Constants.SOLENOID_VALVES);

				for (int i = 1; i < lines.size(); i++) {
					String[] currentLine = lines.get(i);
					double co2Diff = parseDoubleValue(currentLine,
							Constants.CO2_ABS)
							- getCO2DiffForLine(currentLine, lines,
									allReferenceLines);
					writeCO2Diff(writer, currentLine, co2Diff);

					progressBar
							.setValue((int) (i * 1.0 / lines.size() * 100.0 * 0.5));
				}
			}
			log.info("CO2 Diff for Data Values done.");
			progressBar.setValue(50);
			writer.close();
			{
				// now compute needed valus for standard derivation file
				if (!standardDeviationInputFile.exists())
					return false;

				standardDeviationOutputFile = new File(outputFolder,
						"standard-derivation-co2diff.csv");

				standardDeviationOutputFile.createNewFile();
				if (!standardDeviationOutputFile.exists())
					return false;

				standardDerivationWriter = getCsvWriter(standardDeviationOutputFile);
				WriteUtils.writeHeader(standardDerivationWriter);

				List<String[]> lines = readAllLinesInFile(standardDeviationInputFile);

				for (int i = 1; i < lines.size(); i++) {
					// if (i % 1000 == 0)
					// System.out.println("Writing Standard Derivation Line "
					// + i);
					String[] currentLine = lines.get(i);
					double co2Diff = parseDoubleValue(currentLine,
							Constants.CO2_ABS)
							- getCO2DiffForLine(currentLine, lines,
									allReferenceLines);
					writeCO2Diff(standardDerivationWriter, currentLine, co2Diff);
					progressBar
							.setValue((int) ((i * 1.0 / lines.size() * 100.0 * 0.5) + 50.0));
				}
			}
			log.info("CO2 Diff for StandardDerivation Values done.");
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("CO2Diff done");
		progressBar.setValue(100);
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
			List<String[]> referenceLines) throws ParseException {
		double co2Diff = 0.0;
		if (parseDoubleValue(line, Constants.SOLENOID_VALVES) != referenceChamberValue) {
			Date date = dateFormat.parse(line[Constants.DATE_AND_TIME]);
			long shortestedDistance = Long.MAX_VALUE;
			for (String[] refLineIndex : referenceLines) {
				Date refDate = dateFormat
						.parse(refLineIndex[Constants.DATE_AND_TIME]);
				long rawDifference = Math.abs(date.getTime()
						- refDate.getTime());
				if (rawDifference < shortestedDistance) {
					co2Diff = parseDoubleValue(refLineIndex, Constants.CO2_ABS);
					shortestedDistance = rawDifference;
				}
			}
			return co2Diff;
		}
		// TODO return the value of that reference chamber
		return parseDoubleValue(line, Constants.CO2_ABS);
	}
}
