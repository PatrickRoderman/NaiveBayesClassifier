/** 
 * 	NaiveBayesClassifier
 * 	A generalized implementation of a text based Naive Bayes
 * 	Classifier.
 * 
 * 	@author PatrickRoderman
 * 	@version 1.0
 * 	@since   2016-02-21 
 */

package bayes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class NaiveBayesClassifier {
	//	boolean settings
	//	add text and its predicted classification into the model
	public boolean isEvidential = false;
	
	// (feature names , (class, count))
	public HashMap<String, HashMap<String, Integer>> features = new HashMap<String, HashMap<String, Integer>>();

	// count for classes  (class, count)
	public HashMap<String, Integer> classCounts = new HashMap<String, Integer>();

	// maintains vector of keywords/features
	public Vector<String> featureNames = new Vector<String>();

	public NaiveBayesClassifier(String featureFile, String trainingFile, boolean isEvidential) {
		this.isEvidential = isEvidential;
		// get features from file
		System.out.println("Attempting to read features file...");
		Vector<String> keyWordLines = fileReader(featureFile);
		System.out.println("Features successfully read.");
		// add features and classes to globals
		addFeatures(keyWordLines);
		
		// read training data and add to counters
		System.out.println("Attempting to read training file...");
		Vector<String> trainingLines = fileReader(trainingFile);
		System.out.println("Training entries successfully read.\n");
		
		//set counters for each feature and class occurrence
		setCounters(trainingLines);
	}
	
	/** 
	 * 	Sets the global data structures (increments features and classes per instance of occurrence)
	 * 	@param (Vector<String>) trainingLines - training text stored in individual elements
	 */
	public void setCounters(Vector<String> trainingLines) {
		Vector<String> lines = trainingLines;
		
		//initializes features maps and class counts
		for (String line : lines) {
			
			//parses features and class
			String[] sample = line.split(" : ");
			String sampleClass = "";
			// Check file format
			try{ 
				sampleClass = sample[1];
			}catch(Exception NullPointerException){
				System.out.println("Invalid file formating - reconfigure file");
				sampleClass = "";
			}
			String[] allWords = sample[0].split(" ");
			Vector<String> lineFeatures = getFeatures(allWords);
			
			//sets default values in HashMap to handle null
			for (String feat : lineFeatures) {
				//features.get(feat).probs.put(sampleClass, 0);
				features.get(feat).put(sampleClass, 0);
			}
		}
		
		//sets counts
		for (String line : lines) {
			String[] sample = line.split(" : ");
			String sampleClass = sample[1];
			String[] allWords = sample[0].split(" ");

			// update count for features
			Vector<String> lineFeatures = getFeatures(allWords);
			// update class count
			classCounts.put(sampleClass, (classCounts.get(sampleClass) + 1));
			for (String feat : lineFeatures) {
				//features.get(feat).probs.put(sampleClass,(features.get(feat).probs.get(sampleClass)+1));
				features.get(feat).put(sampleClass,(features.get(feat).get(sampleClass)+1));
			}	
		}
	}
	
	/** 
	 *	Reads file and returns Vector of elements delimited by new lines
	 *	@param (String) filePath - global path to file
	 *	@return (Vector<String>) - each element contains a line from file
	 */
	public Vector<String> fileReader(String filePath) {
		
		Vector<String> extracted = new Vector<String>();
		Vector<String> allLines = new Vector<String>();
		String fileName = filePath;
		String line = null;
		
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				// add line
				allLines.add(line);
			}
			bufferedReader.close();
			

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
			return null;
		} catch (IOException ex) {
			System.out.println("Error reading '" + fileName + "'");
			return null;
		} finally {
			
			for(String s : allLines){
				//check if empty
				String check = s.replaceAll(" ", "");
				
				String entry = "";
				if(s.contains(";")){
					String append = s.replace(";", "");
					entry = entry + append;
					extracted.add(entry);
				}else if(check.equals("")){
					
					
				}else{
					entry = entry + s;
				}
			}
			
		}
		return extracted;
	}
	
	/** 
	 * 	Returns features from specified class
	 * 	@param (String[]) alWords - each word from text stored in different elements
	 * 	@param (String) featureClass - the name of the class you want to get features from
	 *  @return (Vector<String>) filtered - features from class name : String featureClass
	 */
	public Vector<String> getFeatures(String[] allWords, String featureClass) {
		Vector<String> filtered = new Vector<String>();
		// only add keywords from V into filtered
		for (int i = 0; i < allWords.length; i++) {
			for (String keyword : featureNames) {
				if ((allWords[i].toLowerCase()).contains(keyword.toLowerCase())) {
					//filtered.add(allWords[i].toLowerCase());
					filtered.add(keyword.toLowerCase());
				}
			}
		}
		// return all non-duplicate values from filtered Vector
		return (Vector<String>) removeDups(filtered);
	}
	
	/** 
	 * 	Returns all features
	 * 	@param (String[]) allWords - each word from text stored in different element
	 *  @return (Vector<String>) filtered - contains all features from String[] allWords
	 */	
	public Vector<String> getFeatures(String[] allWords) {
		Vector<String> filtered = new Vector<String>();

		for (int i = 0; i < allWords.length; i++) {
			for (String keyword : featureNames) {
				if ((allWords[i].toLowerCase()).contains((keyword).toLowerCase())) {
					//filtered.add(allWords[i].toLowerCase());
					filtered.add(keyword.toLowerCase());
				}
			}
		}
		// return all non-duplicate values from filtered Vector
		return (Vector<String>) removeDups(filtered);
	}

	/** 
	 * 	Parses line for features.
	 * 	Individual features should be delimited by "," and all features delimited by ":" from their class
	 *	Both features and their class should be delimited by a new line
	 *	@param (Vector<String> lines - each element should contain the entire line/review/text
	 */
	public void addFeatures(Vector<String> lines) {
		for (String sample : lines) {
			// remove spaces
			sample = sample.replaceAll("\\s+", "");
			
			// separate into features, and class
			String[] associate = sample.split(":");
			
			// separate features and class
			String[] allFeatures = associate[0].split(",");
			String featuresClass = associate[1];
			Vector<String> featuresVector = new Vector<String>();

			// copy features into list to remove duplicates
			for (int i = 0; i < allFeatures.length; i++) {
				featuresVector.add(allFeatures[i]);
			}
			Vector<String> featuresNoDups = (Vector<String>) removeDups(featuresVector);
			
			// add feature Class to vector to start
			classCounts.put(featuresClass, 0);
			for (String feature : featuresNoDups) {
				// add feature and its details to feature (HashMap)
				features.put(feature, new HashMap<String, Integer>());
				features.put(feature, new HashMap<String, Integer>());
				
				// add features to feature vector
				featureNames.add(feature);
			}
		}
	}

	/** 
	 *	Removes duplicate keywords from lists
	 *	@param (List<T> list - list for duplicate removal
	 *	@return (Vector<T>) - Vector without any duplicates
	 */
	private static <T> List<T> removeDups(List<T> list) {
		return new Vector<T>(new LinkedHashSet<T>(list));
	}

	/** 
	 * 	Calculates conditional probability P(Features | Class)
	 *	@param (Vector<String>) sample - text for feature extraction
	 *  @param (String) featureClass - name of Class
	 *	@return (double) P(Features | Class)
	 */
	public double conditionalProb(Vector<String> sample, String featureClass){
		double prob = 1;
		String file = sample.get(0);
		String[] allWords = file.split(" ");
		Vector<String> featuresList = getFeatures(allWords);
		
		//get all classes
		Set<String> keys = classCounts.keySet();
		int combinedClassCount = 0;
		
		for(String key : keys){
			
			combinedClassCount += classCounts.get(key);
		}
				
		//counter for no prior data
		int offset = 0;
		for (String s : featuresList) {
			if(features.get(s).get(featureClass) == null){
				features.get(s).put(featureClass, 0);
			}
			System.out.println( "P(" + s + " | " + featureClass +") = " + features.get(s).get(featureClass) + "/" + classCounts.get(featureClass));
			double featureProb = features.get(s).get(featureClass);
			
			if(featureProb == 0){
				offset++;
			}else{
				prob += featureProb; 
			}
		}
		System.out.println("\n" + prob + "/" + ((combinedClassCount) - offset));
		return prob/(combinedClassCount - offset);
	}
	
	/** 
	 * 	Displays conditional probabilities and conclusion - P(Features | Class)
	 *	@param (String) filePath - global file path to text you want classified (.txt)
	 *	@return (String) - class name with highest probability
	 */
	public String classify(String filePath){
		
		//parse input to lines
		Vector<String> sample = fileReader(filePath);
		
		//Get all Class Names
		Set<String> allClasses = classCounts.keySet();
		
		
		Vector<String> featureClasses = new Vector<String>();
		
		// Store all class probabilities
		double[] classProb = new double[classCounts.size()];
		
		// Checks for reviews
		if(sample.size() == 1){
			int counter = 0;
		
			for(String featureClass : allClasses){
				System.out.println("---------------------------------");
				featureClasses.add(featureClass);
				classProb[counter] = conditionalProb(sample, featureClass);
				counter++;
			}
			System.out.println("---------------------------------");
			
			//sort array
			double highestValue = 0;
			String highestValueClass = "";
		
			for(int i = 0; i<classProb.length; i++){
				System.out.println("P("+ featureClasses.get(i) + ") = " + classProb[i]);
				//set highest value
				if(highestValue < classProb[i]){
					highestValue = classProb[i];
					highestValueClass = featureClasses.get(i);
				}
			}
			
			System.out.println("---------------------------------");
			
			System.out.println("\n\nCONCLUSION:\n----------------\nThe review is most likely to be classified as : " + highestValueClass + ".\n");
			
			// Evidential learning / set counters
			
			if(isEvidential){
				// append predicted Classification class to review
				sample.set(0, sample.get(0)+ " : " + highestValueClass);
				setCounters(sample);
			}
			
			return highestValueClass;
		}else{
			//If supplied file contains more or less than one line
			System.out.println("The file you loaded was not properly formated.");
			return null;
		}
	}
}