package CompilerCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all AR elements and deals with printing
 */
public class AR { 
	public static List<ARelement> instructions = new ArrayList<ARelement>();
	
	/**
	 * Adds an AR command
	 * @param cmd command being added
	 * @param parameters list of parameters for command
	 */
	public static void addCommand(ARelement.command cmd, String[] parameters) {
		instructions.add(new ARelement(cmd, parameters));
	}
	
	/**
	 * Adds an AR command (Only for single parameter commands)
	 * @param cmd command being added
	 * @param parameter single parameter being used for command
	 */
	public static void addCommand(ARelement.command cmd, String parameter) {
		instructions.add(new ARelement(cmd, new String [] {parameter}));
	}
	
	/**
	 * Adds an AR command (Only for no parameter commands)
	 * @param cmd command being added
	 */
	public static void addCommand(ARelement.command cmd) {
		instructions.add(new ARelement(cmd, new String [] {}));
	}
	
	/**
	 * Prints out the AR
	 */
	public static void printAR() {
		for(ARelement elem : instructions) {
			if (elem.cmd != ARelement.command.label) {
				System.out.print("\t");
			}
			System.out.println(elem.toString());
		}
	}
	
	public static String ARtoFile() {
		StringBuilder string = new StringBuilder();
		for(ARelement elem : instructions) {
			if (elem.cmd != ARelement.command.label) {
				string.append("\t");
			}
			string.append(elem.toString());
		}
		return string.toString();
	}
}
