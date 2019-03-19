package front;

import front.Token.type_enum;

public class IRcreation {
	public void createIR(Ptree tree, SymbolTable table) {
		//These functions will not be called in this order. Only calling declarationHandler
		declarationHandler(tree, table);
		functionHandler(tree, table);
		whileHandler(tree, table);
		forHandler(tree, table);
		ifHandler(tree, table);
		expressionHandler(tree, table);
	}
	
	
	/**
	 * because I don't wana type "System.out.println()" a bunch of times for error handling & java doesn't allow goto
	 * @param handler where the error occured
	 */
	private void errorIn(String handler) {
		System.out.println("~~ ERROR in " + handler +  " ~~");
	}
	
	/**
	 * gets the Ptree i children(0)s down 
	 * @param tree the starting tree
	 * @param i how far to traverse
	 * @return the Ptree i children(0)s down
	 */
	private Ptree treverseDown(Ptree tree, int i) {
		for(int j = 0; j < i; j++) {
			tree = tree.children.get(0);
		}
		return tree;
	}
	
	
	/**
	 * returns the int of how far down the first children line it takes to find a child of type
	 * @param tree
	 * @param type
	 * @return
	 */
	private int findType(Ptree tree, Token.type_enum type) {
		int i = 0;
		while(tree.token.type != type) {
			i++;
			tree = tree.children.get(0);
		}
		return i;
	}
	
	/**
	 * returns the int of how far down the first child to have 2 children is, or has no children
	 * @param tree the starting tree
	 * @param i how far to traverse
	 * @return the Ptree i children(0)s down
	 */
	private int Find2Kids(Ptree tree) {
		int i = 0;
		while(tree.children.size() != 1){
			i++;
			tree = tree.children.get(0);
		}
		return i;
	}
	
	/**
	 * Global variable declarations and function declarations. Everything passed on to further functions.
	 * Calls functionHandler, and variableDeclarationHandler
	 * 
	 */
	private void declarationHandler(Ptree tree, SymbolTable table) {
		
		
		switch(tree.token.type) {
		case program:
			if(tree.children.size() > 0) {
				declarationHandler(tree.children.get(0), table);
			}
			else {
				errorIn("Declaration Handler");
			}
			break;
		case declarationList:
			if(tree.children.size() >= 1) {
				for(int i = 0; i < tree.children.size(); i++) {
					declarationHandler(tree.children.get(i), table);
				}
			}
			else {
				errorIn("Declaration Handler");
			}
			break;
		case declaration:
			declarationHandler(tree.children.get(0), table); //SHOULD never have >1 child
			break;
		case functionDeclaration:
			functionHandler(tree, table);
			break;
		case variableDeclaration:
			variableDeclarationHandler(tree, table);
			break;
		default:
			errorIn("Declaration Handler");
		}
	}
	
	//Deals with function declaration.
	//calls statementHandler
	private void functionHandler(Ptree tree, SymbolTable table) {
		
	}
	
	//Deals with all statements.
	//Calls whileH, forH, ifH, varDecH, expressionHandler, and any others we need to add.
	private void statementHandler(Ptree tree, SymbolTable table) {
		
	}
	
	//Calls simpleExprHandler for while check
	//Calls statementHandler for body
	//Add jmps as needed.
	private void whileHandler(Ptree tree, SymbolTable table) {
		
	}
	
	//For first part it can be nothing, varDecHandler, or expressionHandler
	//Middle part is simpleExprHandler
	//Third part is expressionHandler
	//Calls statementHandler for body
	//Add jmps as needed.
	private void forHandler(Ptree tree, SymbolTable table) {
		
	}
	
	//Calls simpleExpressionHandler for the if check
	//Calls statementHandler for body
	//Also check for an else and call statementHandler again.
	//jmps added as needed.
	private void ifHandler(Ptree tree, SymbolTable table) {
		
	}
	
	//Calls simpleExpressionHandler
	private void variableDeclarationHandler(Ptree tree, SymbolTable table) {
		System.out.print("declare "); //can currently handle 1 
		System.out.print(treverseDown(tree, findType(tree, Token.type_enum.variableTypeSpecifier)).children.get(0).token.token);
		System.out.print(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.identifier)).token.token);
		//up to varID varName
		
		//if doing assignment at declaration
		if(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.size() > 1) { //inline assignment
			System.out.print(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.get(1).token.token); //1 is the =
			simpleExpressionHandler(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.get(2), table); //pumps the simple expression assignment to the simple expression handler
		}
		
		//System.out.print(treverseDown(tree.children.get(1), 3).token.token); //outs the var name
		if(tree.children.get(2).token.type == Token.type_enum.semicolon) { //prints a new line with the ;
			System.out.println("");
		}
	}
	
	//Something like setting variables
	//Check if increment or decrement
	//Check if function call
	//Check if variable assignment +=, *=, /=, -=, or =
	//Calls simpleExpressionHandler
	private void expressionHandler(Ptree tree, SymbolTable table) {
		
	}
	
	//Deals with math and other things involved in simple expressions
	private void simpleExpressionHandler(Ptree tree, SymbolTable table) {
		
	}
	
	//Adds setting temp variables before function call.
	private void functionCallHandler(Ptree tree, SymbolTable table) {
		
	}
}
