package de.atomfrede.tools.evalutation.interval;

public class ClosedInterval extends AbstractInterval {

	public ClosedInterval(double min, double max) {
		super(min, max);
	}

	/**
	 * Returns if the given value lies within the closed interval.
	 * 
	 * So this method returns true iff:<br>
	 * min <= value <= max
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public boolean isInside(double value) {
		if (value >= min) {
			if (max >= value)
				return true;
		}
		return false;
	}

}
