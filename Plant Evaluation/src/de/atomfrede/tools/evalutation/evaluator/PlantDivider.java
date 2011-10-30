/**
 * 	PlantDivider.java is part of Plant Evaluation.
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

public class PlantDivider extends AbstractEvaluator {

	static final int TIME_VALUE = 8;

	static final int numberOfPlants = 6;
	// 22.07.11 10:59:38
	static final String PLANT_ONE_START = "26.07.11 09:20:00";
	static final String PLANT_ONE_END = "27.07.11 13:32:00";

	static final String PLANT_TWO_START = "27.07.11 19:04:00";
	static final String PLANT_TWO_END = "29.07.11 21:31:00";

	static final String PLANT_THREE_START = "29.07.11 23:30:00";
	static final String PLANT_THREE_END = "31.07.11 20:59:00";

	static final String PLANT_FOUR_START = "31.07.11 23:54:00";
	static final String PLANT_FOUR_END = "02.08.11 21:23:00";

	static final String PLANT_FIVE_START = "06.08.11 22:03:00";
	static final String PLANT_FIVE_END = "08.08.11 21:29:00";

	static final String PLANT_SIX_START = "08.08.11 09:11:00";
	static final String PLANT_SIX_END = "10.08.11 22:03:00";

	// The dates for each plant
	Date plantOneStart, plantOneEnd, plantTwoStart, plantTwoEnd,
			plantThreeStart, plantThreeEnd, plantFourStart, plantFourEnd,
			plantFiveStart, plantFiveEnd, plantSixStart, plantSixEnd;

	File inputFile;
	List<File> outputFiles;

	List<String[]> allInputLines;

	public PlantDivider(File inputFile) {
		super("plant");
		this.inputFile = inputFile;
		outputFiles = new ArrayList<File>();
		parseAllDates();
		boolean done = evaluate();
		if (done)
			new PhotoSynthesisEvaluator(outputFiles);
	}

	void parseAllDates() {
		try {
			plantOneStart = temperatureAndPlantDateFormat
					.parse(PLANT_ONE_START);
			plantOneEnd = temperatureAndPlantDateFormat.parse(PLANT_ONE_END);

			plantTwoStart = temperatureAndPlantDateFormat
					.parse(PLANT_TWO_START);
			plantTwoEnd = temperatureAndPlantDateFormat.parse(PLANT_TWO_END);

			plantThreeStart = temperatureAndPlantDateFormat
					.parse(PLANT_THREE_START);
			plantThreeEnd = temperatureAndPlantDateFormat
					.parse(PLANT_THREE_END);

			plantFourStart = temperatureAndPlantDateFormat
					.parse(PLANT_FOUR_START);
			plantFourEnd = temperatureAndPlantDateFormat.parse(PLANT_FOUR_END);

			plantFiveStart = temperatureAndPlantDateFormat
					.parse(PLANT_FIVE_START);
			plantFiveEnd = temperatureAndPlantDateFormat.parse(PLANT_FIVE_END);

			plantSixStart = temperatureAndPlantDateFormat
					.parse(PLANT_SIX_START);
			plantSixEnd = temperatureAndPlantDateFormat.parse(PLANT_SIX_END);

		} catch (ParseException pe) {
			System.out.println("ParseException " + pe.getMessage());
		}
	}

	@Override
	public boolean evaluate() {
		try {
			allInputLines = readAllLinesInFile(inputFile);
			for (int i = 0; i < numberOfPlants; i++) {
				File outputFile = new File(outputFolder, "plant-0" + i + ".csv");

				CSVWriter writer = getCsvWriter(outputFile);
				List<String[]> values;
				switch (i) {
				case 0:
					// first plant
					values = getAllDateLinesBetween(plantOneStart, plantOneEnd);
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);
					break;
				case 1:
					// second plant
					values = getAllDateLinesBetween(plantTwoStart, plantTwoEnd);
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);
					break;
				case 2:
					// third plant
					values = getAllDateLinesBetween(plantThreeStart,
							plantThreeEnd);
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);
					break;
				case 3:
					// fourth plant
					values = getAllDateLinesBetween(plantFourStart,
							plantFourEnd);
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);
					break;
				case 4:
					// fifth plant
					values = getAllDateLinesBetween(plantFiveStart,
							plantFiveEnd);
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);
					break;
				case 5:
					// last plant
					values = getAllDateLinesBetween(plantSixStart, plantSixEnd);
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);
					break;
				default:
					break;
				}

			}
		} catch (IOException ioe) {
			System.out.println("IOException " + ioe.getMessage());
			return false;
		} catch (ParseException pe) {
			System.out.println("Parexception " + pe.getMessage());
			return false;
		}
		System.out.println("Plant Divider Done");
		return true;
	}

	List<String[]> getAllDateLinesBetween(Date fromDate, Date tillDate)
			throws ParseException {
		List<String[]> dataBetween = new ArrayList<String[]>();
		for (int i = 1; i < allInputLines.size(); i++) {
			String[] currentLine = allInputLines.get(i);
			Date currentDate = dateFormat.parse(currentLine[TIME_VALUE]);

			if (currentDate.getTime() >= fromDate.getTime()
					&& tillDate.getTime() >= currentDate.getTime()) {
				dataBetween.add(currentLine);
			}
		}
		return dataBetween;
	}
}
