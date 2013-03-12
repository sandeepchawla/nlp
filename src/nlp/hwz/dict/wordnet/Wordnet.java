package nlp.hwz.dict.wordnet;

import java.util.ArrayList;
import java.util.List;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class Wordnet {

	private static Wordnet instance;
	private static WordNetDatabase database;
	private static StringBuilder sb;

	public static void main(String[] args) {
		String wordForm = "accomplish";
		System.out.println(Wordnet.getInstance().getSenses(wordForm));
	}

	private Wordnet() {
		System.setProperty("wordnet.database.dir",
				"C:\\Program Files (x86)\\WordNet\\2.1\\dict");
	}

	public static Wordnet getInstance() {
		if (instance == null) {
			instance = new Wordnet();
			database = WordNetDatabase.getFileInstance();
			sb = new StringBuilder();
		}
		return instance;
	}

	public List<String> getSenses(String wordForm) {
		List<String> senses = new ArrayList<String>();
		// Get the synsets containing the wrod form

		Synset[] synsets = database.getSynsets(wordForm);
		// Display the word forms and definitions for synsets retrieved
		if (synsets.length > 0) {
//			senses = new ArrayList<String>();
			for (int i = 0; i < synsets.length; i++) {
				String[] wordForms = synsets[i].getWordForms();
				sb.delete(0, sb.length());
				for (int j = 0; j < wordForms.length; j++) {
					sb.append(wordForms[j] + "; ");
				}
				sb.append(synsets[i].getDefinition());
				senses.add(sb.toString());
			}
		} else {
			// System.err.println("No synsets exist that contain "
			// + "the word form '" + wordForm + "'");
		}
		senses.add(wordForm);
		return senses;
	}
}