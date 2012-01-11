/**
 *  Copyright 2011 Frederik Hahne fred
 *
 * 	AbstractPlot.java is part of Plant Evaluation.
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

import java.io.File;
import java.util.Date;

import org.jfree.chart.JFreeChart;

import de.atomfrede.tools.evalutation.tools.plot.wrapper.XYDatasetWrapper;

public abstract class AbstractPlot {

	public enum PlotType {
		SIMPLE, TIME
	}

	protected File dataFile;
	protected PlotType type;
	protected int width, height;

	public AbstractPlot(File dataFile) {
		this.dataFile = dataFile;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
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

	public PlotType getType() {
		return type;
	}

	public void setType(PlotType type) {
		this.type = type;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	protected abstract JFreeChart createChart(XYDatasetWrapper... datasetWrappers);

}
