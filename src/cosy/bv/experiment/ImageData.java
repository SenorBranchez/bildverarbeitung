package cosy.bv.experiment;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import cosy.bv.converter.ColorSpaceConverter;
import cosy.bv.converter.Image;
import cosy.bv.converter.RGBImage;

public class ImageData {	
			
	private String path = "";
	private ImageVector DctVector;
	private Pattern pattern;
	
	public HashMap<String, Image> colorspaces = new HashMap<String, Image>();
	public HashMap<String, ImageVector> featureVectors = new HashMap<String, ImageVector>();
	
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
	}
	
	public BufferedImage load() {
		
		try {
			RGBImage rgbImg = new RGBImage(path);
			
			colorspaces.put("rgb", rgbImg);
//			colorspaces.put("hsv", ColorSpaceConverter.convertRgbToHsv(rgbImg));
//			colorspaces.put("lab", ColorSpaceConverter.convertRgbToLab(rgbImg));
//			colorspaces.put("yuv", ColorSpaceConverter.convertRgbToYuv(rgbImg));
			
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
	
	public String getPath() {
		return this.path;
	}
}
