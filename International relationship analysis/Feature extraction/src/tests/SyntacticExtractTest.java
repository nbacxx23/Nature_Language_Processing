package tests;

import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import main.EntityPair;
import main.PairExtractor;
import main.Record;

public class SyntacticExtractTest {

	public static void main(String[] args) throws Exception, Throwable, IOException {
		// TODO Auto-generated method stub
		System.out.println("***************Test syntacticExtract()********\n");
		new PairExtractor();
		PairExtractor.verbose = true;
		
		String sentence = "UN stops counting Syrian dead as first chemical weapons removed from the country";
		Record record = new Record();
		record.setPair(new EntityPair("UN","Syrian"));
		record.getPair().setSentence(sentence);
		
		Annotation doc = new Annotation(sentence);
		PairExtractor.pipeLine.annotate(doc);
		List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
		
		PairExtractor.syntacticExtract(sentences, record, true);
		System.out.println("\n******************************************************\n");
	}

}
