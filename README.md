# Naive Bayes Classifier (Java)
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

## Set Up
Training Entries
In a training data text file, include entries with their clasification delimited by ":" with each indivual entry delimited by ";".
e.g.   Your training entry : ClassificationName;

Key Words
In a key words text file, include your initial associations. Key words which belong to the same classification should be delimited by "," and the group associated to its classification by ":". The classified key words should be delimited by ";"
e.g.  keyWord1, keyWord2, keyWord3 : ClassificationName; 
