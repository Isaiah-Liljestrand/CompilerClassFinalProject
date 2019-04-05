package CompilerCode;

import CompilerCode.Token.type_enum;

public class SymbolTableEntry {
	protected String name;
	//protected Ptree value;
	protected type_enum value;
	/**
	 * generic constructor
	 * We don't have generic constructors for anything else we can either remove this, use it, or add generic constructors for everything else
	 */
	public SymbolTableEntry() {
		this.name = null;
		this.value = null;
	}
	
	/**
	 * constructor takes arguments
	 * @param name variable identifier
	 * @param value tree of assignment to value
	 */
	public SymbolTableEntry(String name, type_enum value) {
		this.name = name;
		this.value = value;
	}
}
