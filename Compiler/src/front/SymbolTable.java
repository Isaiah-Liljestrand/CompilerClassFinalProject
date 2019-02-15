package front;

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
}
