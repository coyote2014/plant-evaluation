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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.constants.CommonConstants;

/**
 * Special comparator that compares two lines of a .csv file and compares them
 * by date and time
 */
public class EntryComparator implements Comparator<String[]> {

	private final Log log = LogFactory.getLog(EntryComparator.class);

	public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

	@Override
	public int compare(String[] line, String[] lineToCompare) {
		try {
			Date lineDate = dateFormat.parse(line[CommonConstants.DATE] + " " + line[CommonConstants.TIME]);
			Date compareDate = dateFormat.parse(lineToCompare[CommonConstants.DATE] + " " + lineToCompare[CommonConstants.TIME]);
			return lineDate.compareTo(compareDate);
		} catch (ParseException pe) {
			log.error("Parse Exception in comparator..." + pe);
		}
		return 0;
	}

}
