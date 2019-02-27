package front;

import java.util.ArrayList;
import java.util.List;

/**
 * Bacicly Ptree but squished
 * @author Ben
 *
 */
public class AST {
	private List<Token> children;
	
	public AST() {
		children = new ArrayList<Token>();
	}
	
	public AST(Ptree input) {
		children = new ArrayList<Token>();
		interperet(input);
	}
	
	public AST(Grammar input) {
		children = new ArrayList<Token>();
		interperet(input.getRoot());
	}
	
	public void populate(Ptree input) {
		interperet(input);
	}
	
	public void populate(Grammar input) {
		interperet(input.getRoot());
	}
	
	/**
	 * takes a Ptree tree as input and converts it to an AST with the called AST as the root
	 * 					~~~Assumes DFS is desired~~~
	 * @param input
	 */
	protected void interperet(Ptree input){
		for(int i = 0;i < input.getChildren().size(); i++){
			interperet(input.getChildren().get(i));
		}
		children.add(input.getToken());
	}
	
	/**
	 * To flatten the current tree, height should not exceed 2
	 * Basically flatten all the Ptree nodes to sequential children in this AST root's list (Ben's current understanding atleast)
	 */
	protected void flatten() {
		
	}

	
	/**
	 * gets child i's token
	 * for ease of handling latter
	 * @param i the child token off of root
	 * @return
	 */
	public Token getToken(int i) {
		return children.get(i);
	}
	
	/**
	 * sets child i's token
	 * for ease of handling latter
	 * @param token
	 * @param i the child token off of root
	 */
	/**public void setToken(Token token, int i) {
		children.get(i(token);
	}*/

	/**
	 * 
	 * @return the current AST's children
	 */
	public List<Token> getChildren() {
		return children;
	}

	/**
	 * sets current AST children list to input
	 * @param children
	 */
	public void setChildren(List<Token> children) {
		this.children = children;
	}
	
	/**
	 * adds AST child to the current AST children list
	 * @param child
	 */
	public void addChild(Token child) {
		this.children.add(child);
	}

}
