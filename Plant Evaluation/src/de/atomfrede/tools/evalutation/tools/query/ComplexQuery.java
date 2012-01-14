package de.atomfrede.tools.evalutation.tools.query;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.atomfrede.tools.evalutation.interval.AbstractInterval;

/**
 * A more complex query that can query multiple columns for different intervals.
 */
public class ComplexQuery extends AbstractQuery {

	Map<Integer, AbstractInterval> column_to_interval;

	public ComplexQuery(File inputFile) {
		super(inputFile);
		this.column_to_interval = new HashMap<Integer, AbstractInterval>();
	}

	public void addQuery(int column, AbstractInterval interval) {
		this.column_to_interval.put(column, interval);
	}
}
