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
		switch(str) {
		case "declare": return command.declare;
		case "destroy": return command.destroy;
		case "set": return command.set;
		case "add": return command.add;
		case "sub": return command.sub;
		case "mul": return command.mul;
		case "div": return command.div;
		case "mod": return command.mod;
		case "eq": return command.eq;
		case "neq": return command.neq;
		case "not": return command.not;
		case "bor": return command.bor;
		case "bxor": return command.bxor;
		case "band": return command.band;
		case "or": return command.or;
		case "and": return command.and;
		case "xor": return command.xor;
		case "jmp": return command.jmp;
		case "jmpcnd": return command.jmpcnd;
		case "function": return command.function;
		case "call": return command.call;
		case "ret": return command.ret;
		case "label": return command.label;
		case "goto_": return command.goto_;
		case "gotolabel": return command.gotolabel;
		case "endfunction": return command.endfunction;
		case "break_": return command.break_;
		default: ErrorHandler.addError("Command '" + str + "' not found while creating IRelement.\n");
		}
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
	
	/**
	 * Tests if the IRelement is an ending to a subchunk
	 * @return true if it is a whileend, forend, or ifend label, false otherwise
	 */
	public boolean isChunkEnd() {
		if(cmd == command.label) {
			String n = parameters.get(0);
			if(n.length() > 8 && n.substring(0, 8).equals("whileend")) {
				return true;
			} else if(n.length() > 6 && n.substring(0, 6).equals("forend")) {
				return true;
			} else if(n.length() > 5 && n.substring(0, 5).equals("ifend")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tests if the IRelement is a beggining to a subchunk
	 * @return true if it is a whilestart, forstart, or ifstart, false otherwise
	 */
	public boolean isChunkBeginning() {
		if(cmd == command.label) {
			String n = parameters.get(0);
			if(n.length() > 10 && n.substring(0, 10).equals("whilestart")) {
				return true;
			} else if(n.length() > 8 && n.substring(0, 8).equals("forstart")) {
				return true;
			} else if(n.length() > 7 && n.substring(0, 7).equals("ifstart")) {
				return true;
			}
		}
		return false;
	}
}
