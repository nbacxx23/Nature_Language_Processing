package tests;

import java.io.IOException;

import main.EntityPair;
import main.PairExtractor;
import main.Record;

public class LexicalExtractTest {

	public static void main(String[] args) throws Exception, Exception, Throwable {
		// TODO Auto-generated method stub
		System.out.println("***************Test lexicalExtract()********\n");
		new PairExtractor();
		PairExtractor.verbose = true;
		String sentence = "26 killed in DR Congo attack blamed on Ugandan rebels: army";
		//String sentence = "EU says Ukraine rebel vote 'new obstacle' to peace";
		//String sentence = "Kurds thwart IS in Syria town but Iraqi forces on back foote";
		//String sentence = "Kurds blames attack IS in Syria town but Iraqi forces on back foote";
		
		Record record = new Record();
		record.setPair(new EntityPair("DR Congo","rebels"));
		//record.setPair(new EntityPair("EU","Ukraine"));
		//record.setPair(new EntityPair("Kurds","forces"));
		record.getPair().setSentence(sentence);
		PairExtractor.extract(sentence, record, true);
		System.out.println("\n******************************************************\n");
	}

}
