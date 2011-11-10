/**
 *  Copyright 2011 Frederik Hahne
 * 
 * 	EntryComparator.java is part of Plant Evaluation.
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class EntryComparator implements Comparator<String[]> {

	public SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss,SS");

	@Override
	public int compare(String[] line, String[] lineToCompare) {
		try {
			Date lineDate = dateFormat.parse(line[Constants.DATE] + " "
					+ line[Constants.TIME]);
			Date compareDate = dateFormat.parse(lineToCompare[Constants.DATE]
					+ " " + lineToCompare[Constants.TIME]);
			return lineDate.compareTo(compareDate);
		} catch (ParseException pe) {
			System.out.println("Parse Exception in comparator..." + pe);
		}
		return 0;
	}

}
