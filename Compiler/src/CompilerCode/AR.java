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
	 * Prints out the AR
	 */
	public static void printAR() {
		for(ARelement elem : instructions) {
			System.out.println(elem.toString());
		}
	}
}
