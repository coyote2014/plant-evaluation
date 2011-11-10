/**
 *  Copyright 2011 Frederik Hahne
 *  
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.options.Options;

public abstract class AbstractEvaluator {

	public double referenceChamberValue = 1.0;

	public File outputRootFolder = Options.getOutputFolder();
	public File inputRootFolder = Options.getInputFolder();
	public File outputFolder;
	public List<String[]> allReferenceLines;

	public AbstractEvaluator(String outputFolderName) {
		try {
			if (!outputRootFolder.exists()) {
				outputFolder.mkdir();
			}
			if (!inputRootFolder.exists())
				throw new RuntimeException("No input folder avaliable!");

			outputFolder = new File(outputRootFolder, outputFolderName);
			if (!outputFolder.exists())
				outputFolder.mkdir();
		} catch (Exception ioe) {
			throw new RuntimeException("Outputfolder could not be created!");
		}
	}

	// the date format: "2011-08-01 00:30:23,54"
	public SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SS");

	// 22.07.11 10:59:38
	public SimpleDateFormat temperatureAndPlantDateFormat = new SimpleDateFormat(
			"dd.MM.yy HH:mm:ss");

	public abstract boolean evaluate() throws Exception;

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

	public List<String[]> findAllReferenceLines(List<String[]> lines,
			int SOLENOID_VALUE) {
		List<String[]> referenceLines = new ArrayList<String[]>();
		for (int i = 1; i < lines.size(); i++) {
			if (parseDoubleValue(lines.get(i), SOLENOID_VALUE) == referenceChamberValue)
				referenceLines.add(lines.get(i));
		}
		return referenceLines;
	}

	public List<Integer> findAllReferenceChambers(List<String[]> lines,
			int SOLENOID_VALUE) {
		List<Integer> referenceChamberLines = new ArrayList<Integer>();
		for (int i = 1; i < lines.size(); i++) {
			if (parseDoubleValue(lines.get(i), SOLENOID_VALUE) == referenceChamberValue)
				referenceChamberLines.add(i);
		}
		return referenceChamberLines;
	}

	public String[] getReferenceLineToUse(String[] line,
			List<String[]> allLines, List<String[]> referenceLines,
			int TIME_VALUE) throws ParseException {

		Date date = dateFormat.parse(line[TIME_VALUE]);
		long shortestedDistance = Long.MAX_VALUE;
		String[] refIndex2Use = null;
		for (String[] refLineIndex : referenceLines) {
			Date refDate = dateFormat.parse(refLineIndex[TIME_VALUE]);
			long difference = Math.abs(date.getTime() - refDate.getTime());
			if (shortestedDistance > difference) {
				shortestedDistance = difference;
				refIndex2Use = refLineIndex;
			}
		}

		return refIndex2Use;
	}
}
