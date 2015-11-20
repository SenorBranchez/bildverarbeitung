package cosy.bv.experiment;

public class DctAlgorithm {

	public static ImageVector compute(double[][] block, int blocksize) {
		
//		for(double[] i : block) {
//			System.out.print("\n");
//			for(double j : i){
//				System.out.print(j + ", ");
//			}
//		}
		
		
		ImageVector ret = new ImageVector(block.length);
		
		//Nested loop for calculating the colums		
		for(int row = 0; row < block.length; row++) {
			ret.data[row] = dct1D(block[row]);			
		}
		
		double[][] transposed = new double[block.length][block.length];
		
		for(int i = 0; i < block.length; i++) {
			for(int j = 0; j < block.length; j++) {
				transposed[i][j] = ret.data[j][i];
			}
		}

		//Nested loop for calculating the colums		
		for(int row = 0; row < block.length; row++) {
			ret.data[row] = dct1D(transposed[row]);			
		}
		
//		System.out.println(ret);
		
		return ret;
	}

	private static double[] dct1D(double[] input) {
			
		double[] ret = new double[input.length];
		
		for(int k = 0; k < input.length; k++) {
			
			Double sum = 0.0;
			Double cu = (k == 0) ? Math.sqrt(2) : 0;
			
			
			for(int n = 0; n < input.length; n++) {
				sum += input[n] * Math.cos(((Math.PI / input.length) * (n + 0.5) * k) );
			}
			
			ret[k] = sum;
		}
		
		return ret;		
	}
}
