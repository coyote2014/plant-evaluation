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

	static boolean isCo2AbsoluteAutoscale;
	static boolean isDeltaRawAutoscale;

	static double co2AbsoluteDatasetMinimum;
	static double co2AbsoluteDatasetMaximum;
	static double deltaRawDatasetMinimum;
	static double deltaRawDatasetMaximum;

	public static boolean isCo2AbsoluteAutoscale() {
		return isCo2AbsoluteAutoscale;
	}

	public static void setCo2AbsoluteAutoscale(boolean isCo2AbsoluteAutoscale) {
		TypeBEvaluationOptions.isCo2AbsoluteAutoscale = isCo2AbsoluteAutoscale;
	}

	public static boolean isDeltaRawAutoscale() {
		return isDeltaRawAutoscale;
	}

	public static void setDeltaRawAutoscale(boolean isDeltaRawAutoscale) {
		TypeBEvaluationOptions.isDeltaRawAutoscale = isDeltaRawAutoscale;
	}

	public static double getCo2AbsoluteDatasetMinimum() {
		return co2AbsoluteDatasetMinimum;
	}

	public static void setCo2AbsoluteDatasetMinimum(double co2AbsoluteDatasetMinimum) {
		TypeBEvaluationOptions.co2AbsoluteDatasetMinimum = co2AbsoluteDatasetMinimum;
	}

	public static double getCo2AbsoluteDatasetMaximum() {
		return co2AbsoluteDatasetMaximum;
	}

	public static void setCo2AbsoluteDatasetMaximum(double co2AbsoluteDatasetMaximum) {
		TypeBEvaluationOptions.co2AbsoluteDatasetMaximum = co2AbsoluteDatasetMaximum;
	}

	public static double getDeltaRawDatasetMinimum() {
		return deltaRawDatasetMinimum;
	}

	public static void setDeltaRawDatasetMinimum(double deltaRawDatasetMinimum) {
		TypeBEvaluationOptions.deltaRawDatasetMinimum = deltaRawDatasetMinimum;
	}

	public static double getDeltaRawDatasetMaximum() {
		return deltaRawDatasetMaximum;
	}

	public static void setDeltaRawDatasetMaximum(double deltaRawDatasetMaximum) {
		TypeBEvaluationOptions.deltaRawDatasetMaximum = deltaRawDatasetMaximum;
	}

}
