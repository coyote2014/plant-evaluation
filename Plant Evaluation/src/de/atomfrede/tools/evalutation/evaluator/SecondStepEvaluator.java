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
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.WriteUtils;

public class SecondStepEvaluator extends AbstractEvaluator {

	public static int SOLENOID_VALUE = 6;
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

			List<Integer> referenceChamberLines = findAllReferenceChambers(lines);

			writer.close();
		} catch (IOException ioe) {

		}
		System.out.println("2nd Step done");
	}

	double getCO2DiffForLine(String[] line, List<String[]> allLines) {
		if (parseDoubleValue(line, SOLENOID_VALUE) != referenceChamberValue) {

		}
		return -1.0;
	}

	List<Integer> findAllReferenceChambers(List<String[]> lines) {
		List<Integer> referenceChamberLines = new ArrayList<Integer>();
		for (int i = 1; i < lines.size(); i++) {
			if (parseDoubleValue(lines.get(i), SOLENOID_VALUE) == referenceChamberValue)
				referenceChamberLines.add(i);
		}
		return referenceChamberLines;
	}

}
