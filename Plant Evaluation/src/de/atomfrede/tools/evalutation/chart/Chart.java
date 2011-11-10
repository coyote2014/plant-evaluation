/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	Chart.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;

public class Chart {

	public Chart() {
		DefaultStatisticalCategoryDataset dataSet = new DefaultStatisticalCategoryDataset();
		dataSet.add(25, 30, "Test", "Test");
		dataSet.add(20, 25, "Test1", "Test1");
		dataSet.add(15, 20, "Test2", "Test2");
		JFreeChart chart = ChartFactory.createLineChart("Test", "Test", "Test",
				dataSet, PlotOrientation.HORIZONTAL, false, false, false);
		// ChartFactory.createXY
		// ChartFactory.createXYLineChart("test", "test", "test", dataSet,
		// PlotOrientation.HORIZONTAL, null, null, null);
	}
}
