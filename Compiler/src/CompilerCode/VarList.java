package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarList {
	public static List<VarElement> list = new ArrayList<VarElement>();
		
	public void addElement(String name, String location) {
		list.add(new VarElement(name, location));
	}
	
	public void removeElement(String Name) {
		int i = 0;
		for(VarElement var : list) {
			if(var.getName() == Name) {
				list.remove(i);
				break;
			}
			i++;
		}
	}
	
	public List<String> varLocation(String Name) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				return var.getLocation();
			}
		}
		return null;
	}
	
	public String locVariable(String Location) {
		for(VarElement var : list) {
			for(String loc : var.getLocation()) {
				if(loc == Location) {
					return var.getName();
				}
			}
		}
		return null;
	}
	
	public void changeLocation(String Name, List<String> Locations) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				var.location = Locations;
			}
		}
	}
}
