/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	Plant.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.plant;

import java.util.Date;

/**
 * Data class that holds all necessary information about a plant, so that the
 * PSR (or other things) can be computed.
 */
public class Plant {
	// start and end date of measurement
	Date startDate, endDate;
	// leaf area for different chambers and the pressure that might change
	// slightly over a measurement
	double upperLeafArea, lowerLeafArea, pressureAtStartDay, pressureAtEndDay;

	public Plant() {
		this(new Date());
	}

	public Plant(Date startDate) {
		this(startDate, new Date());
	}

	public Plant(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getUpperLeafArea() {
		return upperLeafArea;
	}

	public void setUpperLeafArea(double upperLeafArea) {
		this.upperLeafArea = upperLeafArea;
	}

	public double getLowerLeafArea() {
		return lowerLeafArea;
	}

	public void setLowerLeafArea(double lowerLeafArea) {
		this.lowerLeafArea = lowerLeafArea;
	}

	public double getPressureAtEndDay() {
		return pressureAtEndDay;
	}

	public void setPressureAtEndDay(double pressureAtEndDay) {
		this.pressureAtEndDay = pressureAtEndDay;
	}

	public double getPressureAtStartDay() {
		return pressureAtStartDay;
	}

	public void setPressureAtStartDay(double pressureAtStartDay) {
		this.pressureAtStartDay = pressureAtStartDay;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Plant: \n");
		sb.append("\t" + getStartDate() + "- \t " + getEndDate() + "\n");
		sb.append("\t upper " + getUpperLeafArea() + "\t lower " + getLowerLeafArea());

		return sb.toString();

	}

}
