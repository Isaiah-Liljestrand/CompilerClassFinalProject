package front;

public class SymbolTableEntry {
	public String name;
	public Ptree value;
	
	/**
	 * generic constructor
	 */
	public SymbolTableEntry(){
		
	}
	
	/**
	 * constructor takes arguments
	 * @param name
	 * @param value
	 */
	public SymbolTableEntry(String name, Ptree value) {
		this.name = name;
		this.value = value;
	}
	
	
}
