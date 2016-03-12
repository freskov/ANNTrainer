package hr.fer.zemris.optjava.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dataset implements Iterable<Sample> {
	
	private List<Sample> samples = new ArrayList<>();
	
	public Dataset() {
	}
	
	public Dataset(Sample[] samples) {
		for (Sample s : samples) {
			this.samples.add(s);
		}
	}
	
	public int getNumberOfSamples() {
		return samples.size();
	}
	
	public int getInputDimension() {
		return samples.get(0).getInput().length;
	}
	
	public int getOutputDimension() {
		return samples.get(0).getOutput().length;
	}
	
	public void add(Sample s) {
		samples.add(s);
	}
	
	public Sample get(int index) {
		return samples.get(index);
	}

	@Override
	public Iterator<Sample> iterator() {
		return samples.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dataset=");
		sb.append(samples.toString());
		return sb.toString();
	}

}
