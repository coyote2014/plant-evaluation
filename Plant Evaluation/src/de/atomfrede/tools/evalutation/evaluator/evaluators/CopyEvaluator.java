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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.EntryComparator;
import de.atomfrede.tools.evalutation.InputFileConstants;
import de.atomfrede.tools.evalutation.evaluator.AbstractEvaluator;
import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.util.ColumnCheckUtil;
import de.atomfrede.tools.evalutation.util.PreProcessor;

/**
 * First evaluator of every evaluation. Just processes all input files and
 * copies the content (except the header) into one new file and sorts them
 * ascending by date and time
 * 
 * This makes it easier to find changes of the solenoid valves and to process
 * date and time of each data set.
 */
public class CopyEvaluator extends AbstractEvaluator {

	private final Log log = LogFactory.getLog(CopyEvaluator.class);

	File outputFile;
	List<Double> solenoidValvesOfInterest = new ArrayList<Double>();

	public CopyEvaluator() {
		super("copy");
		this.name = "Copy Files";
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	@Override
	public boolean evaluate() {
		log.info("Copy Evaluator started");
		// setup all solenoid valves of interest
		solenoidValvesOfInterest = Options.getSolenoidValvesOfInterest();

		CSVWriter writer = null;
		try {
			// create one output file
			outputFile = new File(outputFolder, "allLaserData.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;
			// create the corresponding csv writer to write lines into the
			// output file
			writer = getCsvWriter(outputFile);

			// read all files in the input folder
			File[] allInputFiles = inputRootFolder.listFiles();
			log.debug("#Files " + allInputFiles.length);

			String[] header = null;
			List<String[]> allLines = new ArrayList<String[]>();
			for (int i = 0; i < allInputFiles.length; i++) {
				File inputFile = allInputFiles[i];
				progressBar.setValue((int) ((i * 1.0 / allInputFiles.length) * 100.0));
				if (inputFile.isFile()) {
					// read all lines and write them to the new file
					List<String[]> currentLines = readAllLinesInFile(inputFile);
					if (currentLines.get(0).length == 1) {
						// if the length of the current line is equal to 1, the
						// input files are not separated by comma
						// instead by a fixed width. Preprocess the file so it
						// is a nice comma separated file
						PreProcessor.replaceWhiteSpacesWithComma(inputFile);
						currentLines = readAllLinesInFile(inputFile);
					}
					// if there is no header use the first line
					if (header == null) {
						header = currentLines.get(0);
						ColumnCheckUtil.checkHeader(header);
					}
					// then remove it, because we don't need the header of the
					// other files anymore
					currentLines.remove(0);

					for (int j = 1; j < currentLines.size(); j++) {
						String[] currentLine = currentLines.get(j);
						double solenoidValue = parseDoubleValue(currentLine, InputFileConstants.SOLENOID_VALVE_INPUT);
						// only copy the solenoid valves of interest
						if (solenoidValvesOfInterest.contains(Double.valueOf(solenoidValue))) {
							allLines.add(currentLine);
						}
					}

				}
			}
			// sort all lines
			Collections.sort(allLines, new EntryComparator());
			// add the header as first line
			allLines.add(0, header);
			// write all lines to output file
			writer.writeAll(allLines);
			progressBar.setValue(100);

		} catch (IOException ioe) {
			log.error(ioe);
			return false;
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					log.error(e);
				}
		}

		log.info("Copy Evaluator Done.");
		return true;
	}
}
