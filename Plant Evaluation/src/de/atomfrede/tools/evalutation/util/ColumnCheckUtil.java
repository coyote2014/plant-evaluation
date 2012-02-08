/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	ColumnCheckUtil.java is part of Plant Evaluation.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.constants.OutputFileConstants;

/**
 * Checks if the current header still fits to the expected column numbers. If not it changes the the column numbers according to the current file format.
 */
public class ColumnCheckUtil {

	private static final Log log = LogFactory.getLog(ColumnCheckUtil.class);

	/**
	 * Checks if the order of columns in the inputfiles (from picarro device) has changed. If thats the case the constants are changed accordingly, so the program "learns" the
	 * correct file formats and adapts accordingly
	 * 
	 * @param inputFileheader
	 */
	public static void checkInputFileHeader(String[] inputFileheader) throws Exception {

		if (!inputFileheader[InputFileConstants._12CO2].equals(InputFileConstants.HEADER_12_CO2)) {
			InputFileConstants.set_12CO2(findColumn(inputFileheader, InputFileConstants.HEADER_12_CO2));
		}

		if (!inputFileheader[InputFileConstants._12CO2_DRY].equals(InputFileConstants.HEADER_12_CO2_DRY)) {
			InputFileConstants.set_12CO2_DRY(findColumn(inputFileheader, InputFileConstants.HEADER_12_CO2_DRY));
		}

		if (!inputFileheader[InputFileConstants._13CO2].equals(InputFileConstants.HEADER_13_CO2)) {
			InputFileConstants.set_13CO2(findColumn(inputFileheader, InputFileConstants.HEADER_13_CO2));
		}

		if (!inputFileheader[InputFileConstants._13CO2_DRY].equals(InputFileConstants.HEADER_13_CO2_DRY)) {
			InputFileConstants.set_13CO2_DRY(findColumn(inputFileheader, InputFileConstants.HEADER_13_CO2_DRY));
		}

		if (!inputFileheader[InputFileConstants.DELTA_5_MINUTES].equals(InputFileConstants.HEADER_DELTA_5_MINUTES)) {
			InputFileConstants.setDELTA_5_MINUTES(findColumn(inputFileheader, InputFileConstants.HEADER_DELTA_5_MINUTES));
		}

		if (!inputFileheader[InputFileConstants.DELTA_RAW].equals(InputFileConstants.HEADER_DELTA_RAW)) {
			InputFileConstants.setDELTA_RAW(findColumn(inputFileheader, InputFileConstants.HEADER_DELTA_RAW));
		}

		if (!inputFileheader[InputFileConstants.EPOCH_TIME].equals(InputFileConstants.HEADER_EPOCH_TIME)) {
			InputFileConstants.setEPOCH_TIME(findColumn(inputFileheader, InputFileConstants.HEADER_EPOCH_TIME));
		}

		if (!inputFileheader[InputFileConstants.H2O].equals(InputFileConstants.HEADER_H2O)) {
			InputFileConstants.setH2O(findColumn(inputFileheader, InputFileConstants.HEADER_H2O));
		}

		if (!inputFileheader[InputFileConstants.SOLENOID_VALVE_INPUT].equals(InputFileConstants.HEADER_SOLENOID_VALVE)) {
			InputFileConstants.setSOLENOID_VALVE_INPUT(findColumn(inputFileheader, InputFileConstants.HEADER_SOLENOID_VALVE));
		}
	}

	/**
	 * Checks the given outputfile header for changes in the column order. So the outputformat can be changed dynamically. Each time a single evaluator is started this method must
	 * be called in order to use the correct columns.
	 * 
	 * @param header
	 */
	public static void checkOutputFileHeader(String[] header) throws Exception {

		if (!header[OutputFileConstants.CO2_ABSOLUTE].equals(OutputFileConstants.HEADER_CO2_ABSOLUTE)) {
			OutputFileConstants.setCO2_ABSOLUTE(findColumn(header, OutputFileConstants.HEADER_CO2_ABSOLUTE));
		}

		if (!header[OutputFileConstants.CO2_DIFF].equals(OutputFileConstants.HEADER_CO2_DIFFERENCE)) {
			OutputFileConstants.setCO2_DIFF(findColumn(header, OutputFileConstants.HEADER_CO2_DIFFERENCE));
		}

		if (!header[OutputFileConstants.DATE_AND_TIME].equals(OutputFileConstants.HEADER_DATE_AND_TIME)) {
			OutputFileConstants.setDATE_AND_TIME(findColumn(header, OutputFileConstants.HEADER_DATE_AND_TIME));
		}

		if (!header[OutputFileConstants.DELTA13].equals(OutputFileConstants.HEADER_DELTA_13)) {
			OutputFileConstants.setDELTA13(findColumn(header, OutputFileConstants.HEADER_DELTA_13));
		}

		if (!header[OutputFileConstants.MEAN_12CO2_DRY].equals(OutputFileConstants.HEADER_12_CO2_DRY)) {
			OutputFileConstants.setMEAN_12CO2_DRY(findColumn(header, OutputFileConstants.HEADER_12_CO2_DRY));
		}

		if (!header[OutputFileConstants.MEAN_13CO2_DRY].equals(OutputFileConstants.HEADER_13_CO2_DRY)) {
			OutputFileConstants.setMEAN_13CO2_DRY(findColumn(header, OutputFileConstants.HEADER_13_CO2_DRY));
		}

		if (!header[OutputFileConstants.MEAN_DELTA_5_MINUTES].equals(OutputFileConstants.HEADER_DELTA_5_MINUTES)) {
			OutputFileConstants.setMEAN_DELTA_5_MINUTES(findColumn(header, OutputFileConstants.HEADER_DELTA_5_MINUTES));
		}

		if (!header[OutputFileConstants.MEAN_H2O].equals(OutputFileConstants.HEADER_H2O)) {
			OutputFileConstants.setMEAN_H2O(findColumn(header, OutputFileConstants.HEADER_H2O));
		}

		if (!header[OutputFileConstants.PSR].equals(OutputFileConstants.HEADER_PHOTO_SYNTHESIS_RATE)) {
			OutputFileConstants.setPSR(findColumn(header, OutputFileConstants.HEADER_PHOTO_SYNTHESIS_RATE));
		}

		if (!header[OutputFileConstants.SD_DELTA13].equals(OutputFileConstants.HEADER_DELTA_13_STANDARD_DEVIVATION)) {
			OutputFileConstants.setSD_DELTA13(findColumn(header, OutputFileConstants.HEADER_DELTA_13_STANDARD_DEVIVATION));
		}

		if (!header[OutputFileConstants.SD_PSR].equals(OutputFileConstants.HEADER_PHOTO_SYNTHESIS_RATE_STANDARD_DEVIVATION)) {
			OutputFileConstants.setSD_PSR(findColumn(header, OutputFileConstants.HEADER_PHOTO_SYNTHESIS_RATE_STANDARD_DEVIVATION));
		}

		if (!header[OutputFileConstants.SOLENOID_VALVES].equals(OutputFileConstants.HEADER_SOLENOID_VALVE)) {
			OutputFileConstants.setSOLENOID_VALVES(findColumn(header, OutputFileConstants.HEADER_SOLENOID_VALVE));
		}

		if (!header[OutputFileConstants.TEMPERATURE].equals(OutputFileConstants.HEADER_TEMPERATURE)) {
			OutputFileConstants.setTEMPERATURE(findColumn(header, OutputFileConstants.HEADER_TEMPERATURE));
		}
	}

	private static int findColumn(String[] header, String headerToFind) throws Exception {
		log.info("The column " + headerToFind + " has changed, looking for the new position.");
		for (int i = 0; i < header.length; i++) {
			String cHeader = header[i];
			if (cHeader.equals(headerToFind)) {
				log.info("The column " + headerToFind + " is now at " + i + "-th position.");
				return i;
			}
		}
		throw new Exception("The requested column with name " + headerToFind + " could not be found in the provided header.");
	}
}
