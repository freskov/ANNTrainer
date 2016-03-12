package hr.fer.zemris.optjava.ann;

import hr.fer.zemris.optjava.algorithm.IFunction;
import hr.fer.zemris.optjava.data.Dataset;

public interface IFFANN {
	
	int getWeightsCount();
	IFunction getFitnessFunction(Dataset dataset);
	IFunction getLossFunction(Dataset dataset);
	double getMeanSquaredError(Dataset dataset, double[] weights);

}
