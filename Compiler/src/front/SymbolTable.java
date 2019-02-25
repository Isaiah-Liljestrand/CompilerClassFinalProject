package front;

import java.util.ArrayList;
import java.util.List;

import front.Token.type_enum;

public class SymbolTable {
	public String label;
	public List<SymbolTableEntry> entries;
	public List<SymbolTable> children;
	public SymbolTable parent;
	
	public SymbolTable() {
		this.label = "program";
		this.parent = null;
		this.children = new ArrayList<SymbolTable>();
		this.entries = new ArrayList<SymbolTableEntry>();
	}
	private SymbolTable(SymbolTable t, String label) {
		this.label = label;
		this.parent = t;
		this.children = new ArrayList<SymbolTable>();
		this.entries = new ArrayList<SymbolTableEntry>();
	}
	
	public void AddEntry(String name, String value)	{
		SymbolTableEntry newEntry = new SymbolTableEntry(name, value);
		entries.add(newEntry);
	}
	
	/*public void AddFunctionEntry(String name, String value)	{
		//SymbolTable childTable = new SymbolTable();
		childTable.parent = this;
		SymbolTableEntry newEntry = new SymbolTableEntry(name, value);
		entries.add(newEntry);
	}*/
	
	/*public List<SymbolTable> GetChildTables() {
		for(SymbolTableEntry entry : entries) {
			if (entry.childTable != null) {
				childTables.add(entry.childTable);
			}
		}
		return childTables;
	}*/
	
	/**
	 * Checks if a variable name is in scope only applicable to variable names not functions
	 * @param name variable name to be checked
	 * @return true if in scope, false if not
	 */
	public boolean IsNameInScope(String name) {
		for(SymbolTableEntry entry : entries) {
			if (entry.name.equals(name)) {
				return true;
			}
		}
		if (parent != null) {
			return parent.IsNameInScope(name);
		}
		return false;
	}
	
	public void buildSymbolTable(Ptree root) {
		buildDeclarationTable(root, this);
		return;
		
		/*tree = findTrees(root, tree, type_enum.functionDeclaration); // First find the function Declarations 
		int i = 0;
		
		for(Ptree t: tree) { // Then find the symbols's in each of those functions
			AddFunctionEntry(t.getToken().getToken(), t.getToken().getToken());
			List<Ptree> Lists1 = new ArrayList<Ptree>();
			List<Ptree> Lists2 = new ArrayList<Ptree>();
			Lists1 = findTrees(t, Lists1, type_enum.variableTypeSpecifier);
			Lists2 = findTrees(t, Lists2, type_enum.identifier);
			for(Ptree tr: Lists1) {
				AddEntry(Lists2.get(i).getToken().getToken(), tr.getToken().getToken());
			}
		}*/
	}
	
	/**
	 * Builds the top level declaration table and recursively creates lower level ones
	 * @param tree current Ptree being dealt with
	 * @param table top level symbol table
	 */
	private static void buildDeclarationTable(Ptree tree, SymbolTable table) {
		switch(tree.getToken().type) {
		case functionDeclaration:
			SymbolTable sTable = new SymbolTable(table, tree.getChildren().get(1).getToken().token);
			buildStatementTable(tree, sTable);
			return;
		case variableDeclaration:
			//read in variable to table
			return;
		default:
			if(tree.getChildren() != null) {
				for(Ptree t : tree.getChildren()) {
					buildDeclarationTable(t, table);
				}
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
		switch(tree.getToken().type) {
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
		case expressionStatement:
			//modify existing entry if applicable and check if it was declared for function calls and variable modifications
			return;
		default:
			if(tree.getChildren() != null) {
				for(Ptree t : tree.getChildren()) {
					buildDeclarationTable(t, table);
				}
			}
			
		}
	}
	
	/*public static List<Ptree> findTrees(Ptree tree, List<Ptree> trees, type_enum type) {
		if(tree.getToken().type == type) {
		//	System.out.println("Found");
			trees.add(tree);
		}
		if(tree.verifyChildren()) {
			List<Ptree> tt = tree.getChildren();
			for(Ptree t: tt) {
				//	System.out.println("Test");
				if(t.verifyChildren()) {
					trees = findTrees(t, trees, type);
				}
			}
		}
		return trees;
	}*/
}
