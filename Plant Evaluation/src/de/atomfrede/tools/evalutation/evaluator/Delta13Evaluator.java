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

	public Delta13Evaluator(File inputFile) {
		super("delta13");
		this.inputFile = inputFile;
		evaluate();
		new TemperatureEvaluator(outputFile);
	}

	@Override
	public void evaluate() {
		CSVWriter writer = null;
		try {
			if (!inputFile.exists())
				return;

			outputFile = new File(outputFolder, "laser-001-delta13.csv");

			outputFile.createNewFile();
			if (!outputFile.exists())
				return;

			writer = getCsvWriter(outputFile);
			WriteUtils.writeHeader(writer);

			List<String[]> allLines = readAllLinesInFile(inputFile);

			List<Integer> referenceLines = findAllReferenceChambers(allLines,
					SOLENOID_VALUE);

			for (int i = 1; i < allLines.size(); i++) {
				String[] currentLine = allLines.get(i);
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

		} catch (IOException ioe) {

		} catch (ParseException pe) {

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("Delta13 done.");
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
