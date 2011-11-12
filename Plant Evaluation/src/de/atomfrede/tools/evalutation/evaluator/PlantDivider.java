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

package de.atomfrede.tools.evalutation.evaluator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.Plant;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.main.PlantHelper;

public class PlantDivider extends AbstractEvaluator {

	File standardDerivationInputFile;
	List<File> standardDerivationOutpufiles;

	File inputFile;
	List<File> outputFiles;

	List<String[]> allInputLines;

	public PlantDivider(File inputFile, File standardDerivationInputFile) {
		super("plant");
		this.inputFile = inputFile;
		this.standardDerivationInputFile = standardDerivationInputFile;
		outputFiles = new ArrayList<File>();
		standardDerivationOutpufiles = new ArrayList<File>();
		// parseAllDates();
		boolean done = evaluate();
		if (done)
			new PhotoSynthesisEvaluator(outputFiles,
					standardDerivationOutpufiles);
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

					values = getAllDateLinesBetween(
							currentPlant.getStartDate(),
							currentPlant.getEndDate());
					WriteUtils.writeHeader(writer);
					writer.writeAll(values);
					writer.close();
					outputFiles.add(outputFile);
				}

			}
			System.out.println("Dividing mean values done.");
			{
				allInputLines = readAllLinesInFile(standardDerivationInputFile);

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
					standardDerivationOutpufiles.add(outputFile);
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
