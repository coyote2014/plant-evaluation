/**
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

package de.atomfrede.tools.evalutation.evaluator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.WriteUtils;

public class TemperatureEvaluator extends AbstractEvaluator {

	// laser data input file
	static final int TIME_VALUE = 8;
	// temperature input file
	static final int DATE_TIME = 1;
	static final int TEMPERATURE = 2;

	File standardDerivationInputFile;
	File standardDerivationOutputFile;
	File dataInputFile;
	File temperatureInputFile;
	File outputFile;

	List<String[]> temperatureDataLines;

	public TemperatureEvaluator(File dataInputFile,
			File standardDerivationInputFile) {
		super("temperature");
		this.dataInputFile = dataInputFile;
		this.standardDerivationInputFile = standardDerivationInputFile;
		boolean done = evaluate();
		if (done)
			new PlantDivider(outputFile, standardDerivationOutputFile);
	}

	@Override
	public boolean evaluate() {
		CSVWriter writer = null;
		CSVWriter standardDerivationWriter = null;
		try {
			temperatureInputFile = new File(inputRootFolder, "/temp/temp.csv");
			if (!temperatureInputFile.exists())
				return false;

			temperatureDataLines = readAllLinesInFile(temperatureInputFile);

			{
				outputFile = new File(outputFolder, "mean-temperature.csv");

				outputFile.createNewFile();
				if (!outputFile.exists())
					return false;

				writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				List<String[]> allDataLines = readAllLinesInFile(dataInputFile);

				for (int i = 1; i < allDataLines.size(); i++) {
					String[] currentLine = allDataLines.get(i);
					double temperature = findTemperatureForLine(currentLine);
					writeTemperature(writer, currentLine, temperature);

				}
			}
			writer.close();
			System.out.println("Writing Temperature for mean data done.");
			{
				standardDerivationOutputFile = new File(outputFolder,
						"standard-derivation-temperature.csv");

				standardDerivationOutputFile.createNewFile();
				if (!standardDerivationOutputFile.exists())
					return false;

				standardDerivationWriter = getCsvWriter(standardDerivationOutputFile);
				WriteUtils.writeHeader(standardDerivationWriter);

				List<String[]> allDataLines = readAllLinesInFile(standardDerivationInputFile);

				for (int i = 1; i < allDataLines.size(); i++) {
					String[] currentLine = allDataLines.get(i);
					double temperature = findTemperatureForLine(currentLine);
					writeTemperature(standardDerivationWriter, currentLine,
							temperature);

				}
			}
		} catch (IOException ioe) {
			System.out.println("IOException " + ioe.getMessage());
			return false;
		} catch (ParseException pe) {
			System.out.println("ParseException " + pe.getMessage());
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

		System.out.println("Temperature done.");
		return true;
	}

	void writeTemperature(CSVWriter writer, String[] currentLine,
			double temperature) {
		String[] newLine = new String[currentLine.length + 1];
		int i = 0;
		for (i = 0; i < currentLine.length; i++) {
			newLine[i] = currentLine[i];
		}
		newLine[i] = temperature + "";
		writer.writeNext(newLine);
	}

	double findTemperatureForLine(String[] currentLine) throws ParseException {
		// first parse the date from the laser data input file
		Date dateOfLaser = dateFormat.parse(currentLine[TIME_VALUE]);

		double temperature = 0.0;
		long shortestedDistance = Long.MAX_VALUE;
		for (int i = 2; i < temperatureDataLines.size(); i++) {
			String[] currentTemperatureLine = temperatureDataLines.get(i);
			Date temperatureDate = temperatureAndPlantDateFormat
					.parse(currentTemperatureLine[DATE_TIME]);
			long difference = Math.abs(dateOfLaser.getTime()
					- temperatureDate.getTime());

			if (shortestedDistance > difference) {
				shortestedDistance = difference;
				temperature = parseDoubleValue(currentTemperatureLine,
						TEMPERATURE);
			}
		}
		return temperature;
	}
}
