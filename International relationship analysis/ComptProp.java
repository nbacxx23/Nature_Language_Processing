import java.io.*;
import java.util.*;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreNLPProtos;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

/**
 * Created by xchen on 2017/1/19.
 */
public class ComptProp {

    public static TreeMap<String, Integer> preposition = new TreeMap<>();

    public static String corpus = "/Users/xchen/Documents/AIC/Extraction_information/corpus.txt";

    public static String prepo = "/Users/xchen/Documents/AIC/Extraction_information/preposition.txt";

    public static void getPreposition(String text){
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit,pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text_d = text;

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text_d);

        // run all Annotators on this text
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            //HashMap<String,String> posTag = new HashMap<>();
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);

                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                // this is the NER label of the token
                //String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if(pos.equals("IN") || pos.equals("TO")){
                    if(!preposition.containsKey(word)){
                        preposition.put(word,1);
                    }else{
                        int time = preposition.get(word);
                        preposition.put(word,time+1);
                    }
                }
            }

            // this is the parse tree of the current sentence
            //Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            //System.out.println(tree);
            // this is the Stanford dependency graph of the current sentence
            //SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
        }
    }

    public static void outputPreposition(){

        System.out.println("the statistic is finished");
        for (Map.Entry<String, Integer> pre : preposition.entrySet()) {
            System.out.println(pre.getKey()+"\t"+pre.getValue());
        }


        List freList = new ArrayList(preposition.entrySet());

        Collections.sort(freList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map.Entry obj1 = (Map.Entry) o1;
                Map.Entry obj2 = (Map.Entry) o2;

                return ((Integer) obj2.getValue()).compareTo((Integer) obj1
                        .getValue());
            }
        });

        int l = 20;
        for(int i=0;i<l;i++){
            System.out.println(freList.get(i));
        }

        File output = new File(prepo);

        try {
            FileWriter fw = new FileWriter(output);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);


            for(int j=0;j<l;j++){
                out.println(freList.get(j));
            }

            out.close();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {

        File file = new File(corpus);

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine())!=null){
                getPreposition(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputPreposition();
    }

}

