package de.atomfrede.tools.evalutation.interval;

public abstract class AbstractInterval {

	protected double min, max;

	public AbstractInterval(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public abstract boolean isInside(double value);

}
