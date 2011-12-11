/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	TemperatureEvaluator.java is part of Plant Evaluation.
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
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.evaluator.SingleInputFileEvaluator;

public class TemperatureEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(TemperatureEvaluator.class);

	/**
	 * Column number for date/time in the temperature file of the hobo
	 */
	static final int DATE_TIME_TEMPERATURE = 1;
	/**
	 * Column number of the temperature
	 */
	static final int TEMPERATURE = 2;

	File temperatureInputFile;

	List<String[]> temperatureDataLines;

	public TemperatureEvaluator(File dataInputFile, File standardDerivationInputFile) {
		super("temperature", dataInputFile, standardDerivationInputFile);
		this.name = "Temperature";
	}

	@Override
	public boolean evaluate() {
		CSVWriter writer = null;
		CSVWriter standardDerivationWriter = null;
		try {
			temperatureInputFile = new File(inputRootFolder, "/temp/temp.csv");
			if (!temperatureInputFile.exists())
				return false;

			// read all lines in hobo's temperature file
			temperatureDataLines = readAllLinesInFile(temperatureInputFile, ';');

			{
				// read the temperature for the mean value file
				outputFile = new File(outputFolder, "mean-temperature.csv");

				outputFile.createNewFile();
				if (!outputFile.exists())
					return false;

				writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				List<String[]> allDataLines = readAllLinesInFile(inputFile);

				for (int i = 1; i < allDataLines.size(); i++) {
					String[] currentLine = allDataLines.get(i);
					double temperature = findTemperatureForLine(currentLine);
					writeTemperature(writer, currentLine, temperature);
					progressBar.setValue((int) (i * 1.0 / allDataLines.size() * 100.0 * 0.5));

				}
			}
			writer.close();
			progressBar.setValue(50);
			log.info("Writing Temperature for mean data done.");
			{
				// read the temperature for the standard deviation file
				standardDeviationOutputFile = new File(outputFolder, "standard-derivation-temperature.csv");

				standardDeviationOutputFile.createNewFile();
				if (!standardDeviationOutputFile.exists())
					return false;

				standardDerivationWriter = getCsvWriter(standardDeviationOutputFile);
				WriteUtils.writeHeader(standardDerivationWriter);

				List<String[]> allDataLines = readAllLinesInFile(standardDeviationInputFile);

				for (int i = 1; i < allDataLines.size(); i++) {
					String[] currentLine = allDataLines.get(i);
					double temperature = findTemperatureForLine(currentLine);
					writeTemperature(standardDerivationWriter, currentLine, temperature);
					progressBar.setValue((int) ((i * 1.0 / allDataLines.size() * 100.0 * 0.5) + 50.0));

				}
			}
		} catch (IOException ioe) {
			log.error(ioe);
			return false;
		} catch (ParseException pe) {
			log.error(pe);
			return false;
		} finally {
			try {
				if (writer != null)
					writer.close();
				if (standardDerivationWriter != null)
					standardDerivationWriter.close();
			} catch (IOException ioe) {
				System.out.println("IOException when trying to close writers.");
			}
		}

		log.info("Temperature done.");
		progressBar.setValue(100);
		return true;
	}

	void writeTemperature(CSVWriter writer, String[] currentLine, double temperature) {
		String[] newLine = new String[currentLine.length + 1];
		int i = 0;
		for (i = 0; i < currentLine.length; i++) {
			newLine[i] = currentLine[i];
		}
		newLine[i] = temperature + "";
		writer.writeNext(newLine);
	}

	/**
	 * Finds that line in the temperature data that is the nearest (in time)
	 * 
	 * @param currentLine
	 * @return
	 * @throws ParseException
	 */
	double findTemperatureForLine(String[] currentLine) throws ParseException {
		// first parse the date from the laser data input file
		Date dateOfLaser = dateFormat.parse(currentLine[Constants.DATE_AND_TIME]);

		double temperature = 0.0;
		// start with the longst distance
		long shortestedDistance = Long.MAX_VALUE;
		for (int i = 2; i < temperatureDataLines.size(); i++) {
			String[] currentTemperatureLine = temperatureDataLines.get(i);
			Date temperatureDate = temperatureAndPlantDateFormat.parse(currentTemperatureLine[DATE_TIME_TEMPERATURE]);
			// compute difference between date of temperature and the date in
			// the given dataset line
			long difference = Math.abs(dateOfLaser.getTime() - temperatureDate.getTime());
			// if the new difference is smaller than the current shortest
			// distance, this becomes the new shortest distance
			if (shortestedDistance > difference) {
				shortestedDistance = difference;
				// the temperature looking for is always the temperature with
				// the shortest distance
				temperature = parseDoubleValue(currentTemperatureLine, TEMPERATURE);
			}
		}
		return temperature;
	}
}
