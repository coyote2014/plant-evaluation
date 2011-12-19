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
package de.atomfrede.tools.evalutation.tools.plot;

import java.util.Date;
import java.util.List;

import org.jfree.data.xy.XYDataset;

public abstract class XYDatasetWrapper {

	XYDataset dataset;
	double minimum, maximum;
	List<String[]> allLines;
	String seriesName;

	public XYDatasetWrapper(String seriesName, List<String[]> allLines) {
		this.seriesName = seriesName;
		this.allLines = allLines;

		minimum = Double.MAX_VALUE;
		maximum = Double.MIN_VALUE;
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
		return Double.parseDouble(line[type].replace(",", "."));
	}

	public long parseLong(String[] line, int type) {
		return Long.parseLong(line[type].replace(".", ""));
	}

	public Date parseDate(String[] line, int type) {
		return new Date(parseLong(line, type));
	}

}
