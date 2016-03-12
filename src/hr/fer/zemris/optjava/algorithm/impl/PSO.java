package hr.fer.zemris.optjava.algorithm.impl;

import java.util.Random;

import hr.fer.zemris.optjava.algorithm.IFunction;
import hr.fer.zemris.optjava.algorithm.IOptAlgorithm;
import hr.fer.zemris.optjava.random.RandomSingleton;

public class PSO implements IOptAlgorithm {
	
	private int n;
	private int d;
	private int dimension;
	private IFunction lossFunction;
	private double maxErr;
	private int maxIter;
	private double c1;
	private double c2;
	private double wMin;
	private double wMax;
	private double[] gBest;
	private double gBestF;
	
	public PSO(int n, int dimension, IFunction lossFunction, double maxErr, 
			int maxIter, double c1, double c2, double wMin, double wMax) {
		super();
		this.n = n;
		this.d = 0;
		this.dimension = dimension;
		this.lossFunction = lossFunction;
		this.maxErr = maxErr;
		this.maxIter = maxIter;
		this.c1 = c1;
		this.c2 = c2;
		this.wMin = wMin;
		this.wMax = wMax;
	}

	public PSO(int n, int d, int dimension, IFunction lossFunction, double maxErr, 
			int maxIter, double c1, double c2, double wMin, double wMax) {
		super();
		this.n = n;
		this.d = d;
		this.dimension = dimension;
		this.lossFunction = lossFunction;
		this.maxErr = maxErr;
		this.maxIter = maxIter;
		this.c1 = c1;
		this.c2 = c2;
		this.wMin = wMin;
		this.wMax = wMax;
	}

	@Override
	public double[] run() {
		Particle[] swarm = generateSwarm();
		int iter = 0;
		while (iter++ < maxIter) {
			evaluate(swarm);
			if (iter%100 == 0) System.out.println("generation "+iter+": err="+gBestF);
			if (gBestF < maxErr) break;
			updateParticles(swarm, iter);
			
		}
		return gBest;
	}
	
	private Particle[] generateSwarm() {
		Particle[] swarm = new Particle[n];
		for (int i = 0; i < n; ++i) {
			swarm[i] = generateRandomParticle();
		}
		return swarm;
	}

	private Particle generateRandomParticle() {
		Random rand = RandomSingleton.getInstance();
		Particle p = new Particle(dimension);
		for (int i = 0; i < dimension; ++i) {
			p.x[i] = 2*rand.nextDouble()-1;
			p.v[i] = 2*rand.nextDouble()-1; 
		}
		return p;
	}

	private void evaluate(Particle[] swarm) {
		for (int i = 0; i < n; ++i) {
			Particle p = swarm[i];
			// evaluate
			double f = lossFunction.getValueAt(p.x);
			// new personal best?
			if (p.pBest == null || f < p.pBestF) {
				p.pBest = p.x;
				p.pBestF = f;
			}
			// new local best?
			for (int j = -d; j <= d; ++j) {
				Particle neighbor = swarm[(i+j+n)%n];
				if (neighbor.lBest == null || f < neighbor.lBestF) {
					neighbor.lBest = swarm[i].x;
					neighbor.lBestF = f;
				}
			}
			// new global best?
			if (gBest == null || f < gBestF) {
				gBest = p.x;
				gBestF = f;
			}
 		}
	}

	private void updateParticles(Particle[] swarm, int iter) {
		Random rand = RandomSingleton.getInstance();
		double w = (double)iter/maxIter*(wMax-wMin) + wMin;
		for (int j = 0; j < swarm.length; ++j) {
			Particle p = swarm[j];
			for (int i = 0; i < dimension; ++i) {
				if (d == 0) {
					p.v[i] = w*p.v[i] + c1*rand.nextDouble()*(p.pBest[i]-p.x[i]) + 
							c2*rand.nextDouble()*(gBest[i]-p.x[i]);
				} else {
					p.v[i] = w*p.v[i] + c1*rand.nextDouble()*(p.pBest[i]-p.x[i]) + 
							c2*rand.nextDouble()*(p.lBest[i]-p.x[i]);
				}
				p.x[i] = p.x[i] + p.v[i];
			}
		}
	}

	private class Particle {
		
		double[] x;
		double[] v;
		double[] pBest;
		double pBestF;
		double[] lBest;
		double lBestF;
		
		public Particle(int dimension) {
			x = new double[dimension];
			v = new double[dimension];
		}
				
	}

}
