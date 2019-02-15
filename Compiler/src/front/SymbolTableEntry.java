package front;

public class SymbolTableEntry {
	public String name;
	public String value;
	public SymbolTable childTable;
	
	public SymbolTableEntry(String name, String value, SymbolTable childTable) {
		this.name = name;
		this.value = value;
		this.childTable = childTable;
	}
	
	public SymbolTableEntry(String name, String value) {
		this.name = name;
		this.value = value;
		this.childTable = null;
	}
}
