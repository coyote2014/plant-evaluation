/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	CSVUtil.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CSVUtil {

	public static String[] getHeader(File dataFile) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(dataFile));
			return reader.readNext();
		} catch (IOException ioe) {

		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {

			}
		}
		return new String[1];
	}

	public static List<String[]> getAllDataLines(File dataFile) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(dataFile));
			return reader.readAll();
		} catch (IOException ioe) {

		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {

			}
		}

		return new ArrayList<String[]>();
	}
}
