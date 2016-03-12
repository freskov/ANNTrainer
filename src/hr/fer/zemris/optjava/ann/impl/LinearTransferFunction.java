package hr.fer.zemris.optjava.ann.impl;

import hr.fer.zemris.optjava.ann.ITransferFunction;

public class LinearTransferFunction implements ITransferFunction {

	@Override
	public double getValueAt(double x) {
		return x;
	}

}
