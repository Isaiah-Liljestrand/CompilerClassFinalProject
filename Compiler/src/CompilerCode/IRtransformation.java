package CompilerCode;


/*
 * IRtransformation applies minor changes to the IR in order to prepare it for x86 transformations
 */
public class IRtransformation {
	public static void IRtransformationFunction() {
		breakHandler();
		logicalOr();
		logicalAnd();
		equal();
		notEqual();
		gotoHandler();
	}
	
	
	/**
	 * Removes all break statements and replaces them with jumps to the end of the current loop function
	 */
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
		for(IRelement e : IR.instructions) {
			if(e.cmd == IRelement.command.or) {
				
			}
		}
	}
	
	public static void logicalAnd() {
		for(IRelement e : IR.instructions) {
			if(e.cmd == IRelement.command.and) {
				
			}
		}
	}
	
	public static void equal() {
		for(IRelement e : IR.instructions) {
			if(e.cmd == IRelement.command.eq) {
				
			}
		}
	}
	
	public static void notEqual() {
		for(IRelement e : IR.instructions) {
			if(e.cmd == IRelement.command.neq) {
				
			}
		}
	}
	
	/**
	 * Moves all destroy functions forward in the code to free up register and stack space earlier
	 */
	public static void destroyOptimizer() {
		for(int j = 0; j < IR.instructions.size(); j++) {
			IRelement e = IR.instructions.get(j);
			if(e.cmd == IRelement.command.destroy) {
				String n = e.parameters.get(0);
				int i = IR.instructions.indexOf(e);
				IR.instructions.remove(e);
				for(; i > -1; i--) {
					if(IR.instructions.get(i).parameters.contains(n)) {
						IR.instructions.add(i + 1, e);
					}
				}
			}
		}
	}
	
	/**
	 * Removes all goto calls and statements and replaces them with jumps and labels
	 */
	public static void gotoHandler() {
		for(IRelement e : IR.instructions) {
			if(e.cmd == IRelement.command.goto_) {
				e.parameters.set(0,"goto_" + e.parameters.get(0));
				e.cmd = IRelement.command.jmp;
			}
			if(e.cmd == IRelement.command.gotolabel) {
				e.parameters.set(0,"goto_" + e.parameters.get(0));
				e.cmd = IRelement.command.label;
			}
		}
	}
}
