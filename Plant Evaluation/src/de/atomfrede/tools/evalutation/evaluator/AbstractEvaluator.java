/**
 * 	AbstractEvaluator.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.evaluator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public abstract class AbstractEvaluator {

	public double referenceChamberValue = 1.0;

	// the date format: "2011-08-01 00:30:23,54"
	public SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SS");

	public abstract void evaluate();

	public double parseDoubleValue(String[] line, int type) {
		return Double.parseDouble(line[type].replace(",", "."));
	}

	public double[] list2DoubleArray(List<Double> values) {
		return ArrayUtils
				.toPrimitive(values.toArray(new Double[values.size()]));
	}

	public int[] list2IntArray(List<Integer> values) {
		return ArrayUtils
				.toPrimitive(values.toArray(new Integer[values.size()]));
	}

	public CSVWriter getCsvWriter(File outputFile) throws IOException {
		return new CSVWriter(new FileWriter(outputFile));
	}

	public List<String[]> readAllLinesInFile(File input) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(input));
		return reader.readAll();
	}
}
