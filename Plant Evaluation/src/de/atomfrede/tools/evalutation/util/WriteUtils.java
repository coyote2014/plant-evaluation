/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	WriteUtils.java is part of Plant Evaluation.
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import de.atomfrede.tools.evalutation.constants.CommonConstants;
import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.constants.OutputFileConstants;

import au.com.bytecode.opencsv.CSVWriter;

public class WriteUtils {

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

	public static void writeHeader(CSVWriter writer) {
		String[] header = { CommonConstants.DATE_HEADER, CommonConstants.TIME_HEADER, OutputFileConstants.HEADER_12_CO2_DRY,
				OutputFileConstants.HEADER_13_CO2_DRY, OutputFileConstants.HEADER_DELTA_5_MINUTES, OutputFileConstants.HEADER_H2O,
				OutputFileConstants.HEADER_SOLENOID_VALVE, OutputFileConstants.HEADER_CO2_ABSOLUTE, OutputFileConstants.HEADER_DATE_AND_TIME,
				OutputFileConstants.HEADER_CO2_DIFFERENCE, OutputFileConstants.HEADER_DELTA_13, OutputFileConstants.HEADER_TEMPERATURE,
				OutputFileConstants.HEADER_PHOTO_SYNTHESIS_RATE, OutputFileConstants.HEADER_PHOTO_SYNTHESIS_RATE_STANDARD_DEVIVATION,
				OutputFileConstants.HEADER_DELTA_13_STANDARD_DEVIVATION };
		writer.writeNext(header);
	}

	public static void appendMeanValues(Date date2Write, String solenoid2Write, Map<Integer, Double> type2MeanValue, CSVWriter writer) {
		String date = dateFormat.format(date2Write).split(" ")[0];
		String time = dateFormat.format(date2Write).split(" ")[1];
		String meanDeltaFiveMinutes = type2MeanValue.get(InputFileConstants.DELTA_5_MINUTES) + "";
		String meanH2O = type2MeanValue.get(InputFileConstants.H2O) + "";
		String mean12CO2 = type2MeanValue.get(InputFileConstants._12CO2_DRY) + "";
		String mean13CO2 = type2MeanValue.get(InputFileConstants._13CO2_DRY) + "";
		String co2Abs = Math.abs((type2MeanValue.get(InputFileConstants._12CO2_DRY) + type2MeanValue.get(InputFileConstants._13CO2_DRY))) + "";
		String[] newLine = { date, time, mean12CO2, mean13CO2, meanDeltaFiveMinutes, meanH2O, solenoid2Write, co2Abs, dateFormat.format(date2Write) };
		writer.writeNext(newLine);
	}

	public static void appendColumn(String[] line, String valueToAppend, CSVWriter writer) {
		String[] newLine = new String[line.length + 1];
		int i = 0;
		for (i = 0; i < line.length; i++) {
			newLine[i] = line[i];
		}
		newLine[i] = valueToAppend;
		writer.writeNext(newLine);
	}
}
