package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarElement {
	public String name;
	public List<Integer> location = new ArrayList<Integer>();
	
	public VarElement(String name, int location) {
		this.name = name;
		this.location.add(location);
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<Integer> getLocation() {
		return this.location;
	}
}
