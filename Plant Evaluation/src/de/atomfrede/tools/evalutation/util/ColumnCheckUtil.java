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

import de.atomfrede.tools.evalutation.InputFileConstants;

/**
 * Checks if the current header still fits to the expected column numbers. If
 * not it changes the the column numbers according to the current file format.
 */
public class ColumnCheckUtil {

	private static final Log log = LogFactory.getLog(ColumnCheckUtil.class);

	public static void checkHeader(String[] header) {

		if (!header[InputFileConstants._12CO2].equals(InputFileConstants.HEADER_12_CO2)) {
			InputFileConstants.set_12CO2(findColumn(header, InputFileConstants.HEADER_12_CO2));
		}

		if (!header[InputFileConstants._12CO2_DRY].equals(InputFileConstants.HEADER_12_CO2_DRY)) {
			InputFileConstants.set_12CO2_DRY(findColumn(header, InputFileConstants.HEADER_12_CO2_DRY));
		}

		if (!header[InputFileConstants._13CO2].equals(InputFileConstants.HEADER_13_CO2)) {
			InputFileConstants.set_13CO2(findColumn(header, InputFileConstants.HEADER_13_CO2));
		}

		if (!header[InputFileConstants._13CO2_DRY].equals(InputFileConstants.HEADER_13_CO2_DRY)) {
			InputFileConstants.set_13CO2_DRY(findColumn(header, InputFileConstants.HEADER_13_CO2_DRY));
		}

		if (!header[InputFileConstants.DELTA_5_MINUTES].equals(InputFileConstants.HEADER_DELTA_5_MINUTES)) {
			InputFileConstants.setDELTA_5_MINUTES(findColumn(header, InputFileConstants.HEADER_DELTA_5_MINUTES));
		}

		if (!header[InputFileConstants.DELTA_RAW].equals(InputFileConstants.HEADER_DELTA_RAW)) {
			InputFileConstants.setDELTA_RAW(findColumn(header, InputFileConstants.HEADER_DELTA_RAW));
		}

		if (!header[InputFileConstants.EPOCH_TIME].equals(InputFileConstants.HEADER_EPOCH_TIME)) {
			InputFileConstants.setEPOCH_TIME(findColumn(header, InputFileConstants.HEADER_EPOCH_TIME));
		}

		if (!header[InputFileConstants.H2O].equals(InputFileConstants.HEADER_H2O)) {
			InputFileConstants.setH2O(findColumn(header, InputFileConstants.HEADER_H2O));
		}

		if (!header[InputFileConstants.SOLENOID_VALVE_INPUT].equals(InputFileConstants.HEADER_SOLENOID_VALVE)) {
			InputFileConstants.setSOLENOID_VALVE_INPUT(findColumn(header, InputFileConstants.HEADER_SOLENOID_VALVE));
		}
	}

	private static int findColumn(String[] header, String headerToFind) {
		log.info("The column " + headerToFind + " has changed, looking for the new position.");
		for (int i = 0; i < header.length; i++) {
			String cHeader = header[i];
			if (cHeader.equals(headerToFind)) {
				log.info("The column " + headerToFind + " is now at " + i + "-th position.");
				return i;
			}
		}
		return 0;
	}
}
