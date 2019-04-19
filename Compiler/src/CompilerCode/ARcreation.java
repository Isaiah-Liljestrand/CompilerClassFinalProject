package CompilerCode;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ARcreation {
	public void createAR(IR ir) {
		int line = 0;
		for(IRelement element : ir.instructions) {
			switch(element.cmd) {
			case declare: //Jacob
				VarList.declaration(element.parameters.get(0));
				break;
			case destroy: //Jacob
				VarList.destroy(element.parameters.get(0));
				break;
			case set: //Ben
				break;
			case add: //Chris
				break;
			case sub: //Ben
				break;
			case mul: //Chris
				break;
			case div: //Ben
				break;
			case mod: //Chris
				break;
			case eq: //Ben
				break;
			case neq: //Chris
				break;
			case not: //Ben
				break;
			case bor:
				AR.addCommand(ARelement.command.or, ARParamsFromIRelem(element));
				break;
			case bxor:
				AR.addCommand(ARelement.command.xor, ARParamsFromIRelem(element));
				break;
			case band:
				AR.addCommand(ARelement.command.and, ARParamsFromIRelem(element));
				break;
			case or: //Chris
				break;
			case and: //Ben
				break;
			case jmp: //Jacob
				AR.addCommand(ARelement.command.jmp, new String[] {element.parameters.get(0)});
				break;
			case jmpcnd: //Jacob
				//%1 to register
				//This will only work assuming a register is returned! If a memory access is returned it won't work.
				//We should talk about if this could ever happen.
				String tempvar = RegStack.intToReg(1);
				//Just compares tempvar %1 with constant 1. If equal it then jumps.
				AR.addCommand(ARelement.command.cmp, new String[] {"$1", tempvar});
				AR.addCommand(ARelement.command.je, new String[] {element.parameters.get(0)});
				break;
			case function:
				AR.addCommand(ARelement.command.label, new String[] {"fun_" + element.parameters.get(0)});
				//Account for passed in parameters parameters
				break;
			case call: //Isaiah
				break;
			case ret: //Isaiah
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
	
	private static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException ex) {
			return false;
		}
	}
	
	//Reverses the order of parameters and replaces IR parameters with AR equivalent
	private static String[] ARParamsFromIRelem(IRelement element) {
		String[] newparams = new String[element.parameters.size()];
		int i = newparams.length - 1;
		for(String param : element.parameters) {
			if (isInt(param)) {
				newparams[i] = "$" + param;
			}
			else if(param.charAt(0) == '%') {
				newparams[i] = RegStack.intToReg(Integer.parseInt((param.substring(1, param.length()))));
			} else {
				newparams[i] = RegStack.intToReg(VarList.varLocation(param));
			}
			i--;
		}
		return newparams;
	}
}
