package hr.fer.zemris.optjava.data;

import java.util.Arrays;

public class Sample {
	
	private double[] input;
	private double[] output;
	
	public Sample(double[] input, double[] output) {
		super();
		this.input = input;
		this.output = output;
	}
	
	public double[] getInput() {
		return input;
	}
	
	public double[] getOutput() {
		return output;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(Arrays.toString(input));
		sb.append(", ");
		sb.append(Arrays.toString(output));
		sb.append(")");
		return sb.toString();
	}

}
