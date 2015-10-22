package cosy.bv.experiment;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageData {	
			
	private String path = "";
	private ImageVector DctVector;
	private Pattern pattern;
	
	public ImageData(String path) {
		this.path = path;
		
		if(path.contains("Pit Pattern I/")) {
			pattern = Pattern.PATTERN_1;
		}
		else if(path.contains("Pit Pattern II/")) {
			pattern = Pattern.PATTERN_2;
		}
		else if(path.contains("Pit Pattern III L/")) {
			pattern = Pattern.PATTERN_3L;
		}
		else if(path.contains("Pit Pattern III S/")) {
			pattern = Pattern.PATTERN_3S;
		}
		else if(path.contains("Pit Pattern IV/")) {
			pattern = Pattern.PATTERN_4;
		}
		else if(path.contains("Pit Pattern V/")) {
			pattern = Pattern.PATTERN_5;
		}
		
		System.out.println("Created " + path + " " + pattern.ordinal());
	}
	
	public BufferedImage load() {
		
		try {
			return ImageIO.read(new File(path));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public void setDctVector(ImageVector vector) {
		this.DctVector = vector;
	}
	
	public ImageVector getDctVector() {
		return this.DctVector;
	}
	
	public Pattern getPattern() {
		return this.pattern;
	}
}
