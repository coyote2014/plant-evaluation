/**
 * 	Evaluator.java is part of Plant Evaluation.
 *
 *  Plant Evaluation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Foobar is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.atomfrede.tools.evalutation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Evaluator {

	File input;
	File output;

	public Evaluator() {

	}

	// reading the complete input
	List<String[]> getFirstChamber() throws IOException {
		CSVReader reader = new CSVReader(new FileReader(input));
		return reader.readAll();
	}

	int findEndOfFirstChamber(List<String[]> values) {
		for (String[] line : values) {

		}
		return 0;
	}

}
