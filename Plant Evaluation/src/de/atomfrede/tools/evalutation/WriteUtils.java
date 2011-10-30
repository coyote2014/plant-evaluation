/**
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

package de.atomfrede.tools.evalutation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;

public class WriteUtils {

	static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SS");

	public static void writeHeader(CSVWriter writer) {
		String[] header = { Constants.DATE_HEADER, Constants.TIME_HEADER,
				Constants._12CO2_dry_HEADER, Constants._13CO2_dry_HEADER,
				Constants.delta5minutes_HEADER, Constants.H2O_HEADER,
				Constants.solenoid_HEADER, Constants.CO2_ABS_HEADER,
				Constants.COMPLETE_TIME_HEADER, Constants.CO2_DIFF_HEADER,
				Constants.DELTA_13_HEADER, Constants.TEMPERATURE_HEADER,
				Constants.PHOTO_SYNTHESIS_RATE_HEADER };
		writer.writeNext(header);
	}

	public static void appendValuesInFirstStep(Date date2Write,
			String solenoid2Write, Map<Integer, Double> type2MeanValue,
			CSVWriter writer) {
		String date = dateFormat.format(date2Write).split(" ")[0];
		String time = dateFormat.format(date2Write).split(" ")[1];
		String meanDeltaFiveMinutes = type2MeanValue
				.get(Constants.delta5minutes) + "";
		String meanH2O = type2MeanValue.get(Constants.H2O) + "";
		String mean12CO2 = type2MeanValue.get(Constants._12CO2_dry) + "";
		String mean13CO2 = type2MeanValue.get(Constants._13CO2_dry) + "";
		String co2Abs = Math
				.abs((type2MeanValue.get(Constants._12CO2_dry) + type2MeanValue
						.get(Constants._13CO2_dry)))
				+ "";
		String[] newLine = { date, time, mean12CO2, mean13CO2,
				meanDeltaFiveMinutes, meanH2O, solenoid2Write, co2Abs,
				dateFormat.format(date2Write) };
		writer.writeNext(newLine);
	}
}
