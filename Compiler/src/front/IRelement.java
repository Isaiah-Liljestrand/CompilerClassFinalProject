package front;

import java.util.ArrayList;
import java.util.List;

public class IRelement {
	public command cmd;
	public List<String> parameters;
	
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
		gotolabel
	}
	
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
		return null;
	}
	
	public String toString() {
		if (this.cmd == null) {
			return "";
		}
		String output = this.cmd.toString();
		for(String param : parameters) {
			output += " " + param;
		}
		return output;
	}
	
	public IRelement(command cmd, List<String> parameters) {
		this.cmd = cmd;
		this.parameters = parameters;
	}
	
	public IRelement(String cmd, List<String> parameters) {
		this.cmd = commandFromString(cmd);
		this.parameters = parameters;
	}
	
	public IRelement(String input/*, String delimiter*/) {   //this seemed unnecessary
		String[] split = input.split(/*delimiter*/" ");
		if (split.length > 0) {
			this.cmd = commandFromString(split[0]);
			this.parameters = new ArrayList<String>();
			for(int i = 1; i < split.length; i++) {
				this.parameters.add(split[i]);
			}
		}
	}
}
