package cosy.bv.experiment;

import java.util.Arrays;

public class ImageVector {

	double[][] data;
	
	public ImageVector(int size) {
		
		data = new double[size][size]; 
	}
	
	/**
	 * Calculate the euclidean distance between two vectors
	 */
	public double distanceTo(ImageVector v2) {
		
		double sum = 0;
		
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data.length; j++) {
				sum += Math.pow(data[i][j] - v2.data[i][j], 2);
			}
		}
		
		return Math.sqrt(sum);
	}

	@Override
	public String toString() {
		
		String ret = "";
		
		
		for(int i = 0; i < 5; i++) {
			
			ret += Arrays.toString(data[i]) + "\n";
		}

		return ret;
	}
}
