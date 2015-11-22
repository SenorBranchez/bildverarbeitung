package cosy.bv.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cosy.bv.converter.Channel;
import cosy.bv.converter.Image;

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

		System.out.println("1.) Read Mapping");
		
		// retrieve image paths
		patientMapping = readPatientMapping();
		
		System.out.println("2.) Calculate Feature Vectors");
		
		// calculate feature vector for every imagee
		// Loop through patients
		for(Iterator<ArrayList<ImageData>> itPatient = patientMapping.values().iterator(); itPatient.hasNext();) {

			ArrayList<ImageData> imageData = itPatient.next();

			// Loop through images of the current patient
			for(Iterator<ImageData> itImageData = imageData.iterator(); itImageData.hasNext();) {
				
				computeFeatureVectors(itImageData.next());
			}
		}
				
		System.out.println("3.) Starting Experiments");
		
		int n = 0;
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
				
		while(n < 100) {
			
			int patientNr = (int)(1 + Math.random() * patientMapping.size());
			
			// extract one patient randomly (aka the probing patient)
			ArrayList<ImageData> probingPatientImages = patientMapping.remove(patientNr);
	
			int imageNr = (int)(Math.random() * (probingPatientImages.size()));
			ImageData probingImage = probingPatientImages.get(imageNr);
			
			for(String channel : probingImage.featureVectors.keySet()) {
				
				// find NEIGHBORHOOD_SIZE-nearest neighbors
				Pattern result = classifyImage(probingImage, channel);
				
				System.out.println(result.name() + " " + probingImage.getPattern().name());
				
				if(result.name().equals(probingImage.getPattern().name())) {
					
					if(! counts.containsKey(channel)) {
						counts.put(channel, 0);
					}
					
					counts.put(channel, counts.get(channel) + 1);
				}
			}
		
			patientMapping.put(patientNr, probingPatientImages);
			
			n++;
		}
		
		System.out.println("\n\n4.) Finished");
		
		for(Map.Entry<String, Integer> res : counts.entrySet()) {
			System.out.println(res.getKey() + " " + (float) res.getValue() / n);
		}
	}

	/**
	 * Compare the vector of @param imageData to all other vectors 
	 * and find  @var NEIGHBORHOOD_SIZE nearest neighbors
	 * 
	 * @return The classification
	 */
	private Pattern classifyImage(ImageData imageData, String channelName) {

		ImageVector probingVector = imageData.featureVectors.get(channelName);
		HashMap<ImageData, Double> neighborhood = new HashMap<ImageData, Double>();

		// Loop through patients
		for(Iterator<ArrayList<ImageData>> patientIt = patientMapping.values().iterator(); patientIt.hasNext(); ) {

			// Loop through pictures of partientIt
			for(Iterator<ImageData> imageIt = patientIt.next().iterator(); imageIt.hasNext(); ) {

				// Get the next vector to compare probingVector with
				ImageData img = imageIt.next();
				ImageVector candidateVector = img.featureVectors.get(channelName);

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
		int counts[] = new int[Pattern.values().length];

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
	private void computeFeatureVectors(ImageData itImageData) {

		ImageData currentImage = itImageData;
	
		// Apply DCT to 8x8 block					
		currentImage.load();
		
		for(Map.Entry<String, Image> entry : currentImage.colorspaces.entrySet()) {
			
			Image theImage = entry.getValue();
			
			for(Map.Entry<String, Channel> channel : theImage.getChannels().entrySet()) {
			
				Channel<Double> theChannel = channel.getValue();
				
				Object[][] data = theChannel.toArray();
				ImageVector[][] coefficients = new ImageVector[data.length/DCT_BLOCKSIZE][data.length/DCT_BLOCKSIZE];
	
				for(int y = 0; y < data.length; y += DCT_BLOCKSIZE) {
					for(int x = 0; x < data[0].length; x += DCT_BLOCKSIZE) {
	
						double[][] block = new double[DCT_BLOCKSIZE][DCT_BLOCKSIZE];
	
						for(int i = 0; i < DCT_BLOCKSIZE; i++) {
	
							double[] row = new double[DCT_BLOCKSIZE];
	
							for(int j = 0; j < DCT_BLOCKSIZE; j++) {
								
								if(data[y + i][x + j] instanceof Integer) {
									row[j] = ((Integer) data[y + i][x + j]).doubleValue();
								}
								else if(data[y + i][x + j] instanceof Float) {
									row[j] = ((Float) data[y + i][x + j]).doubleValue();
								}
							}
	
							block[i] = row;
						}
	
						ImageVector coeffBlock = DctAlgorithm.compute(block, DCT_BLOCKSIZE);
						Statistics.normalize(coeffBlock);
						//System.out.println(coeffBlock);
						
						coefficients[y / DCT_BLOCKSIZE][x / DCT_BLOCKSIZE] = coeffBlock;
					}
				}
				
				currentImage.featureVectors.put(entry.getKey() + "_" + channel.getKey(), Statistics.simpleAverage(coefficients));
			}
			
			currentImage.colorspaces.remove(entry.getKey());
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
