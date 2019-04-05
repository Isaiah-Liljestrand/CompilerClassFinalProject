package CompilerCode;



public class IRtransformation {
	public static void IRtransformationFunction() {
		breakHandler();
		logicalOr();
		logicalAnd();
		equal();
		notEqual();
		gotoHandler();
	}
	
	
	
	public static void breakHandler() {
		int i, j;
		for(i = 0; i < IR.instructions.size(); i++) {
			if(IR.instructions.get(i).cmd == IRelement.command.break_) {
				for(j = i + 1; j < IR.instructions.size(); j++) {
					if(IR.instructions.get(j).cmd == IRelement.command.label) {
						String n = IR.instructions.get(j).parameters.get(0);
						if(n.length() > 6 && n.substring(0, 6).equals("forend")) {
							IR.instructions.get(i).cmd = IRelement.command.jmp;
							IR.instructions.get(i).parameters.add(n);
							break;
						}
						if(n.length() > 6 && n.substring(0, 8).equals("whileend")) {
							IR.instructions.get(i).cmd = IRelement.command.jmp;
							IR.instructions.get(i).parameters.add(n);
							break;
						}
					}
				}
			}
		}
	}
	
	public static void logicalOr() {
		
	}
	
	public static void logicalAnd() {
		
	}
	
	public static void equal() {
		
	}
	
	public static void notEqual() {
		
	}
	
	public static void gotoHandler() {
		for(IRelement e : IR.instructions) {
			if(e.cmd == IRelement.command.goto_) {
				e.parameters.set(0,"goto" + e.parameters.get(0));
				e.cmd = IRelement.command.jmp;
			}
			if(e.cmd == IRelement.command.gotolabel) {
				e.parameters.set(0,"goto" + e.parameters.get(0));
				e.cmd = IRelement.command.label;
			}
		}
	}
}
