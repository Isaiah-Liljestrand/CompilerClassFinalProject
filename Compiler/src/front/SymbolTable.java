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
	private void addEntry(String name, type_enum value)	{
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
			type_enum vType = tree.children.get(0).children.get(0).token.type;
			variableDeclarationHelper(tree, vType, table);
			return;
		default:
			for(Ptree t : tree.children) {
				buildDeclarationTable(t, table);
			}
		}
	}
	
	
	/**
	 * Adds variable declarations to a table and checks if they are in scope
	 * @param tree subtree with variable declaration on top
	 * @param vType int or char
	 * @param table symbol table of the current scope
	 */
	public static void variableDeclarationHelper(Ptree tree, type_enum vType, SymbolTable table) {
		switch(tree.token.type) {
		case variableDeclarationID:
			String string = tree.children.get(0).token.token;
			if(!table.isVariableNameInScope(string)) {
				table.addEntry(string, vType);
				return;
			}
			System.out.println("Error: Variable already declared");   // needs to be elaborated
			return;
		default:
			for(Ptree t : tree.children) {
				variableDeclarationHelper(t, vType, table);
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
		case parameter:
			String string = tree.children.get(1).token.token;
			type_enum type = tree.children.get(0).token.type;
			if(!table.isVariableNameInScope(string)) {
				table.addEntry(string,  type);
			}
			System.out.println("Error: parameter passed in through function is already declared");
			return;
		case ifStatement:
			sTable = new SymbolTable(table, "if");
			buildStatementTable(tree, sTable);
			return;
		case whileStatement:
			sTable = new SymbolTable(table, "while");
			buildStatementTable(tree, sTable);
			return;
		case variableDeclaration:
			variableDeclarationHelper(tree, tree.children.get(0).children.get(0).token.type, table);
			return;
		case call:
			//verify that function call is in scope
			//System.out.println("Call");
			if(table.isFunctionNameInScope(tree.children.get(0).token.token)){
				return;
			} else {
				System.out.println("Warning: Function " + tree.token.token + " Not defined in current scope");
				return;
			}
		case variable:
			//System.out.println("Variable");
			//verify legitimacy and check that it is in scope
			if(table.isVariableNameInScope(tree.children.get(0).token.token)) {
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
