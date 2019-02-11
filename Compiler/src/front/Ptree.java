package front;

import java.util.List;
import java.util.ArrayList;

import front.Token.type_enum;

public class Ptree {
	private Token token;
	private List<Ptree> children;
	/*enum type_enum {
		//based off of our current BNF types
		root,
		program,
		declarationList,
		declaration,
		recDeclaration,
		varDeclaration,
		scopedVarDeclaration,
		varDeclList,
		varDeclInitialize,
		varDeclId,
		scopedTypeSpecifier,
		typeSpecifier,
		returnTypeSpecifier,
		funDeclaration,
		params,
		paramList,
		paramTypeList,
		paramIdList,
		paramId,
		statement,
		compoundStmt,
		localDeclarations,
		statementList,
		expressionStmt,
		selectionStmt,
		iterationStmt,
		returnStmt,
		breakStmt,
		expression,
		simpleExpression,
		andExpression,
		unaryRelExpression,
		relExpression,
		relop,
		sumExpression,
		sumop,
		term,
		mulop,
		unaryExpression,
		unaryop,
		factor,
		mutable,
		immutable,
		call,
		args,
		argList,
		constant,
		IDfunc //not actualy sure what this is...
	}*/
	
	
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
	 * Formats as described in class 2/8/2019
	 * @param i the depth of the current child
	 */
	private void PT(int i){
		if(!children.isEmpty()){
			int j = 0;
			for(j = 0; j < i; j++){
				System.out.print("  ");
			}
			System.out.print("[" + this.token.type);
	
			for(j = 0; j < this.children.size(); j++){
				System.out.println();
				children.get(j).PT(i+1);
			}
			System.out.print("]");
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
		//int i;
		if(this.children.size() != num) {
			return false;
		}
		if (this.children.contains(null)) {
			return false;
		}
		/*for(i = 0; i < num; i++) {
			if(this.children.get(i) == null) {
				return false;
			}
		}*/
		return true;
	}
	
	/**
	 * @param treein a single child Ptree to add to this class' list
	 */
	protected void addChild(Ptree treein) {
		children.add(treein);
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
