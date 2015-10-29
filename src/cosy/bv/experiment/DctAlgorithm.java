package cosy.bv.experiment;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class DctAlgorithm {

	public static void compute(ImageData imgData, int blocksize) throws Exception {

		int[][] pixels = convertTo2D(imgData.load());
		ImageVector ret = new ImageVector(pixels.length);
		
		//Nested loop for calculating the colums		
		for(int row = 0; row < pixels.length; row++) {
			dct1D(pixels[row], ret.data[row]);			
		}
		
		int[][] transposed = new int[pixels.length][pixels.length];
		
		for(int i = 0; i < pixels.length; i++) {
			for(int j = 0; j < pixels.length; j++) {
				transposed[i][j] = (int) ret.data[j][i];
			}
		}

		//Nested loop for calculating the colums		
		for(int row = 0; row < pixels.length; row++) {
			dct1D(transposed[row], ret.data[row]);			
		}
		
		
		System.out.println(ret);
		imgData.setDctVector(ret);
	}

	private static void dct1D(int[] input, double[] output) {
				
		for(int offset = 0; offset * Experiment.DCT_BLOCKSIZE < input.length; offset++) {
		
			for(int i = 0; i < Experiment.DCT_BLOCKSIZE; i++) {
				
				int sum = 0;
				
				for(int x = 0; x < Experiment.DCT_BLOCKSIZE; x++) {
					
					//int value = (input[i] >> 16) & 0xFF;
					sum +=  input[i] * Math.cos(Math.PI / Experiment.DCT_BLOCKSIZE * (x + 0.5) * i);
				}
				
				if(i == 0) {
					sum *= 1.0 / Math.sqrt(2.0);
				}
				
				output[i * offset] = sum / 2;			
			}
		}
	}
	
	
	private static int[][] convertTo2D(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();

		int[][] result = new int[height][width];

		final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
			int argb = 0;
//			argb += -16777216; // 255 alpha
//			argb += ((int) pixels[pixel] & 0xff); // blue
//			argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
			argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
			result[row][col] = argb;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}

		return result;
	}
}
