/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	AbstractDatasetWrapper.java is part of Plant Evaluation.
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

import java.awt.Color;
import java.util.List;

public abstract class AbstractDatasetWrapper {

	List<String[]> allLines;
	String seriesName;
	Color seriesColor = Color.ORANGE;
	Float stroke = 0.3f;

	public AbstractDatasetWrapper(String seriesName, List<String[]> allLines) {
		this.seriesName = seriesName;
		this.allLines = allLines;

	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public List<String[]> getAllLines() {
		return allLines;
	}

	public void setAllLines(List<String[]> allLines) {
		this.allLines = allLines;
	}

	public Color getSeriesColor() {
		return seriesColor;
	}

	public void setSeriesColor(Color seriesColor) {
		this.seriesColor = seriesColor;
	}

	public Float getStroke() {
		return stroke;
	}

	public void setStroke(Float stroke) {
		this.stroke = stroke;
	}
}
