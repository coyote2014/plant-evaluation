/**
 *  Copyright 2011 Frederik Hahne
 *  
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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.stat.StatUtils;

import au.com.bytecode.opencsv.CSVWriter;
import de.atomfrede.tools.evalutation.constants.CommonConstants;
import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.evaluator.SingleInputFileEvaluator;
import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.util.DialogUtil;
import de.atomfrede.tools.evalutation.util.EntryComparator;
import de.atomfrede.tools.evalutation.util.WriteUtils;

public class MeanValueEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(MeanValueEvaluator.class);

	List<Integer> linesNeedForStandardDerivation;
	CSVWriter standardDerivationWriter = null;

	public MeanValueEvaluator(File inputFile) {
		super("mean-values", inputFile, null);
		this.name = "Mean Values";
		linesNeedForStandardDerivation = new ArrayList<Integer>();
	}

	@Override
	public boolean evaluate() throws Exception {
		CSVWriter writer = null;
		try {
			outputFile = new File(outputFolder, "laser-mean-values.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;

			writer = getCsvWriter(outputFile);
			WriteUtils.writeHeader(writer);

			List<String[]> lines = readAllLinesInFile(inputFile);
			int startIndex = 1;
			while (startIndex < lines.size() && startIndex > 0) {
				startIndex = findEndOfChamber(lines, startIndex);
				if (startIndex > 1 && startIndex < lines.size()) {
					String[] currentLine = lines.get(startIndex - 1);

					Date date2Write = dateFormat.parse(currentLine[CommonConstants.DATE] + " " + currentLine[CommonConstants.TIME]);

					if (Options.isShiftByOneHour())
						date2Write = new Date(date2Write.getTime() + CommonConstants.oneHour);

					String solenoid2Write = currentLine[InputFileConstants.SOLENOID_VALVE_INPUT];
					Map<Integer, double[]> type2RawValues = collectValuesOfLastFiveMinutes(lines, startIndex - 1);
					Map<Integer, Double> type2MeanValue = computeMeanValues(type2RawValues);
					WriteUtils.appendMeanValues(date2Write, solenoid2Write, type2MeanValue, writer);
					progressBar.setValue((int) (((startIndex * 1.0 / lines.size()) * 100) * 0.5));
				}
			}

			progressBar.setValue(50);

			// Standard Derivation Stuff
			standardDeviationOutputFile = new File(outputFolder, "standard-derivation-file.csv");
			standardDeviationOutputFile.createNewFile();

			if (!standardDeviationOutputFile.exists())
				return false;

			standardDerivationWriter = getCsvWriter(standardDeviationOutputFile);
			WriteUtils.writeHeader(standardDerivationWriter);

			writeAllLinesForStandardDerivation(lines);
		} catch (IOException ioe) {
			log.error(ioe);
			DialogUtil.getInstance().showError(ioe);
			return false;
		} catch (ParseException pe) {
			log.error(pe);
			DialogUtil.getInstance().showError(pe);
			return false;
		} catch (Exception e) {
			log.error(e);
			DialogUtil.getInstance().showError(e);
			return false;
		} finally {
			if (writer != null)
				writer.close();
		}
		log.info("Mean Values computed.");
		progressBar.setValue(100);
		return true;
	}

	Map<Integer, Double> computeMeanValues(Map<Integer, double[]> type2RawValues) {
		Map<Integer, Double> type2MeanValues = new HashMap<Integer, Double>();
		type2MeanValues.put(InputFileConstants.DELTA_5_MINUTES, StatUtils.mean(type2RawValues.get(InputFileConstants.DELTA_5_MINUTES)));
		type2MeanValues.put(InputFileConstants._12CO2_DRY, StatUtils.mean(type2RawValues.get(InputFileConstants._12CO2_DRY)));
		type2MeanValues.put(InputFileConstants._13CO2_DRY, StatUtils.mean(type2RawValues.get(InputFileConstants._13CO2_DRY)));
		type2MeanValues.put(InputFileConstants.H2O, StatUtils.mean(type2RawValues.get(InputFileConstants.H2O)));
		return type2MeanValues;
	}

	int findEndOfChamber(List<String[]> lines, int startIndex) {
		String[] currentLine = lines.get(startIndex);
		if (currentLine.length < InputFileConstants.SOLENOID_VALVE_INPUT)
			return -1;

		String lastSolenoid = currentLine[InputFileConstants.SOLENOID_VALVE_INPUT];
		for (int i = startIndex; i < lines.size(); i++) {
			if (lines.get(i).length < InputFileConstants.SOLENOID_VALVE_INPUT) {
				String[] errorLine = lines.get(i);
				log.trace("No Solenoid " + lines.get(i));
				for (int j = 0; j < errorLine.length; j++) {
					log.trace(j + "-th value " + errorLine[j]);
				}
			} else {
				String actualSolenoid = lines.get(i)[InputFileConstants.SOLENOID_VALVE_INPUT];
				if (!actualSolenoid.equals(lastSolenoid)) {
					// System.out.println("Change!");
					return i;
				}

			}
		}

		return -1;
	}

	void writeAllLinesForStandardDerivation(List<String[]> allLines) throws ParseException {
		List<String[]> standardDerivationLines = new ArrayList<String[]>();
		for (Integer index : linesNeedForStandardDerivation) {
			String[] currentLine = allLines.get(index);
			// writeLinesForStandardDerivation(currentLine);
			standardDerivationLines.add(currentLine);

		}
		int count = 0;
		Collections.sort(standardDerivationLines, new EntryComparator());
		for (String[] lineToWrite : standardDerivationLines) {
			writeLinesForStandardDerivation(lineToWrite);
			progressBar.setValue((int) (50.0 + (count * 1.0 / standardDerivationLines.size()) * 100.0 * 0.5));
			count++;
		}
	}

	void writeLinesForStandardDerivation(String[] lineToWrite) throws ParseException {
		// First collect all values
		String date = lineToWrite[CommonConstants.DATE];
		String time = lineToWrite[CommonConstants.TIME];
		Date date2Write = dateFormat.parse(date + " " + time);

		if (Options.isShiftByOneHour())
			date2Write = new Date(date2Write.getTime() + CommonConstants.oneHour);

		date = dateFormat.format(date2Write).split(" ")[0];
		time = dateFormat.format(date2Write).split(" ")[1];
		double _12CO2_dry_value = parseDoubleValue(lineToWrite, InputFileConstants._12CO2_DRY);
		double _13CO2_dry_value = parseDoubleValue(lineToWrite, InputFileConstants._13CO2_DRY);
		double _solenoidValue = parseDoubleValue(lineToWrite, InputFileConstants.SOLENOID_VALVE_INPUT);
		double h2O = parseDoubleValue(lineToWrite, InputFileConstants.H2O);
		double delta5Minutes = parseDoubleValue(lineToWrite, InputFileConstants.DELTA_5_MINUTES);
		double co2Abs = Math.abs(_12CO2_dry_value + _13CO2_dry_value);
		String date_time = date + " " + time;
		// then write a new line in standardderivation file
		String[] newLine = { date, time, _12CO2_dry_value + "", _13CO2_dry_value + "", delta5Minutes + "", h2O + "", _solenoidValue + "", co2Abs + "",
				date_time };
		standardDerivationWriter.writeNext(newLine);

	}

	Map<Integer, double[]> collectValuesOfLastFiveMinutes(List<String[]> lines, int startIndex) throws ParseException {
		List<Double> fiveMinutesDeltaValues = new ArrayList<Double>();
		List<Double> _12CO2_dry_Values = new ArrayList<Double>();
		List<Double> _13CO2_dry_Values = new ArrayList<Double>();
		List<Double> _H20_Values = new ArrayList<Double>();

		Map<Integer, double[]> mapping = new HashMap<Integer, double[]>();

		mapping.put(InputFileConstants.DELTA_5_MINUTES, list2DoubleArray(fiveMinutesDeltaValues));
		mapping.put(InputFileConstants._12CO2_DRY, list2DoubleArray(_12CO2_dry_Values));
		mapping.put(InputFileConstants._13CO2_DRY, list2DoubleArray(_13CO2_dry_Values));
		mapping.put(InputFileConstants.H2O, list2DoubleArray(_H20_Values));

		String[] startLine = lines.get(startIndex);
		// save line for later computation for standard derivation
		double startSolenoid = parseDoubleValue(startLine, InputFileConstants.SOLENOID_VALVE_INPUT);
		if (startSolenoid != 1.0)
			linesNeedForStandardDerivation.add(Integer.valueOf(startIndex));
		else if (startSolenoid == 1.0 && Options.isRecordReferenceChambers())
			linesNeedForStandardDerivation.add(Integer.valueOf(startIndex));

		StringBuilder dateBuilder = new StringBuilder();
		dateBuilder.append(startLine[CommonConstants.DATE]);
		dateBuilder.append(" ");
		dateBuilder.append(startLine[CommonConstants.TIME]);
		Date startDate = dateFormat.parse(dateBuilder.toString());
		Date currentDate = startDate;

		fiveMinutesDeltaValues.add(parseDoubleValue(startLine, InputFileConstants.DELTA_5_MINUTES));
		_12CO2_dry_Values.add(parseDoubleValue(startLine, InputFileConstants._12CO2_DRY));
		_13CO2_dry_Values.add(parseDoubleValue(startLine, InputFileConstants._13CO2_DRY));
		_H20_Values.add(parseDoubleValue(startLine, InputFileConstants.H2O));

		int currentIndex = startIndex - 1;
		while (Math.abs(startDate.getTime() - currentDate.getTime()) <= CommonConstants.fiveMinutes && currentIndex >= 1) {

			String[] currentLine = lines.get(currentIndex);
			double currentSolenoid = parseDoubleValue(currentLine, InputFileConstants.SOLENOID_VALVE_INPUT);
			if (currentSolenoid != startSolenoid) {
				log.trace("Break");
				break;
			}
			// save line for later computation of standard derivation
			if (currentIndex % Options.getSampleRate() == 0 && startSolenoid != 1.0) {
				if (startSolenoid != 1.0)
					linesNeedForStandardDerivation.add(currentIndex);
				else if (startSolenoid == 1.0 && Options.isRecordReferenceChambers())
					linesNeedForStandardDerivation.add(currentIndex);
			}

			currentDate = dateFormat.parse(currentLine[CommonConstants.DATE] + " " + currentLine[CommonConstants.TIME]);
			fiveMinutesDeltaValues.add(parseDoubleValue(currentLine, InputFileConstants.DELTA_5_MINUTES));
			_12CO2_dry_Values.add(parseDoubleValue(currentLine, InputFileConstants._12CO2_DRY));
			_13CO2_dry_Values.add(parseDoubleValue(currentLine, InputFileConstants._13CO2_DRY));
			_H20_Values.add(parseDoubleValue(currentLine, InputFileConstants.H2O));

			currentIndex -= 1;
		}

		mapping.put(InputFileConstants.DELTA_5_MINUTES, list2DoubleArray(fiveMinutesDeltaValues));
		mapping.put(InputFileConstants._12CO2_DRY, list2DoubleArray(_12CO2_dry_Values));
		mapping.put(InputFileConstants._13CO2_DRY, list2DoubleArray(_13CO2_dry_Values));
		mapping.put(InputFileConstants.H2O, list2DoubleArray(_H20_Values));
		return mapping;
	}
}
