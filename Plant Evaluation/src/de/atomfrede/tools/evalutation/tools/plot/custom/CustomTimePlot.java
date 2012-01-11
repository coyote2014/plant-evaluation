/**
 *  Copyright 2012 Frederik Hahne 
 *
 * 	CustomTimePlot.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.custom;

import java.io.File;

import org.jfree.chart.JFreeChart;

import com.lowagie.text.pdf.DefaultFontMapper;

import de.atomfrede.tools.evalutation.options.Options;
import de.atomfrede.tools.evalutation.tools.plot.TimePlot;
import de.atomfrede.tools.evalutation.tools.plot.util.PlotUtil;
import de.atomfrede.tools.evalutation.tools.plot.wrapper.TimeDatasetWrapper;

public class CustomTimePlot extends TimePlot {

	int width;
	int height;
	TimeDatasetWrapper[] wrappers;
	String fileName;

	/**
	 * @param inputFile
	 */
	public CustomTimePlot(File inputFile, String fileName, int width, int height, TimeDatasetWrapper... wrappers) {
		super(inputFile);
		this.width = width;
		this.height = height;
		this.wrappers = wrappers;
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.atomfrede.tools.evalutation.tools.plot.TimePlot#plot()
	 */
	@Override
	public void plot() throws Exception {
		JFreeChart chart = createChart(wrappers);

		File fileName = new File(Options.getOutputFolder(), this.fileName + ".pdf");
		File svgFile = new File(Options.getOutputFolder(), this.fileName + ".svg");

		PlotUtil.saveChartAsPDF(fileName, chart, width, height, new DefaultFontMapper());

		PlotUtil.saveChartAsSVG(svgFile, chart, width, height);
	}
}
