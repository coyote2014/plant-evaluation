/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	PhotoSynthesisEvaluator.java is part of Plant Evaluation.
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
import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.Plant;
import de.atomfrede.tools.evalutation.WriteUtils;
import de.atomfrede.tools.evalutation.evaluator.common.MultipleInputFileEvaluator;
import de.atomfrede.tools.evalutation.main.PlantHelper;

public class PhotoSynthesisEvaluator extends MultipleInputFileEvaluator {

	private final Log log = LogFactory.getLog(PhotoSynthesisEvaluator.class);

	static double MILLION = 1000000.0;

	static double UPPER_CHAMBER = 2.0;
	static double LOWER_CHAMBER = 4.0;

	static double flowRateLowerChamber = 500.0;
	static double flowRateUpperChamber = 3000.0;

	static double VM = 8.314472;
	static double PAMP = 99.7;
	static double PRESSURE = PAMP * 1000.0;

	// in cm
	static double lowerHeight = 10.0;
	static double upperHeight = 50.0;
	// in cm
	static double upperDiameter = 25.0;
	static double lowerDiameter = 7.0;

	List<String[]> allLinesInCurrentFile;
	// List<Integer> referenceLines;

	int currentPlant;

	public PhotoSynthesisEvaluator(List<File> inputFiles,
			List<File> standardDerivationInputFiles) {
		super("photosythesis", inputFiles, standardDerivationInputFiles);
		this.name = "PSR";
	}

	@Override
	public boolean evaluate() {
		try {
			{
				currentPlant = -1;
				for (File currentDataFile : inputFiles) {

					currentPlant++;
					// assume it is ordered alphabetically
					allLinesInCurrentFile = readAllLinesInFile(currentDataFile);
					File outputFile = new File(outputFolder, "psr-0"
							+ (currentPlant) + ".csv");

					CSVWriter writer = getCsvWriter(outputFile);
					WriteUtils.writeHeader(writer);

					for (int i = 1; i < allLinesInCurrentFile.size(); i++) {

						String[] currentLine = allLinesInCurrentFile.get(i);

						// referenceLines = findAllReferenceChambers(
						// allLinesInCurrentFile, SOLENOID_VALUE);
						allReferenceLines = findAllReferenceLines(
								allLinesInCurrentFile,
								Constants.SOLENOID_VALVES);

						double psrForCurrentLine = computePhotoSynthesisRate(currentLine);
						writePsr(writer, currentLine, psrForCurrentLine);

						progressBar.setValue((int) (i * 1.0
								/ allLinesInCurrentFile.size() * 100.0 * 0.5
								* 1.0 / inputFiles.size() * 1.0));

					}

					writer.close();
					outputFiles.add(outputFile);
				}
			}
			progressBar.setValue(50);
			log.info("PSR for mean values done.");

			{
				currentPlant = -1;
				for (File currentDataFile : standardDerivationInputFiles) {
					currentPlant++;
					// assume it is ordered alphabetically
					allLinesInCurrentFile = readAllLinesInFile(currentDataFile);
					File outputFile = new File(outputFolder,
							"standard-derivation-psr-0" + (currentPlant)
									+ ".csv");

					CSVWriter writer = getCsvWriter(outputFile);
					WriteUtils.writeHeader(writer);

					for (int i = 1; i < allLinesInCurrentFile.size(); i++) {

						String[] currentLine = allLinesInCurrentFile.get(i);

						// referenceLines = findAllReferenceChambers(
						// allLinesInCurrentFile, SOLENOID_VALUE);

						double psrForCurrentLine = computePhotoSynthesisRate(currentLine);
						writePsr(writer, currentLine, psrForCurrentLine);

						progressBar
								.setValue((int) ((i * 1.0
										/ allLinesInCurrentFile.size() * 100.0
										* 0.5 * 1.0
										/ standardDerivationInputFiles.size() * 1.0) + 50.0));

					}

					writer.close();
					standardDerivationOutputFiles.add(outputFile);
				}
			}
		} catch (IOException ioe) {
			log.error(ioe);
			return false;
		} catch (ParseException pe) {
			log.error(pe);
			return false;
		}
		log.info("PSR Evaluator Done.");
		progressBar.setValue(100);
		return true;

	}

	void writePsr(CSVWriter writer, String[] currentLine, double psr) {
		// first reuse the old values
		String[] newLine = new String[currentLine.length + 1];
		int i = 0;
		for (i = 0; i < currentLine.length; i++) {
			newLine[i] = currentLine[i];
		}
		newLine[i] = psr + "";
		writer.writeNext(newLine);
	}

	double computePhotoSynthesisRate(String[] line) throws ParseException {
		if (parseDoubleValue(line, Constants.SOLENOID_VALVES) == 1.0)
			return 0.0;

		double solenoid = parseDoubleValue(line, Constants.SOLENOID_VALVES);
		if (solenoid == 2.0 || solenoid == 4.0 || solenoid == 8.0) {
			// compute it here
			// first find the correponsing referenceLine
			String[] refLine = getReferenceLineToUse(line,
					allLinesInCurrentFile, allReferenceLines,
					Constants.DATE_AND_TIME);
			// String[] refLine = allLinesInCurrentFile.get(refIndex);
			double height = 0.0;
			double diameter = 0.0;
			double flowRate = 0.0;
			if (solenoid == 2.0) {
				height = upperHeight;
				diameter = upperDiameter;
				flowRate = flowRateUpperChamber;
			} else if (solenoid == 4.0) {
				height = lowerHeight;
				diameter = lowerDiameter;
				flowRate = flowRateLowerChamber;
			} else if (solenoid == 8.0) {

			}
			double chamberVolume = getChamberVolume(height, diameter);
			double leafArea = getLeafArea(currentPlant, solenoid);
			return getPhotoSynthesisRate(getCO2Abs(line), getCO2Abs(refLine),
					getCO2Diff(line), getH2O(line), getH2O(refLine),
					getH2ODiff(line, refLine), getTemperature(line), PRESSURE,
					chamberVolume, flowRate, leafArea);
		}

		return 0.0;
	}

	// temperatur in celvin
	double getPhotoSynthesisRate(double co2Abs, double co2Ref, double co2Diff,
			double h2o, double h2ORef, double h2oDiff, double temperature,
			double pressure, double chamberVolume, double flowRate,
			double leafArea) {

		double ui = flowRate / MILLION / 60 * (pressure / (VM * temperature));

		double w0 = h2o;

		double wi = h2ORef;

		double e = ui * (w0 - wi) / leafArea * (1.0 - w0) * 1000.0;

		double b5 = co2Ref;
		double x5 = e;
		double f5 = co2Diff;
		double v5 = w0;
		double w5 = wi;
		double m5 = leafArea;
		double u5 = ui;

		double psr = (((u5 * 1000.0 / m5) * ((1.0 - w5) / (1.0 - v5)) * f5) - (x5 * b5)) * 1000.0;
		return psr;
	}

	double getLeafArea(int currentPlant, double solenoidValue) {
		// 2 is upper chamber
		// 4 is lower chamber
		// 8 doesn't matter
		Plant plant = PlantHelper.getDefaultPlantList().get(currentPlant);
		if (solenoidValue == LOWER_CHAMBER)
			return plant.getLowerLeafArea();
		else if (solenoidValue == UPPER_CHAMBER)
			return plant.getUpperLeafArea();
		return 1.0;
	}

	double getH2ODiff(String[] line, String[] refLine) {
		// return (parseDoubleValue(line, H2O_VALUE) * 10000 - parseDoubleValue(
		// refLine, H2O_VALUE) * 10000) / MILLION;
		return getH2O(line) - getH2O(refLine);
	}

	double getH2O(String[] line) {
		return (parseDoubleValue(line, Constants.MEAN_H2O) * 10000) / MILLION;
	}

	double getCO2Diff(String[] line) {
		return parseDoubleValue(line, Constants.CO2_DIFF) / MILLION;
	}

	double getCO2Abs(String[] line) {
		return parseDoubleValue(line, Constants.CO2_ABS) / MILLION;
	}

	double getTemperature(String[] line) {
		return convertToCelvin(parseDoubleValue(line, Constants.TEMPERATURE));
	}

	double convertToCelvin(double celsius) {
		return 273.15 + celsius;
	}

	double getChamberVolume(double height, double diameter) {
		// grundfläche * höhe = (1/4 * PI * diameter²) * height
		return (1 / 4 * Math.PI * (diameter * diameter)) * height;
	}

}
