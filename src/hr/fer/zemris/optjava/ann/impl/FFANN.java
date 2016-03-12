package hr.fer.zemris.optjava.ann.impl;

import hr.fer.zemris.optjava.algorithm.IFunction;
import hr.fer.zemris.optjava.ann.IFFANN;
import hr.fer.zemris.optjava.ann.ITransferFunction;
import hr.fer.zemris.optjava.data.Dataset;
import hr.fer.zemris.optjava.data.Sample;

public class FFANN implements IFFANN {
	
	private int[] layers;
	private ITransferFunction[] functions;
	private int weightsCount;
	
	public FFANN(int[] layers, ITransferFunction[] functions) {
		super();
		this.layers = layers;
		for (int i = 1; i < layers.length; ++i) {
			weightsCount += layers[i]*(layers[i-1]+1);
		}
		this.functions = functions;
	}

	@Override
	public int getWeightsCount() {
		return weightsCount;
	}
	
	@Override
	public IFunction getFitnessFunction(Dataset dataset) {
		return new IFunction() {
			@Override
			public double getValueAt(double[] x) {
				return 1./getMeanSquaredError(dataset, x);
			}
		};
	}
	
	@Override
	public IFunction getLossFunction(Dataset dataset) {
		return new IFunction() {
			@Override
			public double getValueAt(double[] x) {
				return getMeanSquaredError(dataset, x);
			}
		};
	}
	
	@Override
	public double getMeanSquaredError(Dataset dataset, double[] weights) {
		double error = 0;
		for (Sample sample : dataset) {
			double[] inputs = sample.getInput();
			double[] outputs = new double[dataset.getOutputDimension()];
			calcOutputs(inputs, weights, outputs);
			for (int i = 0; i < outputs.length; ++i) {
				error += Math.pow(sample.getOutput()[i]-outputs[i], 2);
			}
		}
		return error/dataset.getNumberOfSamples();
	}
	
	public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
		checkArgs(inputs, weights, outputs);
		
		// first layer
		ITransferFunction f = new LinearTransferFunction();
		double[] in = new double[inputs.length];
		for (int i = 0; i < inputs.length; ++i) {
			in[i] = f.getValueAt(inputs[i]);
		}
		
		// other layers
		for (int layer = 1, weight = 0; layer < layers.length; ++layer) {
			double[] out = new double[layers[layer]];
			for (int j = 0; j < layers[layer]; ++j) {
				double net = weights[weight++];
				for (int i = 0; i < layers[layer-1]; ++i) {
					net += weights[weight++]*in[i];
				}
				out[j] = functions[layer-1].getValueAt(net);
			}
			if (layer == layers.length-1) {
				// last layer
				for (int i = 0; i < outputs.length; ++i) {
					outputs[i] = out[i];
				}
			} else {
				in = out;
			}
		}
	}
	
	private void checkArgs(double[] inputs, double[] weights, double[] outputs) {
		if (inputs.length != layers[0]) {
			throw new IllegalArgumentException("Illegal input dimension:"+inputs.length);
			}
		if (weights.length != weightsCount) {
			throw new IllegalArgumentException("Illegal weigths dimension:"+weights.length);
		}
		if (outputs.length != layers[layers.length-1]) {
			throw new IllegalArgumentException("Illegal output dimension:"+outputs.length);
		}
	}

}
