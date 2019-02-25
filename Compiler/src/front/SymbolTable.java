package front;

import java.util.ArrayList;
import java.util.List;

import front.Token.type_enum;

public class SymbolTable {
	public List<SymbolTableEntry> entries;
	public SymbolTable parent;
	
	public void AddEntry(String name, String value)	{
		SymbolTableEntry newEntry = new SymbolTableEntry(name, value);
		entries.add(newEntry);
	}
	
	public void AddFunctionEntry(String name, String value)	{
		SymbolTable childTable = new SymbolTable();
		childTable.parent = this;
		SymbolTableEntry newEntry = new SymbolTableEntry(name, value, childTable);
		entries.add(newEntry);
	}
	
	public List<SymbolTable> GetChildTables() {
		List<SymbolTable> childTables = new ArrayList<SymbolTable>();
		for(SymbolTableEntry entry : entries) {
			if (entry.childTable != null) {
				childTables.add(entry.childTable);
			}
		}
		return childTables;
	}
	
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
	
	public void BuildSymTable(Ptree root) {
		List<Ptree> tree = new ArrayList<Ptree>();
		tree = findTrees(root, tree, type_enum.functionDeclaration); // First find the function Declarations
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
		}
		
	}
	
	public static List<Ptree> findTrees(Ptree tree, List<Ptree> trees, type_enum type) {
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
	}
}
