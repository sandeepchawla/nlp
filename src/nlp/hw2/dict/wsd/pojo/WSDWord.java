package nlp.hw2.dict.wsd.pojo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;

public class WSDWord {
	public String word;
	// a hashmap which maps a sense to list of list of unigrams, list of bigrams
	// and list of trigrams
	public HashMap<Integer, List<LinkedHashSet<String>>> senseToNgrams;

	// a hashmap which maps a sense to content overlap scores with different
	// senses in context
	public HashMap<Integer, Integer> overlapScores;

	// TODO created for testings
	public WSDWord() {
		super();
		overlapScores = new HashMap<Integer, Integer>();
	}

	public WSDWord(String word,
			HashMap<Integer, List<LinkedHashSet<String>>> senseNgrams) {
		this.word = word;
		senseToNgrams = senseNgrams;
		overlapScores = new HashMap<Integer, Integer>();
	}

	@Override
	public String toString() {
		String str = word + "\n";
		Iterator<Entry<Integer, List<LinkedHashSet<String>>>> itr = senseToNgrams
				.entrySet().iterator();
		while (itr.hasNext()) {
			List<LinkedHashSet<String>> ngrams = itr.next().getValue();
			for (int i = 0; i < ngrams.size(); i++) {
				str += ngrams.get(i) + "\n";
			}
		}
		return str;
	}

}
