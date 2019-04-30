package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarList {
	public static List<VarElement> list = new ArrayList<VarElement>();
		
	/**
	 * Adds an element to the current list of variables
	 * @param name the name of the variable
	 * @param location the location of the variable on the stack
	 */
	public static void addElement(String name, int location) {
		list.add(new VarElement(name, location));
	}

	/**
	 * Returns the location index of the variable
	 * @param Name name of the variable
	 * @return index of where the variable is on the stack
	 */
	public static int varLocation(String Name) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				return var.getLocation();
			}
		}
		ErrorHandler.addError("varLocation called on name that was not in the list");
		return -1;
	}
	
	/**
	 * Returns the name of the variable at the specified location
	 * @param Location index of the variable 
	 * @return Sting containing the name of the variable
	 */
	public static String locVariable(int Location) {
		for(VarElement var : list) {
			if(var.location.get(0) == Location) {
				return var.getName();
			}
		}
		return null;
	}
	
	/**
	 * Moves the location of a variable elsewhere
	 * @param Name name of the variable to move
	 * @param Locations location of the variable to move to
	 */
	public static void changeLocation(String Name, List<Integer> Locations) {
		for(VarElement var : list) {
			if(var.getName() == Name) {
				var.location = Locations;
			}
		}
	}
	
	public static void paramdeclaration(String name) {
		int location = RegStack.addToParamStack();
		for(VarElement e : list) {
			if(name == e.name) {
				e.location.add(0, location);
				return;
			}
		}
		addElement(name, location);
	}
	
	/**
	 * Declares a variable and adds it to the stack
	 * @param name name of the variable being declared
	 */
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
	
	/**
	 * Destroys a variable from the variable list and removes it from the stack
	 * @param name of the variable to be destroyed
	 */
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
