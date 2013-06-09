

public class Node{
	String word = "-1";
	int frequency = 0;
	String code = "";
	Node left = null;
	Node right = null;
	
	public Node(){
		//default constructor
	}
	
	public Node(String word){
		this.word = word;//ascii code number of char. ex:'A'=65
		this.frequency = 0;
		this.code = "";//result of this word after encode. ex:"0100"
		this.left = null;
		this.right = null;
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node rigth) {
		this.right = rigth;
	}

	public boolean isCodeNull(){
		if(this.code == null)
			return true;
		else
			return false;
	}
	
}