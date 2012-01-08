/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	TypeAEvaluationOptions.java is part of Plant Evaluation.
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


public class TypeAEvaluationOptions {

	// shift every date by one hour to cope with summer and winter time
	static boolean shiftByOneHour;
	// should the reference chambers be recorded?
	static boolean recordReferenceValve;
	// how many datasets should be recorded for SD computation?
	static double sampleRate;

	public static double getSampleRate() {
		return sampleRate;
	}

	public static void setSampleRate(double count) {
		sampleRate = count;
		Options.configuration.setProperty(TypeAEvaluationOptions.OPTIONS_SAMPLE_RATE, count);
	}

	public static boolean isShiftByOneHour() {
		return shiftByOneHour;
	}

	public static void setShiftByOneHour(boolean value) {
		shiftByOneHour = value;
		Options.configuration.setProperty(TypeAEvaluationOptions.OPTION_SHIFT_BY_ONE_HOUR, value);
	}

	public static boolean isRecordReferenceValve() {
		return recordReferenceValve;
	}

	public static void setRecordReferenceValve(boolean value) {
		recordReferenceValve = value;
		Options.configuration.setProperty(TypeAEvaluationOptions.OPTIONS_RECORD_REFERENCE_VALVE, value);
	}

	public static String OPTIONS_RECORD_REFERENCE_VALVE = "options.recordReference";
	public static String OPTIONS_SAMPLE_RATE = "options.sampleRate";
	public static String OPTION_SHIFT_BY_ONE_HOUR = "options.shift";

}
