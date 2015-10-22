package cosy.bv.experiment;

public class ImageVector {

	double[][] data;
	
	public ImageVector() {
		
		data = new double[Experiment.DCT_BLOCKSIZE][Experiment.DCT_BLOCKSIZE]; 
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
}
