package front;

import java.util.ArrayList;
import java.util.List;

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
}
