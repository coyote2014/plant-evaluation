/**
 * 	CopyEvaluator.java is part of Plant Evaluation.
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
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;

public class CopyEvaluator extends AbstractEvaluator {

	File outputFile;

	public CopyEvaluator() {
		super("copy");
		boolean done = evaluate();
		if (done)
			new MeanValueEvaluator(outputFile);
	}

	@Override
	public boolean evaluate() {
		CSVWriter writer = null;
		try {
			outputFile = new File(outputFolder, "allLaserData.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;

			writer = getCsvWriter(outputFile);
			// WriteUtils.writeHeader(writer);

			File[] allInputFiles = inputRootFolder.listFiles();
			System.out.println("#Files " + allInputFiles.length);
			for (int i = 0; i < allInputFiles.length; i++) {
				File inputFile = allInputFiles[i];

				if (inputFile.isFile()) {
					// read all lines and write them to the new file
					List<String[]> allLines = readAllLinesInFile(inputFile);
					if (i == 0) {
						writer.writeNext(allLines.get(i));
						allLines.remove(0);
					}
					if (i != 0)
						// remove the header
						allLines.remove(0);
					for (String[] line : allLines) {
						double solenoidValue = parseDoubleValue(line,
								Constants.solenoidValue);
						if (solenoidValue == 1.0 || solenoidValue == 4.0
								|| solenoidValue == 2.0)
							writer.writeNext(line);

					}
					// writer.writeAll(allLines);
				}
			}

		} catch (IOException ioe) {
			System.out.println("IOException " + ioe.getMessage());
			return false;
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		System.out.println("Copy Evaluator Done.");
		return true;
	}

}