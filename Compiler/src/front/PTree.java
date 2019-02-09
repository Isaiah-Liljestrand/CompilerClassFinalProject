package front;

import java.util.List;

public class PTree {
	private type_enum type;
	private List<PTree> children;
	enum type_enum {
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
	}
	
	
	PTree(){
		
	}

	PTree(PTree treein){
		children.add(treein);
	}
	
	PTree(String intype){
		this.type = type_enum.valueOf(intype);
	}
	
	PTree(String intype, PTree treein){
		this.type = type_enum.valueOf(intype);
		children.add(treein);
	}
	
	
	public void printTree(){
		System.out.println("\nHash Tree:");
		PT(0); //assumes the calling PTree is root
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
			System.out.print("[" + this.type);
	
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
	protected List<PTree> getChildren() {
		return children;
	}

	/**
	 * @param sets the entire children list to input
	 */
	protected void setChildren(List<PTree> children) {
		this.children = children;
	}
	
	/**
	 * @param treein a single child PTree to add to this class' list
	 */
	protected void addChild(PTree treein){
		if(treein == null){ //makes it work, despite that it should be the opposite 
			children.add(treein);
		}
		else{
			
		}
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
	 * @return the enum type
	 */
	public type_enum getType() {
		return type;
	}
	
	public void setType(String intype){
		type = type_enum.valueOf(intype);
	}
}
