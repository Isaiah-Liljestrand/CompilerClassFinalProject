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
				break;
			case destroy: //Jacob
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
				break;
			case jmpcnd: //Jacob
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
	
	private static String[] ARParamsFromIRelem(IRelement element) {
		String[] newparams = new String[element.parameters.size()];
		int i = 0;
		for(String param : element.parameters) {
			if (isInt(param)) {
				newparams[i] = "$" + param;
			}
			else if(param.charAt(0) == '%') {
				newparams[i] = RegStack.intToReg(Integer.parseInt((param.substring(1, param.length()))));
			} else {
				newparams[i] = RegStack.intToReg(VarList.varLocation(param));
			}
			i++;
		}
		return newparams;
	}
}
