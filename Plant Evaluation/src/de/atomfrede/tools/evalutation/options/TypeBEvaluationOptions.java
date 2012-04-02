/**
 *  Copyright 2012 Frederik Hahne
 *
 * 	TypeBEvaluationOptions.java is part of Plant Evaluation.
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

public class TypeBEvaluationOptions {

	public static String OPTIONS_TYPE_B_IS_CO2_ABSOLUTE_AUTOSCALE = "options.type.b.autoscale.co2absolute";
	public static String OPTIONS_TYPE_B_IS_DELTA_RAW_AUTOSCALE = "options.type.b.autoscale.deltaraw";
	public static String OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MINIMUM = "options.type.b.scale.minimum.co2absolute";
	public static String OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MAXIMUM = "options.type.b.scale.maximum.co2absolute";
	public static String OPTIONS_TYPE_B_DELTA_RAW_SCALE_MINIMUM = "options.type.b.scale.minimum.deltaraw";
	public static String OPTIONS_TYPE_B_DELTA_RAW_SCALE_MAXIMUM = "options.type.b.scale.maximum.deltaraw";
	public static String OPTIONS_TYPE_B_DENSITY = "options.type.b.density";

	static boolean isCo2AbsoluteAutoscale;
	static boolean isDeltaRawAutoscale;

	static double co2AbsoluteDatasetMinimum;
	static double co2AbsoluteDatasetMaximum;
	static double deltaRawDatasetMinimum;
	static double deltaRawDatasetMaximum;

	static int density;

	public static boolean isCo2AbsoluteAutoscale() {
		return isCo2AbsoluteAutoscale;
	}

	public static void setCo2AbsoluteAutoscale(boolean isCo2AbsoluteAutoscale) {
		TypeBEvaluationOptions.isCo2AbsoluteAutoscale = isCo2AbsoluteAutoscale;
		Options.configuration.setProperty(TypeBEvaluationOptions.OPTIONS_TYPE_B_IS_CO2_ABSOLUTE_AUTOSCALE, TypeBEvaluationOptions.isCo2AbsoluteAutoscale);
	}

	public static boolean isDeltaRawAutoscale() {
		return isDeltaRawAutoscale;
	}

	public static void setDeltaRawAutoscale(boolean isDeltaRawAutoscale) {
		TypeBEvaluationOptions.isDeltaRawAutoscale = isDeltaRawAutoscale;
		Options.configuration.setProperty(TypeBEvaluationOptions.OPTIONS_TYPE_B_IS_DELTA_RAW_AUTOSCALE, TypeBEvaluationOptions.isDeltaRawAutoscale());
	}

	public static double getCo2AbsoluteDatasetMinimum() {
		return co2AbsoluteDatasetMinimum;
	}

	public static void setCo2AbsoluteDatasetMinimum(double co2AbsoluteDatasetMinimum) {
		TypeBEvaluationOptions.co2AbsoluteDatasetMinimum = co2AbsoluteDatasetMinimum;
		Options.configuration.setProperty(TypeBEvaluationOptions.OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MINIMUM, TypeBEvaluationOptions.co2AbsoluteDatasetMinimum);
	}

	public static double getCo2AbsoluteDatasetMaximum() {
		return co2AbsoluteDatasetMaximum;
	}

	public static void setCo2AbsoluteDatasetMaximum(double co2AbsoluteDatasetMaximum) {
		TypeBEvaluationOptions.co2AbsoluteDatasetMaximum = co2AbsoluteDatasetMaximum;
		Options.configuration.setProperty(TypeBEvaluationOptions.OPTIONS_TYPE_B_CO2_ABSOLUTE_SCALE_MAXIMUM, TypeBEvaluationOptions.co2AbsoluteDatasetMaximum);
	}

	public static double getDeltaRawDatasetMinimum() {
		return deltaRawDatasetMinimum;
	}

	public static void setDeltaRawDatasetMinimum(double deltaRawDatasetMinimum) {
		TypeBEvaluationOptions.deltaRawDatasetMinimum = deltaRawDatasetMinimum;
		Options.configuration.setProperty(TypeBEvaluationOptions.OPTIONS_TYPE_B_DELTA_RAW_SCALE_MINIMUM, TypeBEvaluationOptions.deltaRawDatasetMinimum);
	}

	public static double getDeltaRawDatasetMaximum() {
		return deltaRawDatasetMaximum;
	}

	public static void setDeltaRawDatasetMaximum(double deltaRawDatasetMaximum) {
		TypeBEvaluationOptions.deltaRawDatasetMaximum = deltaRawDatasetMaximum;
		Options.configuration.setProperty(TypeBEvaluationOptions.OPTIONS_TYPE_B_DELTA_RAW_SCALE_MAXIMUM, TypeBEvaluationOptions.deltaRawDatasetMaximum);
	}

	public static void setDensity(int density) {
		TypeBEvaluationOptions.density = density;
		Options.configuration.setProperty(TypeBEvaluationOptions.OPTIONS_TYPE_B_DENSITY, TypeBEvaluationOptions.density);
	}

	public static int getDensity() {
		return density;
	}
}
