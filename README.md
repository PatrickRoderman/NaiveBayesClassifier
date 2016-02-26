# NaiveBayesClassifier
Generalized Naive Bayes Classifier for text (.txt) written in Java.

## Code Example
```Java
//create new ojbect
NaiveBayesClassifier NBC = new NaiveBayesClassifier("\\keywords.txt", "\\trainingData.txt", true);
					
//classify input
String input = "Your entry here";
NBC.classify(input);
```
Configure the file location for training keywords and training entries in the NaiveBayesClassifier constructor.
Third constructor paramter is to enable evidential learning. 
You can set the threshold for evidential learning inside the NaiveBayesClassifier if you wish (no threshold by default).

## Training
In you training data text file, include entries with their clasification delimited by ":" with each indivual entry delimited by ";".
e.g.   Your training entry : ClassificationName;
