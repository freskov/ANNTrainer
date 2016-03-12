package hr.fer.zemris.optjava;

import java.io.IOException;
import java.util.Arrays;

import hr.fer.zemris.optjava.algorithm.impl.ClonAlg;
import hr.fer.zemris.optjava.algorithm.impl.PSO;
import hr.fer.zemris.optjava.ann.IFFANN;
import hr.fer.zemris.optjava.ann.ITransferFunction;
import hr.fer.zemris.optjava.ann.impl.FFANN;
import hr.fer.zemris.optjava.ann.impl.SigmoidTransferFunction;
import hr.fer.zemris.optjava.data.Dataset;

/**
 * Console program for training artificial neural networks. Expected command
 * line arguments are: file (file containing dataset), algorithm ('pso-a', 'pso-b-d'
 * or 'clonalg'), n (population size), maxerr (maximum mean squared error),
 * maxiter (maximum number of generations).
 * 
 * @author filip
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length != 5) {
			System.err.println("Expected 5 args: file, alg, n, maxerr, maxiter");
			System.exit(1);
		}

		String file = args[0];
		String alg = args[1];
		int n = Integer.parseInt(args[2]);
		double maxErr = Double.parseDouble(args[3]);
		int maxIter = Integer.parseInt(args[4]);

		Dataset dataset = Loader.loadData(file);
		System.out.println("Samples in dataset: " + dataset.getNumberOfSamples());
		IFFANN ffann = new FFANN(
				new int[] { 4, 5, 3, 3 }, 
				new ITransferFunction[] { 
						new SigmoidTransferFunction(),
						new SigmoidTransferFunction(), 
						new SigmoidTransferFunction() });

		double[] weights = null;
		if (alg.equals("pso-a")) {
			weights = new PSO(
					n, ffann.getWeightsCount(), ffann.getLossFunction(dataset), 
					maxErr, maxIter, 2, 2, 0, 1).run();
		} else if (alg.startsWith("pso-b-")) {
			int d = Integer.parseInt(alg.substring(6));
			weights = new PSO(
					n, d, ffann.getWeightsCount(), ffann.getLossFunction(dataset), 
					maxErr, maxIter, 2, 2, 0, 1).run();
		} else if (alg.equals("clonalg")) {
			weights = new ClonAlg(n, 50, 3, 10, 6, ffann.getWeightsCount(), 
					ffann.getFitnessFunction(dataset), maxErr, maxIter).run();
		} else {
			System.err.println("Unknown algorithm.");
			System.exit(2);
		}
		System.out.println(Arrays.toString(weights));
		System.out.println("error:" + ffann.getMeanSquaredError(dataset, weights));
	}

}
