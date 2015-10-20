package cosy.bv.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

/* 
 *
 * @author kremwolf
 * 
 * Models a full experiment run.
 */
public class Experiment {

	// == CONFIGURATION
	private static final int NEIGHBORHOOD_SIZE = 3; 
	private static final int DCT_BLOCKSIZE = 8;
	// ================
	
	// === VARS
	public static final String ASSET_PATH = "./assets"; 
	
	private HashMap<Integer, ArrayList<ImageData>> patientMapping;
	// ========
	
	
	// === METHODS
	public Experiment() {
				
		// retrieve image paths
		patientMapping = readPatientMapping();
		
		// calculate feature vector for every image
		computeFeatureVectors();
		
		// extract one patient randomly (aka the probing patient)
		ArrayList<ImageData> probingPatient = patientMapping.remove((int) Math.random() * patientMapping.size());
		
		// find NEIGHBORHOOD_SIZE-nearest neighbors
		classifyImage(probingPatient.get((int) Math.random() * probingPatient.size()));
		
		
		
	}

	/**
	 * Compare the vector of @param imageData to all other vectors 
	 * and find  @var NEIGHBORHOOD_SIZE nearest neighbors
	 * 
	 * @return The classification
	 */
	private Pattern classifyImage(ImageData imageData) {
		
		Vector probingVector = imageData.getDctVector();
		int[] nearest = new int[NEIGHBORHOOD_SIZE];
		
		
		// Loop through patients
		for(Iterator<ArrayList<ImageData>> patientIt = patientMapping.values().iterator(); patientIt.hasNext(); ) {
			
			// Loop through pictures of partientIt
			for(Iterator<ImageData> imageIt = patientIt.next().iterator(); imageIt.hasNext(); ) {
				
				ImageData img = imageIt.next();
				Vector candidateVector = img.getDctVector();				
			}
		}
		
		return null;		
	}



	/**
	 * 1. Applies DCT to every image in the mapping
	 * 2. Calculate statistics  
	 * 3. profit = feature vector of the image
	 * 
	 */
	private void computeFeatureVectors() {
		
		// Loop through patients
		for(Iterator<ArrayList<ImageData>> itPatient = patientMapping.values().iterator(); itPatient.hasNext();) {
		
			ArrayList<ImageData> imageData = itPatient.next();
			
			// Loop through images of the current patient
			for(Iterator<ImageData> itImageData = imageData.iterator(); itImageData.hasNext();) {
				
				ImageData currentImage = itImageData.next();
				
				// Apply DCT
				try {
					DctAlgorithm.compute(currentImage, DCT_BLOCKSIZE);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				
			}
		}		
	}


	/*
	 * Creates mapping patient => List of image paths belonging to patient
	 */
	private HashMap<Integer, ArrayList<ImageData>> readPatientMapping() {
				
		HashMap<Integer, ArrayList<ImageData>> ret = new HashMap<Integer, ArrayList<ImageData>>();
		
		try {
			// Create CSV Reader
			BufferedReader br = new BufferedReader(new FileReader(ASSET_PATH + "/patientmapping.csv"));
						
			String line = "";
			
			while((line = br.readLine()) != null) {
				
				// Structure patient data
				String[] patientData = line.split(";");
				String imgPath = findImageInAssets(patientData[0]);
				Integer patId = new Integer(patientData[1]);				
				
				ArrayList<ImageData> patImageList;
				
				// Get patient from list, insert if not already in there;
				if((patImageList = ret.get(patId)) == null) {
					
					patImageList = new ArrayList<ImageData>();
					ret.put(patId, patImageList);
				}
								
				// Add new ImageData to patient
				patImageList.add(new ImageData(imgPath));
			}
			
			br.close();
				
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}

	private String findImageInAssets(String imgName) throws Exception {
		
		//Open assets folder
		File assetsDir = new File(Experiment.ASSET_PATH);
		
		if(! assetsDir.isDirectory()) {
			throw new Exception("Please create \"assets\" folder under root directory containing the contents of the .tar archive!");
		}
		
		// Loop through asset folder
		for(File subAssets : assetsDir.listFiles()) {
			
			if(!subAssets.isDirectory() || !subAssets.getName().startsWith("Pit Pattern")) {
				continue;
			}
			
			// list Pit Pattern _ folder and look for imgName
			if(Arrays.asList(subAssets.list()).contains(imgName)) {
				return subAssets.getAbsolutePath() + "/" + imgName;
			}
			
		}
		
		// We couldn't find our imgName which means there is a missing file. Should not happen though...
		throw new Exception("Missmatch between CSV and actual files, could not find " + imgName);
	}
}
