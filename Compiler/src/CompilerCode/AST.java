package CompilerCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Not currently being used, may be actually filled out in order to print trees more cleanly
 * @author Ben
 */
public class AST {
	private Token token;
	private List<AST> children;
	
	public AST() {
		children = new ArrayList<AST>();
	}
	
	public AST(Ptree input) {
		children = new ArrayList<AST>();
		interpret(input);
	}
	
	public AST(Grammar input) {
		children = new ArrayList<AST>();
		interpret(input.root);
	}
	
	public void populate(Ptree input) {
		interpret(input);
	}
	
	public void populate(Grammar input) {
		interpret(input.root);
	}
	
	/**
	 * takes a Ptree tree as input and converts it to an AST with the called AST as the root
	 * 					~~~Assumes DFS is desired~~~
	 * @param input
	 */
	public void interpret(Ptree input) {
		Ptree simpleinput = simplify(input);
		if (simpleinput != null) {
			token = simpleinput.token;		
			for(Ptree child : simpleinput.children){
				AST newAST = new AST(child);
				if (newAST.token != null) {
					children.add(newAST);
				}
			}
		}
	}
	
	private Ptree simplify(Ptree tree) {
		switch (tree.token.type) {
		case openParenthesis:
			return null;
		case closedParenthesis:
			return null;
		case openCurlyBracket:
			return null;
		case closedCurlyBracket:
			return null;
		case semicolon:
			return null;
		default:
			break;
		}
			
		return tree;
	}
	
	/**
	 * Prints the tree using the the recursive PT function
	 */
	public void printTree() {
		System.out.println("\nHash Tree:");
		PT(0); //assumes the calling Ptree is root
	}
	
	/**
	 * Prints the leaf value
	 * Recursively calls self on all children
	 * @param i the depth of the current child
	 */
	private void PT(int i) {
		System.out.println();
		for(int j = 0; j < i; j++){
			System.out.print("  ");
		}
		System.out.print(this.token.type);
		if (this.token.token != null) {
			System.out.print(" | " + this.token.token);
		}
		for(AST child : children){
			child.PT(i+1);
		}
	}
	
	/**
	 * To flatten the current tree, height should not exceed 2
	 * Basically flatten all the Ptree nodes to sequential children in this AST root's list (Ben's current understanding atleast)
	 */
	/*protected void flatten() {
		
	}*/

	
	/**
	 * gets child i's token
	 * for ease of handling latter
	 * @param i the child token off of root
	 * @return
	 */
	/*public Token getTokenAt(int i) {
		return children.get(i);
	}*/
	
	/**
	 * adds AST child to the current AST children list
	 * @param child
	 */
	/*public void addChild(Token child) {
		this.children.add(child);
	}*/
}
