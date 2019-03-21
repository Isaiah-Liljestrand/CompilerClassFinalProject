package front;

import java.util.List;

import front.Token.type_enum;

public class IRcreation {
	private static int whilecount;
	
	public static void createIR(Ptree tree) {
		whilecount = 0;
		//These functions will not be called in this order. Only calling declarationHandler
		declarationHandler(tree);
		functionHandler(tree);
		whileHandler(tree);
		forHandler(tree);
		ifHandler(tree);
		expressionHandler(tree);
	}
	
	
	/**
	 * !!!Won't this be entirely handled by ErrorHandler???!!!
	 * 
	 * because I don't wana type "System.out.println()" a bunch of times for error handling & java doesn't allow goto
	 * @param handler where the error occured
	 */
	private static void errorIn(String handler) {
		System.out.println("~~ ERROR in " + handler +  " ~~");
	}
	
	/**
	 * gets the Ptree i children(0)s down 
	 * @param tree the starting tree
	 * @param i how far to traverse
	 * @return the Ptree i children(0)s down
	 */
	private static Ptree treverseDown(Ptree tree, int i) {
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
	private static int findType(Ptree tree, Token.type_enum type) {
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
	private static int find2Kids(Ptree tree) {
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
	private static void declarationHandler(Ptree tree) {
		
		
		switch(tree.token.type) {
		case program:
			if(tree.children.size() > 0) {
				declarationHandler(tree.children.get(0));
			}
			else {
				errorIn("Declaration Handler");
			}
			break;
		case declarationList:
			if(tree.children.size() >= 1) {
				for(int i = 0; i < tree.children.size(); i++) {
					declarationHandler(tree.children.get(i));
				}
			}
			else {
				errorIn("Declaration Handler");
			}
			break;
		case declaration:
			declarationHandler(tree.children.get(0)); //SHOULD never have >1 child
			break;
		case functionDeclaration:
			functionHandler(tree);
			break;
		case variableDeclaration:
			variableDeclarationHandler(tree);
			break;
		default:
			errorIn("Declaration Handler");
		}
	}
	
	//Deals with function declaration.
	//calls statementHandler
	private static void functionHandler(Ptree tree) {
		
	}
	
	//Deals with all statements.
	//Calls whileH, forH, ifH, varDecH, expressionHandler, and any others we need to add.
	private static List<String> statementHandler(Ptree tree) {
		
	}
	
	private static void destroyVars(List<String> varnames) {
		for(String name : varnames) {
			IR.addCommand(IRelement.command.destroy, new String[]{name});
		}
	}
	
	//Calls simpleExprHandler for while check
	//Calls statementHandler for body
	//Add jmps as needed.
	private static void whileHandler(Ptree tree) {
		if (tree.children.size() != 7) {
			//Add error check
			return;
		}
		String wc = Integer.toString(whilecount);
		whilecount++;
		//Add the initial while label which is unconditionally jumped to at the end of each while loop.
		IR.addCommand(IRelement.command.label, new String[]{"whilestart" + wc});
		//Temp variable %1 should be equal to the result of the simple expression
		simpleExpressionHandler(tree.children.get(2));
		//Test variable %1 and jump to whileend label if the test fails
		IR.addCommand(IRelement.command.jmpcnd, new String[]{"whileend" + wc});
		//Handle the statements inside the while loop.
		//The returned list of strings includes the names of all variables created in the statement list.
		List<String> statement_return = statementHandler(tree.children.get(5));
		//Unconditionally jump back to the start of the while loop
		IR.addCommand(IRelement.command.jmp, new String[]{"whilestart" + wc});
		//Add a label for the end of the while loop.
		IR.addCommand(IRelement.command.label, new String[]{"whileend" + wc});
		//Destroy the variables created inside the while loop.
		destroyVars(statement_return);
	}
	
	//For first part it can be nothing, varDecHandler, or expressionHandler
	//Middle part is simpleExprHandler
	//Third part is expressionHandler
	//Calls statementHandler for body
	//Add jmps as needed.
	private static void forHandler(Ptree tree) {
		
	}
	
	//Calls simpleExpressionHandler for the if check
	//Calls statementHandler for body
	//Also check for an else and call statementHandler again.
	//jmps added as needed.
	private static void ifHandler(Ptree tree) {
		
	}
	
	//Calls simpleExpressionHandler
	private static void variableDeclarationHandler(Ptree tree) {
		System.out.print("declare "); //can currently handle 1 
		System.out.print(treverseDown(tree, findType(tree, Token.type_enum.variableTypeSpecifier)).children.get(0).token.token);
		System.out.print(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.identifier)).token.token);
		//up to varID varName
		
		//if doing assignment at declaration
		if(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.size() > 1) { //inline assignment
			System.out.print(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.get(1).token.token); //1 is the =
			simpleExpressionHandler(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.get(2)); //pumps the simple expression assignment to the simple expression handler
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
	private static void expressionHandler(Ptree tree) {
		
	}
	
	//Deals with math and other things involved in simple expressions
	private static void simpleExpressionHandler(Ptree tree) {
		
	}
	
	//Adds setting temp variables before function call.
	private static void functionCallHandler(Ptree tree) {
		
	}
}
