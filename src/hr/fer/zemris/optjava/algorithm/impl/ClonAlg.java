package hr.fer.zemris.optjava.algorithm.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.algorithm.IFunction;
import hr.fer.zemris.optjava.algorithm.IOptAlgorithm;
import hr.fer.zemris.optjava.random.RandomSingleton;

public class ClonAlg implements IOptAlgorithm {
	
	private int n;
	private int l;
	private int d;
	private double beta;
	private double ro;
	private int dimension;
	private IFunction antigene;
	private double maxErr;
	private int maxIter;
	
	public ClonAlg(int n, int l, int d, double beta, double ro, int dimension, IFunction fitnessFunction, double maxErr,
			int maxIter) {
		super();
		this.n = n;
		this.l = l;
		this.d = d;
		this.beta = beta;
		this.ro = ro;
		this.dimension = dimension;
		this.antigene = fitnessFunction;
		this.maxErr = maxErr;
		this.maxIter = maxIter;
	}

	@Override
	public double[] run() {
		Antibody[] population = generateAntibodies(l);
		evaluate(population);
		int iter = 0;
		while (iter++ < maxIter) {
			population = pick(population, n);
			Antibody[] pClone = clone(population);
			Antibody[] pHyper = hypermutate(pClone);
			evaluate(pHyper);
			population = pick(pHyper, n);
			Antibody[] pBirth = generateAntibodies(d);
			replace(population, pBirth);
			evaluate(population);
			double err = 1./population[0].affinity; 
			if (err < maxErr) break;
			if (iter%100 == 0) System.out.println("generation "+iter+": err="+err);
		}
		return population[0].x;
	}
	
	private Antibody[] generateAntibodies(int n) {
		Antibody[] population = new Antibody[n];
		for(int i = 0; i < n; ++i) {
			population[i] = generateAntibody();
		}
		return population;
	}

	private Antibody generateAntibody() {
		Random rand = RandomSingleton.getInstance();
		Antibody ab = new Antibody(dimension);
		for (int i = 0; i < dimension; ++i) {
			ab.x[i] = rand.nextDouble()*2-1;
		}
		ab.affinity = antigene.getValueAt(ab.x);
		return ab;
	}

	private void evaluate(Antibody[] population) {
		Arrays.sort(population, (a, b) -> -Double.compare(a.affinity, b.affinity));
	}

	private Antibody[] pick(Antibody[] population, int n) {
		return Arrays.copyOf(population, n);
	}

	private Antibody[] clone(Antibody[] population) {
		List<Antibody> pClone = new LinkedList<>();
		for (int i = 0; i < n; ++i) {
			int nC = (int) (beta * n / (i+1));
			for (int j = 0; j < nC; ++j) {
				Antibody ab = new Antibody(population[i].x);
				pClone.add(ab);
			}
		}
		return pClone.toArray(population);
	}

	private Antibody[] hypermutate(Antibody[] population) {
		Random rand = RandomSingleton.getInstance();
		for (int j = 1; j < population.length; ++j) {
			Antibody ab = population[j];
			double p = Math.exp(-ro*ab.affinity);
			for (int i = 0; i < dimension; ++i) {
				if (rand.nextDouble() < p) {
					ab.x[i] += rand.nextGaussian();
				}
			}
			ab.affinity = antigene.getValueAt(ab.x);
		}
		return population;
	}

	private void replace(Antibody[] population, Antibody[] pBirth) {
		int k = (n-1)/d;
		for (int i = 0; i < d; ++i) {
			population[(i+1)*k] = pBirth[i];
		}
	}

	private class Antibody {
		
		double[] x;
		double affinity;
		
		public Antibody(int dimension) {
			x = new double[dimension];
		}

		public Antibody(double[] x) {
			this.x = Arrays.copyOf(x, x.length);
		}
		
	}

}
