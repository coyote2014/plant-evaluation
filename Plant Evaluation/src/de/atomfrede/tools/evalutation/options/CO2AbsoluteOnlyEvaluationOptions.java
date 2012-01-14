/**
 *  Copyright 2012 Frederik Hahne
 *
 * 	CO2AbsoluteOnlyEvaluationOptions.java is part of Plant Evaluation.
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


public class CO2AbsoluteOnlyEvaluationOptions {

	// COAbsolute Plots Options
	static boolean isAutoScaleCO2Absolute;
	static boolean isAutoScaleDeltaFiveMinutes;
	static double co2AbsoluteDatasetMinimum;
	static double co2AbsoluteDatasetMaximum;
	static double deltaFiveMinutesMinimum;
	static double deltaFiveMinutesMaximum;

	// all options for co2absolute only evaluation
	public static boolean isAutoScaleCO2Absolute() {
		return isAutoScaleCO2Absolute;
	}

	public static void setAutoScaleCO2Absolute(boolean autoScaleCO2Absolute) {
		isAutoScaleCO2Absolute = autoScaleCO2Absolute;
		Options.configuration.setProperty(CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_IS_CO2_ABSOLUTE_AUTOSCALE, autoScaleCO2Absolute);
	}

	public static void co2AbsoluteOnly_setDeltaFiveMinutesMaximum(double value) {
		deltaFiveMinutesMaximum = value;
		Options.configuration.setProperty(CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_DELTA_FIVE_MINUTES, value);
	}

	public static double co2AbsoluteOnly_getDeltaFiveMinutesMaximum() {
		return deltaFiveMinutesMaximum;
	}

	public static void co2AbsoluteOnly_setDeltaFiveMinutesMinimum(double value) {
		deltaFiveMinutesMinimum = value;
		Options.configuration.setProperty(CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_DELTA_FIVE_MINUTES, value);
	}

	public static double co2AbsoluteOnly_getDeltaFiveMinutesMinimum() {
		return deltaFiveMinutesMinimum;
	}

	public static void co2AbsoluteOnly_setCo2AbsoluteDatasetMaximum(double value) {
		co2AbsoluteDatasetMaximum = value;
		Options.configuration.setProperty(CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_CO2_ABSOLUTE, value);
	}

	public static double co2AbsoluteOnly_getCo2AbsoluteDatasetMaximum() {
		return co2AbsoluteDatasetMaximum;
	}

	public static void co2AbsoluteOnly_setCo2AbsoluteDatasetMinimum(double value) {
		co2AbsoluteDatasetMinimum = value;
		Options.configuration.setProperty(CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_CO2_ABSOLUTE, value);
	}

	public static double getCo2AbsoluteDatasetMinimum() {
		return co2AbsoluteDatasetMinimum;
	}

	public static boolean isAutoScaleDeltaFiveMinutes() {
		return isAutoScaleDeltaFiveMinutes;
	}

	public static void setAutoScaleDeltaFiveMinutes(boolean autoScaleDeltaFiveMinutes) {
		isAutoScaleDeltaFiveMinutes = autoScaleDeltaFiveMinutes;
		Options.configuration.setProperty(CO2AbsoluteOnlyEvaluationOptions.OPTIONS_CO2_ABSOLUTE_IS_DELTA_FIVE_MINUTES_AUTOSCALE, autoScaleDeltaFiveMinutes);
	}

	public static String OPTIONS_CO2_ABSOLUTE_IS_CO2_ABSOLUTE_AUTOSCALE = "options.co2absolute.autoscale.co2absolute";
	public static String OPTIONS_CO2_ABSOLUTE_IS_DELTA_FIVE_MINUTES_AUTOSCALE = "options.co2absolute.autoscale.deltafiveminutes";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_CO2_ABSOLUTE = "options.co2absolute.scale.minimum.co2absolute";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_CO2_ABSOLUTE = "options.co2absolute.scale.maximum.co2absolute";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MINIMUM_DELTA_FIVE_MINUTES = "options.co2absolute.scale.minimum.deltafiveminutes";
	public static String OPTIONS_CO2_ABSOLUTE_SCALE_MAXIMUM_DELTA_FIVE_MINUTES = "options.co2absolute.scale.maximum.deltafiveminutes";

}
