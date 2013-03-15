package nlp.hw2.dict.confusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Confusion {

	public static final String RESULTS_INPUT_FILENAME = "mainFile.txt";
	public static final String RESULTS_OUTPUT_FILENAME = "output.txt";

	Confusion() {

	}

	static Integer[] findConfusionBit(String[] tokens) {
		Boolean isConfused = false;
		for (int i = 0; i < tokens.length; i++) {
			System.out.print(":" + tokens[i] + ":");
		}
		System.out.println();

		Double max = new Double(0.0);

		for (int i = 0; i < tokens.length; i++) {
			if (Double.compare(max, Double.parseDouble(tokens[i])) < 0) {
				max = Double.parseDouble(tokens[i]);
			}
		}

		System.out.println("Max Values is :: " + max);

		for (int i = 0; i < tokens.length; i++) {
			for (int j = 0; j < tokens.length; j++) {
				if (i == j) {
					continue;
				}
				Double a = new Double(Double.parseDouble(tokens[i]));
				Double b = new Double(Double.parseDouble(tokens[j]));

				if (a.equals(b) && a.equals(max)) {
					isConfused = true;
				}
			}
		}

		Integer[] a = new Integer[tokens.length + 1];
		if (isConfused) {
			a[0] = 1;
		} else {
			a[0] = 0;
		}

		Double threshold = new Double((double)(1) / (double)(tokens.length));

		for (int i = 0; i < tokens.length; i++) {
			if (Double.compare(Double.parseDouble(tokens[i]), threshold) >= 0) {
				a[i + 1] = new Integer(1);
			} else {
				a[i + 1] = new Integer(0);
			}
		}

		return a;

	}

	static Integer[] process(String line) {
		String tokens[] = line.split("@")[1].trim().split(" ");
		int howManyAreCorrect = 0, j = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].trim().length() > 0)
				howManyAreCorrect++;
		}
		String tokensRefined[] = new String[howManyAreCorrect];
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].trim().length() > 0) {
				tokensRefined[j] = tokens[i].trim();
				j++;
			}
		}
		return findConfusionBit(tokensRefined);
	}

	static void readInput(String filename) {
		BufferedReader buffreader = null;
		try {
			buffreader = new BufferedReader(new FileReader(filename));
			String line;
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(RESULTS_OUTPUT_FILENAME);
				BufferedWriter out = new BufferedWriter(fstream);
				int lines = 0;
				while ((line = buffreader.readLine()) != null) {
					line = line.trim();
					Integer a[] = null;
					if (line.length() > 0) {
						a = process(line);
					}
					for (int i = 0; i < a.length; i++) {
						lines++;
						out.write(a[i].toString() + " ");	
						out.newLine();
					}
				}
				System.out.println(lines);
				out.close();
			} catch (Exception e) {
				System.out.println("Something Wrong!");
				e.printStackTrace();
			}

		} catch (Exception e) {
			System.out
					.println("Inside function readInput! Something went wrong while reading the input!");
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		readInput(RESULTS_INPUT_FILENAME);
	}

}
