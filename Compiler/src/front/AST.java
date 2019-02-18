package front;

import java.util.List;

public class AST {
	private Token token;
	private List<AST> children;
	
	public AST() {

	}
	
	public AST(Ptree input) {
		interperet(input);
	}
	
	/**
	 * takes a Ptree tree as input and converts it to an AST with the called AST as the root
	 * @param input
	 */
	protected void interperet(Ptree input){
		
	}
	
	/**
	 * To flatten the current tree, height should not exceed 2
	 * Basically flatten all the Ptree nodes to sequential children in this AST root's list (Ben's current understanding atleast)
	 */
	protected void flatten() {
		
	}

	/**
	 * gets current AST token
	 * @return
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * gets child i's token
	 * for ease of handling latter
	 * @param i the child token off of root
	 * @return
	 */
	public Token getToken(int i) {
		return children.get(i).getToken();
	}

	/**
	 * sets current AST's Token
	 * @param token
	 */
	public void setToken(Token token) {
		this.token = token;
	}
	
	/**
	 * sets child i's token
	 * for ease of handling latter
	 * @param token
	 * @param i the child token off of root
	 */
	public void setToken(Token token, int i) {
		children.get(i).setToken(token);
	}

	/**
	 * 
	 * @return the current AST's children
	 */
	public List<AST> getChildren() {
		return children;
	}

	/**
	 * sets current AST children list to input
	 * @param children
	 */
	public void setChildren(List<AST> children) {
		this.children = children;
	}
	
	/**
	 * adds AST child to the current AST children list
	 * @param child
	 */
	public void addChild(AST child) {
		this.children.add(child);
	}

}
