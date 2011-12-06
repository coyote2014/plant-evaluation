/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	PlantHelper.java is part of Plant Evaluation.
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PlantHelper {

	static double UPPER_LEAF_AREA_PLANT_ONE = 0.105823;
	static double UPPER_LEAF_AREA_PLANT_TWO = 0.1224748;
	static double UPPER_LEAF_AREA_PLANT_THREE = 0.1277336;
	static double UPPER_LEAF_AREA_PLANT_FOUR = 0.1532759;
	static double UPPER_LEAF_AREA_PLANT_FIVE = 0.1115781;
	static double UPPER_LEAF_AREA_PLANT_SIX = 0.1099036;

	// lower leaf area in m²
	static double LOWER_LEAF_AREA_PLANT_ONE = 0.002912956;
	static double LOWER_LEAF_AREA_PLANT_TWO = 0.002663034;
	static double LOWER_LEAF_AREA_PLANT_THREE = 0.002163335;
	static double LOWER_LEAF_AREA_PLANT_FOUR = 0.0020615199;
	static double LOWER_LEAF_AREA_PLANT_FIVE = 0.0024611709;
	static double LOWER_LEAF_AREA_PLANT_SIX = 0.0032749886;

	static final String PLANT_ONE_START = "26.07.11 09:20:00";
	static final String PLANT_ONE_END = "27.07.11 13:32:00";

	static final String PLANT_TWO_START = "27.07.11 19:04:00";
	static final String PLANT_TWO_END = "29.07.11 21:31:00";

	static final String PLANT_THREE_START = "29.07.11 23:30:00";
	static final String PLANT_THREE_END = "31.07.11 20:59:00";

	static final String PLANT_FOUR_START = "31.07.11 23:54:00";
	static final String PLANT_FOUR_END = "02.08.11 21:23:00";

	static final String PLANT_FIVE_START = "06.08.11 22:03:00";
	static final String PLANT_FIVE_END = "08.08.11 21:29:00";

	static final String PLANT_SIX_START = "08.08.11 09:11:00";
	static final String PLANT_SIX_END = "10.08.11 22:03:00";

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yy HH:mm:ss");

	static List<Plant> defaultPlants = new ArrayList<Plant>();

	static {
		try {
			Plant plantOne = new Plant(dateFormat.parse(PLANT_ONE_START),
					dateFormat.parse(PLANT_ONE_END));
			plantOne.setLowerLeafArea(LOWER_LEAF_AREA_PLANT_ONE);
			plantOne.setUpperLeafArea(UPPER_LEAF_AREA_PLANT_ONE);

			// Plant plantTwo = new Plant(dateFormat.parse(PLANT_TWO_START),
			// dateFormat.parse(PLANT_TWO_END));
			// plantTwo.setLowerLeafArea(LOWER_LEAF_AREA_PLANT_TWO);
			// plantTwo.setUpperLeafArea(UPPER_LEAF_AREA_PLANT_TWO);
			//
			// Plant plantThree = new Plant(dateFormat.parse(PLANT_THREE_START),
			// dateFormat.parse(PLANT_THREE_END));
			// plantThree.setLowerLeafArea(LOWER_LEAF_AREA_PLANT_THREE);
			// plantThree.setUpperLeafArea(UPPER_LEAF_AREA_PLANT_THREE);
			//
			// Plant plantFour = new Plant(dateFormat.parse(PLANT_FOUR_START),
			// dateFormat.parse(PLANT_FOUR_END));
			// plantFour.setLowerLeafArea(LOWER_LEAF_AREA_PLANT_FOUR);
			// plantFour.setUpperLeafArea(UPPER_LEAF_AREA_PLANT_FOUR);
			//
			// Plant plantFive = new Plant(dateFormat.parse(PLANT_FIVE_START),
			// dateFormat.parse(PLANT_FIVE_END));
			// plantFive.setLowerLeafArea(LOWER_LEAF_AREA_PLANT_FIVE);
			// plantFive.setUpperLeafArea(UPPER_LEAF_AREA_PLANT_FIVE);
			//
			// Plant plantSix = new Plant(dateFormat.parse(PLANT_SIX_START),
			// dateFormat.parse(PLANT_SIX_END));
			// plantSix.setLowerLeafArea(LOWER_LEAF_AREA_PLANT_SIX);
			// plantSix.setUpperLeafArea(UPPER_LEAF_AREA_PLANT_SIX);

			defaultPlants.add(plantOne);
			// defaultPlants.add(plantTwo);
			// defaultPlants.add(plantThree);
			// defaultPlants.add(plantFour);
			// defaultPlants.add(plantFive);
			// defaultPlants.add(plantSix);

		} catch (Exception e) {
			System.out.println("Error during parsing default plants");
		}
	}

	public static List<Plant> getDefaultPlantList() {
		return defaultPlants;
	}

}
