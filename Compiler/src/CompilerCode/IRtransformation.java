package CompilerCode;



/*
 * IRtransformation applies minor changes to the IR in order to prepare it for x86 transformations
 */
public class IRtransformation {
	public static void IRtransformationFunction(boolean global) {
		breakHandler();
		if(gotoHandler() && !global && !highintvars()) {
			declarationDestructionOptimizer();
		}
	}
	
	public static boolean highintvars() {
		for(IRelement e : IR.instructions) {
			for(String s : e.parameters) {
				if(s.charAt(0) == '%' && Integer.parseInt(s.substring(1)) > 13) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isHighIntVar(String v) {
		if(v.charAt(0) == '%' && Integer.parseInt(v.substring(1)) > 13) {
			return true;
		}
		return false;
	}
	
	
	
	
	/**
	 * Removes all break statements and replaces them with jumps to the end of the current loop function
	 */
	private static void breakHandler() {
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
	 * Moves all declaration calls in an IR forward in order to 
	 */
	private static void declarationDestructionOptimizer() {
		for(int i = 0; i < IR.instructions.size(); i++) {
			IRelement dec = IR.instructions.get(i);
			if(dec.cmd == IRelement.command.declare) {
				
				int d = destroyFinder(i);
				moveDeclaration(i);
				moveDestroy(d);
			}
		}
	}
	
	/**
	 * Given the index of a declaration call, finds the index of the corresponding destroy for said variable
	 * @param index of the declaration of the variable being dealt with
	 * @return index of the destoy call
	 */
	private static int destroyFinder(int index) {
		int offset = 0;
		String var = IR.instructions.get(index).parameters.get(0);
		for(int i = index + 1; i < IR.instructions.size(); i++) {
			
			//Declare commad with matching name
			if(IR.instructions.get(i).cmd == IRelement.command.declare &&
			   IR.instructions.get(i).parameters.get(0).equals(var)) {
				offset++;
				
			//Destroy command with correct name
			} else if(IR.instructions.get(i).cmd == IRelement.command.destroy) {
				if(IR.instructions.get(i).parameters.get(0).equals(var)) {
					if(offset > 0) {
						offset--;
					} else {
						return i;
					}
				}
			}
		}
		System.out.println("destroy finder function called on line " + index + " failed");
		//ErrorHandler.addError("destroy finder function called on line " + index + " failed");
		return -1;
	}
	
	/**
	 * Moves a given declaration to the first place a variable is used
	 * @param dec index of the declaration being dealt with
	 */
	private static void moveDeclaration(int dec) {
		int first = findFirstLastUsage(dec, true), offset = 0, valid = dec + 1;
		for(int i = dec + 1; i <= first; i++) {
			if(IR.instructions.get(i).isChunkBeginning()) {
				if(offset == 0) {
					valid = i;
				}
				offset++;
				continue;
			} else if(IR.instructions.get(i).isChunkEnd()) {
				offset--;
				continue;
			} else if(offset == 0) {
				valid = i;
			}
		}
		valid--;
		IRelement e = IR.instructions.get(dec);
		IR.instructions.remove(dec);
		IR.instructions.add(valid, e);
	}
	
	/**
	 * Moves a given destruction to the last place the variable is used
	 * @param destr index of the destroy being dealt with
	 */
	private static void moveDestroy(int destr) {
		int last = findFirstLastUsage(destr, false), offset = 0, valid = destr;
		for(int i = destr - 1; i > last; i--) {
			if(IR.instructions.get(i).isChunkBeginning()) {
				offset--;
				if(offset == 0) {
					valid = i;
				}
				continue;
			} else if(IR.instructions.get(i).isChunkEnd()) {
				if(offset == 0) {
					valid = i + 1;
				}
				offset++;
				continue;
			} else if(offset == 0) {
				valid = i;
			}
		}
		IRelement e = IR.instructions.get(destr);
		IR.instructions.remove(destr);
		IR.instructions.add(valid, e);
	}
	
	/**
	 * Finds the first or last usage of a variable
	 * @param index of the declaration or destroy of the variable being used
	 * @param first true if the first usage of a variable is being looked for, false otherwise
	 * @return index of the IRelement that uses the specified variable
	 */
	private static int findFirstLastUsage(int index, boolean first) {
		String v = IR.instructions.get(index).parameters.get(0);
		int offset = 0;
		if(first) {
			for(int i = index + 1; i < IR.instructions.size(); i++) {
				if(IR.instructions.get(i).cmd == IRelement.command.declare) {
					if(IR.instructions.get(i).parameters.get(0).equals(v)) {
						offset++;
					}
				} else if(IR.instructions.get(i).cmd == IRelement.command.destroy) {
					if(IR.instructions.get(i).parameters.get(0).equals(v)) {
						offset--;
					}
				} else if(IR.instructions.get(i).parameters.contains(v) && offset == 0 && 
						  IR.instructions.get(i).cmd != IRelement.command.label) {
					return i;
				}
			}
		} else {
			for(int i = index - 1; i > 0; i--) {
				if(IR.instructions.get(i).cmd == IRelement.command.declare) {
					if(IR.instructions.get(i).parameters.get(0).equals(v)) {
						offset++;
					}
				} else if(IR.instructions.get(i).cmd == IRelement.command.destroy) {
					if(IR.instructions.get(i).parameters.get(0).equals(v)) {
						offset--;
					}
				} else if(IR.instructions.get(i).parameters.contains(v) && offset == 0 && 
						  IR.instructions.get(i).cmd != IRelement.command.label) {
					return i;
				}
			}
		}
		ErrorHandler.addError("findFirstLastUsage failed to return a value for variable " + v);
		return -1;
	}
	
	/**
	 * Removes all goto calls and statements and replaces them with jumps and labels
	 */
	private static boolean gotoHandler() {
		boolean b = true;
		for(IRelement e : IR.instructions) {
			if(e.cmd == IRelement.command.goto_) {
				e.parameters.set(0,"goto_" + e.parameters.get(0));
				e.cmd = IRelement.command.jmp;
				b = false;
			}
			if(e.cmd == IRelement.command.gotolabel) {
				e.parameters.set(0,"goto_" + e.parameters.get(0));
				e.cmd = IRelement.command.label;
			}
		}
		return b;
	}
}
