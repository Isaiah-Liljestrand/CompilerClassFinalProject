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
	
	Ptree(Token tok) {
		token = tok;
		children = new ArrayList<Ptree>();
	}
	
	Ptree(type_enum e) {
		token = new Token(e);
		children = new ArrayList<Ptree>();
	}
	
	
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
		if(!children.isEmpty()){
			System.out.println();
			for(int j = 0; j < i; j++){
				System.out.print("  ");
			}
			System.out.print(this.token.type);
	
			for(Ptree child : children){
				//System.out.println();
				child.PT(i+1);
			}
			//System.out.println();
			//for(int j = 0; j < i; j++){
			//	System.out.print("  ");
			//}
			//System.out.print("]");
		}
	}

	/**
	 * @return the children
	 */
	protected List<Ptree> getChildren() {
		if(children.size() == 0) {
			return null;
		}
		return children;
	}

	/**
	 * @param sets the entire children list to input
	 */
	protected void setChildren(List<Ptree> children) {
		this.children = children;
	}
	
	protected boolean verifyChildren(int num) {
		if(this.children.size() != num) {
			return false;
		}
		if (this.children.contains(null)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param treein a single child Ptree to add to this class' list
	 */
	protected void addChild(Ptree treein) {
		children.add(treein);
	}
	
	protected void addChildFromToken(Token token) {
		children.add(new Ptree(token));
	}
	
	/**
	 * kills the first born (removes the first child in the list)
	 * does nothing if there are no children nodes
	 */
	protected void removeChild(){
		if(!this.children.isEmpty()){
			this.children.remove(0);
		}
	}
	
	/**
	 * 
	 * @return the Token
	 */
	public Token getToken() {
		return token;
	}
	
	public void setToken(Token token){
		this.token = token;
	}
}
