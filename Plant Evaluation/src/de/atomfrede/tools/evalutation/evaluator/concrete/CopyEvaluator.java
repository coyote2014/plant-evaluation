/**
 *  Copyright 2011 Frederik Hahne
 *  
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

package de.atomfrede.tools.evalutation.evaluator.concrete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.EntryComparator;
import de.atomfrede.tools.evalutation.evaluator.common.AbstractEvaluator;
import de.atomfrede.tools.evalutation.helper.PreProcessor;

public class CopyEvaluator extends AbstractEvaluator {

	File outputFile;

	public CopyEvaluator() {
		super("copy");
		this.name = "Copy Files";
		// boolean done = evaluate();
		// if (done)
		// new MeanValueEvaluator(outputFile);
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
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

			List<String[]> allLines = new ArrayList<String[]>();
			for (int i = 0; i < allInputFiles.length; i++) {
				File inputFile = allInputFiles[i];
				progressBar
						.setValue((int) ((i * 1.0 / allInputFiles.length) * 100.0));
				if (inputFile.isFile()) {
					// read all lines and write them to the new file
					List<String[]> currentLines = readAllLinesInFile(inputFile);
					if (currentLines.get(0).length == 1) {
						PreProcessor.replaceWhiteSpacesWithComma(inputFile);
						currentLines = readAllLinesInFile(inputFile);
					}

					if (i != 0) {
						currentLines.remove(0);
					} else {
						allLines.add(currentLines.get(0));
					}

					for (int j = 1; j < currentLines.size(); j++) {
						String[] currentLine = currentLines.get(j);
						double solenoidValue = parseDoubleValue(currentLine,
								Constants.SOLENOID_VALVE_INPUT);

						if (solenoidValue == 1.0 || solenoidValue == 4.0
								|| solenoidValue == 2.0) {
							allLines.add(currentLine);
						}
					}

				}
			}
			String[] header = allLines.get(0);
			allLines.remove(0);
			Collections.sort(allLines, new EntryComparator());
			allLines.add(0, header);
			writer.writeAll(allLines);
			progressBar.setValue(100);

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
