package de.atomfrede.tools.evalutation.tools.query;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.interval.AbstractInterval;

/**
 * Simple query class that queries a dataset (in form of a .csv file) for values
 * of a specific column that lie inside a specific interval.
 * 
 * In particular it will return all values of column i that are [min,max], so
 * min and max belong to the interval.
 */
public class SimpleQuery extends AbstractQuery {

	private final Log log = LogFactory.getLog(SimpleQuery.class);

	int column;
	AbstractInterval interval;

	public SimpleQuery(int column, AbstractInterval interval, File inputFile) {
		super(inputFile);
		this.column = column;
		this.interval = interval;
		log.trace("SimpleQuery created with column " + column + " and interval " + interval);
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public AbstractInterval getInterval() {
		return interval;
	}

	public void setInterval(AbstractInterval interval) {
		this.interval = interval;
	}

}
