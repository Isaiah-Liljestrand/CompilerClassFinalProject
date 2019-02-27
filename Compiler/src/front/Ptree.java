package front;

import java.util.List;
import java.util.ArrayList;

import front.Token.type_enum;

/**
 * Parse Tree Class
 * Keeps track of the hierarchy order of the parsing
 * Idea described by Isaiah and Jacob
 * @author Ben
 *
 */
public class Ptree {
	private Token token;
	private List<Ptree> children;
	
	/**
	 * Constructs Ptree from a token. Used to add a token to the tree
	 * @param tok token being added to the Ptree node
	 */
	Ptree(Token tok) {
		token = tok;
		children = new ArrayList<Ptree>();
	}
	
	/**
	 * Constructs a new Ptree node, used for intermediary tree nodes
	 * @param e type the Ptree is intended to be
	 */
	Ptree(type_enum e) {
		token = new Token(e);
		children = new ArrayList<Ptree>();
	}
	
	/**
	 * Prints the tree using the the recursive PT function
	 */
	public void printTree(){
		System.out.println("\nHash Tree:");
		PT(0); //assumes the calling Ptree is root
	}
	
	/**
	 * Prints the leaf value
	 * Recursively calls self on all children
	 * @param i the depth of the current child
	 */
	private void PT(int i){
		System.out.println();
		for(int j = 0; j < i; j++){
			System.out.print("  ");
		}
		System.out.print(this.token.type);
		if (this.token.token != null) {
			System.out.print(" | " + this.token.token);
		}
		for(Ptree child : children){
			child.PT(i+1);
		}
	}

	/**
	 * Getter for children of a Ptree
	 * @return the children
	 */
	protected List<Ptree> getChildren() {
		return children;
	}

	/**
	 * Setter for children of a Ptree
	 * @param sets the entire children list to input
	 */
	protected void setChildren(List<Ptree> children) {
		this.children = children;
	}
	
	/**
	 * Checks that the children are not null
	 * @return true if no children are null
	 */
	protected boolean verifyChildren() {
		if (this.children.contains(null)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Adds a single tree to the child list
	 * @param treein a single child Ptree to add to this class' list
	 */
	protected void addChild(Ptree treein) {
		//System.out.println("Added Child");
		children.add(treein);
	}
	
	
	/**
	 * kills the first born (removes the first child in the list)
	 * does nothing if there are no children nodes
	 */
	protected void removeChildren() {
		while(!this.children.isEmpty()) {
			this.children.remove(0);
		}
	}
	
	/**
	 * Getter for token of a Ptree node
	 * @return the Token in this Ptree node instance
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Setter for token of a Ptree node
	 * @param token to be set
	 */
	public void setToken(Token token){
		this.token = token;
	}
	
	
	/**
	 * Checks if all goto calls have a matching goto jump location
	 * @param tree root of the parse tree
	 * @return true if all goto statements match up
	 */
	public static boolean gotoChecker(Ptree tree) {
		List<Ptree> gotoJumpPlace = new ArrayList<Ptree>();
		gotoJumpPlace = findTrees(tree, gotoJumpPlace, type_enum.gotoJumpPlace);
		List<Ptree> gotoCalls = new ArrayList<Ptree>();
		gotoCalls = findTrees(tree, gotoCalls, type_enum.gotoStatement);
		boolean b;
		for(Ptree tcall : gotoCalls) {
			b = false;
			for(Ptree tjump : gotoJumpPlace) {
				if(tcall.getChildren().get(1).token.token.equals(tjump.getChildren().get(0).token.token)) {
					b = true;
				}
			}
			if(!b) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * finds all of one type of Ptree recursively
	 * @param token to be found
	 * @param trees must be initialized to a new arraylist before call
	 * @return List of Ptrees containing specified token
	 */
	public static List<Ptree> findTrees(Ptree tree, List<Ptree> trees, type_enum type) {
		if(tree.token.type == type) {
			trees.add(tree);
		}
		
		for(Ptree t: tree.getChildren()) {
			trees = findTrees(t, trees, type);
		}
		return trees;
	}
}
