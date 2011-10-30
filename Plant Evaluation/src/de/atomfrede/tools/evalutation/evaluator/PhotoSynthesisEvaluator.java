/**
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

package de.atomfrede.tools.evalutation.evaluator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.WriteUtils;

public class PhotoSynthesisEvaluator extends AbstractEvaluator {

	public static int H2O_VALUE = 5;
	public static int SOLENOID_VALUE = 6;
	public static int CO2_ABS_VALUE = 7;
	public static int TIME_VALUE = 8;
	public static int CO2_DIFF_VALUE = 9;
	public static int TEMP_VALUE = 11;

	static double MILLION = 1000000.0;
	// leaf area in m²
	static double LEAF_AREA_PLANT_ONE = 0.105823;
	static double LEAF_AREA_PLANT_TWO = 0.1224748;
	static double LEAF_AREA_PLANT_THREE = 0.1277336;
	static double LEAF_AREA_PLANT_FOUR = 0.1532759;
	static double LEAF_AREA_PLANT_FIVE = 0.1115781;
	static double LEAF_AREA_PLANT_SIX = 0.1099036;

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

	List<File> outputFiles;
	List<File> inputFiles;
	List<String[]> allLinesInCurrentFile;
	List<Integer> referenceLines;

	int currentPlant;

	public PhotoSynthesisEvaluator(List<File> inputFiles) {
		super("photosythesis");
		this.inputFiles = inputFiles;
		outputFiles = new ArrayList<File>();
		evaluate();
	}

	@Override
	public boolean evaluate() {
		try {
			currentPlant = -1;
			for (File currentDataFile : inputFiles) {
				currentPlant++;
				System.out.println("Reading File " + currentDataFile.getName());
				// assume it is ordered alphabetically
				allLinesInCurrentFile = readAllLinesInFile(currentDataFile);
				File outputFile = new File(outputFolder, "psr-0"
						+ (currentPlant) + ".csv");

				CSVWriter writer = getCsvWriter(outputFile);
				WriteUtils.writeHeader(writer);

				for (int i = 1; i < allLinesInCurrentFile.size(); i++) {

					String[] currentLine = allLinesInCurrentFile.get(i);

					referenceLines = findAllReferenceChambers(
							allLinesInCurrentFile, SOLENOID_VALUE);

					double psrForCurrentLine = computePhotoSynthesisRate(currentLine);
					writePsr(writer, currentLine, psrForCurrentLine);

				}

				writer.close();
				outputFiles.add(outputFile);

			}
		} catch (IOException ioe) {
			System.out.println("IOException " + ioe.getMessage());
		} catch (ParseException pe) {
			System.out.println("ParseException " + pe.getMessage());
		}
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
		if (parseDoubleValue(line, SOLENOID_VALUE) == 1.0)
			return 0.0;

		double solenoid = parseDoubleValue(line, SOLENOID_VALUE);
		if (solenoid == 2.0 || solenoid == 4.0 || solenoid == 8.0) {
			// compute it here
			// first find the correponsing referenceLine
			int refIndex = getReferenceLineToUse(line, allLinesInCurrentFile,
					referenceLines, TIME_VALUE);
			String[] refLine = allLinesInCurrentFile.get(refIndex);
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
			double leafArea = getLeafArea(currentPlant);
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

		// System.out.println("co2Abs " + co2Abs);
		// System.out.println("co3ref " + co2Ref);
		// System.out.println("co2Diff " + co2Diff);
		// System.out.println("h2o " + h2o);
		// System.out.println("h2oref " + h2ORef);
		// System.out.println("h2odiff " + h2oDiff);
		// System.out.println("Temp " + temperature);
		// System.out.println("Pressure " + pressure);
		// System.out.println("Flow rate " + flowRate);
		// System.out.println("leaf area " + leafArea);
		// double vm = ((VM * temperature) / pressure);
		//
		// double v = chamberVolume / vm;
		//
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

	double getLeafArea(int currentPlant) {
		switch (currentPlant) {
		case 0:
			return LEAF_AREA_PLANT_ONE;
		case 1:
			return LEAF_AREA_PLANT_TWO;
		case 2:
			return LEAF_AREA_PLANT_THREE;
		case 3:
			return LEAF_AREA_PLANT_FOUR;
		case 4:
			return LEAF_AREA_PLANT_FIVE;
		case 5:
			return LEAF_AREA_PLANT_SIX;
		default:
			return 0.0;
		}
	}

	double getH2ODiff(String[] line, String[] refLine) {
		// return (parseDoubleValue(line, H2O_VALUE) * 10000 - parseDoubleValue(
		// refLine, H2O_VALUE) * 10000) / MILLION;
		return getH2O(line) - getH2O(refLine);
	}

	double getH2O(String[] line) {
		return (parseDoubleValue(line, H2O_VALUE) * 10000) / MILLION;
	}

	double getCO2Diff(String[] line) {
		return parseDoubleValue(line, CO2_DIFF_VALUE) / MILLION;
	}

	double getCO2Abs(String[] line) {
		return parseDoubleValue(line, CO2_ABS_VALUE) / MILLION;
	}

	double getTemperature(String[] line) {
		return convertToCelvin(parseDoubleValue(line, TEMP_VALUE));
	}

	double convertToCelvin(double celsius) {
		return 273.15 + celsius;
	}

	double getChamberVolume(double height, double diameter) {
		// grundfläche * höhe = (1/4 * PI * diameter²) * height
		return (1 / 4 * Math.PI * (diameter * diameter)) * height;
	}

}
