package nlp.hw2.dict.wsd.pojo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ThresholdBasedScoring {
	ArrayList<ArrayList<Double>> predictionScores;
	ArrayList<ArrayList<Double>> givenScores;
	
	/*
	 * Reads the score file in the following format
	 * <Sure/Unsure Bit> <Sense Score_1> ... <Sense Score_n> @  <Prediction Score_1> ... <Prediction Score_n>
	 */
	ThresholdBasedScoring(String filename) {
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(filename));
			String line = null;
			predictionScores = new ArrayList<ArrayList<Double>>();
			givenScores = new ArrayList<ArrayList<Double>>();
			int counter = 0;
			while((line = reader.readLine())!= null){
				StringTokenizer tr = new StringTokenizer(line, "@");
				String numLine;
				if(tr.hasMoreTokens()){
					numLine = tr.nextToken();
					StringTokenizer elems = new StringTokenizer(numLine);
					ArrayList<Double> act = new ArrayList<Double>();
					while(elems.hasMoreElements()){
						act.add(Double.parseDouble(elems.nextToken().trim()));
					}
					givenScores.add(act);
				}
				
				if (tr.hasMoreTokens()){
					numLine = tr.nextToken();
					StringTokenizer pelems = new StringTokenizer(numLine);
					ArrayList<Double> pred = new ArrayList<Double>();
					while(pelems.hasMoreElements()){
						pred.add(Double.parseDouble(pelems.nextToken().trim()));
					}
					predictionScores.add(pred);
				}
				if (givenScores.get(counter).size() != (predictionScores.size()+1)) {
					/*
					 * This is just a sanity check!
					 * Ideally this should never happen. 
					 */
					System.out.println("Error: The array sizes don't match"); 
				}
				counter++;
			}
			
		} catch(FileNotFoundException e) {
			System.out.println("Error: File " + filename + " not found");
		} catch (IOException e){
			System.out.println("Error: In reading the file");
		}
	}
	
	Double GetAccuracy(Double threshold) {
		Double accuracy = 0.0, totalElems = 0.0;
		for(int i = 0; i < predictionScores.size(); i++){
			int predIndex = predictionScores.get(i).size()-1;
			Double elemAccuracy = 0.0;
			while(predIndex >= 0){
				if ((predictionScores.get(i).get(predIndex) < threshold && givenScores.get(i).get(predIndex+1) < threshold) ||
						(predictionScores.get(i).get(predIndex) > threshold && givenScores.get(i).get(predIndex+1) > threshold)){
					elemAccuracy++;
				}
				predIndex--;
			}
			/*
			 * This is specifically done to allow modifications to use
			 * another metric for scoring, in case we plan to!
			 */
			totalElems += predictionScores.get(i).size();
			accuracy += elemAccuracy;
		}
		if (totalElems != 0.0) {
			return accuracy / totalElems;
		}
		return 0.0;
	}
	
	Double GetThreshold(){
		Double threshold = 0.0, bestAccuracy = 0.0;
		for(Double tempTh = 0.01; tempTh <= 1.0; tempTh+=0.01){
			Double accuracyFound = GetAccuracy(tempTh);
			if (bestAccuracy < accuracyFound){
				bestAccuracy = accuracyFound;
				threshold = tempTh;
			}
		}
		System.out.println("Best accuracy found at thredhold value: " + threshold);
		System.out.println("Best accuracy is: " + bestAccuracy);
		return threshold;
	}
	/*
	 * Just for verification.
	 */
//	public static void main(String args[]){
//		String file = "text.txt";
//		ThresholdBasedScoring tx = new ThresholdBasedScoring(file);
//		Double threhold = tx.GetThreshold();
//	}
}
