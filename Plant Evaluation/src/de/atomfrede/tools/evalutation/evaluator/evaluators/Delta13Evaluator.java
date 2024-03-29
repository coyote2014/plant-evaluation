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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.constants.OutputFileConstants;
import de.atomfrede.tools.evalutation.evaluator.SingleInputFileEvaluator;
import de.atomfrede.tools.evalutation.util.DialogUtil;
import de.atomfrede.tools.evalutation.util.WriteUtils;

/**
 * Computes for each line in the dataset the delta13 value and appends that
 * value as a new column to the output file.
 */
public class Delta13Evaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(Delta13Evaluator.class);

	public Delta13Evaluator(File inputFile, File standardDerivationInputFile) {
		super("delta13", inputFile, standardDerivationInputFile);
		this.name = "Delta 13";
	}

	@Override
	public boolean evaluate() throws Exception {
		CSVWriter writer = null;
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

				allReferenceLines = findAllReferenceLines(allLines, OutputFileConstants.SOLENOID_VALVES);

				for (int i = 1; i < allLines.size(); i++) {
					String[] currentLine = allLines.get(i);
					double solenoid = parseDoubleValue(currentLine, OutputFileConstants.SOLENOID_VALVES);
					if (solenoid != 1.0) {
						// only for non reference lines compute the values
						String[] refLine2Use = getReferenceLineToUse(currentLine, allLines, allReferenceLines, OutputFileConstants.DATE_AND_TIME);
						double delta13 = computeDelta13(currentLine, refLine2Use);
						writeValue(writer, currentLine, delta13);
					} else {
						writeValue(writer, currentLine, 0.0);
					}
					progressBar.setValue((int) (i * 1.0 / allLines.size() * 100.0 * 0.5));
				}
			}
			writer.close();
			log.info("Delta13 for mean values done.");
			progressBar.setValue(50);
			{
				// first the mean values
				if (!standardDeviationInputFile.exists())
					return false;

				standardDeviationOutputFile = new File(outputFolder, "standard-derivation-delta13.csv");

				standardDeviationOutputFile.createNewFile();
				if (!standardDeviationOutputFile.exists())
					return false;

				writer = getCsvWriter(standardDeviationOutputFile);
				WriteUtils.writeHeader(writer);

				List<String[]> allLines = readAllLinesInFile(standardDeviationInputFile);

				for (int i = 1; i < allLines.size(); i++) {
					String[] currentLine = allLines.get(i);
					double solenoid = parseDoubleValue(currentLine, OutputFileConstants.SOLENOID_VALVES);
					if (solenoid != 1.0) {
						// only for non reference lines compute the values
						String[] refLine2Use = getReferenceLineToUse(currentLine, allLines, allReferenceLines, OutputFileConstants.DATE_AND_TIME);
						double delta13 = computeDelta13(currentLine, refLine2Use);
						writeValue(writer, currentLine, delta13);
					} else {
						writeValue(writer, currentLine, 0.0);
					}
					progressBar.setValue((int) ((i * 1.0 / allLines.size() * 100.0 * 0.5) + 50.0));
				}
			}
		} catch (IOException ioe) {
			log.error("IOException " + ioe.getMessage());
			DialogUtil.getInstance().showError(ioe);
			return false;
		} catch (ParseException pe) {
			log.error("ParseException " + pe.getMessage());
			DialogUtil.getInstance().showError(pe);
			return false;
		} catch (Exception e) {
			log.error(e);
			DialogUtil.getInstance().showError(e);
			return false;
		} finally {
			if (writer != null)
				writer.close();
		}

		log.info("Delta13 done.");
		progressBar.setValue(100);
		return true;
	}

	/**
	 * Computes the Delta-13 Values for the currentLine. <br>
	 * Delta13 is defined as <br>
	 * <br>
	 * ((co2Abs * delta5minutes) - (co2Abs-ref * delta5minutes-ref)) / (co2abs -
	 * co2abs-ref)
	 * 
	 * @param currentLine
	 * @param refLine
	 * @return
	 */
	double computeDelta13(String[] currentLine, String[] refLine) {
		double co2abs = parseDoubleValue(currentLine, OutputFileConstants.CO2_ABSOLUTE);
		double co2absRef = parseDoubleValue(refLine, OutputFileConstants.CO2_ABSOLUTE);
		double delta5Minutes = parseDoubleValue(currentLine, OutputFileConstants.MEAN_DELTA_5_MINUTES);
		double delta5MinutesRef = parseDoubleValue(refLine, OutputFileConstants.MEAN_DELTA_5_MINUTES);

		double delta13 = ((co2abs * delta5Minutes) - (co2absRef * delta5MinutesRef)) / (co2abs - co2absRef);
		return delta13;
	}
}
