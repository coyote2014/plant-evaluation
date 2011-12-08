/**
 *  Copyright 2011 Frederik Hahne fred
 *
 * 	SimplePlot.java is part of Plant Evaluation.
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import au.com.bytecode.opencsv.CSVReader;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

public class SimplePlot extends AbstractPlot {

	private final Log log = LogFactory.getLog(SimplePlot.class);

	public SimplePlot(File dataFile) {
		super(dataFile);
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

		File fileName = new File(System.getProperty("user.home") + "/co2absolute.pdf");

		saveChartAsPDF(fileName, chart, 400, 300, new DefaultFontMapper());

	}

	public double parseDoubleValue(String[] line, int type) {
		return Double.parseDouble(line[type].replace(",", "."));
	}

	JFreeChart createChart(XYDataset dataset, XYDataset dataset2) {
		JFreeChart chart = ChartFactory.createXYLineChart("CO2 Absolute",// chart
																			// title
				"Time (indexed)",
				// x axis label
				"CO2 Absolute",
				// y axis label
				dataset,
				// data
				PlotOrientation.VERTICAL, true,
				// include legend
				true,
				// tooltips
				false
		// urls
				);
		XYPlot plot = (XYPlot) chart.getPlot();
		//

		plot.setDataset(1, dataset2);

		NumberAxis axis2 = new NumberAxis("Delta Raw");
		plot.setRangeAxis(1, axis2);

		plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
		plot.getRangeAxis(1).setLowerBound(-20.0);
		plot.getRangeAxis(1).setUpperBound(5.0);
		plot.getRangeAxis(0).setLowerBound(250);
		plot.getRangeAxisForDataset(0).setUpperBound(1100.0);
		plot.mapDatasetToRangeAxis(1, 1);

		plot.getRenderer(0).setSeriesPaint(0, Color.ORANGE);
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		plot.setRenderer(1, renderer);

		plot.getRenderer(1).setSeriesPaint(0, Color.GREEN);
		// plot.getRenderer(1).setB
		return chart;

	}

	XYDataset createDataset(List<String[]> allLines) {
		XYSeries series = new XYSeries("CO2 Absolute");
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), 37);
			series.add(i, value);
		}

		dataset.addSeries(series);
		return dataset;
	}

	XYDataset createDeltaRawDataSet(List<String[]> allLines) {
		XYSeries series = new XYSeries("DeltaRaw");
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (int i = 1; i < allLines.size(); i++) {
			double value = parseDoubleValue(allLines.get(i), 22);
			series.add(i, value);
		}

		dataset.addSeries(series);
		return dataset;
	}

	public static void saveChartAsPDF(File file, JFreeChart chart, int width, int height, FontMapper mapper) throws IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		writeChartAsPDF(out, chart, width, height, mapper);
		out.close();
	}

	public static void writeChartAsPDF(OutputStream out, JFreeChart chart, int width, int height, FontMapper mapper) throws IOException {
		Rectangle pagesize = new Rectangle(width, height);
		Document document = new Document(pagesize, 50, 50, 50, 50);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			// document.addAuthor("Plant-Evaluation");
			// document.addSubject("CO2 Absolute Only");
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = cb.createTemplate(width, height);
			Graphics2D g2 = tp.createGraphics(width, height, mapper);
			Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
			chart.draw(g2, r2D);
			g2.dispose();
			cb.addTemplate(tp, 0, 0);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}
		document.close();
	}

}
