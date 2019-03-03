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

}
