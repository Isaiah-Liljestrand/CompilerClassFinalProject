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
	
	public static void addCommand(IRelement elem) {
		initialize();
		instructions.add(elem);
	}
	
	public static void addCommand(IRelement.command cmd, List<String> parameters) {
		initialize();
		instructions.add(new IRelement(cmd, parameters));
	}
	
	public static void addCommand(IRelement.command cmd, String[] parameters) {
			initialize();
			instructions.add(new IRelement(cmd, new ArrayList<String>(Arrays.asList(parameters))));
	}
	
	public static void addCommand(String cmd, List<String> parameters) {
		initialize();
		instructions.add(new IRelement(cmd, parameters));
	}
	
	public static void createIRFromString(String file) {
		String[] lines = file.split(" ");
		for (String line : lines) {
			IRelement newelem = new IRelement(line, " ");
			if (newelem != null) {
				addCommand(newelem);
			}
		}
	}
	
	public static void createIRFromStringList(List<String> lines) {
		for (String line : lines) {
			IRelement newelem = new IRelement(line, " ");
			if (newelem != null) {
				addCommand(newelem);
			}
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
