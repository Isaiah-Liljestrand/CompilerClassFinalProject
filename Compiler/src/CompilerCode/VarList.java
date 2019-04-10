package CompilerCode;

import java.util.ArrayList;
import java.util.List;

public class VarList {
	public static List<VarElement> list = new ArrayList<VarElement>();
		
	public void addElement(String name, String location) {
		list.add(new VarElement(name, location));
	}
}
