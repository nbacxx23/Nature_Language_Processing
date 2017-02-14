package main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Record {
	
	//Entities pair
	private EntityPair pair = new EntityPair();
	
	//entities features
	private int distance_pair = -1;
	private int len_sentence = 0;
	private int pos_et1 = -1;
	private int pos_et2 = -1;
	private String type_et1 = "";
	private String type_et2  ="";
	private int num_ets_left = 0;
	private int num_ets_right = 0;
	private int num_ets_inside = 0;
	
	//lexical features
	private int has_triggers = 0;// have 1, or not 0.
	private int presence_triggers_left = 0;
	private int presence_triggers_inside = 0;
	private int presence_triggers_right = 0;
	private int num_triggers = 0;
	private String type_triggers = "";
	//triggers
	private int num_triggers_left = 0;
	private int num_triggers_inside = 0;
	private int num_triggers_right = 0;
	//type of triggers
	private int num_triggers_nouns_left = 0;
	private int num_triggers_nouns_inside = 0;
	private int num_triggers_nouns_right = 0;

	private int num_triggers_verbs_left = 0;
	private int num_triggers_verbs_inside = 0;
	private int num_triggers_verbs_right = 0;
	
	private boolean has_negation = false;
	private int num_neg_left = 0;
	private int num_neg_inside = 0;
	private int num_neg_right = 0;
	private int num_preps_left = 0;
	private int num_preps_inside = 0;
	private int num_preps_right = 0;
	

	//syntactic features
	private int isHead_e1 = 0;
	private int isHead_e2 = 0;
	
	private int size_chunk_e1 = 0;
	private int size_chunk_e2 = 0;
	
	private int num_verbs_left = 0;
	private int num_verbs_inside = 0;
	private int num_verbs_right = 0;
	
	private int num_nouns_left = 0;
	private int num_nouns_inside = 0;
	private int num_nouns_right = 0;
	
	//Step B
	private int num_pos_triggers = 0;
	private int num_neg_triggers = 0;
	
	private int num_pos_triggers_left = 0;
	private int num_pos_triggers_inside = 0;
	private int num_pos_triggers_right = 0;
	
	private int num_neg_triggers_left = 0;
	private int num_neg_triggers_inside = 0;
	private int num_neg_triggers_right = 0;
	
	private boolean negation = false;
	
	private List<String> lemmas = new ArrayList();
	
	public Record(){
		
	}
	
	public void setLemmas(List<String> lemmas){
		this.lemmas = lemmas;
	}
	
	public List<String> getLemmas(){
		return this.lemmas;
	}

	public int getDistance_pair() {
		return distance_pair;
	}

	public void setDistance_pair(int distance_pair) {
		this.distance_pair = distance_pair;
	}

	public int getLen_sentence() {
		return len_sentence;
	}

	public void setLen_sentence(int len_sentence) {
		this.len_sentence = len_sentence;
	}

	public int getPos_et1() {
		return pos_et1;
	}

	public void setPos_et1(int pos_et1) {
		this.pos_et1 = pos_et1;
	}

	public int getPos_et2() {
		return pos_et2;
	}

	public void setPos_et2(int pos_et2) {
		this.pos_et2 = pos_et2;
	}

	public String getType_et1() {
		return type_et1;
	}

	public void setType_et1(String type_et1) {
		this.type_et1 = type_et1;
	}

	public String getType_et2() {
		return type_et2;
	}

	public void setType_et2(String type_et2) {
		this.type_et2 = type_et2;
	}


	public int getNum_ets_inside() {
		return num_ets_inside;
	}

	public void setNum_ets_inside(int num_ets_inside) {
		this.num_ets_inside = num_ets_inside;
	}

	public int getHas_triggers() {
		return has_triggers;
	}

	public void setHas_triggers(int has_triggers) {
		this.has_triggers = has_triggers;
	}

	public int getNum_triggers() {
		return num_triggers;
	}

	public void setNum_triggers(int num_triggers) {
		this.num_triggers = num_triggers;
	}

	public String getType_triggers() {
		return type_triggers;
	}

	public void setType_triggers(String type_triggers) {
		this.type_triggers = type_triggers;
	}
	
	public int getIsHead_e1() {
		return isHead_e1;
	}

	public void setIsHead_e1(int isHead_e1) {
		this.isHead_e1 = isHead_e1;
	}

	public int getIsHead_e2() {
		return isHead_e2;
	}

	public void setIsHead_e2(int isHead_e2) {
		this.isHead_e2 = isHead_e2;
	}

	public int getSize_chunk_e1() {
		return size_chunk_e1;
	}

	public void setSize_chunk_e1(int size_chunk_e1) {
		this.size_chunk_e1 = size_chunk_e1;
	}

	public int getSize_chunk_e2() {
		return size_chunk_e2;
	}

	public void setSize_chunk_e2(int size_chunk_e2) {
		this.size_chunk_e2 = size_chunk_e2;
	}

	public int getNum_verbs_left() {
		return num_verbs_left;
	}

	public void setNum_verbs_left(int num_verbs_left) {
		this.num_verbs_left = num_verbs_left;
	}

	public int getNum_verbs_inside() {
		return num_verbs_inside;
	}

	public void setNum_verbs_inside(int num_verbs_inside) {
		this.num_verbs_inside = num_verbs_inside;
	}

	public int getNum_verbs_right() {
		return num_verbs_right;
	}

	public void setNum_verbs_right(int num_verbs_right) {
		this.num_verbs_right = num_verbs_right;
	}

	public int getNum_nouns_left() {
		return num_nouns_left;
	}

	public void setNum_nouns_left(int num_nouns_left) {
		this.num_nouns_left = num_nouns_left;
	}

	public int getNum_nouns_inside() {
		return num_nouns_inside;
	}

	public void setNum_nouns_inside(int num_nouns_inside) {
		this.num_nouns_inside = num_nouns_inside;
	}

	public int getNum_nouns_right() {
		return num_nouns_right;
	}

	public void setNum_nouns_right(int num_nouns_right) {
		this.num_nouns_right = num_nouns_right;
	}

	public EntityPair getPair() {
		return pair;
	}

	public void setPair(EntityPair pair) {
		this.pair = pair;
	}

	public int getNum_ets_left() {
		return num_ets_left;
	}

	public void setNum_ets_left(int num_ets_left) {
		this.num_ets_left = num_ets_left;
	}

	public int getNum_ets_right() {
		return num_ets_right;
	}

	public void setNum_ets_right(int num_ets_right) {
		this.num_ets_right = num_ets_right;
	}

	public int getNum_triggers_left() {
		return num_triggers_left;
	}

	public void setNum_triggers_left(int num_triggers_left) {
		this.num_triggers_left = num_triggers_left;
	}

	public int getNum_triggers_inside() {
		return num_triggers_inside;
	}

	public void setNum_triggers_inside(int num_triggers_inside) {
		this.num_triggers_inside = num_triggers_inside;
	}

	public int getNum_triggers_right() {
		return num_triggers_right;
	}

	public void setNum_triggers_right(int num_triggers_right) {
		this.num_triggers_right = num_triggers_right;
	}

	public boolean isHas_negation() {
		return has_negation;
	}

	public void setHas_negation(boolean has_negation) {
		this.has_negation = has_negation;
	}
	
	
	
	public int getPresence_triggers_left() {
		return presence_triggers_left;
	}

	public void setPresence_triggers_left(int presence_triggers_left) {
		this.presence_triggers_left = presence_triggers_left;
	}

	public int getPresence_triggers_inside() {
		return presence_triggers_inside;
	}

	public void setPresence_triggers_inside(int presence_triggers_inside) {
		this.presence_triggers_inside = presence_triggers_inside;
	}

	public int getPresence_triggers_right() {
		return presence_triggers_right;
	}

	public void setPresence_triggers_right(int presence_triggers_right) {
		this.presence_triggers_right = presence_triggers_right;
	}

	public int getNum_triggers_nouns_left() {
		return num_triggers_nouns_left;
	}

	public void setNum_triggers_nouns_left(int num_triggers_nouns_left) {
		this.num_triggers_nouns_left = num_triggers_nouns_left;
	}

	public int getNum_triggers_nouns_inside() {
		return num_triggers_nouns_inside;
	}

	public void setNum_triggers_nouns_inside(int num_triggers_nouns_inside) {
		this.num_triggers_nouns_inside = num_triggers_nouns_inside;
	}

	public int getNum_triggers_nouns_right() {
		return num_triggers_nouns_right;
	}

	public void setNum_triggers_nouns_right(int num_triggers_nouns_right) {
		this.num_triggers_nouns_right = num_triggers_nouns_right;
	}

	public int getNum_triggers_verbs_left() {
		return num_triggers_verbs_left;
	}

	public void setNum_triggers_verbs_left(int num_triggers_verbs_left) {
		this.num_triggers_verbs_left = num_triggers_verbs_left;
	}

	public int getNum_triggers_verbs_inside() {
		return num_triggers_verbs_inside;
	}

	public void setNum_triggers_verbs_inside(int num_triggers_verbs_inside) {
		this.num_triggers_verbs_inside = num_triggers_verbs_inside;
	}

	public int getNum_triggers_verbs_right() {
		return num_triggers_verbs_right;
	}

	public void setNum_triggers_verbs_right(int num_triggers_verbs_right) {
		this.num_triggers_verbs_right = num_triggers_verbs_right;
	}

	public int getNum_pos_triggers() {
		return num_pos_triggers;
	}

	public void setNum_pos_triggers(int num_pos_triggers) {
		this.num_pos_triggers = num_pos_triggers;
	}

	public int getNum_neg_triggers() {
		return num_neg_triggers;
	}

	public void setNum_neg_triggers(int num_neg_triggers) {
		this.num_neg_triggers = num_neg_triggers;
	}

	public int getNum_pos_triggers_left() {
		return num_pos_triggers_left;
	}

	public void setNum_pos_triggers_left(int num_pos_triggers_left) {
		this.num_pos_triggers_left = num_pos_triggers_left;
	}

	public int getNum_pos_triggers_inside() {
		return num_pos_triggers_inside;
	}

	public void setNum_pos_triggers_inside(int num_pos_triggers_inside) {
		this.num_pos_triggers_inside = num_pos_triggers_inside;
	}

	public int getNum_pos_triggers_right() {
		return num_pos_triggers_right;
	}

	public void setNum_pos_triggers_right(int num_pos_triggers_right) {
		this.num_pos_triggers_right = num_pos_triggers_right;
	}

	public int getNum_neg_triggers_left() {
		return num_neg_triggers_left;
	}

	public void setNum_neg_triggers_left(int num_neg_triggers_left) {
		this.num_neg_triggers_left = num_neg_triggers_left;
	}

	public int getNum_neg_triggers_inside() {
		return num_neg_triggers_inside;
	}

	public void setNum_neg_triggers_inside(int num_neg_triggers_inside) {
		this.num_neg_triggers_inside = num_neg_triggers_inside;
	}

	public int getNum_neg_triggers_right() {
		return num_neg_triggers_right;
	}

	public void setNum_neg_triggers_right(int num_neg_triggers_right) {
		this.num_neg_triggers_right = num_neg_triggers_right;
	}

	public boolean isNegation() {
		return negation;
	}

	public void setNegation(boolean negation) {
		this.negation = negation;
	}

	public static void writeToFile(File output, List<Record> records) throws IOException{
		OutputStream os = new FileOutputStream(output);
		OutputStreamWriter osr = new OutputStreamWriter(os,"utf-8");
		BufferedWriter bw = new BufferedWriter(osr);
		bw.write(
				"distance_pair"+"\t"
				+ "len_sentence"+"\t"
				+ "pos_et1"+"\t"
				+ "pos_et2"+"\t"
				
				+ "num_ets_left"+"\t"
				+ "num_ets_inside"+"\t"
				+ "num_ets_right"+"\t"
				
				+ "presence_triggers_left"+"\t"
				+ "presence_triggers_inside"+"\t"
				+ "presence_triggers_right"+"\t"
				
				+ "num_triggers_left"+"\t"
				+ "num_triggers_inside"+"\t"
				+ "num_triggers_right"+"\t"
				
				+ "num_triggers_verbs_left" + "\t"
				+ "num_triggers_verbs_inside" + "\t"
				+ "num_triggers_verbs_right" + "\t"
				
				+ "num_triggers_nouns_left" + "\t"
				+ "num_triggers_nouns_insight" + "\t"
				+ "num_triggers_nouns_right" + "\t"
				
				+ "num_pos_triggers_left"+"\t"
				+ "num_pos_triggers_inside"+"\t"
				+ "num_pos_triggers_right"+"\t"
				
				+ "num_neg_triggers_left"+"\t"
				+ "num_neg_triggers_inside"+"\t"
				+ "num_neg_triggers_right"+"\t"
				
				+ "num_neg_left" + "\t"
				+ "num_neg_inside" + "\t"
				+ "num_neg_right" + "\t"
				
				+ "num_preps_left" + "\t"
				+ "num_preps_inside" + "\t"
				+ "num_preps_right" + "\t"
				
				+ "is_head_e1"+"\t"
				+ "is_head_e2"+"\t"
				+ "size_chunk_e1"+"\t"
				+ "size_chunk_e2"+"\t"
				
				+ "num_verbs_left"+"\t"
				+ "num_verbs_inside"+"\t"
				+ "num_verbs_right"+"\t"
				
				+ "num_nouns_left"+"\t"
				+ "num_nouns_inside"+"\t"
				+ "num_nouns_right"+"\t"
				
				+ "num_pos_triggers"+"\t"
				+ "num_neg_triggers"+"\t"
				
				+ "target"+"\t"
				+ "entity_pair" + "\t"
				+ "sentence" + "\n");
		for(Record record: records){
			//entities
			bw.write(record.getDistance_pair()+"\t");
			bw.write(record.getLen_sentence()+"\t");
			bw.write(record.getPos_et1()+"\t");
			bw.write(record.getPos_et2()+"\t");
			
			bw.write(record.getNum_ets_left()+"\t");
			bw.write(record.getNum_ets_inside()+"\t");
			bw.write(record.getNum_ets_right()+"\t");
			
			//lexical features
			bw.write(record.getPresence_triggers_left()+"\t");
			bw.write(record.getPresence_triggers_inside()+"\t");
			bw.write(record.getPresence_triggers_right()+"\t");
			
			bw.write(record.getNum_triggers_left()+"\t");
			bw.write(record.getNum_triggers_inside()+"\t");
			bw.write(record.getNum_triggers_right()+"\t");
			
			bw.write(record.getNum_triggers_verbs_left()+"\t");
			bw.write(record.getNum_triggers_verbs_inside()+"\t");
			bw.write(record.getNum_triggers_verbs_right()+"\t");
			
			bw.write(record.getNum_triggers_nouns_left()+"\t");
			bw.write(record.getNum_triggers_nouns_inside()+"\t");
			bw.write(record.getNum_triggers_nouns_right()+"\t");
			
			bw.write(record.getNum_pos_triggers_left()+"\t");
			bw.write(record.getNum_pos_triggers_inside()+"\t");
			bw.write(record.getNum_pos_triggers_right()+"\t");
			
			bw.write(record.getNum_neg_triggers_left()+"\t");
			bw.write(record.getNum_neg_triggers_inside()+"\t");
			bw.write(record.getNum_neg_triggers_right()+"\t");
			//negations 
			bw.write(record.getNum_neg_left()+"\t");
			bw.write(record.getNum_neg_inside()+"\t");
			bw.write(record.getNum_neg_right()+"\t");
			
			//the 20 most frequent prepositions
			bw.write(record.getNum_preps_left()+"\t");
			bw.write(record.getNum_preps_inside()+"\t");
			bw.write(record.getNum_preps_right()+"\t");
			
			//syntactic features
			//bw.write(record+"\t");
			//write the target label 
			
			bw.write(record.getIsHead_e1()+"\t");
			bw.write(record.getIsHead_e2()+"\t");
			bw.write(record.getSize_chunk_e1()+"\t");
			bw.write(record.getSize_chunk_e2()+"\t");

			bw.write(record.getNum_verbs_left()+"\t");
			bw.write(record.getNum_verbs_inside()+"\t");
			bw.write(record.getNum_verbs_right()+"\t");
			
			bw.write(record.getNum_nouns_left()+"\t");
			bw.write(record.getNum_nouns_inside()+"\t");
			bw.write(record.getNum_nouns_right()+"\t");
			
			bw.write(record.getNum_pos_triggers()+"\t");
			bw.write(record.getNum_neg_triggers()+"\t");
			
			bw.write(record.getPair().getLabel()+"\t");
			bw.write(record.getPair().toString()+"\t");
			bw.write(record.getPair().getSentence()+"\t");
			bw.write("\n");
		}
		bw.close();
		
	}

	public int getNum_neg_left() {
		return num_neg_left;
	}

	public void setNum_neg_left(int num_neg_left) {
		this.num_neg_left = num_neg_left;
	}

	public int getNum_neg_inside() {
		return num_neg_inside;
	}

	public void setNum_neg_inside(int num_neg_inside) {
		this.num_neg_inside = num_neg_inside;
	}

	public int getNum_neg_right() {
		return num_neg_right;
	}

	public void setNum_neg_right(int num_neg_right) {
		this.num_neg_right = num_neg_right;
	}
	
	public int getNum_preps_left() {
		return num_preps_left;
	}

	public void setNum_preps_left(int num_preps_left) {
		this.num_preps_left = num_preps_left;
	}

	public int getNum_preps_inside() {
		return num_preps_inside;
	}

	public void setNum_preps_inside(int num_preps_inside) {
		this.num_preps_inside = num_preps_inside;
	}

	public int getNum_preps_right() {
		return num_preps_right;
	}

	public void setNum_preps_right(int num_preps_right) {
		this.num_preps_right = num_preps_right;
	}

	public void print(){
		System.out.println(
				
				"sentence: "+this.getPair().getSentence() + "\n"
				+ "entity_pair=" + this.getPair() + "\n"
				+ "target="+ this.getPair().getLabel() + "\n"
				
				+ "distance_pair="+ this.getDistance_pair() + ", "
				+ "len_sentence="+ this.getLen_sentence() + ", "
				+ "pos_et1=" + this.getPos_et1() +", "
				+ "pos_et2="+ this.getPos_et2() + "\n"
				
				+ "num_ets_left="+ this.getNum_ets_left() + ", "
				+ "num_ets_inside="+ this.getNum_ets_inside() + ", "
				+ "num_ets_right="+ this.getNum_ets_right() + "\n"
				
				+ "presence_triggers_left="+ this.getPresence_triggers_left() + ", "
				+ "presence_triggers_inside="+ this.getPresence_triggers_inside() + ", "
				+ "presence_triggers_right="+ this.getPresence_triggers_right() + "\n"
					
				+ "num_triggers_left="+ this.getNum_triggers_left() + ", "
				+ "num_triggers_inside="+ this.getNum_triggers_inside() + ", "
				+ "num_triggers_right="+ this.getNum_triggers_right() + "\n"
				
				+ "num_triggers_verbs_left="+ this.getNum_triggers_verbs_left() + ", "
				+ "num_triggers_verbs_inside="+ this.getNum_triggers_verbs_inside() + ", "
				+ "num_triggers_verbs_right="+ this.getNum_triggers_verbs_right() + "\n"
				
				+ "num_triggers_nouns_left="+ this.getNum_triggers_nouns_left() + ", "
				+ "num_triggers_nouns_inside="+ this.getNum_triggers_nouns_inside() + ", "
				+ "num_triggers_nouns_right="+ this.getNum_triggers_nouns_right() + "\n"
				
				+ "num_pos_triggers_left="+ this.getNum_pos_triggers_left() + ", "
				+ "num_pos_triggers_inside="+ this.getNum_pos_triggers_inside() + ", "
				+ "num_pos_triggers_right="+ this.getNum_pos_triggers_right() + "\n"
				
				+ "num_neg_triggers_left="+ this.getNum_neg_triggers_left() + ", "
				+ "num_neg_triggers_inside="+ this.getNum_neg_triggers_inside() + ", "
				+ "num_neg_triggers_right="+ this.getNum_neg_triggers_right() + "\n"
				
				+ "num_neg_left=" + this.getNum_neg_left() +", "
				+ "num_neg_inside=" + this.getNum_neg_inside() + ", "
				+ "num_neg_right=" + this.getNum_neg_right() + "\n"
				
				
				+ "num_preps_left=" + this.getNum_preps_left() + ", "
				+ "num_preps_inside=" + this.getNum_preps_inside() + ", "
				+ "num_preps_right=" + this.getNum_preps_right() + "\n "
				
				+ "is_head_e1=" + this.getIsHead_e1() + ", "
				+ "is_head_e2=" + this.getIsHead_e2() + ", "
				+ "size_chunk_e1=" + this.getSize_chunk_e1() + ", "
				+ "size_chunk_e2=" + this.getSize_chunk_e2() + "\n"
				
				+ "num_verbs_left=" + this.getNum_verbs_left() + ", "
				+ "num_verbs_inside=" + this.getNum_verbs_inside() + ", "
				+ "num_verbs_right=" + this.getNum_verbs_right() + "\n"
				
				+ "num_nouns_left=" + this.getNum_nouns_left() + ", "
				+ "num_nouns_inside=" + this.getNum_nouns_inside() + ", "
				+ "num_nouns_right=" + this.getNum_nouns_right() + "\n");
	}
}
