/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	CommonConstants.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.constants;

/**
 * A simple class holding common constants, like headers and column values
 */
public final class CommonConstants {

	public static String DATE_HEADER = "DATE";
	public static String TIME_HEADER = "TIME";

	/**
	 * Date Column (without time)
	 */
	public static int DATE = 0;
	/**
	 * Time Column (without date)
	 */
	public static int TIME = 1;

	/**
	 * Time Constants used during evaluation
	 */
	// 1 hour are 3 600 000ms
	public static long oneHour = 3600000L;
	// 5 minutes are 300000ms
	public static double fiveMinutes = 300000.0;

}
