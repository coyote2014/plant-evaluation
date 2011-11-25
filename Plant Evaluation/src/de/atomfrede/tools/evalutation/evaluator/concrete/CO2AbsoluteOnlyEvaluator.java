/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	CO2AbsoluteOnlyEvaluator.java is part of Plant Evaluation.
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
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.evaluator.common.SingleInputFileEvaluator;

public class CO2AbsoluteOnlyEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(CO2AbsoluteOnlyEvaluator.class);

	public CO2AbsoluteOnlyEvaluator(File inputFile) {
		super("CO2-Absolute-Only", inputFile, null);
		this.name = "CO2 Absolute Values";
	}

	@Override
	public boolean evaluate() throws Exception {
		CSVWriter writer = null;
		try {
			outputFile = new File(outputFolder, "co2-absolute-only.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;

			writer = getCsvWriter(outputFile);

			List<String[]> lines = readAllLinesInFile(inputFile);
			log.trace("done reading all input files.");
			writeHeader(writer, lines.get(0));

			for (int i = 1; i < lines.size(); i++) {
				String[] currentLine = lines.get(i);
				double _12CO2_dry = parseDoubleValue(currentLine,
						Constants._12CO2_DRY);
				double _13Co2_dry = parseDoubleValue(currentLine,
						Constants._13CO2_DRY);

				double co2AbsoluteValue = Math.abs(_12CO2_dry + _13Co2_dry);

				Date date2Write = dateFormat.parse(currentLine[Constants.DATE]
						+ " " + currentLine[Constants.TIME]);

				appendDateAndCO2AbsoluteValue(writer, currentLine, date2Write,
						co2AbsoluteValue);

				progressBar.setValue((int) ((i * 1.0 / lines.size()) * 100.0));
			}

			progressBar.setValue(100);
		} catch (IOException ioe) {
			log.error(ioe);
			return false;
		} finally {
			writer.close();
		}
		log.info("CO2 Absolute Only Evaluator Done");
		return true;
	}

	private void appendDateAndCO2AbsoluteValue(CSVWriter writer, String[] line,
			Date date2Write, double co2Absolute) {
		String[] newLine = new String[line.length + 2];
		int i = 0;
		for (i = 0; i < line.length; i++) {
			newLine[i] = line[i];
		}
		newLine[i] = dateFormat.format(date2Write);
		newLine[i + 1] = co2Absolute + "";

		writer.writeNext(newLine);
	}

	private void writeHeader(CSVWriter writer, String[] line) {
		String[] newLine = new String[line.length + 2];
		int i = 0;
		for (i = 0; i < line.length; i++) {
			newLine[i] = line[i];
		}
		newLine[i] = "Zeit";
		newLine[i + 1] = "CO2 Absolute";

		writer.writeNext(newLine);

	}
}
