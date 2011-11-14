/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	Options.java is part of Plant Evaluation.
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

import java.io.File;

public class Options {

	static boolean shiftByOneHour = false;
	static boolean recordReferenceChambers = false;
	static int sampleRate = 10;
	static File inputFolder = new File("input"); //$NON-NLS-1$
	static File outputFolder = new File("output"); //$NON-NLS-1$
	static File temperatureInputFolder = new File(inputFolder, "temp"); //$NON-NLS-1$

	public static int getSampleRate() {
		return sampleRate;
	}

	public static void setSampleRate(int count) {
		sampleRate = count;
	}

	public static boolean isShiftByOneHour() {
		return shiftByOneHour;
	}

	public static void setShiftByOneHour(boolean shiftByOneHour) {
		Options.shiftByOneHour = shiftByOneHour;
	}

	public static File getInputFolder() {
		return inputFolder;
	}

	public static void setInputFolder(File inputFolder) {
		Options.inputFolder = inputFolder;
	}

	public static File getOutputFolder() {
		return outputFolder;
	}

	public static void setOutputFolder(File outputFolder) {
		Options.outputFolder = outputFolder;
	}

	public static File getTemperatureInputFolder() {
		return temperatureInputFolder;
	}

	public static void setTemperatureInputFolder(File temperatureInputFolder) {
		Options.temperatureInputFolder = temperatureInputFolder;
	}

	public static boolean isRecordReferenceChambers() {
		return recordReferenceChambers;
	}

	public static void setRecordReferenceChambers(
			boolean recordReferenceChambers) {
		Options.recordReferenceChambers = recordReferenceChambers;
	}

}
