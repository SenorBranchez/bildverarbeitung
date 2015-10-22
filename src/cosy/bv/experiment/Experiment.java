package cosy.bv.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/* 
 *
 * @author kremwolf
 * 
 * Models a full experiment run.
 */
public class Experiment {

	// == CONFIGURATION
	public static final int NEIGHBORHOOD_SIZE = 3; 
	public static final int DCT_BLOCKSIZE = 8;
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
		
		ImageVector probingVector = imageData.getDctVector();
		HashMap<ImageData, Double> neighborhood = new HashMap<ImageData, Double>();
		
		// Loop through patients
		for(Iterator<ArrayList<ImageData>> patientIt = patientMapping.values().iterator(); patientIt.hasNext(); ) {
			
			// Loop through pictures of partientIt
			for(Iterator<ImageData> imageIt = patientIt.next().iterator(); imageIt.hasNext(); ) {
				
				// Get the next vector to compare probingVector with
				ImageData img = imageIt.next();
				ImageVector candidateVector = img.getDctVector();	
				
				// Calculate distance between the vectors and see if it is small enough to 
				// be in the k-neighborhood
				double distance = probingVector.distanceTo(candidateVector);
				
				if(neighborhood.size() < NEIGHBORHOOD_SIZE) {
					neighborhood.put(img, distance);
				}
				else {
					for(Entry<ImageData, Double> entry : neighborhood.entrySet()) {
						
						if(distance < entry.getValue()) {
							neighborhood.remove(entry.getKey());
							neighborhood.put(img, distance);
							break;
						}
					}
				}
			}
		}
		
		
		//Now that we have found the k-nearest neighbors, look at the most common type
		int counts[] = new int[6];
		
		for(Iterator<ImageData> it =  neighborhood.keySet().iterator(); it.hasNext(); ) {
			counts[it.next().getPattern().ordinal()]++;
		}
		
		Pattern max = Pattern.PATTERN_1;
		
		for(int i = 0; i < counts.length; i++) {
			
			if(counts[i] > counts[max.ordinal()]) {
				max = Pattern.values()[i];
			}
		}
		
		return max;		
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

	/**
	 * Find image with filename imgName in the Pit Pattern directories
	 * @param imgName
	 * @return
	 * @throws Exception
	 */
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
