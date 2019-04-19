package CompilerCode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents individual lines of IR
 */
public class ARelement {
	protected command cmd;
	protected List<String> parameters;
	
	/**
	 * List of possible assembly commands
	 */
	enum command {
		mov,
		push,
		pop,
		lea,
		ret,
		sub,
		add,
		dec,
		inc,
		imul,
		idiv,
		and,
		or,
		xor,
		not,
		jmp,
		je,
		jne,
		//jg,  Shouldn't need these, no less than or greater than operands currently
		//jl,
		//jle,
		cmp,
		call,
		label
	}
	
	//private static String[] registers = RegStack.getRegisters();
	private static String[] registers = new String[] {"%eax", "%ebx", "%ecx", "%edx", "%esi", "%edi", "%esp", "%ebp", "%r8d", "%r9d", "%r10d", "%r11d", "%r12d", "%r13d", "%r14d", "%r15d"};
	private static String register_regex = String.join("|", registers);
	
	//Tests if a string is a label
	public static boolean isLabel(String str) {
		return str.charAt(str.length() - 1) == ':';
	}
	
	//Tests if a string is a constant
	public static boolean isConstant(String str) {
		Pattern REGEX = Pattern.compile("\\$\\d+");
		Matcher test = REGEX.matcher(str);
		return test.matches();
	}
	
	//Tests if a string is a register
	public static boolean isReg(String str) {
		String test_pat = register_regex; //Tests just a register by itself: %eax
		Pattern REGEX = Pattern.compile(test_pat);
		Matcher test = REGEX.matcher(str);
		return test.matches();
	}
	
	//Tests if a string is a memory access
	public static boolean isMem(String str) {
		String test_pat = "(\\d?\\((" + register_regex + ")\\))|"; //Tests register in parenthesis with an optional int in front: (%eax) or 5(%eax)
		test_pat += "(\\((" + register_regex + "), ?(" + register_regex + "), ?\\$\\d\\))"; //Tests three params in parenthesis: (%edx,%eax,5)
		Pattern REGEX = Pattern.compile(test_pat);
		Matcher test = REGEX.matcher(str);
		return test.matches();
	}
	
	//Checks if each parameter in an array of parameters matches a type specified in the string check.
	//c is constant, r is register, m is memory access, and l is label.
	//For example "crm" will check if there are three parameters where the first is a constant, second a register, and third a memory access.
	private static boolean checkParams(String check, String[] parameters) {
		if (check.length() != parameters.length) {
			return false;
		}
		int i = 0;
		for(String param : parameters) {
			if (check.charAt(i) == 'c' && !isConstant(param)) {
				return false;
			}
			if (check.charAt(i) == 'r' && !isReg(param)) {
				return false;
			}
			if (check.charAt(i) == 'm' && !isMem(param)) {
				return false;
			}
			if (check.charAt(i) == 'l' && !isLabel(param)) {
				return false;
			}
			i++;
		}
		return true;
	}
	
	//Checks if an array of parameters matches multiple options for possible types.
	//c is constant, r is register, m is memory access, and l is label.
	//Each possible test is separated by a |.
	//For example "rr|cr" will return true if the second parameter is a register and the first is either a register or a constant.
	private static boolean checkMulParams(String check, String[] parameters) {
		String[] checks = check.split("\\|");
		for(String c : checks) {
			if (checkParams(c, parameters)) {
				return true;
			}
		}
		return false;
	}
		
	//Tests if an assembly command is valid
	public ARelement(command cmd, String[] parameters) {
		//Trim white space off of the parameters
		String[] trimmed = new String[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			trimmed[i] = parameters[i].trim();
		}
		
		this.cmd = cmd;
		this.parameters = Arrays.asList(trimmed);
		
		//Check validity. Return errors if a assembly command is invalid.
		boolean error = false;
		switch(cmd) {
		//Isaiah
		case mov:
			//For checkMulParams the first parameter represents the possible types of parameters.
			//r is a register
			//m is a memory access
			//c is a constant
			//l is a label
			//Bars | separate each possibility.
			if (!checkMulParams("rr|rm|mr|cr|cm", trimmed)) {
				error = true;
			}
			break;
		//Jacob
		case push:
			if (!checkMulParams("r|m|c", trimmed)) {
				error = true;
			}
			break;
		//Isaiah
		case pop:
			if (!checkMulParams("r|m", trimmed)) {
				error = true;
			}
			break;
		//Chris
		case lea:
			if (!checkMulParams("mr", trimmed)) {
				error = true;
			}
			break;
		//Isaiah
		case ret:
			if (!checkMulParams("", trimmed)) {
				error = true;
			}
			break;
		//Chris
		case sub:
			if (!checkMulParams("rr|mr|rm|cr|cm", trimmed)) {
				error = true;
			}
			break;
		//Isaiah
		case add:
			if (!checkMulParams("rr|mr|rm|cr|cm", trimmed)) {
				error = true;
			}
			break;
		//Jacob
		case dec:
			if (!checkMulParams("r|m", trimmed)) {
				error = true;
			}
			break;
		//Ben
		case inc:
			if (!checkMulParams("r|m", trimmed)) {
				error = true;
			}
			break;
		//Jacob
		case imul:
			if (!checkMulParams("rr|mr|crr|cmr", trimmed)) {
				error = true;
			}
			break;
		//Chris
		case idiv:
			if (!checkMulParams("r|m", trimmed)) {
				error = true;
			}
			break;
		//Chris
		case and:
			if (!checkMulParams("rr|mr|rm|cr|cm", trimmed)) {
				error = true;
			}
			break;
		//Ben
		case or:
			if (!checkMulParams("rr|mr|rm|cr|cm", trimmed)) {
				error = true;
			}
			break;
		//Ben
		case xor:
			if (!checkMulParams("rr|mr|rm|cr|cm", trimmed)) {
				error = true;
			}
			break;
		//Chris
		case not:
			if (!checkMulParams("r|m", trimmed)) {
				error = true;
			}
			break;
		//Isaiah
		case jmp:
			if (!checkMulParams("l", trimmed)) {
				error = true;
			}
			break;
		//Jacob
		case je:
			if (!checkMulParams("l", trimmed)) {
				error = true;
			}
			break;
		//Isaiah
		case jne:
			if (!checkMulParams("l", trimmed)) {
				error = true;
			}
			break;
		//Isaiah
		/*case jg:
			if (!checkMulParams("l", trimmed)) {
				error = true;
			}
			break;
		//Chris
		case jl:
			if (!checkMulParams("l", trimmed)) {
				error = true;
			}
			break;
		//Jacob
		case jle:
			if (!checkMulParams("l", trimmed)) {
				error = true;
			}
			break;*/
		//Jacob
		case cmp:
			if (!checkMulParams("rr|mr|rm|cr", trimmed)) {
				error = true;
			}
			break;
		//Jacob
		case call:
			if (!checkMulParams("l", trimmed)) {
				error = true;
			}
			break;
		//Chris		
		case label:
			break;
		default:
			ErrorHandler.addError("ARelement method in ARelement. Default in switch case shouldn't have been reached. Assembly command must not have been accounted for.");
			break;
		}
		if (error) {
			ErrorHandler.addError("Parameters are invalid in assembly command: " + this.toString());
		}
	}
	
	//Returns a string representing this ARelement.
	//Returns in the format: "cmd p1, p2, ...pn" where cmd is the command enum and the parameters are comma separated.
	@Override
	public String toString() {
		String newstring = cmd.toString();
		if (cmd == command.label) {
			newstring = "";
		}
		
		//Append comma separated parameters.
		newstring += " " + String.join(", ", parameters);
		if (cmd == command.label) {
			newstring += ":";
		}
		return newstring;
	}
}
