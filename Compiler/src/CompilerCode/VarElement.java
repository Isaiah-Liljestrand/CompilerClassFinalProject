package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarElement {
	public String name;
	public List<Integer> location = new ArrayList<Integer>();
	
	/**
	 * Constructs a new variable to go on the stack
	 * @param name name of the variable
	 * @param location of the variable on the stack
	 */
	public VarElement(String name, int location) {
		this.name = name;
		this.location.add(location);
	}
	
	/**
	 * Name of the variable at the specified location
	 * @return the name of the variable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Location of the variable in it's current scope
	 * @return index of location on the stack
	 */
	public int getLocation() {
		return location.get(0);
	}
}
