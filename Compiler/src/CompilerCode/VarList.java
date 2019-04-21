package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarList {
	public static List<VarElement> list = new ArrayList<VarElement>();
		
	public static void addElement(String name, int location) {
		list.add(new VarElement(name, location));
	}

	
	public static int varLocation(String Name) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				return var.getLocation();
			}
		}
		ErrorHandler.addError("varLocation called on name that was not in the list");
		return -1;
	}
	
	public static String locVariable(int Location) {
		for(VarElement var : list) {
			if(var.location.get(0) == Location) {
				return var.getName();
			}
		}
		return null;
	}
	
	public static void changeLocation(String Name, List<Integer> Locations) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				var.location = Locations;
			}
		}
	}
	
	public static void declaration(String name) {
		int location = RegStack.addToStack();
		for(VarElement e : list) {
			if(name == e.name) {
				e.location.add(0, location);
				return;
			}
		}
		addElement(name, location);
	}
	
	
	
	public static void destroy(String name) {
		for(VarElement e : list) {
			if(name == e.name) {
				RegStack.removeFromStack(e.getLocation());
				if(e.location.size() > 1) {
					e.location.remove(0);
					return;
				} else {
					list.remove(e);
					return;
				}
			}
		}
	}
}
