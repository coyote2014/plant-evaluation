/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	XYDatasetWrapper.java is part of Plant Evaluation.
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

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYDatasetWrapper extends AbstractDatasetWrapper {

	XYDataset dataset;
	Double minimum, maximum;
	int dataColumn;

	public XYDatasetWrapper(String seriesName, List<String[]> allLines, int dataColum) {
		this(seriesName, allLines);
		this.dataColumn = dataColum;
	}

	public XYDatasetWrapper(String seriesName, List<String[]> allLines) {
		super(seriesName, allLines);
		minimum = null;
		maximum = null;
	}

	public void createDataset() {
		XYSeries series = new XYSeries(seriesName);
		dataset = new XYSeriesCollection();

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), dataColumn);
			series.add(i, value);
		}

		((XYSeriesCollection) dataset).addSeries(series);
	}

	public XYDataset getDataset() {
		return dataset;
	}

	public void setDataset(XYDataset dataset) {
		this.dataset = dataset;
	}

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	public double parseDoubleValue(String[] line, int type) {
		double value = Double.parseDouble(line[type].replace(",", "."));
		if (maximum == null && minimum == null) {
			maximum = value;
			minimum = value;
		} else {
			if (value > maximum)
				maximum = value;
			if (value < minimum)
				minimum = value;
		}
		return value;
	}

	public long parseLong(String[] line, int type) {
		long value = Long.parseLong(line[type].replace(".", ""));
		return value;
	}

	public Date parseDate(String[] line, int type) {
		return new Date(parseLong(line, type));
	}
}
