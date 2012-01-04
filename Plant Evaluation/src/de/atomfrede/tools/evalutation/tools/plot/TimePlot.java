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

import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.tools.plot.util.PlotUtil;

public class TimePlot extends AbstractPlot {

	private final Log log = LogFactory.getLog(TimePlot.class);
	boolean co2AbsoluteOnly;

	public TimePlot(File inputFile, boolean co2AbsoluteOnly) {
		super(inputFile);
		setType(PlotType.TIME);
		this.co2AbsoluteOnly = co2AbsoluteOnly;
	}

	public void plot() throws Exception {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(dataFile));
			List<String[]> allLines = reader.readAll();

			XYDatasetWrapper dataset = createCO2AbsoluteDatasetWrapper(allLines);
			XYDatasetWrapper dataset2 = createDeltaRawDatasetWrapper(allLines);
			XYDatasetWrapper dataset3 = createDeltaFiveMinutesDatasetWrapper(allLines);
			JFreeChart chart;
			if (co2AbsoluteOnly) {
				XYDatasetWrapper[] wrappers = { dataset, dataset3 };
				chart = createChart(wrappers);
			} else {
				XYDatasetWrapper[] wrappers = { dataset, dataset2 };
				chart = createChart(wrappers);
			}
			// JFreeChart chart = createChart(dataset, dataset2);

			File fileName = new File(dataFile.getParent(), "co2absolute-time.pdf");
			File svgFile = new File(dataFile.getParent(), "co2absolute-time.svg");

			PlotUtil.saveChartAsPDF(fileName, chart, 800, 300, new DefaultFontMapper());

			PlotUtil.saveChartAsSVG(svgFile, chart, 800, 300);
		} catch (Exception e) {
			log.error("Error during plot", e);
			throw (e);
		} finally {
			if (reader != null)
				reader.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.atomfrede.tools.evalutation.tools.plot.AbstractPlot#createChart(de
	 * .atomfrede.tools.evalutation.tools.plot.XYDatasetWrapper[])
	 */
	@Override
	JFreeChart createChart(XYDatasetWrapper... datasetWrappers) {
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

	@Deprecated
	JFreeChart createChart(XYDatasetWrapper dataset, XYDatasetWrapper dataset2) {
		log.trace("createChart() called");

		JFreeChart chart = ChartFactory.createTimeSeriesChart(dataset.getSeriesName() + "/" + dataset2.getSeriesName(), "Time", dataset.getSeriesName(),
				dataset.getDataset(), true, true, false);

		XYPlot plot = (XYPlot) chart.getPlot();
		// first add the second plot (-> delta raw)
		plot.setDataset(1, dataset2.getDataset());

		// change the number axis for second dataset
		NumberAxis axis2 = new NumberAxis(dataset2.getSeriesName());
		plot.setRangeAxis(1, axis2);
		plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
		// minimum and maximum for Delta raw axis
		plot.getRangeAxis(1).setLowerBound(dataset2.getMinimum() - 15.0);
		plot.getRangeAxis(1).setUpperBound(dataset2.getMaximum() - dataset2.getMaximum() + 1000);
		// map the second dataset to the second axis
		plot.mapDatasetToRangeAxis(1, 1);

		// adapt minimum and maximum for first dataset
		// range for CO2-Absolute axis
		plot.getRangeAxis(0).setLowerBound(dataset.getMinimum());
		plot.getRangeAxis(0).setUpperBound(dataset.getMaximum());

		// some additional "design" stuff for the plot
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

		// format the date axis
		DateAxis axis = (DateAxis) plot.getDomainAxis();

		axis.setDateFormatOverride(new SimpleDateFormat("dd.MM HH:mm"));
		axis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1));
		axis.setVerticalTickLabels(true);

		return chart;
	}

	XYDatasetWrapper createCO2AbsoluteDatasetWrapper(List<String[]> allLines) {
		int size = allLines.get(1).length - 1;
		TimeDatasetWrapper wrapper = new TimeDatasetWrapper("CO2 Absolute", allLines, size, InputFileConstants.EPOCH_TIME);
		wrapper.createDataset();
		return wrapper;
	}

	XYDatasetWrapper createDeltaRawDatasetWrapper(List<String[]> allLines) {
		TimeDatasetWrapper wrapper = new TimeDatasetWrapper("Delta Raw", allLines, InputFileConstants.DELTA_RAW, InputFileConstants.EPOCH_TIME);
		wrapper.createDataset();
		wrapper.setSeriesColor(Color.GREEN);
		return wrapper;
	}

	XYDatasetWrapper createDeltaFiveMinutesDatasetWrapper(List<String[]> allLines) {
		TimeDatasetWrapper wrapper = new TimeDatasetWrapper("Delta 5 Minutes", allLines, InputFileConstants.DELTA_5_MINUTES, InputFileConstants.EPOCH_TIME);
		wrapper.createDataset();
		wrapper.setSeriesColor(Color.GREEN);
		return wrapper;
	}

	@Deprecated
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

	@Deprecated
	XYDataset createDataset(List<String[]> allLines) {

		XYSeries series = new XYSeries("CO2 Absolute");
		XYSeriesCollection dataset = new XYSeriesCollection();

		int size = allLines.get(1).length - 1;

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), size);
			Date date = parseDate(allLines.get(i), InputFileConstants.EPOCH_TIME);
			series.add(date.getTime(), value);
		}

		dataset.addSeries(series);
		return dataset;
	}

	@Deprecated
	XYDataset createDeltaRawDataSet(List<String[]> allLines) {
		XYSeries series = new XYSeries("DeltaRaw");
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), InputFileConstants.DELTA_RAW);
			Date date = parseDate(allLines.get(i), InputFileConstants.EPOCH_TIME);
			series.add(date.getTime(), value);
		}

		dataset.addSeries(series);
		return dataset;
	}

}
