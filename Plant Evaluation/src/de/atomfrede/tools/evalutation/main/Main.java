/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	Main.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.main;

import java.io.File;

public class Main {

	static File outputFolder = new File("output");
	static File photosynthesisFolder = new File(outputFolder, "photosythesis/");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// CopyEvaluator evaluator = new CopyEvaluator();

		String s = "DATE                      TIME                      FRAC_DAYS_SINCE_JAN1      FRAC_HRS_SINCE_JAN1       EPOCH_TIME                ALARM_STATUS              CavityPressure            CavityTemp                DasTemp                   EtalonTemp                WarmBoxTemp               species                   MPVPosition               InletValve                OutletValve               solenoid_valves           12CO2                     12CO2_dry                 13CO2                     13CO2_dry                 H2O                       CH4                       Delta_Raw                 Delta_30s                 Delta_2min                Delta_5min                Ratio_Raw                 Ratio_30s                 Ratio_2min                Ratio_5min                CH4_High_Precision        peak_75                   ch4_splinemax_for_correct peak87_baseave_spec       peak88_baseave            ";
		System.out.println(s);
		s = s.replaceAll("\\s+", ",");
		System.out.println(s);

		// List<File> inputFiles = new ArrayList<File>();
		// List<File> sdFiles = new ArrayList<File>();
		//
		// File[] allFiles = photosynthesisFolder.listFiles();
		// for (File file : allFiles) {
		// if (file.isFile()) {
		// if (file.getName().startsWith("psr"))
		// inputFiles.add(file);
		// else
		// sdFiles.add(file);
		// }
		// }
		//
		// StandardDerivationEvaluator sdEvaluator = new
		// StandardDerivationEvaluator(
		// inputFiles, sdFiles);

	}
}
