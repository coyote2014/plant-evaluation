/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	TimeDatasetWrapper.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.wrapper;

import java.util.Date;
import java.util.List;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TimeDatasetWrapper extends XYDatasetWrapper {

	int timeColumn;

	public TimeDatasetWrapper(String seriesName, List<String[]> allLines, int dataColumn, int timeColumn) {
		super(seriesName, allLines);
		this.dataColumn = dataColumn;
		this.timeColumn = timeColumn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.atomfrede.tools.evalutation.tools.plot.XYDatasetWrapper#createDataset
	 * ()
	 */
	@Override
	public void createDataset() {
		XYSeries series = new XYSeries(seriesName);
		dataset = new XYSeriesCollection();

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), dataColumn);
			Date date = parseDate(allLines.get(i), timeColumn);
			series.add(date.getTime(), value);
		}

		((XYSeriesCollection) dataset).addSeries(series);

	}
}
