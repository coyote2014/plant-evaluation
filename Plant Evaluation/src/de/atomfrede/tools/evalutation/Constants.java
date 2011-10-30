/**
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
	public static String delta5minutes_HEADER = "MEAN delta5Minutes";
	public static String _12CO2_dry_HEADER = "MEAN 12CO2_dry";
	public static String _13CO2_dry_HEADER = "MEAN 13CO2_dry";
	public static String H2O_HEADER = "MEAN H2O";
	public static String solenoid_HEADER = "solenoid value";
	public static String CO2_ABS_HEADER = "CO2 ABS";
	public static String COMPLETE_TIME_HEADER = "Zeit";
	public static String CO2_DIFF_HEADER = "CO2Diff";
	public static String DELTA_13_HEADER = "δ13Pflanze";
	public static String TEMPERATURE_HEADER = "Temperature in °C";
	public static String PHOTO_SYNTHESIS_RATE_HEADER = "PSR";

	public static int DATE = 0;
	public static int TIME = 1;
	public static int _12CO2 = 2;
	public static int _12CO2_dry = 3;
	public static int _13CO2 = 4;
	public static int _13CO2_dry = 5;
	public static int delta5minutes = 6;
	public static int dataRaw = 7;
	public static int H2O = 8;
	public static int solenoidValue = 9;
	// 1 hour are 3 600 000ms
	public static long oneHour = 3600000L;
	// 5 minutes are 300000ms
	public static double fiveMinutes = 300000.0;

}
