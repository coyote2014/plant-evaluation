/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	Constants.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation;

public final class OutputFileConstants {

	public static String HEADER_DELTA_5_MINUTES = "Σ delta5Minutes";
	public static String HEADER_12_CO2_DRY = "Σ 12CO2_dry";
	public static String HEADER_13_CO2_DRY = "Σ 13CO2_dry";
	public static String HEADER_H2O = "Σ H2O";
	public static String HEADER_SOLENOID_VALVE = "solenoid valves";
	public static String HEADER_CO2_ABSOLUTE = "CO2 Absolute";
	public static String HEADER_DATE_AND_TIME = "Zeit";
	public static String HEADER_CO2_DIFFERENCE = "CO2 Difference";
	public static String HEADER_DELTA_13 = "δ13Pflanze";
	public static String HEADER_TEMPERATURE = "Temperature in °C";
	public static String HEADER_PHOTO_SYNTHESIS_RATE = "PSR";
	public static String HEADER_PHOTO_SYNTHESIS_RATE_STANDARD_DEVIVATION = "SD of PSR";
	public static String HEADER_DELTA_13_STANDARD_DEVIVATION = "SD of δ13";

	// Columns for the generated files
	/**
	 * Column for mean of 12CO2 Dry values
	 */
	public static int MEAN_12CO2_DRY = 2;
	/**
	 * Column for mean of 13CO2 Dry values
	 */
	public static int MEAN_13CO2_DRY = 3;
	/**
	 * Column for mean of Delta 5 minutes values
	 */
	public static int MEAN_DELTA_5_MINUTES = 4;
	/**
	 * Column for mean of H2O Dry values
	 */
	public static int MEAN_H2O = 5;
	/**
	 * Column for colenoid valve number
	 */
	public static int SOLENOID_VALVES = 6;
	/**
	 * Column for CO2 Absolute values
	 */
	public static int CO2_ABSOLUTE = 7;
	/**
	 * Column for Date + Time Column values
	 */
	public static int DATE_AND_TIME = 8;
	/**
	 * Column for Difference of CO2 values
	 */
	public static int CO2_DIFF = 9;
	/**
	 * Column for delta13 values
	 */
	public static int DELTA13 = 10;
	/**
	 * Column for Temperature values
	 */
	public static int TEMPERATURE = 11;
	/**
	 * Column for PSR (PhotoSynthesisRate) values
	 */
	public static int PSR = 12;
	/**
	 * Column for SD (StandardDerivation) of PSR values
	 */
	public static int SD_PSR = 13;
	/**
	 * Column for SD (StandardDerivation) of Delta13 values
	 */
	public static int SD_DELTA13 = 14;

	public static void setMEAN_12CO2_DRY(int mEAN_12CO2_DRY) {
		MEAN_12CO2_DRY = mEAN_12CO2_DRY;
	}

	public static void setMEAN_13CO2_DRY(int mEAN_13CO2_DRY) {
		MEAN_13CO2_DRY = mEAN_13CO2_DRY;
	}

	public static void setMEAN_DELTA_5_MINUTES(int mEAN_DELTA_5_MINUTES) {
		MEAN_DELTA_5_MINUTES = mEAN_DELTA_5_MINUTES;
	}

	public static void setMEAN_H2O(int mEAN_H2O) {
		MEAN_H2O = mEAN_H2O;
	}

	public static void setSOLENOID_VALVES(int sOLENOID_VALVES) {
		SOLENOID_VALVES = sOLENOID_VALVES;
	}

	public static void setCO2_ABSOLUTE(int cO2_ABSOLUTE) {
		CO2_ABSOLUTE = cO2_ABSOLUTE;
	}

	public static void setDATE_AND_TIME(int dATE_AND_TIME) {
		DATE_AND_TIME = dATE_AND_TIME;
	}

	public static void setCO2_DIFF(int cO2_DIFF) {
		CO2_DIFF = cO2_DIFF;
	}

	public static void setDELTA13(int dELTA13) {
		DELTA13 = dELTA13;
	}

	public static void setTEMPERATURE(int tEMPERATURE) {
		TEMPERATURE = tEMPERATURE;
	}

	public static void setPSR(int pSR) {
		PSR = pSR;
	}

	public static void setSD_PSR(int sD_PSR) {
		SD_PSR = sD_PSR;
	}

	public static void setSD_DELTA13(int sD_DELTA13) {
		SD_DELTA13 = sD_DELTA13;
	}

}
