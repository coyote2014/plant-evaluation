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

public class MeanValueEvaluator extends AbstractEvaluator {

	List<Integer> linesNeedForStandardDerivation;
	File standardDerivationOutputFile;
	File outputFile;
	File inputFile;
	CSVWriter standardDerivationWriter = null;;

	public MeanValueEvaluator(File inputFile) {
		super("mean-values");
		this.inputFile = inputFile;
		linesNeedForStandardDerivation = new ArrayList<Integer>();
		boolean done = evaluate();
		if (done)
			new CO2DiffEvaluator(outputFile, standardDerivationOutputFile);
	}

	@Override
	public boolean evaluate() {
		CSVWriter writer = null;
		try {
			outputFile = new File(outputFolder, "laser-mean-values.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;

			writer = getCsvWriter(outputFile);
			WriteUtils.writeHeader(writer);

			standardDerivationOutputFile = new File(outputFolder,
					"standard-derivation-file.csv");
			standardDerivationOutputFile.createNewFile();

			if (!standardDerivationOutputFile.exists())
				return false;

			standardDerivationWriter = getCsvWriter(standardDerivationOutputFile);
			WriteUtils.writeHeader(standardDerivationWriter);

			List<String[]> lines = readAllLinesInFile(inputFile);
			int startIndex = 1;
			while (startIndex < lines.size() && startIndex > 0) {
				startIndex = findEndOfChamber(lines, startIndex);
				if (startIndex >= 0 && startIndex < lines.size()) {
					String[] currentLine = lines.get(startIndex - 1);
					if (startIndex == 1)
						currentLine = lines.get(startIndex);
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

			writeAllLinesForStandardDerivation(lines);
		} catch (IOException ioe) {
			System.out.println("IOException " + ioe.getMessage());
			return false;
		} catch (ParseException pe) {
			System.out.println("ParseException " + pe.toString() + "  "
					+ pe.getMessage());
			return false;
		} finally {
			try {
				if (writer != null)
					writer.close();
				if (standardDerivationWriter != null)
					standardDerivationWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Mean Values computed.");
		return true;
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
		String[] currentLine = lines.get(startIndex);
		if (currentLine.length < Constants.solenoidValue)
			return -1;

		String lastSolenoid = currentLine[Constants.solenoidValue];
		for (int i = startIndex; i < lines.size(); i++) {
			if (lines.get(i).length < Constants.solenoidValue) {
				String[] errorLine = lines.get(i);
				System.out.println("No Solenoid " + lines.get(i));
				for (int j = 0; j < errorLine.length; j++) {
					System.out.println(j + "-th value " + errorLine[j]);
				}
			} else {
				String actualSolenoid = lines.get(i)[Constants.solenoidValue];
				if (!actualSolenoid.equals(lastSolenoid))
					return i;
			}
		}

		return -1;
	}

	void writeAllLinesForStandardDerivation(List<String[]> allLines) {
		for (Integer index : linesNeedForStandardDerivation) {
			String[] currentLine = allLines.get(index);
			writeLinesForStandardDerivation(currentLine);
		}
	}

	void writeLinesForStandardDerivation(String[] lineToWrite) {
		// First collect all values
		String date = lineToWrite[Constants.DATE];
		String time = lineToWrite[Constants.TIME];
		double _12CO2_dry_value = parseDoubleValue(lineToWrite,
				Constants._12CO2_dry);
		double _13CO2_dry_value = parseDoubleValue(lineToWrite,
				Constants._13CO2_dry);
		double _solenoidValue = parseDoubleValue(lineToWrite,
				Constants.solenoidValue);
		double h2O = parseDoubleValue(lineToWrite, Constants.H2O);
		double delta5Minutes = parseDoubleValue(lineToWrite,
				Constants.delta5minutes);
		double co2Abs = Math.abs(_12CO2_dry_value + _13CO2_dry_value);
		String date_time = date + " " + time;
		// then write a new line in standardderivation file
		String[] newLine = { date, time, _12CO2_dry_value + "",
				_13CO2_dry_value + "", delta5Minutes + "", h2O + "",
				_solenoidValue + "", co2Abs + "", date_time };
		standardDerivationWriter.writeNext(newLine);

	}

	Map<Integer, double[]> collectValuesOfLastFiveMinutes(List<String[]> lines,
			int startIndex) throws ParseException {
		List<Double> fiveMinutesDeltaValues = new ArrayList<Double>();
		List<Double> _12CO2_dry_Values = new ArrayList<Double>();
		List<Double> _13CO2_dry_Values = new ArrayList<Double>();
		List<Double> _H20_Values = new ArrayList<Double>();

		String[] startLine = lines.get(startIndex);
		// save line for later computation for standard derivation
		linesNeedForStandardDerivation.add(Integer.valueOf(startIndex));
		// writeLinesForStandardDerivation(startLine);

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
		while (Math.abs(startDate.getTime() - currentDate.getTime()) <= Constants.fiveMinutes
				&& currentIndex >= 1) {

			String[] currentLine = lines.get(currentIndex);

			// save line for later computation of standard derivation
			if (currentIndex % 10 == 0)
				// writeLinesForStandardDerivation(currentLine);
				linesNeedForStandardDerivation.add(Integer
						.valueOf(currentIndex));

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
