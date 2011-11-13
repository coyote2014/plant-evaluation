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

public final class Constants {

	public static String DATE_HEADER = "DATE";
	public static String TIME_HEADER = "TIME";
	public static String delta5minutes_HEADER = "Σ delta5Minutes";
	public static String _12CO2_dry_HEADER = "Σ 12CO2_dry";
	public static String _13CO2_dry_HEADER = "Σ 13CO2_dry";
	public static String H2O_HEADER = "Σ H2O";
	public static String solenoid_HEADER = "solenoid valve";
	public static String CO2_ABS_HEADER = "CO2 ABS";
	public static String COMPLETE_TIME_HEADER = "Zeit";
	public static String CO2_DIFF_HEADER = "CO2Diff";
	public static String DELTA_13_HEADER = "δ13Pflanze";
	public static String TEMPERATURE_HEADER = "Temperature in °C";
	public static String PHOTO_SYNTHESIS_RATE_HEADER = "PSR";
	public static String PHOTO_SYNTHESIS_RATE_STANDARD_DERIVATION_HEADER = "SD of PSR";
	public static String DELTA_13_STANDARD_DERIVATION_HEADER = "SD of δ13";

	// These are the columns for the raw input files
	/**
	 * Column of 12CO2 in input file
	 */
	public static int _12CO2 = 16;
	/**
	 * Column of 12CO2 Dry in input file
	 */
	public static int _12CO2_DRY = 17;
	/**
	 * Column of 13CO2 in input file
	 */
	public static int _13CO2 = 18;
	/**
	 * Column of 13CO2 Dry in input file
	 */
	public static int _13CO2_DRY = 19;
	/**
	 * Column of Delta 5 Minutes in input file
	 */
	public static int DELTA_5_MINUTES = 25;
	/**
	 * Column of Delta Raw in input file
	 */
	public static int DELTA = 22;
	/**
	 * Column of H2O in input file
	 */
	public static int H2O = 20;
	/**
	 * Column of Solenoid Valve in input file
	 */
	public static int SOLENOID_VALVE_INPUT = 15;

	// Columns for the generated files
	/**
	 * Date Column (without time)
	 */
	public static int DATE = 0;
	/**
	 * Time Column (without date)
	 */
	public static int TIME = 1;
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
	public static int CO2_ABS = 7;
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

	/**
	 * Time Constants used during evaluation
	 */
	// 1 hour are 3 600 000ms
	public static long oneHour = 3600000L;
	// 5 minutes are 300000ms
	public static double fiveMinutes = 300000.0;

}
