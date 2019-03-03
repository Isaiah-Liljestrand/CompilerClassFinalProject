package front;

import java.util.ArrayList;

public class IRHead {
	private ArrayList<IRent> opps;
	
	/**
	 * default constructor
	 */
	IRHead(){
		opps = new ArrayList<IRent>();
	}
	
	/**
	 * constructor that interpret a ptree
	 * @param in
	 */
	IRHead(AST in){
		opps = new ArrayList<IRent>();
	}

	/**
	 * converts AST to IR
	 * going to be a big AF function
	 * @param in
	 */
	protected void interperet(AST in){
		for(int i = 0;i < in.children.size(); i++){
			interperet(in.children.get(i));
		}
		opps.add(IRent(in.token));
	}
}
