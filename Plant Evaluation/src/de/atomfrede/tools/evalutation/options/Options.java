/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	Options.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.util.FileConfiguration;
import de.atomfrede.tools.evalutation.util.JarUtil;

/**
 * Simple static class that holds all options only for the current session.
 * 
 */
@SuppressWarnings("unchecked")
public class Options {

	private static final Log log = LogFactory.getLog(Options.class);

	// standard input and output folders
	static File inputFolder;
	static File outputFolder;//$NON-NLS-1$
	static File temperatureInputFolder = new File(inputFolder, "temp"); //$NON-NLS-1$

	// the list of interesting solenoid valves to keep the result data as small
	// as possible
	static List<Double> solenoidValvesOfInterest = new ArrayList<Double>();
	static List<Double> allSolenoidValves = new ArrayList<Double>();

	static PropertiesConfiguration configuration;

	static {
		try {
			solenoidValvesOfInterest.add(Double.valueOf(1.0));
			solenoidValvesOfInterest.add(Double.valueOf(2.0));
			solenoidValvesOfInterest.add(Double.valueOf(4.0));
			solenoidValvesOfInterest.add(Double.valueOf(8.0));
			solenoidValvesOfInterest.add(Double.valueOf(16.0));

			allSolenoidValves.add(Double.valueOf(1.0));
			allSolenoidValves.add(Double.valueOf(2.0));
			allSolenoidValves.add(Double.valueOf(4.0));
			allSolenoidValves.add(Double.valueOf(8.0));
			allSolenoidValves.add(Double.valueOf(16.0));

			configuration = new PropertiesConfiguration(FileConfiguration.getConfigurationFile());
			configuration.setAutoSave(true);

			if (!JarUtil.isRunningOnWindows7()) {
				inputFolder = new File(configuration.getString(FileConfiguration.INPUT_FOLDER, "input"));
				outputFolder = new File(configuration.getString(FileConfiguration.OUTPUT_FOLDER, "output"));
				temperatureInputFolder = new File(configuration.getString(FileConfiguration.TEMPERATURE_FOLDER, "input/temp"));
			} else {
				File userDir = JarUtil.getPlantEvaluationUserHomeDir();
				inputFolder = new File(configuration.getString(FileConfiguration.INPUT_FOLDER, userDir.getAbsolutePath() + "/input"));
				outputFolder = new File(configuration.getString(FileConfiguration.OUTPUT_FOLDER, userDir.getAbsolutePath() + "/output"));
				temperatureInputFolder = new File(configuration.getString(FileConfiguration.TEMPERATURE_FOLDER, userDir.getAbsolutePath() + "/input/temp"));
			}

			if (!inputFolder.exists())
				inputFolder.mkdir();
			if (!outputFolder.exists())
				outputFolder.mkdir();
			if (!temperatureInputFolder.exists())
				temperatureInputFolder.mkdir();

			TypeAEvaluationOptions.shiftByOneHour = configuration.getBoolean(TypeAEvaluationOptions.OPTION_SHIFT_BY_ONE_HOUR, false);
			TypeAEvaluationOptions.recordReferenceValve = configuration.getBoolean(TypeAEvaluationOptions.OPTIONS_RECORD_REFERENCE_VALVE, false);
			TypeAEvaluationOptions.sampleRate = configuration.getDouble(TypeAEvaluationOptions.OPTIONS_SAMPLE_RATE, 10.0);

			solenoidValvesOfInterest = configuration.getList(Options.OPTIONS_SOLENOID_VALVES_OF_INTEREST, solenoidValvesOfInterest);

			checkSolenoidValves();

			// options for co2 absolute only evaluation
			CO2AbsoluteOnlyEvaluationOptions.isAutoScaleCO2Absolute = configuration.getBoolean(
					CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_IS_CO2_ABSOLUTE_AUTOSCALE, true);
			CO2AbsoluteOnlyEvaluationOptions.isAutoScaleDeltaFiveMinutes = configuration.getBoolean(
					CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_IS_DELTA_FIVE_MINUTES_AUTOSCALE, true);
			CO2AbsoluteOnlyEvaluationOptions.co2AbsoluteDatasetMinimum = configuration.getDouble(
					CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_CO2_ABSOLUTE, Integer.MIN_VALUE);
			CO2AbsoluteOnlyEvaluationOptions.co2AbsoluteDatasetMaximum = configuration.getDouble(
					CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_CO2_ABSOLUTE, Integer.MAX_VALUE);
			CO2AbsoluteOnlyEvaluationOptions.deltaFiveMinutesMinimum = configuration.getDouble(
					CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_DELTA_FIVE_MINUTES, Integer.MIN_VALUE);
			CO2AbsoluteOnlyEvaluationOptions.deltaFiveMinutesMaximum = configuration.getDouble(
					CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_DELTA_FIVE_MINUTES, Integer.MAX_VALUE);

			// options for type B (= Ingo's evaluation)
			TypeBEvaluationOptions.isCo2AbsoluteAutoscale = configuration.getBoolean(TypeBEvaluationOptions.OPTIONS_TYPE_B_IS_CO2_ABSOLUTE_AUTOSCALE, true);
			TypeBEvaluationOptions.isDeltaRawAutoscale = configuration.getBoolean(TypeBEvaluationOptions.OPTIONS_TYPE_B_IS_DELTA_RAW_AUTOSCALE, true);
			TypeBEvaluationOptions.co2AbsoluteDatasetMinimum = configuration.getDouble(TypeBEvaluationOptions.OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MINIMUM,
					Integer.MIN_VALUE);
			TypeBEvaluationOptions.co2AbsoluteDatasetMaximum = configuration.getDouble(TypeBEvaluationOptions.OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MAXIMUM,
					Integer.MAX_VALUE);
			TypeBEvaluationOptions.deltaRawDatasetMinimum = configuration
					.getDouble(TypeBEvaluationOptions.OPTIONS_TYPE_B_DELTA_RAW_SCALE_MINIMUM, Integer.MIN_VALUE);
			TypeBEvaluationOptions.deltaRawDatasetMaximum = configuration
					.getDouble(TypeBEvaluationOptions.OPTIONS_TYPE_B_DELTA_RAW_SCALE_MAXIMUM, Integer.MAX_VALUE);
		} catch (ConfigurationException ce) {
			log.error("Could not update configuration.", ce);
		}
	}

	static void saveConfiguration() {
		try {
			configuration.save();
		} catch (ConfigurationException ce) {
			log.error("Could not save Configuration.", ce);
		}
	}

	static void checkSolenoidValves() {
		Object obj = solenoidValvesOfInterest.get(0);
		log.debug("obj is " + obj.getClass());
		List<Double> temp = new ArrayList<Double>();
		if (obj instanceof String) {
			for (int i = 0; i < solenoidValvesOfInterest.size(); i++) {
				obj = solenoidValvesOfInterest.get(i);
				temp.add(Double.valueOf(obj.toString()));
			}
			solenoidValvesOfInterest = temp;
		}
		obj = solenoidValvesOfInterest.get(0);
		log.debug("obj is " + obj.getClass());
	}

	public static File getInputFolder() {
		return inputFolder;
	}

	public static void setInputFolder(File inputFolder) {
		Options.inputFolder = inputFolder;
		configuration.setProperty(FileConfiguration.INPUT_FOLDER, inputFolder.getAbsolutePath());
	}

	public static File getOutputFolder() {
		return outputFolder;
	}

	public static void setOutputFolder(File outputFolder) {
		Options.outputFolder = outputFolder;
		configuration.setProperty(FileConfiguration.OUTPUT_FOLDER, outputFolder.getAbsolutePath());
	}

	public static File getTemperatureInputFolder() {
		return temperatureInputFolder;
	}

	public static void setTemperatureInputFolder(File temperatureInputFolder) {
		Options.temperatureInputFolder = temperatureInputFolder;
		configuration.setProperty(FileConfiguration.TEMPERATURE_FOLDER, temperatureInputFolder.getAbsolutePath());
	}

	public static List<Double> getSolenoidValvesOfInterest() {
		return solenoidValvesOfInterest;
	}

	public static void setSolenoidValvesOfInterest(List<Double> solenoidValvesOfInterest) {
		Options.solenoidValvesOfInterest = solenoidValvesOfInterest;
		configuration.setProperty(Options.OPTIONS_SOLENOID_VALVES_OF_INTEREST, solenoidValvesOfInterest);
	}

	public static List<Double> getAllSolenoidValves() {
		return allSolenoidValves;
	}

	public static void setAllSolenoidValves(List<Double> allSolenoidValves) {
		Options.allSolenoidValves = allSolenoidValves;
	}

	public static String OPTIONS_SOLENOID_VALVES_OF_INTEREST = "options.solenoidValves";

}
