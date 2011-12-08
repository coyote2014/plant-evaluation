/**
 *  Copyright 2011 Frederik Hahne
 *  
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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.evaluator.SingleInputMultipleOutputFileEvaluator;
import de.atomfrede.tools.evalutation.plant.Plant;
import de.atomfrede.tools.evalutation.plant.PlantHelper;

/**
 * Evaluator that takes one single file, containing all data, a input and
 * produces one file of output for each plant corresponding to the dates setup
 * by the user.
 * 
 * This evaluator does not add new columns or data to the files.hdr
 */
public class PlantDivider extends SingleInputMultipleOutputFileEvaluator {

	private final Log log = LogFactory.getLog(PlantDivider.class);

	List<String[]> allInputLines;

	public PlantDivider(File inputFile, File standardDerivationInputFile) {
		super("plant", inputFile, standardDerivationInputFile);
		this.name = "Plant Divider";
	}

	@Override
	public boolean evaluate() {
		try {
			{
				// first read all mean value data
				allInputLines = readAllLinesInFile(inputFile);
				// for each plant in list
				for (int i = 0; i < PlantHelper.getDefaultPlantList().size(); i++) {
					Plant currentPlant = PlantHelper.getDefaultPlantList().get(
							i);

					File outputFile = new File(outputFolder, "plant-0" + i
							+ ".csv");

					CSVWriter writer = getCsvWriter(outputFile);
					List<String[]> values;
					// collect all values between its start and enddate
					values = getAllDateLinesBetween(
							currentPlant.getStartDate(),
							currentPlant.getEndDate());
					// write the header in the current file
					WriteUtils.writeHeader(writer);
					// write all lines for the current plant in its specific
					// file
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);

					progressBar
							.setValue((int) (i * 1.0
									/ PlantHelper.getDefaultPlantList().size()
									* 100.0 * 0.5));
				}

			}
			progressBar.setValue(50);
			log.info("Dividing mean values done.");
			{
				allInputLines = readAllLinesInFile(standardDeviationInputFile);

				for (int i = 0; i < PlantHelper.getDefaultPlantList().size(); i++) {
					Plant currentPlant = PlantHelper.getDefaultPlantList().get(
							i);

					File outputFile = new File(outputFolder,
							"standard-derivation-0" + i + ".csv");

					CSVWriter writer = getCsvWriter(outputFile);
					List<String[]> values;

					values = getAllDateLinesBetween(
							currentPlant.getStartDate(),
							currentPlant.getEndDate());
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					standardDeviationOutputFiles.add(outputFile);

					progressBar
							.setValue((int) ((i * 1.0
									/ PlantHelper.getDefaultPlantList().size()
									* 100.0 * 0.5) + 50.0));
				}
			}
		} catch (IOException ioe) {
			log.error(ioe);
			return false;
		} catch (ParseException pe) {
			log.error(pe);
			return false;
		}
		log.info("Plant Divider Done");
		progressBar.setValue(100);
		return true;
	}

	/**
	 * Collects all lines within the input file that are between the given from
	 * and till date
	 * 
	 * @param fromDate
	 * @param tillDate
	 * @return
	 * @throws ParseException
	 */
	List<String[]> getAllDateLinesBetween(Date fromDate, Date tillDate)
			throws ParseException {
		List<String[]> dataBetween = new ArrayList<String[]>();
		for (int i = 1; i < allInputLines.size(); i++) {
			String[] currentLine = allInputLines.get(i);
			Date currentDate = dateFormat
					.parse(currentLine[Constants.DATE_AND_TIME]);

			if (currentDate.getTime() >= fromDate.getTime()
					&& tillDate.getTime() >= currentDate.getTime()) {
				dataBetween.add(currentLine);
			}
		}
		return dataBetween;
	}
}
