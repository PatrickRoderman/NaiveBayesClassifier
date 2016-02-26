import java.util.Scanner;

import bayes.NaiveBayesClassifier;

public class Init {

	public static Scanner s = new Scanner(System.in);
	public static void main(String[] args) {
		// String for System.In
		String input = "";
		System.out.println(
				"======================================================================================\n\n\n"
				+ "                            NAIVE BAYES TEXT CLASSIFIER                               \n"
				+ "                                  Patrick Roderman                                    \n\n\n"
				+ "======================================================================================\n"
				);
		// Features,Training Data file paths, isEvidential (Evidential learning)
		NaiveBayesClassifier NBC = new NaiveBayesClassifier("E:\\Spring2016\\ML\\NaiveBayesClassifierTXT\\keywords.txt", 
															"E:\\Spring2016\\ML\\NaiveBayesClassifierTXT\\trainingData.txt", 
															true
															);
	
		// input loop
		boolean isRunning = true;
		while(isRunning){
			System.out.println(""
					+ "======================================================================================\n"
					+ "Enter Review (txt) file path to Classify: "
					+ "");
			while (!s.hasNext()) s.next();
			input = s.next();
			System.out.println("======================================================================================\n");
			//Predict Text Class
			NBC.classify(input);
			
		}
	}
}

