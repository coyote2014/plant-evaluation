package de.atomfrede.tools.evalutation.tools.query;

import java.io.File;

public abstract class AbstractQuery {

	protected File inputFile;

	public AbstractQuery(File inputFile) {
		this.inputFile = inputFile;
	}

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

}
