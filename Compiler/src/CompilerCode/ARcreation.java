package CompilerCode;


public class ARcreation {
	public void createAR(IR ir) {
		boolean skip = false;
		String params[]; 
		int line = 0;
		for(IRelement element : IR.instructions) {
			if(skip) {
				skip = false;
				continue;
			}
			switch(element.cmd) {
			case declare:
				
				if(RegStack.isSwiss()) {
					VarList.declaration(element.parameters.get(0));
				} else {
					//If there is a declare followed by the variable being set, use a push, otherwise push 0;
					VarList.declaration(element.parameters.get(0));
					if(IR.instructions.get(line + 1).cmd == IRelement.command.set && IR.instructions.get(line + 1).parameters.get(0) == element.parameters.get(0)) {
						AR.addCommand(ARelement.command.push, ARParamFromIRparam(IR.instructions.get(line + 1).parameters.get(1)));
						skip = true;
					} else {
						//push zero onto the stack to move esp and instantiate uninitialized variable
						AR.addCommand(ARelement.command.push, "0");
					}
				}
				break;
			case destroy:
				VarList.destroy(element.parameters.get(0));
				break;
			case set:
				if(isHighIntVar(element.parameters.get(0))) {
					AR.addCommand(ARelement.command.push, ARParamFromIRparam(element.parameters.get(1)));
				}
				
				//case where no high int vars are used
				AR.addCommand(ARelement.command.mov, ARParamsFromIRelem(element));
				
				break;
			case add:
				params = setUpParams(element);
				AR.addCommand(ARelement.command.add, params);
				pushResults(element);
				break;
			case bor:
				params = setUpParams(element);
				AR.addCommand(ARelement.command.or, params);
				pushResults(element);
				break;
			case bxor:
				params = setUpParams(element);
				AR.addCommand(ARelement.command.xor, params);
				pushResults(element);
				break;
			case band:
				params = setUpParams(element);
				AR.addCommand(ARelement.command.and, params);
				pushResults(element);
				break;
			case sub:
				params = setUpParams(element);
				AR.addCommand(ARelement.command.sub, params);
				pushResults(element);
				break;
			case mul:
				params = setUpParams(element);
				AR.addCommand(ARelement.command.imul, params);
				break;
			//assumes the thing being divided is first argument
			case div: //Ben
				params = setUpParams(element);
				AR.addCommand(ARelement.command.mov, "%rax, %r15d");
				AR.addCommand(ARelement.command.idiv, "%r14d");
				pushResults(element);
				break;
			case mod: //Chris
				params = setUpParams(element);
				//Code
				pushResults(element);
				break;
			case eq: //Ben
				params = setUpParams(element);
				//Code
				pushResults(element);
				break;
			case neq: //Chris
				params = setUpParams(element);
				//Code
				pushResults(element);
				break;
			case or: //Ben
				params = setUpParams(element);
				//Code
				pushResults(element);
				break;
			case and: //Chris
				params = setUpParams(element);
				//Code
				pushResults(element);
				break;
			case not:
				AR.addCommand(ARelement.command.not, ARParamFromIRparam(element.parameters.get(0)));
				break;
			case jmp: //Jacob
				AR.addCommand(ARelement.command.jmp, element.parameters.get(0));
				break;
			case jmpcnd: //Jacob
				AR.addCommand(ARelement.command.cmp, new String[] {"$0", RegStack.intVarToReg("%1")});
				AR.addCommand(ARelement.command.jne, new String[] {element.parameters.get(0)});
				break;
			case function:
				AR.addCommand(ARelement.command.label, new String[] {"fun_" + element.parameters.get(0)});
				//Account for passed in parameters parameters
				break;
			case call: //Isaiah
				break;
			case ret:
				if(element.parameters.size() == 0 || element.parameters.get(0) == "%1") {
					AR.addCommand(ARelement.command.ret);
				} else {
					AR.addCommand(ARelement.command.mov, new String [] {"$" + element.parameters.get(0), "%rax"});
					AR.addCommand(ARelement.command.ret);
				}
				break;
			case label:
				AR.addCommand(ARelement.command.label, new String[] {element.parameters.get(0)});
				break;
			case endfunction:
				//Does nothing.
				break;
			default:
				//break_, goto_, gotoLabel would all end up here. These commands should be gone because of IRtransformation.
				ErrorHandler.addError("Unaccounted for IRelement.command in IR line " + line);
			}
			line++;
		}
	}
	
	/**
	 * Checks if the string passed in is a constant
	 * @param str should be a constant
	 * @return true if it is a constant
	 */
	private static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException ex) {
			return false;
		}
	}
	
	/**
	 * Converts a single parameter from an IR parameter to an AR parameter
	 * @param param String of the IR parameter
	 * @return String of an ARparam
	 */
	private static String ARParamFromIRparam(String param) {
		if(isInt(param)) {
			return "$" + param;
		} else if(param.charAt(0) == '%') {
			return RegStack.intVarToReg(param);
		} else {
			return RegStack.varToStack(param);
		}
	}
	
	/**
	 * Reverses the order of parameters and converts them to x86
	 * @param element IRelement being converted into assembly
	 * @return list of strings that are paramers for the x86 instruction
	 */
	private static String[] ARParamsFromIRelem(IRelement element) {
		String[] newparams = new String[element.parameters.size()];
		int i = newparams.length - 1;
		for(String param : element.parameters) {
			if (isInt(param)) {
				newparams[i] = "$" + param;
			}
			else if(param.charAt(0) == '%') {
				newparams[i] = RegStack.intVarToReg(param);
			} else {
				newparams[i] = RegStack.varToStack(param);
			}
			i--;
		}
		return newparams;
	}
	
	/**
	 * Tests if a string is a high intermediate variable
	 * @param var string being tested
	 * @return true if it is
	 */
	private static boolean isHighIntVar(String var) {
		if(var.charAt(0) == '%' && Integer.parseInt(var.substring(1)) > 12) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sets up the parameters to use for the operation functions
	 * @param ir IRelement being converted
	 * @return string of parameters to be handled
	 */
	private static String[] setUpParams(IRelement ir) {
		String params[];
		if(isHighIntVar(ir.parameters.get(0))) {
			//Both parameters are on the stack
			AR.addCommand(ARelement.command.pop, "%e15d");
			AR.addCommand(ARelement.command.pop, "%e14d");
			params = new String [] {"%e15d", "%e14d"};
		} else if(isHighIntVar(ir.parameters.get(1))) {
			//Only the second parameter is on the stack
			AR.addCommand(ARelement.command.pop, "%e14d");
			params = new String [] {"%e14d", "%e13d"};
		} else {
			params = ARParamsFromIRelem(ir);
		}
		return params;
	}
	
	/**
	 * If two high temporary variables were used, it pushes the result back on the stack
	 * @param ir Irelement being dealt with
	 */
	private static void pushResults(IRelement ir) {
		if(isHighIntVar(ir.parameters.get(0))) {
			AR.addCommand(ARelement.command.push, "%r14d");
		}
	}

	
	/*private static void operation(IRelement ir) {
		String params[];
		if(isHighIntVar(ir.parameters.get(0))) {
			//Both parameters are on the stack
			AR.addCommand(ARelement.command.pop, "%e15d");
			AR.addCommand(ARelement.command.pop, "%e14d");
			params = new String [] {"%e15d", "%e14d"};
		} else if(isHighIntVar(ir.parameters.get(1))) {
			//Only the second parameter is on the stack
			AR.addCommand(ARelement.command.pop, "%e14d");
			params = new String [] {"%e14d", "%e13d"};
		} else {
			params = ARParamsFromIRelem(ir);
		}
		
		
		switch(ir.cmd) {
		case add:
			AR.addCommand(ARelement.command.add, params);
			break;
		case sub:
			AR.addCommand(ARelement.command.sub, params);
			break;
		case bor:
			AR.addCommand(ARelement.command.or, params);
			break;
		case bxor:
			AR.addCommand(ARelement.command.xor, params);
			break;
		case band:
			AR.addCommand(ARelement.command.and, params);
			break;
		case mul: //Chris
			if(isHighIntVar(element.parameters.get(0))) {
				AR.addCommand(ARelement.command.pop, "%r15d");
				AR.addCommand(ARelement.command.pop, "%r14d");
				AR.addCommand(ARelement.command.imul, new String [] {"%r14d", "%r13d"});
				AR.addCommand(ARelement.command.push, "%r14d");
			} else if(isHighIntVar(element.parameters.get(1))) {
				AR.addCommand(ARelement.command.pop, "%r14d");
				AR.addCommand(ARelement.command.imul, new String [] {"%r14d", "%r13d"});
			} else {
				AR.addCommand(ARelement.command.imul, ARParamsFromIRelem(element));
			}
			break;
		case div: //Ben
			if(isHighIntVar(element.parameters.get(0))) { //parameter on the stack
				AR.addCommand(ARelement.command.mov, new String [] {"%rdx", "0"}); //clearing dividend
				AR.addCommand(ARelement.command.pop, "%rax"); //number to be divided
				AR.addCommand(ARelement.command.pop, "%r15d");
				AR.addCommand(ARelement.command.idiv, "%r15d");
				AR.addCommand(ARelement.command.push, "%rax");
			} else if(isHighIntVar(element.parameters.get(1))) {
				AR.addCommand(ARelement.command.mov, new String [] {"%rdx", "0"});
				AR.addCommand(ARelement.command.pop, "%r15d");
				AR.addCommand(ARelement.command.idiv, "%r15d");
				AR.addCommand(ARelement.command.push, "%rax");
			}
				else { 
				AR.addCommand(ARelement.command.mov, new String [] {"%rdx", "0"});
				AR.addCommand(ARelement.command.idiv, "%r15d");
				AR.addCommand(ARelement.command.push, "%rax");
			}
			break;
		case mod: //Chris
			if(isHighIntVar(element.parameters.get(0))) {
				AR.addCommand(ARelement.command.mov, new String [] {"%rdx", "0"});
				AR.addCommand(ARelement.command.pop, "%rax");
				AR.addCommand(ARelement.command.pop, "%rbx");
				AR.addCommand(ARelement.command.idiv, "%rbx");
				AR.addCommand(ARelement.command.push, "%rdx");
			} else if(isHighIntVar(element.parameters.get(0))) {
				AR.addCommand(ARelement.command.mov, new String [] {"%rdx", "0"});
				AR.addCommand(ARelement.command.pop, "%rax");
				AR.addCommand(ARelement.command.mov, new String [] {"%rbx", "%r13d"});
				AR.addCommand(ARelement.command.idiv, "%rbx");
				AR.addCommand(ARelement.command.push, "%rdx");
			} else {
				AR.addCommand(ARelement.command.mov, new String [] {"%rdx", "0"});
				AR.addCommand((ARelement.command.mov, new String [] {"%rax", "%r13d"});
				AR.addCommand(ARelement.command.mov, new String [] {"%rbx", "%r12d"});
				AR.addCommand(ARelement.command.idiv, "%rbx");
			}
			break;
		case eq: //Ben
			break;
		case neq: //Chris
			break;
		case or: //Chris
			if(isHighIntVar(element.parameters.get(0))) {
				AR.addCommand(ARelement.command.pop, "%r15d");
				AR.addCommand(ARelement.command.pop, "%r14d");
				AR.addCommand(ARelement.command.or, new String [] {"%r15d", "%r14d"});
				AR.addCommand(ARelement.command.push, "%r15d");
			} else if(isHighIntVar(element.parameters.get(1))) {
				AR.addCommand(ARelement.command.pop, "%r14d");
				AR.addCommand(ARelement.command.or, new String [] {"%r14d", "%r13d"});
			} else {
				AR.addCommand(ARelement.command.or, ARParamsFromIRelem(element));
			}
			break;
		case and: //Ben
			break;
		default:
			ErrorHandler.addError("Default switch called in operation function");
		}
		

	}*/
}
