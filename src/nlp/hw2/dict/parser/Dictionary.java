package nlp.hw2.dict.parser;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import nlp.hwz.dict.pojo.Dictmap;
import nlp.hwz.dict.pojo.Lexelt;
import nlp.hwz.dict.pojo.Sense;

public class Dictionary {

	private static Dictionary dictInstance = null;
	private static HashMap<String, List<Sense>> senses = new HashMap<String, List<Sense>>();
	private static final String XML_FILE = "Dict.xml";
	private static final String XML_CLASSES = "nlp.hwz.dict.pojo";

	private Dictionary() {
		init();
	}

	private void init() {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(XML_CLASSES);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Dictmap dict = (Dictmap) unmarshaller.unmarshal(new File(XML_FILE));
			List<Lexelt> lexes = dict.getLexelt();
//			System.out.println("Total Lexes: " + lexes.size());
			for (int i = 0; i < lexes.size(); i++) {
				Lexelt currLex = lexes.get(i);
				// Stripping POS tag
				String word = currLex.getItem().substring(0,
						currLex.getItem().indexOf('.'));
				senses.put(word.toLowerCase(), currLex.getSense());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public static Dictionary getInstance() {
		if (dictInstance == null) {
			dictInstance = new Dictionary();
		}
		return dictInstance;
	}

	public HashMap<String, List<Sense>> getSenses() {
		return senses;
	}

	// TODO delete this after testing
//	public static void main(String[] args) {
//		Dictionary.getInstance();
//		List<Sense> wordsenses = senses.get("activate");
//		for (int i = 0; i < wordsenses.size(); i++) {
//			System.out.println(wordsenses.get(i).getGloss());
//		}
//	}

}
