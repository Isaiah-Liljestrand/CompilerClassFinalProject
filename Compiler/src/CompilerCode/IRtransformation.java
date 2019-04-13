package CompilerCode;


/*
 * IRtransformation applies minor changes to the IR in order to prepare it for x86 transformations
 */
public class IRtransformation {
	public static void IRtransformationFunction() {
		breakHandler();
		gotoHandler();
		destroyOptimizer();
		declarationOptimizer();
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
	
	/**
	 * Moves all destroy functions forward in the code to free up register and stack space earlier
	 */
	public static void destroyOptimizer() {
		int offset = 0;
		for(int j = 0; j < IR.instructions.size(); j++) {
			IRelement e = IR.instructions.get(j);
			if(e.cmd == IRelement.command.destroy) {
				String n = e.parameters.get(0);
				IR.instructions.remove(e);
				for(int i = j - 1; i > -1; i--) {
					if(IR.instructions.get(i).cmd == IRelement.command.destroy && IR.instructions.get(i).parameters.contains(n)) {
						offset++;
					} else if(IR.instructions.get(i).cmd == IRelement.command.declare && IR.instructions.get(i).parameters.contains(n)) {
						offset--;
					}
					if(offset == 0 && IR.instructions.get(i).parameters.contains(n)) {
						IR.instructions.add(i + 1, e);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Moves all declaration calls in an IR forward in order to 
	 */
	public static void declarationOptimizer() {
		int offset = 0;
		for(int j = IR.instructions.size() - 1; j >= 0; j--) {
			IRelement e = IR.instructions.get(j);
			if(e.cmd == IRelement.command.declare) {
				String n = e.parameters.get(0);
				IR.instructions.remove(e);
				for(int i = j; i < IR.instructions.size(); i++) {
					if(IR.instructions.get(i).cmd == IRelement.command.destroy && IR.instructions.get(i).parameters.contains(n)) {
						offset--;
					} else if(IR.instructions.get(i).cmd == IRelement.command.declare && IR.instructions.get(i).parameters.contains(n)) {
						offset++;
					}
					if(offset == 0 && IR.instructions.get(i).parameters.contains(n)) {
						IR.instructions.add(i, e);
						break;
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
