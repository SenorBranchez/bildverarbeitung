package cosy.bv.experiment;

public class Statistics {

	public static ImageVector simpleAverage(ImageVector[][] coefficients) {
		
		int n = coefficients.length * coefficients[0].length;
		
		double[][] data = new double[Experiment.DCT_BLOCKSIZE][Experiment.DCT_BLOCKSIZE];
		
		for(int y = 0; y < coefficients.length; y++) {
			for(int x = 0; x < coefficients[0].length; x++) {
				
				for(int i = 0; i < Experiment.DCT_BLOCKSIZE; i++) {
					for(int j = 0; j < Experiment.DCT_BLOCKSIZE; j++) {
						data[i][j] += coefficients[y][x].data[i][j] / n;
					}
				}
				
			}
		}

		return new ImageVector(data);
	}
	
	public static ImageVector simpleVariance(ImageVector[][] coefficients) {
		
		int n = coefficients.length * coefficients[0].length;
		ImageVector avg = simpleAverage(coefficients);
		
		double[][] variance = new double[Experiment.DCT_BLOCKSIZE][Experiment.DCT_BLOCKSIZE];
		
		
		for(int y = 0; y < coefficients.length; y++) {
			for(int x = 0; x < coefficients.length; x++) {
				
				for(int i = 0; i < Experiment.DCT_BLOCKSIZE; i++) {
					for(int j = 0; j < Experiment.DCT_BLOCKSIZE; j++) {
						
						variance[i][j] += Math.pow(coefficients[y][x].data[i][j] - avg.data[i][j], 2) / n;
					}
				}
			} 
		}	
		
		return new ImageVector(variance);
	}
	
	
	public static void normalize(ImageVector block) {
		
		double min = 0, max = 0, offset = 0;
		int a = 0, b = 255;
		
		for(int y = 0; y < block.data.length; y++) {
			for(int x = 0; x < block.data.length; x++) {
				double val = block.data[y][x];
				
				if(val > max) {
					max = val;
				}
				else if(val < min) {
					min = val;
				}
			}
		}
		
		offset = Math.abs(min);
				
		min = min + offset;
		max = max + offset;
		
		for(int y = 0; y < block.data.length; y++) {
			for(int x = 0; x < block.data.length; x++) {
				// Make everything positive
				block.data[y][x] = block.data[y][x] + offset;
				// Scale from 0 to 255
				//block.data[y][x] = Math.round((a + (block.data[y][x] - min) * (b-a)) / (max - min)); 
			}
		}
	}
}
