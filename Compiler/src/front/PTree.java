package front;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse Tree Class.
 * Keeps track of the hierarchy order of the parsing
 * @author Ben
 *
 */
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
	
	/**
	 * basic constructor
	 */
	PTree(){
		children = new ArrayList<PTree>();
	}

	/** 
	 * constructor with a child tree argument
	 * @param treein
	 */
	PTree(PTree treein){
		children = new ArrayList<PTree>();
		children.add(treein);
	}
	
	/**
	 * constructor with a type argument
	 */
	PTree(String intype){
		children = new ArrayList<PTree>();
		this.type = type_enum.valueOf(intype);
	}
	
	/**
	 * constructor with a type & child tree argument
	 */
	PTree(String intype, PTree treein){
		children = new ArrayList<PTree>();
		this.type = type_enum.valueOf(intype);
		children.add(treein);
	}
	
	/**
	 * prints this branch's type and all of it's children's type
	 */
	public void printTree(){
		System.out.print("\nHash Tree:");
		PT(0);
		System.out.println();
	}
	
	/**
	 * Prints the leaf value
	 * Recursively calls self on all children
	 * Formats as described in class 2/8/2019
	 * @param i the depth of the current child
	 */
	private void PT(int i){
		int j = 0;
		if(this.getType() != type_enum.valueOf("root")){
			for(j = 0; j < i; j++){
				System.out.print("  ");
			}
			System.out.print("[" + this.type);
		}
		else{
			i -= 1;
		}
		if(this.children != null){
			for(j = 0; j < this.children.size(); j++){
				System.out.println();
				children.get(j).PT(i+1);
			}
		}
		if(this.getType() != type_enum.valueOf("root")){
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
		if(treein != null){
			this.children.add(treein);
			//System.out.println("made it here");
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
	
	/**
	 * Sets the type
	 * @param intype
	 */
	public void setType(String intype){
		type = type_enum.valueOf(intype);
	}
}
