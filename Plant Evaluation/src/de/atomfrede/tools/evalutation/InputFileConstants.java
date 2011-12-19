/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	InputFileConstants.java is part of Plant Evaluation.
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

public class InputFileConstants {

	/**
	 * Column of 12CO2 in input file
	 */
	public static int _12CO2 = 18;
	/**
	 * Column of 12CO2 Dry in input file
	 */
	public static int _12CO2_DRY = 19;
	/**
	 * Column of 13CO2 in input file
	 */
	public static int _13CO2 = 20;
	/**
	 * Column of 13CO2 Dry in input file
	 */
	public static int _13CO2_DRY = 21;
	/**
	 * Column of Delta 5 Minutes in input file
	 */
	public static int DELTA_5_MINUTES = 24;
	/**
	 * Column of Delta Raw in input file
	 */
	public static int DELTA = 25;
	/**
	 * Column of H2O in input file
	 */
	public static int H2O = 26;
	/**
	 * Column of Solenoid Valve in input file
	 */
	public static int SOLENOID_VALVE_INPUT = 16;

	public static void set_12CO2(int _12co2) {
		_12CO2 = _12co2;
	}

	public static void set_12CO2_DRY(int _12co2_DRY) {
		_12CO2_DRY = _12co2_DRY;
	}

	public static void set_13CO2(int _13co2) {
		_13CO2 = _13co2;
	}

	public static void set_13CO2_DRY(int _13co2_DRY) {
		_13CO2_DRY = _13co2_DRY;
	}

	public static void setDELTA_5_MINUTES(int dELTA_5_MINUTES) {
		DELTA_5_MINUTES = dELTA_5_MINUTES;
	}

	public static void setDELTA(int dELTA) {
		DELTA = dELTA;
	}

	public static void setH2O(int h2o) {
		H2O = h2o;
	}

	public static void setSOLENOID_VALVE_INPUT(int sOLENOID_VALVE_INPUT) {
		SOLENOID_VALVE_INPUT = sOLENOID_VALVE_INPUT;
	}

}
