/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	FileUtil.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.util;

import java.io.File;
import java.security.CodeSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import de.atomfrede.tools.evalutation.ui.AppWindow;

public class JarUtil {

	protected static File getJarLocation() throws Exception {
		CodeSource codeSource = AppWindow.class.getProtectionDomain().getCodeSource();
		File jarFile = new File(codeSource.getLocation().toURI().getPath());

		return jarFile;
	}

	/**
	 * Start the file logging and puts the log file in the directoty where the
	 * executable jar is located instad of using the user.home directory.
	 * 
	 * @throws Exception
	 */
	public static void startFileLogging() throws Exception {
		File jarFile = getJarLocation();
		// only when the program is executed from an executable jar setup
		// file-logging.
		// Otherwise console logging is sufficient.
		if (jarFile.getName().endsWith(".jar")) {
			DailyRollingFileAppender fileLogger = new DailyRollingFileAppender();

			// now setup file logging
			File logDir = new File(jarFile.getParentFile(), "log");

			if (!logDir.exists() && !logDir.mkdirs())
				throw new Exception("Can't create log dir!");

			File logFile = new File(logDir, "log4j.log");

			if (!logFile.exists())
				logFile.createNewFile();
			if (!logFile.canWrite())
				throw new Exception("Can't write log file " + logFile.getPath());

			Level level = Level.toLevel("DEBUG");

			fileLogger.setFile(logFile.getAbsolutePath());

			// fileLogger.setDatePattern();
			// set logging pattern
			fileLogger.setLayout(new PatternLayout("%-5p %d{dd MMM yyyy HH:mm:ss} %c:%L %x - %m%n"));
			// set log level
			fileLogger.setThreshold(level);

			fileLogger.activateOptions();

			BasicConfigurator.configure(fileLogger);
		}
	}
}
