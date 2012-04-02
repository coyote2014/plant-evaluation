/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	TypeBevaluation.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.evaluator.evaluation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.evaluator.AbstractEvaluator;
import de.atomfrede.tools.evalutation.evaluator.evaluators.CO2AbsoluteOnlyEvaluator;
import de.atomfrede.tools.evalutation.evaluator.evaluators.CopyEvaluator;
import de.atomfrede.tools.evalutation.evaluator.evaluators.PickDatasetEvaluator;
import de.atomfrede.tools.evalutation.evaluator.evaluators.ReduceDatasetEvaluator;
import de.atomfrede.tools.evalutation.options.TypeBEvaluationOptions;

/**
 * Type B evaluation, aka Ingo's evaluation
 */
public class TypeBevaluation extends AbstractEvaluation {

	private final Log log = LogFactory.getLog(TypeBevaluation.class);

	CopyEvaluator copyEvaluator;
	CO2AbsoluteOnlyEvaluator co2absEvaluator;
	ReduceDatasetEvaluator reduceDatasetEvalutor;
	PickDatasetEvaluator pickDatasetEvaluator;

	public TypeBevaluation() {
		copyEvaluator = new CopyEvaluator();
		co2absEvaluator = new CO2AbsoluteOnlyEvaluator(copyEvaluator.getOutputFile());
		reduceDatasetEvalutor = new ReduceDatasetEvaluator(co2absEvaluator.getOutputFile(), TypeBEvaluationOptions.getDensity());
		pickDatasetEvaluator = new PickDatasetEvaluator(reduceDatasetEvalutor.getOutputFile());
		evaluators.add(copyEvaluator);
		evaluators.add(co2absEvaluator);
		evaluators.add(reduceDatasetEvalutor);
		evaluators.add(pickDatasetEvaluator);
	}

	@Override
	public void evaluate() throws Exception {
		log.trace("CO2 Absolute Only Evaluation started.");
		int i = 0;
		boolean done = true;
		while (i < evaluators.size()) {
			if (i == evaluators.size())
				break;
			AbstractEvaluator evaluator = evaluators.get(i);
			done = evaluator.evaluate();
			if (!done)
				break;
			else {
				if (evaluator instanceof CopyEvaluator) {
					CopyEvaluator cpe = (CopyEvaluator) evaluator;
					co2absEvaluator.setInputFile(cpe.getOutputFile());
					i++;
					continue;
				}
				if (evaluator instanceof CO2AbsoluteOnlyEvaluator) {
					CO2AbsoluteOnlyEvaluator eva = (CO2AbsoluteOnlyEvaluator) evaluator;
					reduceDatasetEvalutor.setInputFile(eva.getOutputFile());
					i++;
					continue;
				}
				if (evaluator instanceof ReduceDatasetEvaluator) {
					ReduceDatasetEvaluator eva = (ReduceDatasetEvaluator) evaluator;
					eva.new CO2AbsoluteDeltaRawPlot(eva.getOutputFile(), TypeBEvaluationOptions.isCo2AbsoluteAutoscale(), TypeBEvaluationOptions.isDeltaRawAutoscale()).plot();
					pickDatasetEvaluator.setInputFile(eva.getOutputFile());
					i++;
					continue;
				}
			}
			i++;
		}
	}
}
