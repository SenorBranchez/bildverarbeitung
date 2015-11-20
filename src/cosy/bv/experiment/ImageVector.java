package cosy.bv.experiment;

import java.util.Arrays;

public class ImageVector {

	double[][] data;
	
	public ImageVector(int size) {
		
		data = new double[size][size]; 
	}
	
	public ImageVector(double[][] data) {
		this.data = data;
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
		
		
		for(int i = 0; i < data.length; i++) {
			
			ret += data[i].length + Arrays.toString(data[i]) + "\n";
		}

		System.out.print("\n\n");
		
		return ret;
	}
	
	public void addImageVector(ImageVector imageVector) {
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[0].length; j++) {
				data[i][j] += imageVector.data[i][j];
			}
		}
	}
	
	public void simpleAverage() {
		int n = data.length * data[0].length;
		
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < data[0].length; j++) {
				data[i][j] = data[i][j] / n;
			}
		}
	}
}
