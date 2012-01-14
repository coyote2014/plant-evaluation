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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.constants.OutputFileConstants;
import de.atomfrede.tools.evalutation.evaluator.MultipleInputFileEvaluator;
import de.atomfrede.tools.evalutation.plant.Plant;
import de.atomfrede.tools.evalutation.plant.PlantHelper;
import de.atomfrede.tools.evalutation.util.DialogUtil;
import de.atomfrede.tools.evalutation.util.WriteUtils;

public class PhotoSynthesisEvaluator extends MultipleInputFileEvaluator {

	private final Log log = LogFactory.getLog(PhotoSynthesisEvaluator.class);

	static double MILLION = 1000000.0;

	static double UPPER_CHAMBER = 2.0;
	static double LOWER_CHAMBER = 4.0;

	static double flowRateLowerChamber = 100.0;
	static double flowRateUpperChamber = 3000.0;

	static double VM = 8.314472;
	@Deprecated
	static double PAMP = 99.7;
	@Deprecated
	static double PRESSURE = PAMP * 1000.0;

	// in cm
	static double lowerHeight = 10.0;
	static double upperHeight = 50.0;
	// in cm
	static double upperDiameter = 25.0;
	static double lowerDiameter = 7.0;

	List<String[]> allLinesInCurrentFile;

	int currentPlant;
	Plant plant;

	public PhotoSynthesisEvaluator(List<File> inputFiles, List<File> standardDerivationInputFiles) {
		super("photosythesis", inputFiles, standardDerivationInputFiles);
		this.name = "PSR";
	}

	@Override
	public boolean evaluate() throws Exception {
		CSVWriter writer = null;
		try {
			{
				currentPlant = -1;
				for (File currentDataFile : inputFiles) {

					currentPlant++;

					plant = PlantHelper.getDefaultPlantList().get(currentPlant);
					// assume it is ordered alphabetically
					allLinesInCurrentFile = readAllLinesInFile(currentDataFile);
					File outputFile = new File(outputFolder, "psr-0" + (currentPlant) + ".csv");

					writer = getCsvWriter(outputFile);
					WriteUtils.writeHeader(writer);

					for (int i = 1; i < allLinesInCurrentFile.size(); i++) {

						String[] currentLine = allLinesInCurrentFile.get(i);

						allReferenceLines = findAllReferenceLines(allLinesInCurrentFile, OutputFileConstants.SOLENOID_VALVES);

						double psrForCurrentLine = computePhotoSynthesisRate(currentLine);
						writeValue(writer, currentLine, psrForCurrentLine);

						progressBar.setValue((int) (i * 1.0 / allLinesInCurrentFile.size() * 100.0 * 0.5 * 1.0 / inputFiles.size() * 1.0));

					}

					outputFiles.add(outputFile);
				}
			}
			progressBar.setValue(50);
			log.info("PSR for mean values done.");

			{
				currentPlant = -1;
				for (File currentDataFile : standardDeviationInputFiles) {
					currentPlant++;
					// assume it is ordered alphabetically
					allLinesInCurrentFile = readAllLinesInFile(currentDataFile);
					File outputFile = new File(outputFolder, "standard-derivation-psr-0" + (currentPlant) + ".csv");

					writer = getCsvWriter(outputFile);
					WriteUtils.writeHeader(writer);

					for (int i = 1; i < allLinesInCurrentFile.size(); i++) {

						String[] currentLine = allLinesInCurrentFile.get(i);

						double psrForCurrentLine = computePhotoSynthesisRate(currentLine);
						writeValue(writer, currentLine, psrForCurrentLine);

						progressBar
								.setValue((int) ((i * 1.0 / allLinesInCurrentFile.size() * 100.0 * 0.5 * 1.0 / standardDeviationInputFiles.size() * 1.0) + 50.0));

					}

					standardDeviationOutputFiles.add(outputFile);
				}
			}
		} catch (IOException ioe) {
			log.error(ioe);
			DialogUtil.getInstance().showError(ioe);
			return false;
		} catch (ParseException pe) {
			log.error(pe);
			DialogUtil.getInstance().showError(pe);
			return false;
		} catch (Exception e) {
			log.error(e);
			DialogUtil.getInstance().showError(e);
			return false;
		} finally {
			if (writer != null)
				writer.close();
		}
		log.info("PSR Evaluator Done.");
		progressBar.setValue(100);
		return true;

	}

	@Deprecated
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
		if (parseDoubleValue(line, OutputFileConstants.SOLENOID_VALVES) == 1.0)
			return 0.0;

		double solenoid = parseDoubleValue(line, OutputFileConstants.SOLENOID_VALVES);
		if (solenoid == 2.0 || solenoid == 4.0 || solenoid == 8.0) {
			Date currentDate = dateFormat.parse(line[OutputFileConstants.DATE_AND_TIME]);

			// first find the corresponding referenceLine
			String[] refLine = getReferenceLineToUse(line, allLinesInCurrentFile, allReferenceLines, OutputFileConstants.DATE_AND_TIME);
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
			// select the pressure to use according to the current date
			// first use the pressure for the specific DAY in that date
			double pressure = getPressure(plant, currentDate);

			return getPhotoSynthesisRate(getCO2Abs(line), getCO2Abs(refLine), getCO2Diff(line), getH2O(line), getH2O(refLine), getH2ODiff(line, refLine),
					getTemperature(line), pressure, chamberVolume, flowRate, leafArea);
		}

		return 0.0;
	}

	/**
	 * Gets the pressure for the current date and the given plant.
	 * 
	 * We just assume that the pressure is during the complete start day
	 * constants as well as for the complete end day.
	 * 
	 * Therefore when the given date is the same day like the start date we
	 * return the start pressure, otherwise the end pressure.
	 * 
	 * @param currentPlant
	 * @param date
	 * @return
	 */
	double getPressure(Plant currentPlant, Date date) {

		Date endDate = currentPlant.getEndDate();
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		// get the day of the month at the end of measurement
		int endDateDay = endDateCalendar.get(Calendar.DAY_OF_MONTH);

		Calendar currentDateCalendar = Calendar.getInstance();
		currentDateCalendar.setTime(date);
		// get the current day of the month
		int currentDateDay = currentDateCalendar.get(Calendar.DAY_OF_MONTH);

		// if both end day and current day are equal we have reached the end day
		// and therefore return the pressure at the end day
		if (endDateDay == currentDateDay)
			return plant.getPressureAtEndDay() * 100.0;
		else if (endDateDay != currentDateDay)
			// otherwise return the pressure at start day
			return plant.getPressureAtStartDay() * 100.0;

		return PRESSURE;
	}

	// temperatur in celvin
	/**
	 * Computes the PSR corresponding to the formula given in the popular excel
	 * sheet.
	 * 
	 * @param co2Abs
	 * @param co2Ref
	 * @param co2Diff
	 * @param h2o
	 * @param h2ORef
	 * @param h2oDiff
	 * @param temperature
	 *            in celvin
	 * @param pressure
	 * @param chamberVolume
	 * @param flowRate
	 * @param leafArea
	 * @return
	 */
	double getPhotoSynthesisRate(double co2Abs, double co2Ref, double co2Diff, double h2o, double h2ORef, double h2oDiff, double temperature, double pressure,
			double chamberVolume, double flowRate, double leafArea) {

		double ui = flowRate / MILLION / 60 * (pressure / (VM * temperature));

		double w0 = h2o;

		double wi = h2ORef;

		double e = ui * (w0 - wi) / leafArea * (1.0 - w0) * 1000.0;
		// according to the excel column names
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

	/**
	 * Returns the leaf area of the current plant corresponding to the given
	 * solenoid valve
	 * 
	 * @param currentPlant
	 * @param solenoidValve
	 * @return
	 */
	double getLeafArea(int currentPlant, double solenoidValve) {
		// 2 is upper chamber
		// 4 is lower chamber
		// 8 doesn't matter
		Plant plant = PlantHelper.getDefaultPlantList().get(currentPlant);
		if (solenoidValve == LOWER_CHAMBER)
			return plant.getLowerLeafArea();
		else if (solenoidValve == UPPER_CHAMBER)
			return plant.getUpperLeafArea();
		return 1.0;
	}

	/**
	 * Returns the difference between the H<sub>2</sub>O value and the
	 * H<sub>2</sub>O reference value.
	 * 
	 * @param line
	 * @param refLine
	 * @return
	 */
	double getH2ODiff(String[] line, String[] refLine) {
		return getH2O(line) - getH2O(refLine);
	}

	double getH2O(String[] line) {
		return (parseDoubleValue(line, OutputFileConstants.MEAN_H2O) * 10000) / MILLION;
	}

	double getCO2Diff(String[] line) {
		return parseDoubleValue(line, OutputFileConstants.CO2_DIFF) / MILLION;
	}

	double getCO2Abs(String[] line) {
		return parseDoubleValue(line, OutputFileConstants.CO2_ABSOLUTE) / MILLION;
	}

	double getTemperature(String[] line) {
		return convertToCelvin(parseDoubleValue(line, OutputFileConstants.TEMPERATURE));
	}

	/**
	 * Converts a temperature in °Celsius into °Celvin,
	 * 
	 * @param celsius
	 * @return
	 */
	double convertToCelvin(double celsius) {
		return 273.15 + celsius;
	}

	double getChamberVolume(double height, double diameter) {
		// grundfläche * höhe = (1/4 * PI * diameter²) * height
		return (1 / 4 * Math.PI * (diameter * diameter)) * height;
	}

}
