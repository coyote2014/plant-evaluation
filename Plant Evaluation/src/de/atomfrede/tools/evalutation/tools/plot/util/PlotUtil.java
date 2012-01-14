/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	PlotUtil.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.util;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.*;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Utility methods for handling plotting with jfree chart.
 */
public class PlotUtil {

	private static final Log log = LogFactory.getLog(PlotUtil.class);

	/**
	 * Saves the given chart as a pdf file with the given file name.
	 * 
	 * @param file
	 * @param chart
	 * @param width
	 * @param height
	 * @param mapper
	 * @throws IOException
	 */
	public static void saveChartAsPDF(File file, JFreeChart chart, int width, int height, FontMapper mapper) throws IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		writeChartAsPDF(out, chart, width, height, mapper);
		out.close();
	}

	public static void saveChartAsSVG(File file, JFreeChart chart, int width, int height) throws Exception {
		Writer out = null;
		try {
			DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

			String svgNS = "http://www.w3.org/2000/svg";
			org.w3c.dom.Document document = domImpl.createDocument(svgNS, "svg", null);

			SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
			svgGenerator.getGeneratorContext().setPrecision(6);

			chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height), null);

			boolean useCSS = true;
			out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			svgGenerator.stream(out, useCSS);

		} catch (UnsupportedEncodingException enc) {
			log.error(enc);
		} catch (FileNotFoundException fnf) {
			log.error(fnf);
		} catch (SVGGraphics2DIOException e) {
			log.error(e);
		} catch (DOMException dome) {
			log.error(dome);
		} finally {
			if (out != null)
				out.close();
		}

	}

	protected static void writeChartAsPDF(OutputStream out, JFreeChart chart, int width, int height, FontMapper mapper) throws IOException {
		Rectangle pagesize = new Rectangle(width, height);
		Document document = new Document(pagesize, 50, 50, 50, 50);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
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
