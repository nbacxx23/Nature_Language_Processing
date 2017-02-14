package tests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import main.EntityPair;
import main.PairExtractor;
import main.Record;

public class RecordTest {
	
	protected static String DIRNAME = "B:/LaVieEnFrance/Master/M2-1/EI/projet";
	protected static String FILENAME = DIRNAME + "/all-annotations-release-2016-03-17.txt";

	public static void main(String[] args) throws Exception, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("***************Test record data exactitude********\n");
		new PairExtractor();
		PairExtractor.verbose = true;
		PairExtractor.getAllSentences(new File(FILENAME));
		PairExtractor.extractAllFeatures();
		System.out.println("\n******************************************************\n");
		
	}

	private static void test_getAllTriggers(boolean test) throws IOException {
		// TODO Auto-generated method stub
		if(!test)
			return;
		System.out.println("***************Test getAllTriggers(File input)********\n");
		PairExtractor.verbose = true;
		PairExtractor.getAllTriggers(new File(DIRNAME+"/reaction-lexicon.txt"), true);
		System.out.println("\n******************************************************\n");
	}
	
}
