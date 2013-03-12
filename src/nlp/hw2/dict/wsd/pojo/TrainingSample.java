package nlp.hw2.dict.wsd.pojo;

public class TrainingSample {
	public String word;
	public String sensePattern;
	public String context;

	public TrainingSample(String str) {
		int index = str.indexOf('.');
		word = str.substring(0, index).trim();
		int delimiterIdx = str.indexOf('@');
		sensePattern = str.substring(index + 2, delimiterIdx).trim();
		context = str.substring(delimiterIdx + 1).trim();
	}
}
