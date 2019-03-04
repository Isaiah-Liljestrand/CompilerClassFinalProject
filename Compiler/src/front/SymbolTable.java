package front;

import java.util.ArrayList;
import java.util.List;

import front.Token.type_enum;

public class SymbolTable {
	protected String label;
	protected List<SymbolTableEntry> entries;
	protected List<SymbolTable> children;
	protected SymbolTable parent;
	
	/**
	 * Constructor to be called from main to create the top level symbol table
	 */
	public SymbolTable() {
		this.label = "program";
		this.parent = null;
		this.children = new ArrayList<SymbolTable>();
		this.entries = new ArrayList<SymbolTableEntry>();
	}
	
	/**
	 * Constructor to be called from inside the class to build sub-tables
	 * @param t symbol table parent calling the constructor
	 * @param label name of the function for use in calling later
	 */
	private SymbolTable(SymbolTable parent, String label) {
		this.label = label;
		this.parent = parent;
		this.children = new ArrayList<SymbolTable>();
		this.entries = new ArrayList<SymbolTableEntry>();
	}
	
	
	/**
	 * Adds an entry to the symbol table
	 * @param name entry variable name
	 * @param value entry variable value
	 */
	private void AddEntry(String name, type_enum value)	{
		SymbolTableEntry newEntry = new SymbolTableEntry(name, value);
		entries.add(newEntry);
	}

	
	/**
	 * Checks if a variable name is in scope only applicable to variable names not functions
	 * @param name variable name to be checked
	 * @return true if in scope, false if not
	 */
	private boolean isVariableNameInScope(String name) {
		for(SymbolTableEntry entry : entries) {
			if (entry.name.equals(name)) {
				return true;
			}
		}
		if (parent != null) {
			return parent.isVariableNameInScope(name);
		}
		return false;
	}
	
	/**
	 * Checks if a function call name is in scope
	 * @param name the name of the function being called
	 * @return true if function name is in scope, false if not
	 */
	private boolean isFunctionNameInScope(String name) {
		if(this.parent != null) {
			return this.parent.isFunctionNameInScope(name);
		} else {
			for(SymbolTable table : this.children) {
				if(name.equals(table.label)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public void printSymbolTable() {
	//	System.out.println("TEST");
		for(SymbolTableEntry entry : this.entries) {
			System.out.println(entry.name + " " + entry.value);
		}
		for(SymbolTable table : this.children) {
			for(SymbolTableEntry entry2 : table.entries) {
				System.out.println(entry2.name + " " + entry2.value);
			}
		}
	}
	
	
	/**
	 * Builds the top level declaration table and recursively creates lower level ones
	 * @param tree current Ptree being dealt with
	 * @param table top level symbol table
	 */
	public static void buildDeclarationTable(Ptree tree, SymbolTable table) {
		switch(tree.token.type) {
		case functionDeclaration:
			SymbolTable sTable = new SymbolTable(table, tree.children.get(1).token.token);
			buildStatementTable(tree, sTable);
			return;
		case variableDeclaration:
			SymbolTable sTable1 = new SymbolTable(table, tree.children.get(1).token.token);
			buildStatementTable(tree, sTable1);
			return;
		default:
			for(Ptree t : tree.children) {
				buildDeclarationTable(t, table);
			}
		}
	}

	
	/**
	 * Builds symbol tables under function declarations or control flow statements
	 * @param tree current Ptree being dealt with
	 * @param table top level symbol table
	 */
	private static void buildStatementTable(Ptree tree, SymbolTable table) {
		SymbolTable sTable;
		switch(tree.token.type) {
		case ifStatement:
			//System.out.println("IF");
			sTable = new SymbolTable(table, "if");
			buildStatementTable(tree, sTable);
			return;
		case whileStatement:
			//System.out.println("While");
			sTable = new SymbolTable(table, "while");
			buildStatementTable(tree, sTable);
			return;
		case variableDeclaration:
		//	System.out.println("Var");
			if(!table.isVariableNameInScope(tree.token.token)) {
				List<Ptree> trees = new ArrayList<Ptree>();
				List<Ptree> trees2 = new ArrayList<Ptree>();
				Ptree.findTrees(tree, trees, type_enum.identifier);
				Ptree.findTrees(tree, trees2, type_enum.k_int);
				table.AddEntry(trees.get(0).token.token, trees2.get(0).token.type);
				System.out.println(trees.get(0).token.token);
			}
			return;
		//case expression:
			//check variable validity and change value in table
		//	if(!table.isVariableNameInScope(tree.token.token)) {
		//		System.out.println("Warning: Variable " + tree.token.token + " Not defined in current scope");
		//		return;
		//	} else {
		//		// Update variable, but I'm not sure what value we are updating
		//	}
		//	return;
		case call:
			//verify that function call is in scope
			//System.out.println("Call");
			if(table.isFunctionNameInScope(tree.token.token)){
				return;
			} else {
				System.out.println("Warning: Function " + tree.token.token + " Not defined in current scope");
				return;
			}
		case variable:
			//System.out.println("Variable");
			//verify legitimacy and check that it is in scope
			if(!table.isVariableNameInScope(tree.token.token)) {
				table.AddEntry(tree.token.token, tree.token.type);
				return;
			} else {
				System.out.println("Warning: Variable " + tree.token.token + " Not defined in current scope");
				return;
			}
		default:
			//System.out.println("Default");
			if(tree.children != null) {
				for(Ptree t : tree.children) {
					buildDeclarationTable(t, table);
				}
			}
			
		}
	}

}
