/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	CO2AbsoluteOnlyEvaluator.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.evaluator.evaluators;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.lowagie.text.pdf.DefaultFontMapper;

import de.atomfrede.tools.evalutation.constants.CommonConstants;
import de.atomfrede.tools.evalutation.constants.InputFileConstants;
import de.atomfrede.tools.evalutation.constants.OutputFileConstants;
import de.atomfrede.tools.evalutation.evaluator.SingleInputFileEvaluator;
import de.atomfrede.tools.evalutation.tools.plot.TimeDatasetWrapper;
import de.atomfrede.tools.evalutation.tools.plot.TimePlot;
import de.atomfrede.tools.evalutation.tools.plot.XYDatasetWrapper;
import de.atomfrede.tools.evalutation.tools.plot.util.PlotUtil;
import de.atomfrede.tools.evalutation.util.DialogUtil;

/**
 * Computes the CO2 Absolute values and adds them as a new column to the output
 * file. The rest of the file keeps unchanged.
 * 
 * CO2-Absolute of i-th line is defined as 12CO2_DRY + 13CO2_DRY (of i-th line)
 */
public class CO2AbsoluteOnlyEvaluator extends SingleInputFileEvaluator {

	private final Log log = LogFactory.getLog(CO2AbsoluteOnlyEvaluator.class);

	public CO2AbsoluteOnlyEvaluator(File inputFile) {
		super("CO2-Absolute-Only", inputFile, null);
		this.name = "CO2 Absolute Values";
	}

	@Override
	public boolean evaluate() throws Exception {
		CSVWriter writer = null;
		try {
			outputFile = new File(outputFolder, "co2-absolute-only.csv");
			outputFile.createNewFile();
			if (!outputFile.exists())
				return false;

			writer = getCsvWriter(outputFile);

			List<String[]> lines = readAllLinesInFile(inputFile);
			log.trace("done reading all input files.");
			writeHeader(writer, lines.get(0));

			for (int i = 1; i < lines.size(); i++) {
				String[] currentLine = lines.get(i);
				// read 12CO2_DRY and 13CO2_DRY
				double _12CO2_dry = parseDoubleValue(currentLine, InputFileConstants._12CO2_DRY);
				double _13Co2_dry = parseDoubleValue(currentLine, InputFileConstants._13CO2_DRY);
				// Compute the CO2-Absolute value as the (absolute) sum of 12
				// and 13CO2_DRY
				double co2AbsoluteValue = Math.abs(_12CO2_dry + _13Co2_dry);
				// get date and time for easier post processing in calc
				Date date2Write = dateFormat.parse(currentLine[CommonConstants.DATE] + " " + currentLine[CommonConstants.TIME]);
				// append both, date (with time) and CO2 Absolute to the file
				appendDateAndCO2AbsoluteValue(writer, currentLine, date2Write, co2AbsoluteValue);

				progressBar.setValue((int) ((i * 1.0 / lines.size()) * 100.0));
			}

			progressBar.setValue(100);
		} catch (IOException ioe) {
			log.error(ioe);
			DialogUtil.getInstance().showError(ioe);
			return false;
		} catch (Exception e) {
			log.error(e);
			DialogUtil.getInstance().showError(e);
		} finally {
			if (writer != null)
				writer.close();
		}
		new Plot(outputFile).plot();
		log.info("CO2 Absolute Only Evaluator Done");
		return true;
	}

	/**
	 * Appends the given date and the co2Absolute value to the file in one
	 * single step.<br>
	 * 
	 * After execution the given line contains two additional columns, that
	 * contain the date (with time) and the CO2-Absolute value.
	 * 
	 * 
	 * @param writer
	 * @param line
	 * @param date2Write
	 * @param co2Absolute
	 */
	private void appendDateAndCO2AbsoluteValue(CSVWriter writer, String[] line, Date date2Write, double co2Absolute) {
		String[] newLine = new String[line.length + 2];
		int i = 0;
		for (i = 0; i < line.length; i++) {
			newLine[i] = line[i];
		}
		newLine[newLine.length - 2] = dateFormat.format(date2Write);
		newLine[newLine.length - 1] = co2Absolute + "";

		writer.writeNext(newLine);
	}

	/**
	 * Writes a new header to the file, where the new header is the old header
	 * with two additional columns: <br>
	 * Zeit<br>
	 * CO2 Absolute
	 * 
	 * @param writer
	 * @param line
	 */
	private void writeHeader(CSVWriter writer, String[] line) {
		String[] newLine = new String[line.length + 2];
		int i = 0;
		for (i = 0; i < line.length; i++) {
			newLine[i] = line[i];
		}
		newLine[i] = OutputFileConstants.HEADER_DATE_AND_TIME;
		newLine[i + 1] = OutputFileConstants.HEADER_CO2_ABSOLUTE;

		writer.writeNext(newLine);

	}

	public class Plot extends TimePlot {

		/**
		 * @param inputFile
		 * @param co2AbsoluteOnly
		 */
		public Plot(File inputFile) {
			super(inputFile);
		}

		@Override
		public void plot() throws Exception {
			CSVReader reader = null;
			try {
				reader = new CSVReader(new FileReader(dataFile));
				List<String[]> allLines = reader.readAll();

				XYDatasetWrapper dataset = createCO2AbsoluteDatasetWrapper(allLines);
				XYDatasetWrapper dataset2 = createDeltaFiveMinutesDatasetWrapper(allLines);
				JFreeChart chart;
				XYDatasetWrapper[] wrappers = { dataset, dataset2 };
				chart = createChart(wrappers);

				File fileName = new File(dataFile.getParent(), "plot.pdf");
				File svgFile = new File(dataFile.getParent(), "plot.svg");

				PlotUtil.saveChartAsPDF(fileName, chart, 800, 300, new DefaultFontMapper());

				PlotUtil.saveChartAsSVG(svgFile, chart, 800, 300);
			} catch (Exception e) {
				log.error("Error during plot", e);
				throw (e);
			} finally {
				if (reader != null)
					reader.close();
			}
			log.info("Plotting done.");
		}

		XYDatasetWrapper createCO2AbsoluteDatasetWrapper(List<String[]> allLines) {
			int size = allLines.get(1).length - 1;
			TimeDatasetWrapper wrapper = new TimeDatasetWrapper("CO2 Absolute", allLines, size, InputFileConstants.EPOCH_TIME);
			wrapper.createDataset();
			return wrapper;
		}

		XYDatasetWrapper createDeltaFiveMinutesDatasetWrapper(List<String[]> allLines) {
			TimeDatasetWrapper wrapper = new TimeDatasetWrapper("Delta 5 Minutes", allLines, InputFileConstants.DELTA_5_MINUTES, InputFileConstants.EPOCH_TIME);
			wrapper.createDataset();
			wrapper.setSeriesColor(Color.GREEN);
			return wrapper;
		}
	}
}
