package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarList {
	public static List<VarElement> list = new ArrayList<VarElement>();
		
	public void addElement(String name, int location) {
		list.add(new VarElement(name, location));
	}

	
	public List<Integer> varLocation(String Name) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				return var.getLocation();
			}
		}
		return null;
	}
	
	public String locVariable(int Location) {
		for(VarElement var : list) {
			for(int loc : var.getLocation()) {
				if(loc == Location) {
					return var.getName();
				}
			}
		}
		return null;
	}
	
	public void changeLocation(String Name, List<Integer> Locations) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				var.location = Locations;
			}
		}
	}
	
	public void declaration(String name) {
		for(VarElement e : list) {
			if(name == e.name) {
				//add to beginning of list
				return;
			}
		}
		//add whole element
	}
	
	
	
	public void destroy(String name) {
		for(VarElement e : list) {
			if(name == e.name) {
				if(e.location.size() > 1) {
					e.location.remove(0);
				} else {
					list.remove(e);
				}
			}
		}
	}
}
