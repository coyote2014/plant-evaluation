/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	TimePlot.java is part of Plant Evaluation.
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import de.atomfrede.tools.evalutation.tools.plot.wrapper.XYDatasetWrapper;

public abstract class TimePlot extends AbstractPlot {

	private final Log log = LogFactory.getLog(TimePlot.class);

	public TimePlot(File inputFile) {
		super(inputFile);
		setType(PlotType.TIME);
	}

	public abstract void plot() throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.atomfrede.tools.evalutation.tools.plot.AbstractPlot#createChart(de
	 * .atomfrede.tools.evalutation.tools.plot.XYDatasetWrapper[])
	 */
	@Override
	protected JFreeChart createChart(XYDatasetWrapper... datasetWrappers) {
		XYDatasetWrapper mainDataset = datasetWrappers[0];

		JFreeChart chart = ChartFactory.createTimeSeriesChart(mainDataset.getSeriesName(), "Time", mainDataset.getSeriesName(), mainDataset.getDataset(), true,
				true, false);

		XYPlot plot = (XYPlot) chart.getPlot();
		// all adjustments for first/main dataset
		plot.getRangeAxis(0).setLowerBound(mainDataset.getMinimum());
		plot.getRangeAxis(0).setUpperBound(mainDataset.getMaximum());
		// some additional "design" stuff for the plot
		plot.getRenderer(0).setSeriesPaint(0, mainDataset.getSeriesColor());
		plot.getRenderer(0).setSeriesStroke(0, new BasicStroke(mainDataset.getStroke()));

		for (int i = 1; i < datasetWrappers.length; i++) {
			XYDatasetWrapper wrapper = datasetWrappers[i];
			plot.setDataset(i, wrapper.getDataset());
			chart.setTitle(chart.getTitle().getText() + "/" + wrapper.getSeriesName());

			NumberAxis axis = new NumberAxis(wrapper.getSeriesName());
			plot.setRangeAxis(i, axis);
			plot.setRangeAxisLocation(i, AxisLocation.BOTTOM_OR_RIGHT);

			plot.getRangeAxis(i).setLowerBound(wrapper.getMinimum() - 15.0);
			plot.getRangeAxis(i).setUpperBound(wrapper.getMaximum() + 15.0);
			// map the second dataset to the second axis
			plot.mapDatasetToRangeAxis(i, i);

			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setBaseShapesVisible(false);
			renderer.setSeriesStroke(0, new BasicStroke(wrapper.getStroke()));
			plot.setRenderer(i, renderer);
			plot.getRenderer(i).setSeriesPaint(0, wrapper.getSeriesColor());
		}
		// change the background and gridline colors
		plot.setBackgroundPaint(Color.white);
		plot.setDomainMinorGridlinePaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		// format the date axis
		DateAxis axis = (DateAxis) plot.getDomainAxis();

		axis.setDateFormatOverride(new SimpleDateFormat("dd.MM HH:mm"));
		axis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1));
		axis.setVerticalTickLabels(true);
		return chart;
	}
}
