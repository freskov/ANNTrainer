package hr.fer.zemris.optjava.random;

import java.util.Random;

public class RandomSingleton {

	private static Random instance;
	
	public static Random getInstance() {
		if (instance == null) {
			instance = new Random();
		}
		return instance;
	}
	
}
