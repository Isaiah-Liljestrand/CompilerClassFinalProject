package front;

import java.util.ArrayList;
import java.util.List;

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
	public void AddEntry(String name, Ptree value)	{
		SymbolTableEntry newEntry = new SymbolTableEntry(name, value);
		entries.add(newEntry);
	}

	
	/**
	 * Checks if a variable name is in scope only applicable to variable names not functions
	 * @param name variable name to be checked
	 * @return true if in scope, false if not
	 */
	public boolean isVariableNameInScope(String name) {
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
	public boolean isFunctionNameInScope(String name) {
		for(SymbolTable table : this.children) {
			if(table.label == name) {
				return true;
			}
		}
		if(this.parent != null) {
			return this.parent.isFunctionNameInScope(name);
		} else {
			return false;
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
			//read in variable to table
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
			sTable = new SymbolTable(table, "if");
			buildStatementTable(tree, sTable);
			return;
		case whileStatement:
			sTable = new SymbolTable(table, "while");
			buildStatementTable(tree, sTable);
			return;
		case variableDeclaration:
			//Read in new entry and check if already declared
			return;
		case expression:
			//modify existing entry if applicable and check if it was declared for function calls and variable modifications also, keep calling down tree
			return;
		case call:
			//verify that function call is in scope
			return;
		case variable:
			//verify legitimacy and check that it is in scope
			return;
		default:
			if(tree.children != null) {
				for(Ptree t : tree.children) {
					buildDeclarationTable(t, table);
				}
			}
			
		}
	}

}
