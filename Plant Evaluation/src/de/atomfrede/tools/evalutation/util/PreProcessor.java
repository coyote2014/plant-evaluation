/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	PreProcessor.java is part of Plant Evaluation.
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * PreProcessor that preprocesses the files provided by the picarro isotope
 * analyzer so they become a real .csv file.
 * 
 * After preprocessing each column is seperated by a comma and not by
 * whitespaces anymore
 */
public class PreProcessor {

	public static void replaceWhiteSpacesWithComma(File inputFile)
			throws IOException {
		List<String> inputLines = FileUtils.readLines(inputFile);
		List<String> outputlines = new ArrayList<String>();
		for (String line : inputLines) {
			String outLine = line.replaceAll("\\s+", ",");
			outLine += "\n";
			outputlines.add(outLine);
		}
		int line = 0;
		for (String outputLine : outputlines) {
			if (line == 0)
				FileUtils.writeStringToFile(inputFile, outputLine, false);
			else
				FileUtils.writeStringToFile(inputFile, outputLine, true);
			line++;

		}
	}
}
