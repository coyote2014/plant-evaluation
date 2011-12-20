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
public class Options {

	private static final Log log = LogFactory.getLog(Options.class);

	// shift every date by one hour to cope with summer and winter time
	static boolean shiftByOneHour;
	// should the reference chambers be recorded?
	static boolean recordReferenceChambers;
	// how many datasets should be recorded for SD computation?
	static double sampleRate;
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

			shiftByOneHour = configuration.getBoolean(FileConfiguration.OPTION_SHIFT_BY_ONE_HOUR, false);
			recordReferenceChambers = configuration.getBoolean(FileConfiguration.OPTIONS_RECORD_REFERENCE_CHAMBERS, false);
			sampleRate = configuration.getDouble(FileConfiguration.OPTIONS_SAMPLE_RATE, 10.0);

			solenoidValvesOfInterest = configuration.getList(FileConfiguration.OPTIONS_SOLENOID_VALVES_OF_INTEREST, solenoidValvesOfInterest);

			checkSolenoidValves();
		} catch (ConfigurationException ce) {
			log.error("Could not update configuration.", ce);
		}
	}

	static void saveConfiguration() {
		try {
			configuration.save();
		} catch (ConfigurationException ce) {

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

	public static double getSampleRate() {
		return sampleRate;
	}

	public static void setSampleRate(double count) {
		sampleRate = count;
		configuration.setProperty(FileConfiguration.OPTIONS_SAMPLE_RATE, count);
	}

	public static boolean isShiftByOneHour() {
		return shiftByOneHour;
	}

	public static void setShiftByOneHour(boolean shiftByOneHour) {
		Options.shiftByOneHour = shiftByOneHour;
		configuration.setProperty(FileConfiguration.OPTION_SHIFT_BY_ONE_HOUR, shiftByOneHour);
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

	public static boolean isRecordReferenceChambers() {
		return recordReferenceChambers;
	}

	public static void setRecordReferenceChambers(boolean recordReferenceChambers) {
		Options.recordReferenceChambers = recordReferenceChambers;
		configuration.setProperty(FileConfiguration.OPTIONS_RECORD_REFERENCE_CHAMBERS, recordReferenceChambers);
	}

	public static List<Double> getSolenoidValvesOfInterest() {
		return solenoidValvesOfInterest;
	}

	public static void setSolenoidValvesOfInterest(List<Double> solenoidValvesOfInterest) {
		Options.solenoidValvesOfInterest = solenoidValvesOfInterest;
		configuration.setProperty(FileConfiguration.OPTIONS_SOLENOID_VALVES_OF_INTEREST, solenoidValvesOfInterest);
	}

	public static List<Double> getAllSolenoidValves() {
		return allSolenoidValves;
	}

	public static void setAllSolenoidValves(List<Double> allSolenoidValves) {
		Options.allSolenoidValves = allSolenoidValves;
	}
}
