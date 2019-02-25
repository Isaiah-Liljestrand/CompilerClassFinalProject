package front;

public class SymbolTableEntry {
	public String name;
	public Ptree value;
	
	public SymbolTableEntry(String name, Ptree value) {
		this.name = name;
		this.value = value;
	}
}
