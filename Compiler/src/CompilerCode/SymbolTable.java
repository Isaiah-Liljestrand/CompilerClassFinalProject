package CompilerCode;

import java.util.ArrayList;
import java.util.List;

import CompilerCode.Token.type_enum;

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
		this.parent.children.add(this);
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
	
	/**
	 * Simple function that prints all symbol tables recursively
	 */
	public void printSymbolTable() {
	//	System.out.println("TEST");
		System.out.println("Symbol Table " + label + ":");
		for(SymbolTableEntry entry : this.entries) {
			System.out.println(entry.name + " " + entry.value);
		}
		for(SymbolTable table : this.children) {
			table.printSymbolTable();
		}
	}
	
	
	/**
	 * Builds the top level declaration table and recursively creates lower level ones
	 * @param tree current Ptree being dealt with
	 * @param table top level symbol table
	 */
	public void buildDeclarationTable(Ptree tree) {
		switch(tree.token.type) {
		case functionDeclaration:
			SymbolTable sTable = new SymbolTable(this, tree.children.get(1).token.token);
			sTable.buildStatementTable(tree, false);
			return;
		case variableDeclaration:
			type_enum vType = tree.children.get(0).children.get(0).token.type;
			this.variableDeclarationHelper(tree.children.get(1), vType);
			return;
		default:
			for(Ptree t : tree.children) {
				this.buildDeclarationTable(t);
			}
		}
	}
	
	
	/**
	 * Adds variable declarations to a table and checks if they are in scope
	 * @param tree subtree with variable declaration on top
	 * @param vType int or char
	 * @param table symbol table of the current scope
	 */
	public void variableDeclarationHelper(Ptree tree, type_enum vType) {
		switch(tree.token.type) {
		case variableDeclarationInitialize:
			if(tree.children.size() == 1) {
				variableDeclarationHelper(tree.children.get(0), vType);
			} else {
				variableDeclarationHelper(tree.children.get(2), vType);
				variableDeclarationHelper(tree.children.get(0), vType);
			}
			return;
		case variableDeclarationID:
			String string = tree.children.get(0).token.token;
			for(SymbolTableEntry s : this.entries) {
				if(s.name.equals(string)) {
					ErrorHandler.addError("Variable " + string + " already declared");
				}
			}
			addEntry(string, vType);
			return;
		case simpleExpression:
			buildStatementTable(tree, false);
			return;
		default:
			for(Ptree t : tree.children) {
				variableDeclarationHelper(t, vType);
			}
		}
	}

	
	/**
	 * Builds symbol tables under function declarations or control flow statements
	 * @param tree current Ptree being dealt with
	 * @param table top level symbol table
	 * @param top  whether it is the top of a loop or conditional
	 */
	private void buildStatementTable(Ptree tree, boolean top) {
		SymbolTable sTable;
		switch(tree.token.type) {
		case parameter:
			String string = tree.children.get(1).token.token;
			type_enum type = tree.children.get(0).children.get(0).token.type;
			for(SymbolTableEntry s : this.entries) {
				if(s.name.equals(string)) {
					ErrorHandler.addError("Variable " + string + " already declared");
				}
			}
			addEntry(string, type);
			return;
		case ifStatement:
			if(!top) {
				sTable = new SymbolTable(this, "if");
				sTable.buildStatementTable(tree, true);
			} else {
				for(Ptree t : tree.children) {
					buildStatementTable(t, false);
				}
			}
			return;
		case whileStatement:
			if(!top) {
				sTable = new SymbolTable(this, "while");
				sTable.buildStatementTable(tree, true);
			} else {
				for(Ptree t : tree.children) {
					buildStatementTable(t, false);
				}
			}
			return;
		case forStatement:
			if(!top) {
				sTable = new SymbolTable(this, "for");
				sTable.buildStatementTable(tree, true);
			} else {
				for(Ptree t : tree.children) {
					buildStatementTable(t, false);
				}
			}
			return;
		case variableDeclaration:
			variableDeclarationHelper(tree, tree.children.get(0).children.get(0).token.type);
			return;
		case call:
			if(isFunctionNameInScope(tree.children.get(0).token.token)) {
				return;
			} else {
				ErrorHandler.addError("Function call " + tree.children.get(0).token.token + " not in scope");
				return;
			}
		case variable:
			if(isVariableNameInScope(tree.children.get(0).token.token)) {
				return;
			} else {
				ErrorHandler.addError("Variable " + tree.children.get(0).token.token + " not in scope");
				return;
			}
		default:
			//System.out.println("Default");
			for(Ptree t : tree.children) {
				buildStatementTable(t, false);
			}
			
		}
	}

}
