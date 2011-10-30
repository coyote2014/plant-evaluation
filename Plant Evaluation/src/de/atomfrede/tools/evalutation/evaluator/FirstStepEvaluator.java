/**
 * 	Evaluator.java is part of Plant Evaluation.
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
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.StatUtils;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.Constants;
import de.atomfrede.tools.evalutation.WriteUtils;

public class FirstStepEvaluator extends AbstractEvaluator {

	List<Integer> linesNeedForStandardDerivation;
	File outputFile;

	public FirstStepEvaluator() {
		super("second");
		linesNeedForStandardDerivation = new ArrayList<Integer>();
		evaluate();
		new SecondStepEvaluator(outputFile);
	}

	@Override
	public void evaluate() {
		CSVWriter writer = null;
		try {
			// TODO read all files in input directory
			File inputFile = new File(inputRootFolder, "laser-001.csv");

			if (!inputFile.exists())
				return;

			outputFile = new File(outputFolder, "laser-001-second.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return;

			writer = getCsvWriter(outputFile);
			WriteUtils.writeHeader(writer);

			List<String[]> lines = readAllLinesInFile(inputFile);
			int startIndex = 1;
			while (startIndex < lines.size() && startIndex >= 0) {
				startIndex = findEndOfChamber(lines, startIndex);
				if (startIndex >= 0 && startIndex < lines.size()) {
					String[] currentLine = lines.get(startIndex - 1);
					Date date2Write = dateFormat
							.parse(currentLine[Constants.DATE] + " "
									+ currentLine[Constants.TIME]);
					date2Write = new Date(date2Write.getTime()
							+ Constants.oneHour);
					String solenoid2Write = currentLine[Constants.solenoidValue];
					Map<Integer, double[]> type2RawValues = collectValuesOfLastFiveMinutes(
							lines, startIndex);
					Map<Integer, Double> type2MeanValue = computeMeanValues(type2RawValues);
					WriteUtils.appendValuesInFirstStep(date2Write,
							solenoid2Write, type2MeanValue, writer);
				}
			}
		} catch (IOException ioe) {

		} catch (ParseException pe) {

		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		System.out.println("Done 1st step");
	}

	Map<Integer, Double> computeMeanValues(Map<Integer, double[]> type2RawValues) {
		Map<Integer, Double> type2MeanValues = new HashMap<Integer, Double>();
		type2MeanValues.put(Constants.delta5minutes,
				StatUtils.mean(type2RawValues.get(Constants.delta5minutes)));
		type2MeanValues.put(Constants._12CO2_dry,
				StatUtils.mean(type2RawValues.get(Constants._12CO2_dry)));
		type2MeanValues.put(Constants._13CO2_dry,
				StatUtils.mean(type2RawValues.get(Constants._13CO2_dry)));
		type2MeanValues.put(Constants.H2O,
				StatUtils.mean(type2RawValues.get(Constants.H2O)));
		return type2MeanValues;
	}

	int findEndOfChamber(List<String[]> lines, int startIndex) {
		String lastSolenoid = lines.get(startIndex)[Constants.solenoidValue];
		for (int i = startIndex; i < lines.size(); i++) {
			String actualSolenoid = lines.get(i)[Constants.solenoidValue];
			if (!actualSolenoid.equals(lastSolenoid))
				return i;
		}
		return -1;
	}

	Map<Integer, double[]> collectValuesOfLastFiveMinutes(List<String[]> lines,
			int startIndex) throws ParseException {
		List<Double> fiveMinutesDeltaValues = new ArrayList<Double>();
		List<Double> _12CO2_dry_Values = new ArrayList<Double>();
		List<Double> _13CO2_dry_Values = new ArrayList<Double>();
		List<Double> _H20_Values = new ArrayList<Double>();

		linesNeedForStandardDerivation.add(startIndex);

		String[] startLine = lines.get(startIndex);

		StringBuilder dateBuilder = new StringBuilder();
		dateBuilder.append(startLine[Constants.DATE]);
		dateBuilder.append(" ");
		dateBuilder.append(startLine[Constants.TIME]);
		Date startDate = dateFormat.parse(dateBuilder.toString());
		Date currentDate = startDate;

		fiveMinutesDeltaValues.add(parseDoubleValue(startLine,
				Constants.delta5minutes));
		_12CO2_dry_Values
				.add(parseDoubleValue(startLine, Constants._12CO2_dry));
		_13CO2_dry_Values
				.add(parseDoubleValue(startLine, Constants._13CO2_dry));
		_H20_Values.add(parseDoubleValue(startLine, Constants.H2O));

		int currentIndex = startIndex - 1;
		while (Math.abs(startDate.getTime() - currentDate.getTime()) <= Constants.fiveMinutes) {
			linesNeedForStandardDerivation.add(currentIndex);

			String[] currentLine = lines.get(currentIndex);
			currentDate = dateFormat.parse(currentLine[Constants.DATE] + " "
					+ currentLine[Constants.TIME]);
			fiveMinutesDeltaValues.add(parseDoubleValue(currentLine,
					Constants.delta5minutes));
			_12CO2_dry_Values.add(parseDoubleValue(currentLine,
					Constants._12CO2_dry));
			_13CO2_dry_Values.add(parseDoubleValue(currentLine,
					Constants._13CO2_dry));
			_H20_Values.add(parseDoubleValue(currentLine, Constants.H2O));

			currentIndex -= 1;
		}
		Map<Integer, double[]> mapping = new HashMap<Integer, double[]>();
		mapping.put(Constants.delta5minutes,
				list2DoubleArray(fiveMinutesDeltaValues));
		mapping.put(Constants._12CO2_dry, list2DoubleArray(_12CO2_dry_Values));
		mapping.put(Constants._13CO2_dry, list2DoubleArray(_13CO2_dry_Values));
		mapping.put(Constants.H2O, list2DoubleArray(_H20_Values));
		return mapping;
	}

}
