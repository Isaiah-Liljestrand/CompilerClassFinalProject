package CompilerCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents individual lines of IR
 */
public class IRelement {
	public command cmd;
	public List<String> parameters;
	
	/**
	 * List of possible IR commands
	 */
	enum command {
		declare,
		destroy,
		set,
		add,
		sub,
		mul,
		div,
		mod,
		eq,
		neq,
		not,
		bor,
		bxor,
		band,
		or,
		and,
		xor,
		jmp,
		jmpcnd,
		function,
		call,
		ret,
		label,
		goto_,
		gotolabel,
		endfunction,
		break_
	}
	
	/**
	 * Find the command from a string including the command
	 * @param str string to retrieve command with
	 * @return command
	 */
	public static command commandFromString(String str) {
		if (str.equals("declare")) return command.declare; 
		if (str.equals("destroy")) return command.destroy;
		if (str.equals("set")) return command.set;
		if (str.equals("add")) return command.add;
		if (str.equals("sub")) return command.sub;
		if (str.equals("mul")) return command.mul;
		if (str.equals("div")) return command.div;
		if (str.equals("mod")) return command.mod;
		if (str.equals("eq")) return command.eq;
		if (str.equals("neq")) return command.neq;
		if (str.equals("not")) return command.not;
		if (str.equals("bor")) return command.bor;
		if (str.equals("bxor")) return command.bxor;
		if (str.equals("band")) return command.band;
		if (str.equals("or")) return command.or;
		if (str.equals("and")) return command.and;
		if (str.equals("xor")) return command.xor;
		if (str.equals("jmp")) return command.jmp;
		if (str.equals("jmpcnd")) return command.jmpcnd;
		if (str.equals("function")) return command.function;
		if (str.equals("call")) return command.call;
		if (str.equals("ret")) return command.ret;
		if (str.equals("label")) return command.label;
		if (str.equals("goto_")) return command.goto_;
		if (str.equals("gotolabel")) return command.gotolabel;
		if (str.equals("endfunction")) return command.endfunction;
		if (str.equals("break_")) return command.break_;
		ErrorHandler.addError("Command '" + str + "' not found while creating IRelement.\n");
		return null;
	}
	
	/**
	 * Outputs a string of the IRelement
	 */
	public String toString() {
		if (this.cmd == null) {
			return "";
		}
		String output = cmd.toString();
		for(String param : parameters) {
			output += " " + param;
		}
		return output;
	}
	
	/**
	 * Creates an IR from a command and list of strings
	 * @param cmd command to be added
	 * @param parameters list of parameters pertaining to the command
	 */
	public IRelement(command cmd, List<String> parameters) {
		this.cmd = cmd;
		this.parameters = parameters;
	}
	
	/**
	 * Creates an IRelement from a string that should represent one line of IR
	 * @param input should be one line of IR with spaces seperating arguments
	 */
	public IRelement(String input) {
		String[] split = input.split(" ");
		if (split.length > 0) {
			this.cmd = commandFromString(split[0]);
			this.parameters = new ArrayList<String>();
			for(int i = 1; i < split.length; i++) {
				this.parameters.add(split[i]);
			}
		}
	}
}
