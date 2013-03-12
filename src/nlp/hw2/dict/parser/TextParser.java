package nlp.hw2.dict.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nlp.hw2.dict.wsd.pojo.TrainingSample;
import nlp.hw2.dict.wsd.pojo.WSDWord;
import nlp.hwz.dict.corenlp.Lemmatizer;
import nlp.hwz.dict.pojo.Sense;
import nlp.hwz.dict.wordnet.Wordnet;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TextParser {
	private static final String TOKEN_MODEL = "nl-token.bin";
	private static final String TRAINING_DATA = "Training Data.data";
	private static final String STOP_WORDS_FILE = "stopwords";

	private static Wordnet wordnetInstance;
	private static Lemmatizer lemmatizer;
	private static Dictionary dict;
	private static HashSet<String> stopwordList;

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		prepareStopList();
		// readTrainingData();
		String input = "begin.v 0 1 0 1 0 @ Shall I open these ? To restore the peace . He @began@ to do so . If not the pieces . Then everyone went into the living room , leaving Sara to sweep up.";
		TrainingSample ts = new TrainingSample(input);
		WSDWord m_wsdWord = getWordInfo(ts.word);
		System.out.println(m_wsdWord);
		String crntCtxt = fiterString(ts.context);
		System.out.println(crntCtxt);
		// TODO test method for calculaetOverLap()
		// testcalculateContentOverlap();
		m_wsdWord = disambiguateSense(m_wsdWord, crntCtxt);
		System.out.println(m_wsdWord.overlapScores.get(0));
		System.out.println(m_wsdWord.overlapScores.get(1));
		System.out.println(m_wsdWord.overlapScores.get(2));
		System.out.println(m_wsdWord.overlapScores.get(3));
		System.out.println(System.currentTimeMillis() - start);

	}

	private static WSDWord disambiguateSense(WSDWord word, String context) {
		String[] ctxWrds = context.split(" ");
		for (int i = 0; i < ctxWrds.length; i++) {
			WSDWord ctxWord = getCtxtWordInfo(ctxWrds[i]);
			if (ctxWord != null) {
				word = calculateContentOverlap(word, ctxWord);
			}
		}
		return word;
	}

	public static WSDWord getWordInfo(String word) {
		if (dict == null) {
			dict = Dictionary.getInstance();
		}
		HashMap<String, List<Sense>> dictionary = dict.getSenses();
		List<Sense> senses = dictionary.get(word);
		HashMap<Integer, List<LinkedHashSet<String>>> senseToNgrams = new HashMap<Integer, List<LinkedHashSet<String>>>();
		for (int i = 0; i < senses.size(); i++) {
			List<LinkedHashSet<String>> allNgrams = new ArrayList<LinkedHashSet<String>>();
			String senseTxt = senses.get(i).getGloss();
			allNgrams.add(ngrams(1, fiterString(senseTxt)));
			allNgrams.add(ngrams(2, fiterString(senseTxt)));
			allNgrams.add(ngrams(3, fiterString(senseTxt)));
			senseToNgrams.put(i, allNgrams);
		}
		return new WSDWord(word, senseToNgrams);
	}

	public static WSDWord getCtxtWordInfo(String word) {
		if (wordnetInstance == null) {
			wordnetInstance = Wordnet.getInstance();
		}
		List<String> senses = wordnetInstance.getSenses(word);
		WSDWord wsdWord = null;
		if (senses != null) {
			HashMap<Integer, List<LinkedHashSet<String>>> senseToNgrams = new HashMap<Integer, List<LinkedHashSet<String>>>();
			for (int i = 0; i < senses.size(); i++) {
				List<LinkedHashSet<String>> allNgrams = new ArrayList<LinkedHashSet<String>>();
				String senseTxt = senses.get(i);
				allNgrams.add(ngrams(1, fiterString(senseTxt)));
				allNgrams.add(ngrams(2, fiterString(senseTxt)));
				allNgrams.add(ngrams(3, fiterString(senseTxt)));
				senseToNgrams.put(i, allNgrams);
			}
			wsdWord = new WSDWord(word, senseToNgrams);
		}
		return wsdWord;
	}

	/**
	 * calculateContentOverlap KEY METHOD
	 * 
	 * @param word
	 * @param ctxt
	 * @return
	 */
	private static WSDWord calculateContentOverlap(WSDWord word, WSDWord ctxt) {
		Iterator<List<LinkedHashSet<String>>> itr = word.senseToNgrams.values()
				.iterator();
		int senseIdx = 0;
		while (itr.hasNext()) {
			List<LinkedHashSet<String>> ngrams = itr.next();
			LinkedHashSet<String> unigrams = ngrams.get(0);
			int maxOverlap = Integer.MIN_VALUE;
			Iterator<List<LinkedHashSet<String>>> ctxItr = ctxt.senseToNgrams
					.values().iterator();
			while (ctxItr.hasNext()) {
				List<LinkedHashSet<String>> ctxNgrams = ctxItr.next();
				LinkedHashSet<String> ctxUnigrams = ctxNgrams.get(0);
				Set<String> cloneSet = new LinkedHashSet<String>(unigrams);
				cloneSet.retainAll(ctxUnigrams);
				int uniCnt = cloneSet.size();
				int biCnt = 0;
				int triCnt = 0;
				if (uniCnt > 1) {// calculate for bigrams
					LinkedHashSet<String> bigrams = ngrams.get(1);
					LinkedHashSet<String> ctxBigrams = ctxNgrams.get(1);
					cloneSet = new LinkedHashSet<String>(bigrams);
					cloneSet.retainAll(ctxBigrams);
					biCnt = cloneSet.size();
				}
				if (biCnt > 1) {// calculate for trigrams
					LinkedHashSet<String> trigrams = ngrams.get(2);
					LinkedHashSet<String> ctxTrigrams = ctxNgrams.get(2);
					cloneSet = new LinkedHashSet<String>(trigrams);
					cloneSet.retainAll(ctxTrigrams);
					triCnt = cloneSet.size();
				}
				int overlap = uniCnt + (2 * biCnt) + (3 * triCnt);
				if (overlap > maxOverlap) {
					maxOverlap = overlap;
				}
			}
			// retain the max score obtained for this sense
			word.overlapScores.put(senseIdx, maxOverlap);
			// move to next sense of word
			senseIdx++;
		}
		return word;
	}

	public static ArrayList<String> getTokens(String str) {
		String[] tokens = null;
		ArrayList<String> filteredTokens = new ArrayList<String>();
		InputStream modelIn = null;
		try {
			modelIn = new FileInputStream(TOKEN_MODEL);
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);
			tokens = tokenizer.tokenize(str);
			for (int i = 0; i < tokens.length; i++) {
				if (!stopwordList.contains(tokens[i].toLowerCase())) {
					filteredTokens.add(tokens[i].toLowerCase());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		System.out.println(Arrays.toString(tokens));
		return filteredTokens;
	}

	public static void prepareStopList() {
		String line = null;
		String stopwords = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(STOP_WORDS_FILE));
			while ((line = br.readLine()) != null) {
				stopwords += line.toLowerCase();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		stopwordList = new HashSet<String>(Arrays.asList(stopwords.split(",")));
	}

	public static void readTrainingData() {
		String line = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(TRAINING_DATA));
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static LinkedHashSet<String> ngrams(int n, String str) {
		LinkedHashSet<String> ngrams = new LinkedHashSet<String>();
		String[] words = str.split(" ");
		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i + n));
		return ngrams;
	}

	public static String concat(String[] words, int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? "_" : "") + words[i]);
		return sb.toString();
	}

	public static String fiterString(String str) {
		StringBuilder stbr = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (!((ch == '(') || (ch == ')') || (ch == ';') || (ch == ','))) {
				stbr.append(str.charAt(i));
			}
		}
		String strFltr = stbr.toString();
		// String lemmatized = Lemmatizer.getLemma(strFltr);
		StringBuilder sb = new StringBuilder();
		String[] tokens = strFltr.split(" ");
		if (lemmatizer == null) {
			lemmatizer = Lemmatizer.getInstance();
		}
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].toLowerCase();
			if (!stopwordList.contains(token)) {
				sb.append(lemmatizer.getLemma(token));
			}
		}
		return sb.toString();
	}

	// TODO test method for calculateContentOverlap. Must be deleted later
	private static void testcalculateContentOverlap() {
		String word1sense1 = "A characteristic state or mode of living";
		String word1sense2 = "A prison�term lasting as long as the prisoner lives";
		HashMap<Integer, List<LinkedHashSet<String>>> senseToNgrams = new HashMap<Integer, List<LinkedHashSet<String>>>();
		List<LinkedHashSet<String>> allNgrams = new ArrayList<LinkedHashSet<String>>();
		allNgrams.add(ngrams(1, fiterString(word1sense1)));
		allNgrams.add(ngrams(2, fiterString(word1sense1)));
		allNgrams.add(ngrams(3, fiterString(word1sense1)));
		senseToNgrams.put(0, allNgrams);
		List<LinkedHashSet<String>> allNgrams2 = new ArrayList<LinkedHashSet<String>>();
		allNgrams2.add(ngrams(1, fiterString(word1sense2)));
		allNgrams2.add(ngrams(2, fiterString(word1sense2)));
		allNgrams2.add(ngrams(3, fiterString(word1sense2)));
		senseToNgrams.put(1, allNgrams2);
		WSDWord word = new WSDWord();
		word.word = "life";
		word.senseToNgrams = senseToNgrams;

		WSDWord ctxt = new WSDWord();
		ctxt.word = "sentence";
		String ctxtsense1 = "A characteristic state or mode of living";
		String ctxtsense2 = "A prison�term lasting as long as the prisoner lives";
		// String ctxtsense3 = "The period�of�time a prisoner is imprisoned";
		HashMap<Integer, List<LinkedHashSet<String>>> ctxtSenseToNgrams = new HashMap<Integer, List<LinkedHashSet<String>>>();
		List<LinkedHashSet<String>> allNgramsctxt = new ArrayList<LinkedHashSet<String>>();
		allNgramsctxt.add(ngrams(1, fiterString(ctxtsense1)));
		allNgramsctxt.add(ngrams(2, fiterString(ctxtsense1)));
		allNgramsctxt.add(ngrams(3, fiterString(ctxtsense1)));
		ctxtSenseToNgrams.put(0, allNgramsctxt);
		List<LinkedHashSet<String>> allNgramsctxt2 = new ArrayList<LinkedHashSet<String>>();
		allNgramsctxt2.add(ngrams(1, fiterString(ctxtsense2)));
		allNgramsctxt2.add(ngrams(2, fiterString(ctxtsense2)));
		allNgramsctxt2.add(ngrams(3, fiterString(ctxtsense2)));
		ctxtSenseToNgrams.put(1, allNgramsctxt2);
		// List<LinkedHashSet<String>> allNgramsctxt3 = new
		// ArrayList<LinkedHashSet<String>>();
		// allNgramsctxt3.add(ngrams(1, fiterString(ctxtsense3)));
		// allNgramsctxt3.add(ngrams(2, fiterString(ctxtsense3)));
		// allNgramsctxt3.add(ngrams(3, fiterString(ctxtsense3)));
		// ctxtSenseToNgrams.put(2, allNgramsctxt3);
		ctxt.senseToNgrams = ctxtSenseToNgrams;
		System.out.println(word);
		System.out.println(ctxt);
		calculateContentOverlap(word, ctxt);
	}
}