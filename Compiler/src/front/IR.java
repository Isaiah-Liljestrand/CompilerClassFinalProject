package front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IR {
	public static List<IRelement> instructions = null;
	
	private static void initialize() {
		if (instructions == null) {
			instructions = new ArrayList<IRelement>();
		}
	}
	
	public static void addCommand(IRelement.command cmd, String parameters) {
		initialize();
		instructions.add(new IRelement(cmd, parameters));
	}
	
	public static void addCommand(IRelement.command cmd, List<String> parameters) {
		initialize();
		instructions.add(new IRelement(cmd, parameters));
	}
	
	public static void createIRFromString(String file) {
		initialize();
		String[] lines = file.split(" ");
		for (String line : lines) {
			IRelement newelem = new IRelement(line);
			if (newelem != null) {
				instructions.add(newelem);
			}
		}
	}
	
	public static void createIRFromStringList(List<String> lines) {
		initialize();
		for (String line : lines) {
			IRelement newelem = new IRelement(line);
			if (newelem != null) {
				instructions.add(newelem);
			}
		}
	}
	
	public static void printIR() {
		for(String t : stringListFromIR()) {
			System.out.println(t);
		}
	}
	
	public static List<String> stringListFromIR() {
		List<String> output = new ArrayList<String>();
		for(IRelement elem : instructions) {
			output.add(elem.toString());
		}
		return output;
	}
}
