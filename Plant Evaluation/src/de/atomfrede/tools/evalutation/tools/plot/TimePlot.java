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
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import au.com.bytecode.opencsv.CSVReader;

import com.lowagie.text.pdf.DefaultFontMapper;

import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.tools.plot.util.PlotUtil;

public class TimePlot extends AbstractPlot {

	private final Log log = LogFactory.getLog(TimePlot.class);

	public TimePlot(File inputFile) {
		super(inputFile);

		try {
			plot();
		} catch (Exception e) {
			log.error("Error during plot", e);
		}
	}

	void plot() throws Exception {

		CSVReader reader = new CSVReader(new FileReader(dataFile));

		List<String[]> allLines = reader.readAll();

		XYDataset dataset = createDataset(allLines);
		XYDataset dataset2 = createDeltaRawDataSet(allLines);
		JFreeChart chart = createChart(dataset, dataset2);

		File fileName = new File(Options.getOutputFolder(), "co2absolute-time.pdf");
		File svgFile = new File(Options.getOutputFolder(), "co2absolute-time.svg");

		PlotUtil.saveChartAsPDF(fileName, chart, 800, 300, new DefaultFontMapper());

		PlotUtil.saveChartAsSVG(svgFile, chart, 800, 300);

	}

	JFreeChart createChart(XYDataset dataset, XYDataset dataset2) {
		// Simple plot withpout time x axis
		JFreeChart chart = ChartFactory.createTimeSeriesChart("CO2 Absolute", "Time", "CO2 Absolute", dataset, true, true, false);

		XYPlot plot = (XYPlot) chart.getPlot();

		plot.setDataset(1, dataset2);

		NumberAxis axis2 = new NumberAxis("Delta Raw");
		plot.setRangeAxis(1, axis2);

		plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
		plot.getRangeAxis(1).setLowerBound(-25.0);
		plot.getRangeAxis(1).setUpperBound(3.0);
		plot.getRangeAxis(0).setLowerBound(300);
		plot.getRangeAxisForDataset(0).setUpperBound(1800.0);
		plot.mapDatasetToRangeAxis(1, 1);

		plot.getRenderer(0).setSeriesPaint(0, Color.ORANGE);
		plot.getRenderer(0).setSeriesStroke(0, new BasicStroke(0.2F));
		// new renderer for secondary axes
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);

		renderer.setSeriesStroke(0, new BasicStroke(0.2F));

		plot.setRenderer(1, renderer);

		plot.getRenderer(1).setSeriesPaint(0, Color.GREEN);

		plot.setBackgroundPaint(Color.white);
		plot.setDomainMinorGridlinePaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		DateAxis axis = (DateAxis) plot.getDomainAxis();

		axis.setDateFormatOverride(new SimpleDateFormat("dd.MM HH:mm"));
		axis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 2));
		axis.setVerticalTickLabels(true);

		return chart;

	}

	XYDataset createDataset(List<String[]> allLines) {
		XYSeries series = new XYSeries("CO2 Absolute");
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), 37);
			Date date = parseDate(allLines.get(i), 4);
			series.add(date.getTime(), value);
		}

		dataset.addSeries(series);
		return dataset;
	}

	XYDataset createDeltaRawDataSet(List<String[]> allLines) {
		XYSeries series = new XYSeries("DeltaRaw");
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), 22);
			Date date = parseDate(allLines.get(i), 4);
			series.add(date.getTime(), value);
		}

		dataset.addSeries(series);
		return dataset;
	}

}
