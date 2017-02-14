package main;

public class EntityPair {
	
	private String entity1;
	private String entity2;
	private String sentence = "";
	private String label = "";
	private boolean is_empty = true;
	
	EntityPair(){
		
	}
	
	public EntityPair(String entity1, String entity2){
		this.entity1 = entity1;
		this.entity2 = entity2;
		this.is_empty = false;
	}

	public String getEntity1() {
		return entity1;
	}

	public void setEntity1(String entity1) {
		this.entity1 = entity1;
	}

	public String getEntity2() {
		return entity2;
	}

	public void setEntity2(String entity2) {
		this.entity2 = entity2;
	}
	
	public String getSentence() {
		return sentence;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	public boolean is_empty(){
		return this.is_empty;
	}
	public String toString(){
		return "(" + this.getEntity1() + ", "+ this.getEntity2()+")";
	}
}
