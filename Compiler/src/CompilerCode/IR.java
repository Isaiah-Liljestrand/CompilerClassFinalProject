package CompilerCode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all IR elements and deals with printing and reading in
 */
public class IR {
	public static List<IRelement> instructions = new ArrayList<IRelement>();

	/**
	 * Adds an IR command
	 * @param cmd command being added
	 * @param parameters strings separated by spaces used with the command
	 */
	public static void addCommand(IRelement.command cmd, String parameters) {
		List<String> list = new ArrayList<String>();
		for(String n : parameters.split(" ")) {
			list.add(n);
		}
		instructions.add(new IRelement(cmd, list));
	}
	
	/**
	 * Adds an IR command
	 * @param cmd command being added
	 * @param parameters list of parameters for command
	 */
	public static void addCommand(IRelement.command cmd, List<String> parameters) {
		instructions.add(new IRelement(cmd, parameters));
	}
	
	public static void addCommand(IRelement.command cmd) {
		instructions.add(new IRelement(cmd, null));
	}
	
	/**
	 * Prints out the IR
	 */
	public static void printIR() {
		for(IRelement elem : instructions) {
			if(elem.cmd == IRelement.command.label) {
				System.out.println(elem.toString());
			} else {
				System.out.println("\t" + elem.toString());
			}
		}
	}

	public static String IRtoFile() {
		StringBuilder string = new StringBuilder();
		for(IRelement elem : instructions) {
			if (elem.cmd == IRelement.command.label) {
				string.append(elem.toString());
			} else {
				string.append("\t" + elem.toString());
			}
		}
		return string.toString();
	}
	
	/**
	 * Creates IR from a list of strings
	 * @param lines list of strings representing an IR
	 */
	public static void createIRFromStringList(List<String> lines) {
		for (String line : lines) {
			IRelement newelem = new IRelement(line);
			if (newelem != null) {
				instructions.add(newelem);
			}
		}
	}
	
	/**
	 * Reads in IR from a file
	 * @param file
	 */
	public static void readIRFromFile(String file) {
		List<String> lines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       lines.add(line);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createIRFromStringList(lines);
	}
}
