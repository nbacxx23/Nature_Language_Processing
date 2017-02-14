package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.RegexNERAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.TokensRegexAnnotator;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;

public class PairExtractor {
	protected static String DIRNAME = "B:/LaVieEnFrance/Master/M2-1/EI/projet";
	protected static String FILENAME = DIRNAME + "/all-annotations-release-2016-03-17.txt";
	protected static DependencyParser parser = new DependencyParser(new Properties());
	final static String ClassifierFilename = "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
	static Properties props = new Properties();
	static CRFClassifier classifier;
	static PTBTokenizer<CoreLabel> ptbt;
	static public StanfordCoreNLP pipeLine;
	static List<Triple<String,String,String>> triggers;
	static List<String> words_triggers = new ArrayList();
	static List<String> nouns_triggers = new ArrayList();
	static List<String> verbs_triggers = new ArrayList();
	static List<String> neg_triggers = new ArrayList();
	static List<String> pos_triggers = new ArrayList();
	static List<EntityPair> pairs = new ArrayList();
	static List<String> nouns = new ArrayList();
	static List<String> verbs = new ArrayList();
    static List<Record> records = new ArrayList();
    static List<String> preps = new ArrayList();
    static public boolean verbose = false;
	
    
	public PairExtractor() throws ClassCastException, ClassNotFoundException, IOException{
		
		props.setProperty("ner.model", ClassifierFilename);
		CRFClassifier classifier = CRFClassifier.getClassifier(ClassifierFilename);
		
		props.put( "annotators", "tokenize, ssplit, pos, lemma, parse, ner" );
		pipeLine = new StanfordCoreNLP( props );
		
		ptbt = new PTBTokenizer<>(new FileReader(DIRNAME+"/corpus.txt"),
	              new CoreLabelTokenFactory(), "");
		triggers = getAllTriggers(new File(DIRNAME+"/reaction-lexicon.txt"), false);
		getPreps(new File(DIRNAME+"/preposition.txt"));
		
		nouns.add("NN");
		nouns.add("NNS");
		nouns.add("NNP");
		nouns.add("NNPS");
		
		verbs.add("VB");
		verbs.add("VBD");
		verbs.add("VBP");
		verbs.add("VBZ");
		verbs.add("VBG");
	}
	
	//
	public static void extractAllFeatures() throws IOException, ClassCastException, ClassNotFoundException{
		int i = 0;
		for(EntityPair pair: pairs){
			String sentence = pair.getSentence();
			Record record = new Record();
			record.setPair(pair);
			extract(sentence, record, false);
			records.add(record);
			i++;
			if(verbose){
				if(i>10){
					break;
				}
				record.print();
			}
			System.out.println("record "+ i + " finished.");
		}
		System.out.println("finish");
		if(!verbose)
			Record.writeToFile(new File(DIRNAME+"/train.txt"), records);
	}
	
	//Read all labeled sentences from raw data
	public static void getAllSentences(File input) throws IOException, ClassCastException, ClassNotFoundException{
		Set<String> corpus = new HashSet();
		//lecture du fichier texte	
		InputStream is=new FileInputStream(input); 
		InputStreamReader isr=new InputStreamReader(is,"UTF-8");
		BufferedReader br=new BufferedReader(isr);
		String line;
		while ((line=br.readLine())!=null){
			EntityPair entityPair = extractEntityPair(line);
			String test = line.split("\t")[1];
			String phrase = line.split("\t")[1];
			int len = phrase.length();
			phrase = phrase.substring(1, len-1);
			phrase = phrase.replaceAll("<[/]?e\\d>", "");
			corpus.add(phrase);
			entityPair.setSentence(phrase);
			
			//Read the relationship label
			String relation = br.readLine();
			entityPair.setLabel(relation);
			pairs.add(entityPair);
			br.readLine();
		}
		br.close();
		
		//Store the sentence after deleting the entity pair labels
		File output = new File(DIRNAME+"/corpus.txt");
		OutputStream os = new FileOutputStream(output);
		OutputStreamWriter osr = new OutputStreamWriter(os,"utf-8");
		BufferedWriter bw = new BufferedWriter(osr);
		Iterator<String> it = corpus.iterator();
		while(it.hasNext()){
			bw.write(it.next()+"\n");
		}
		bw.close();
		
	}
	
	//read all the 20 most used prepositions from an external file.
	public static void getPreps(File input) throws IOException{
		Set<String> corpus = new HashSet();
		//lecture du fichier texte	
		InputStream is=new FileInputStream(input); 
		InputStreamReader isr=new InputStreamReader(is,"UTF-8");
		BufferedReader br=new BufferedReader(isr);
		String line;
		while ((line=br.readLine())!=null){
			preps.add(line);
		}
		br.close(); 
	}
	
	
	//Extract a pair of entities from original phrases with labels <e></e>
	public static EntityPair extractEntityPair(String phrase){
		String entity1="";
		String entity2="";
		Pattern pattern1 = Pattern.compile("(<e1>(.*?)</e1>)");
		Pattern pattern2 = Pattern.compile("(<e2>(.*?)</e2>)");
		Matcher matcher1 = pattern1.matcher(phrase);
		Matcher matcher2 = pattern2.matcher(phrase);
		
		if (matcher1.find()&&matcher2.find())
		{
			entity1 = matcher1.group(2);
			entity2 = matcher2.group(2);
		}
		return new EntityPair(entity1, entity2);
	}
	
	//Read the reaction lexicons from file
	public static List<Triple<String,String,String>> getAllTriggers(File file, boolean verbose) throws IOException{
		List<Triple<String,String,String>> triggers = new ArrayList();
		//lecture du fichier texte	
		InputStream is=new FileInputStream(file); 
		InputStreamReader isr=new InputStreamReader(is,"UTF-8");
		BufferedReader br=new BufferedReader(isr);
		String line;
		while ((line=br.readLine())!=null){
			String[] ls = line.split("\t");
			if(ls.length!=3)
				continue;
			//System.out.println(ls[0]+" "+ ls[1]+ " "+ls[2]);
			triggers.add(new Triple(ls[0],ls[1],ls[2]));
			words_triggers.add(ls[1]);
			if(ls[2].equals("pos")){
				pos_triggers.add(ls[1]);
			}
			if(ls[2].equals("neg")){
				neg_triggers.add(ls[1]);
			}
			if(ls[0].equals("verb")){
				verbs_triggers.add(ls[1]);
			}
			else if(ls[0].equals("noun")){
				nouns_triggers.add(ls[1]);
			}
		}
		br.close();
		if(verbose){
			System.out.println(words_triggers.toString());
			System.out.println(neg_triggers.toString());
			System.out.println(pos_triggers.toString());
		}
		return triggers;
	}

	//extract features using the annotation pipeline;
	//includes lexical features and syntactic features.
	public static void extract(String sentence, Record record, boolean test){
		List<String> lemmas = new LinkedList<String>();
		EntityPair pair = record.getPair();
		HashMap<String, Integer> entities = new HashMap();
		HashMap<String, Integer> triggers = new HashMap();
		HashMap<String,Integer> verbs_map = new HashMap();
		HashMap<String, Integer> nouns_map = new HashMap(); 
		HashMap<String, Integer> negs_map = new HashMap();
		HashMap<String, Integer> preps_map = new HashMap();
		HashMap<String, Integer> pos_triggers_map = new HashMap();
		HashMap<String, Integer> neg_triggers_map = new HashMap();
		HashMap<String, Integer> verbs_triggers_map = new HashMap();
		HashMap<String, Integer> nouns_triggers_map = new HashMap();
		if(pair.getEntity1().indexOf(" ")>0){
			String temp = record.getPair().getEntity1().replaceAll(" ", "");
			sentence = sentence.replaceAll(pair.getEntity1(), temp);
			pair.setEntity1(temp);
		}
		if(pair.getEntity2().indexOf(" ")>0){
			String temp = record.getPair().getEntity2().replaceAll(" ", "");
			sentence = sentence.replaceAll(pair.getEntity2(), temp);
			pair.setEntity2(temp);
		}
		
		Annotation doc = new Annotation(sentence);
		pipeLine.annotate(doc);
		List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
		
		if(sentences.size()>0){
			CoreMap phrase = sentences.get(0);
			
			//set the length of the sentence
			record.setLen_sentence(phrase.get(TokensAnnotation.class).size());
			
			String entity1 = pair.getEntity1();
			String entity2 = pair.getEntity2();
			
			for (CoreLabel token: phrase.get(TokensAnnotation.class)) {
			    // this is the text of the token
			    String word = token.get(TextAnnotation.class);
			    // this is the POS tag of the token
			    String pos = token.get(PartOfSpeechAnnotation.class);
			    // this is the NER label of the token
			    String ne = token.get(NamedEntityTagAnnotation.class);
			    // this is the lemma of the token
			    String lemma = token.get(LemmaAnnotation.class);
			    
			    if(test){
			    	System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
			    }
			    
			    //Record the entity pair positions, extract other entities in the sentence
		    	if(word.equals(entity1)){
					record.setPos_et1(token.index());
				}
		    	else if(word.equals(entity2)){
					record.setPos_et2(token.index());
				}
		    	else{
		    		if(ne.equals("LOCATION")||ne.equals("PERSON")||ne.equals("ORGANIZATION"))
		    			entities.put(word, token.index());
		    	    if(word.indexOf(entity1)>=0){
		    			record.setPos_et1(token.index());
		    		}
		    	    if(word.indexOf(entity2)>=0){
		    			record.setPos_et2(token.index());
		    		}
		    		
		    	}
		    	//Extract the negation word whose label is "RB"
		    	if(pos.equals("RB")){
		    		negs_map.put(word,token.index());
		    		if(test){
		    			System.out.println("negation word: "+ word);
		    		}
		    	}
		    	
			    //Count the noun number and verb number around the entity pair,
		    	//store them in the hashmap: nouns_map
			    if(nouns.contains(pos)){
			    	nouns_map.put(lemma, token.index());
			    }
			    if(verbs.contains(pos)){
			    	verbs_map.put(lemma, token.index());
			    }
			    
			    //counts the preposition number  around entities
			    if(preps.contains(word)){
			    	preps_map.put(lemma, token.index());
			    }
			    
			    //Extract the trigger words
			    if(words_triggers.contains(lemma)){
			    	//set the number of triggers
			    	record.setNum_triggers(record.getNum_triggers()+1);
			    	triggers.put(lemma, token.index());
			    	
			    	//Analyze the noun and verb attributes.
			    	if(verbs_triggers.contains(lemma)&&pos.indexOf("VB")>=0){
			    		verbs_triggers_map.put(lemma, token.index());
			    	}
			    	else if(nouns_triggers.contains(lemma)){
			    		nouns_triggers_map.put(lemma, token.index());
			    	}
			    }
			    
			    //step B for the numbers of positive(negative) triggers in the sentence
			    if(pos_triggers.contains(lemma)){
			    	record.setNum_pos_triggers(record.getNum_pos_triggers()+1);
			    	pos_triggers_map.put(lemma,token.index());
			    }
			    if(neg_triggers.contains(lemma)){
			    	record.setNum_neg_triggers(record.getNum_neg_triggers()+1);
			    	neg_triggers_map.put(lemma, token.index());
			    }
			    
			 }
		}
		//Compute the verb number around and between the entity pair
		if(verbs_map.size()>0){
			for(String verb: verbs_map.keySet()){
				int position = verbs_map.get(verb);
				if(position<record.getPos_et1()){
		    		record.setNum_verbs_left(record.getNum_verbs_left()+1);
		    	}
		    	else if(position<record.getPos_et2()){
		    		record.setNum_verbs_inside(record.getNum_verbs_inside()+1);
		    	}
		    	else{
		    		record.setNum_verbs_right(record.getNum_verbs_right()+1);
		    	}
			}
		}
		//Compute the noun number around and between the entity pair
		if(nouns_map.size()>0){
			for(String noun: nouns_map.keySet()){
				int position = nouns_map.get(noun);
				if(position<record.getPos_et1()){
		    		record.setNum_nouns_left(record.getNum_nouns_left()+1);
		    	}
		    	else if(position>record.getPos_et1() && position<record.getPos_et2()){
		    		record.setNum_nouns_inside(record.getNum_nouns_inside()+1);
		    	}
		    	else if(position>record.getPos_et2()){
		    		record.setNum_nouns_right(record.getNum_nouns_right()+1);
		    	}
			}
		}
		//Compute the preposition number around and between the entity pair
		if(preps_map.size()>0){
			for(String prep: preps_map.keySet()){
				int position = preps_map.get(prep);
				if(position<record.getPos_et1()){
		    		record.setNum_preps_left(record.getNum_preps_left()+1);
		    	}
		    	else if(position<record.getPos_et2()){
		    		record.setNum_preps_inside(record.getNum_preps_inside()+1);
		    	}
		    	else{
		    		record.setNum_preps_right(record.getNum_preps_right()+1);
		    	}
			}
		}
		//Number of entities around and inside entities
		if(entities.size()>0){
			for(String entity: entities.keySet()){
				if(entities.get(entity)<record.getPos_et1()){
					record.setNum_ets_left(record.getNum_ets_left()+1);
				}
				else if(entities.get(entity)<record.getPos_et2()){
					record.setNum_ets_inside(record.getNum_ets_inside()+1);
				}
				else{
					record.setNum_ets_right(record.getNum_ets_right()+1);
				}
			}
		}
		//Compute the trigger number around and between the entity pair
		if(triggers.size()>0){
			for(String trigger: triggers.keySet()){
				if(triggers.get(trigger)<record.getPos_et1()){
					record.setPresence_triggers_left(1);
					record.setNum_triggers_left(record.getNum_triggers_left()+1);
					record.setPresence_triggers_left(1);
				}
				else if(triggers.get(trigger)<record.getPos_et2()){
					record.setPresence_triggers_inside(1);
					record.setNum_triggers_inside(record.getNum_triggers_inside()+1);
					record.setPresence_triggers_inside(1);
				}
				else if(triggers.get(trigger)>record.getPos_et2()){
					record.setPresence_triggers_right(1);
					record.setNum_triggers_right(record.getNum_triggers_right()+1);
					record.setPresence_triggers_right(1);
				}
			}
		}
		//Compute the positive trigger word around and between the entity pair
		if(pos_triggers_map.size()>0){
			for(String trigger: pos_triggers_map.keySet()){
				if(pos_triggers_map.get(trigger)<record.getPos_et1()){
					record.setNum_pos_triggers_left(record.getNum_pos_triggers_left()+1);
				}
				else if(pos_triggers_map.get(trigger)<record.getPos_et2()){
					record.setNum_pos_triggers_inside(record.getNum_pos_triggers_inside()+1);
				}
				else if(pos_triggers_map.get(trigger)>record.getPos_et2()){
					record.setNum_pos_triggers_right(record.getNum_pos_triggers_right()+1);
				}
			}
		}
		//Compute the negative trigger word around and between the entity pair
		if(neg_triggers_map.size()>0){
			for(String trigger: neg_triggers_map.keySet()){
				if(neg_triggers_map.get(trigger)<record.getPos_et1()){
					record.setNum_neg_triggers_left(record.getNum_neg_triggers_left()+1);
				}
				else if(neg_triggers_map.get(trigger)<record.getPos_et2()){
					record.setNum_neg_triggers_inside(record.getNum_neg_triggers_inside()+1);
				}
				else if(neg_triggers_map.get(trigger)>record.getPos_et2()){
					record.setNum_neg_triggers_right(record.getNum_neg_triggers_right()+1);
				}
			}
		}
		//Compute the verb trigger word around and between the entity pair
		if(verbs_triggers_map.size()>0){
			for(String trigger: verbs_triggers_map.keySet()){
				if(verbs_triggers_map.get(trigger)<record.getPos_et1()){
					record.setNum_triggers_verbs_left(record.getNum_triggers_verbs_left()+1);
				}
				else if(verbs_triggers_map.get(trigger)<record.getPos_et2()){
					record.setNum_triggers_verbs_inside(record.getNum_triggers_verbs_inside()+1);
				}
				else if(verbs_triggers_map.get(trigger)>record.getPos_et2()){
					record.setNum_triggers_verbs_right(record.getNum_triggers_verbs_right()+1);
				}
			}
		}
		//Compute the verb trigger word around and between the entity pair
		if(nouns_triggers_map.size()>0){
			for(String trigger: nouns_triggers_map.keySet()){
				if(nouns_triggers_map.get(trigger)<record.getPos_et1()){
					record.setNum_triggers_nouns_left(record.getNum_triggers_nouns_left()+1);
				}
				else if(nouns_triggers_map.get(trigger)<record.getPos_et2()){
					record.setNum_triggers_nouns_inside(record.getNum_triggers_nouns_inside()+1);
				}
				else if(nouns_triggers_map.get(trigger)>record.getPos_et2()){
					record.setNum_triggers_nouns_right(record.getNum_triggers_nouns_right()+1);
				}
			}
		}
		
		//entities features
		//Distance of entity pair
		if(record.getPos_et2()!=record.getPos_et1())
			record.setDistance_pair(record.getPos_et2()-record.getPos_et1()-1);
		else if(record.getPos_et2()>=0){
			record.setDistance_pair(0);
		}
		//update the statistics of negations	
		if(negs_map.size()>0){
			for(String neg: negs_map.keySet()){
				if(test){
					System.out.println("negation: "+ neg);
				}
				if(negs_map.get(neg)<record.getPos_et1()){
					record.setNum_neg_left(record.getNum_neg_left()+1);
				}
				else if(negs_map.get(neg)<record.getPos_et2()){
					record.setNum_neg_inside(record.getNum_neg_inside()+1);
				}
				else{
					record.setNum_neg_right(record.getNum_neg_right()+1);
				}
			}
		}
		
		//Extract syntactic features from a sentence
		syntacticExtract(sentences,record, test);
		if(test){
			record.print();
		}
	}
	
	//Extract syntactic features from a sentence
	/**
	 * @param sentences
	 * @param record
	 * @param test
	 */
	public static void syntacticExtract(List<CoreMap> sentences, Record record, boolean test){
		if(sentences.size()>0){
			CoreMap phrase = sentences.get(0);
			
			// this is the parse tree of the current sentence
			Tree tree = phrase.get(TreeAnnotation.class);
			
			getChunks(tree, "NP", record, true);
			//getChunks(tree, "VP");
			
			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies =  phrase.get(CollapsedCCProcessedDependenciesAnnotation.class);
			
			if(test){
				System.out.println("parse tree:\n" );
				tree.pennPrint();
				System.out.println("dependency graph:\n" + dependencies);
				Set<String> negation = new HashSet();
				for(SemanticGraphEdge edge : dependencies.edgeIterable()){
				    IndexedWord dep = edge.getDependent();
				    String dependent = dep.word();
				    int dependent_index = dep.index();
				    IndexedWord gov = edge.getGovernor();
				    String governor = gov.word();
				    int governor_index = gov.index();
				    GrammaticalRelation relation = edge.getRelation();
				    if(relation.toString()=="neg"){
				    	System.out.println("Relation: "+relation.toString()+" Dependent ID: "+dependent_index+" Dependent: "+dependent.toString()+" Governor ID: "+governor_index+" Governor: "+governor.toString());
				    }
				}
			}
			
		}
	}
	
	//get all NP minimal chunks
	public static void getChunks(Tree tree, String node, Record record, boolean test){
		List<Tree> chunks = new ArrayList();
		TregexPattern tgrepPattern = TregexPattern.compile(node);
		TregexMatcher m = tgrepPattern.matcher(tree);
		
		while (m.find()) {
		    Tree subtree = m.getMatch();
		    //System.out.println("chunk: "+subtree);
		    List<Tree> children = subtree.getChildrenAsList();
		    if(children.toString().indexOf("(NP")>0){
		    	continue;
		    }
		    String chunk_str = subtree.getLeaves().toString();
    		chunk_str = chunk_str.substring(1, chunk_str.length()-1);
    		if(chunk_str.indexOf(record.getPair().getEntity1())==0){
    			record.setIsHead_e1(1);
    			record.setSize_chunk_e1(subtree.getLeaves().size());
    		}
    		if(chunk_str.indexOf(record.getPair().getEntity2())==0){
    			record.setIsHead_e2(1);
    			record.setSize_chunk_e2(subtree.getLeaves().size());
    		}

		    if(test){
		    	System.out.println("mini: "+ subtree);
		    	System.out.println("chunk leaves: "+subtree.getLeaves());
		    	System.out.println("chunk labels: "+subtree.labels());
		    }
		}
		if(test){
			record.print();
		}

	}

	public static void main(String[] args) throws IOException, ClassCastException, ClassNotFoundException {
		File file = new File(FILENAME);
        
		new PairExtractor();  

        getAllSentences(file);

		extractAllFeatures();
	}
}
