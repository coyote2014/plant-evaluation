/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	FileConfiguration.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileConfiguration {

	public static String INPUT_FOLDER = "folder.input";
	public static String TEMPERATURE_FOLDER = "folder.temperature";
	public static String OUTPUT_FOLDER = "folder.output";

	public static String OPTION_SHIFT_BY_ONE_HOUR = "options.shift";
	public static String OPTIONS_RECORD_REFERENCE_VALVE = "options.recordReference";
	public static String OPTIONS_SAMPLE_RATE = "options.sampleRate";

	public static String OPTIONS_SOLENOID_VALVES_OF_INTEREST = "options.solenoidValves";

	public static String OPTIONS_CO2_ABSOLUTE_IS_CO2_ABSOLUTE_AUTOSCALE = "options.co2absolute.autoscale.co2absolute";
	public static String OPTIONS_CO2_ABSOLUTE_IS_DELTA_FIVE_MINUTES_AUTOSCALE = "options.co2absolute.autoscale.deltafiveminutes";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_CO2_ABSOLUTE = "options.co2absolute.scale.minimum.co2absolute";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_CO2_ABSOLUTE = "options.co2absolute.scale.maximum.co2absolute";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_DELTA_FIVE_MINUTES = "options.co2absolute.scale.minimum.deltafiveminutes";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_DELTA_FIVE_MINUTES = "options.co2absolute.scale.maximum.deltafiveminutes";

	public static String OPTIONS_TYPE_B_IS_CO2_ABSOLUTE_AUTOSCALE = "options.type.b.autoscale.co2absolute";
	public static String OPTIONS_TYPE_B_IS_DELTA_RAW_AUTOSCALE = "options.type.b.autoscale.deltaraw";
	public static String OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MINIMUM = "options.type.b.scale.minimum.co2absolute";
	public static String OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MAXIMUM = "options.type.b.scale.maximum.co2absolute";
	public static String OPTIONS_TYPE_B_DELTA_RAW_SCALE_MINIMUM = "options.type.b.scale.minimum.deltaraw";
	public static String OPTIONS_TYPE_B_DELTA_RAW_SCALE_MAXIMUM = "options.type.b.scale.maximum.deltaraw";

	private static final Log log = LogFactory.getLog(FileConfiguration.class);

	static File configurationFile;

	static {
		try {
			File jarLocation = JarUtil.getJarLocation();
			if (jarLocation.getName().endsWith(".jar")) {
				if (!JarUtil.isRunningOnWindows7())
					configurationFile = new File(jarLocation.getParent(), "config.properties");
				else {
					configurationFile = new File(JarUtil.getPlantEvaluationUserHomeDir(), "config.properties");
				}
			} else {
				configurationFile = new File("config.properties");
			}
			if (!configurationFile.exists())
				configurationFile.createNewFile();
		} catch (IOException e) {
			log.error("Could not create configuration file", e);
		}
	}

	public static File getConfigurationFile() {
		return configurationFile;
	}
}
