package hr.fer.zemris.optjava.ann.impl;

import hr.fer.zemris.optjava.ann.ITransferFunction;

public class SigmoidTransferFunction implements ITransferFunction {

	@Override
	public double getValueAt(double x) {
		return 1./(1+Math.exp(-x));
	}

}
