package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarElement {
	public String name;
	public List<String> location = new ArrayList<String>();
	
	public VarElement(String name, String location) {
		this.name = name;
		this.location.add(location);
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLocation() {
		return this.location;
	}
}
