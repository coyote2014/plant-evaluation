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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.WriteUtils;

public class SecondStepEvaluator extends AbstractEvaluator {

	public static int SOLENOID_VALUE = 6;
	public static int CO2_ABS_VALUE = 7;
	public static int TIME_VALUE = 8;

	public SecondStepEvaluator() {

	}

	@Override
	public void evaluate() {
		try {
			File inputFile = new File("second/laser-001-second.csv");
			if (!inputFile.exists())
				return;

			File outputFile = new File("third/laser-001-third.csv");

			outputFile.createNewFile();
			if (!outputFile.exists())
				return;

			CSVWriter writer = getCsvWriter(outputFile);
			WriteUtils.writeHeader(writer);

			List<String[]> lines = readAllLinesInFile(inputFile);

			List<Integer> referenceLines = findAllReferenceChambers(lines);

			for (int i = 1; i < lines.size(); i++) {
				String[] currentLine = lines.get(i);
				double co2Diff = parseDoubleValue(currentLine, CO2_ABS_VALUE)
						- getCO2DiffForLine(currentLine, lines, referenceLines);
				System.out.println("CO2 Diff = " + co2Diff);
			}

			writer.close();
		} catch (IOException ioe) {

		} catch (ParseException pe) {

		}
		System.out.println("2nd Step done");
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
				long difference = date.getTime() - refDate.getTime();
				if (difference >= 0L && shortestedDistance >= difference) {
					shortestedDistance = difference;
					refIndex2Use = refLineIndex;
				}
			}
			if (refIndex2Use == -1) {
				for (Integer refLineIndex : referenceLines) {
					Date refDate = dateFormat
							.parse(allLines.get(refLineIndex)[TIME_VALUE]);
					long difference = date.getTime() - refDate.getTime();
					if (shortestedDistance >= difference) {
						shortestedDistance = difference;
						refIndex2Use = refLineIndex;
					}
				}
			}
			return parseDoubleValue(allLines.get(refIndex2Use), CO2_ABS_VALUE);

		}
		return -1.0;
	}

	// double getNearestReferenceChamberValue(Date date, List<>) {
	// return 0.0;
	// }

	List<Integer> findAllReferenceChambers(List<String[]> lines) {
		List<Integer> referenceChamberLines = new ArrayList<Integer>();
		for (int i = 1; i < lines.size(); i++) {
			if (parseDoubleValue(lines.get(i), SOLENOID_VALUE) == referenceChamberValue)
				referenceChamberLines.add(i);
		}
		return referenceChamberLines;
	}

}
