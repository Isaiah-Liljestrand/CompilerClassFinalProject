package front;

import front.IRelement.command;
import front.Token.type_enum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 * Global variable declarations and function declarations. Everything passed on to further functions.
	 * Calls functionHandler, and variableDeclarationHandler
	 * 
	 */
	private static void declarationHandler(Ptree tree) {
		switch(tree.token.type) {
		case program:
			declarationHandler(tree.children.get(0));
			break;
		case declaration:
		case declarationList:
			for(Ptree t: tree.children) {
				declarationHandler(t);
			}
			break;
		case functionDeclaration:
			functionHandler(tree);
			break;
		case variableDeclaration:
			variableDeclarationHandler(tree);
			break;
		default:
			//TODO: error checking
		}
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
	
	private static String paramGetter(Ptree tree) {
		String tmp = new String("");
		if(tree.token.type == Token.type_enum.parameter) {
			tmp = tmp + tree.children.get(0).children.get(0).token.token + " ";
			tmp = tmp + tree.children.get(1).token.token;
		}
		else if(tree.token.type == Token.type_enum.comma) {
			tmp = tmp + ", ";
		}
		else {
			for(Ptree t: tree.children) {
				tmp = tmp + paramGetter(t);
			}	
		}
		return tmp;
	}
	
	//Deals with function declaration.
	//calls statementHandler
	private static void functionHandler(Ptree tree) {
		String tmp = new String(), tmp2 = new String();
		Ptree tree2 = tree.children.get(3); //either the params list or )
		
		tmp = tmp + tree.children.get(0).children.get(0).token.token;
		tmp = tmp + treverseDown(tree, findType(tree, Token.type_enum.variableTypeSpecifier)).children.get(0).token.token;
		tmp = tmp + tree.children.get(1).token.token;
		
		//params
		if(tree2.token.type != Token.type_enum.closedParenthesis) { //parems exist
			tmp2 += paramGetter(tree2);
			
			/**
			tmp2 = tmp2 + treverseDown(tree, findType(tree, Token.type_enum.variableTypeSpecifier)).children.get(0).token.token;
			tmp2 = tmp2 + treverseDown(tree, findType(tree, Token.type_enum.parameter)).children.get(1).token.token;*/
		}
		IR.addCommand(tmp, Arrays.asList(tmp2.split(",")));
		for(int i = 3; i < tree.children.size(); i++) {
			if(tree.children.get(i).token.type == Token.type_enum.openCurlyBracket) {
				if(tree.children.get(i+1).token.type != Token.type_enum.closedCurlyBracket) { //making sure not an empty function
					statementHandler(tree.children.get(i+1));
				}
			}
		}
	}
	
	/**
	 * Deals with all statements.
	 * Calls whileH, forH, ifH, varDecH, expressionHandler, and any others we need to add
	 * @param tree
	 * @return list of var names declared in it
	 */
	
	
	private static List<String> statementHandler(Ptree tree) {
		List<String> vars = new ArrayList<String>();
		
		switch(tree.token.type) {
		case statement:
		case statementList:
			for(Ptree t: tree.children) {
				vars.addAll(statementHandler(t));
			}
			break;
		case variableDeclaration:
		case variableDeclarationList:
			vars.addAll(variableDeclarationHandler(tree));
			break;
		case whileStatement:
			whileHandler(tree);
			break;
		case forStatement:
			forHandler(tree);
			break;
		case ifStatement:
			ifHandler(tree);
			break;
		case returnStatement:
			//returnHandler(tree, table); //unsure if we wana make that funcion or leave it for expression handler
		case simpleExpression:
			expressionHandler(tree);
			break;
		default:
			//expressionHandler(tree, table); //If we have other unhandled cases (like returnStatement) that I can't think of
			for(Ptree t: tree.children) { //If we have unhandled garbage
				declarationHandler(t);
			}
		}
		return vars;
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
		//Temporary variable %1 should be equal to the result of the simple expression
		simpleExpressionHandler(tree.children.get(2), 1);
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
	//Please use recursion as much as possible. 
	private static List<String> variableDeclarationHandler(Ptree tree) {
		//Do something like this

		//1. store variable type
		type_enum type = tree.children.get(0).children.get(0).token.type;
		
		//2. pass type to recursive function that goes to each instance of variableDeclarationInitialize
		return variableHelper(tree.children.get(1), type);
		
		//3. do stuff in subfunction
		
		
		
		
		
		
		
		/*System.out.print("declare "); //can currently handle 1 
		type_enum type = tree.children.get(0).children.get(0).token.type;
		System.out.print(treverseDown(tree, findType(tree, Token.type_enum.variableTypeSpecifier)).children.get(0).token.token);
		System.out.print(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.identifier)).token.token);
		//up to varID varName
		
		//if doing assignment at declaration
		if(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.size() > 1) { //inline assignment
			System.out.print(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.get(1).token.token); //1 is the =
			simpleExpressionHandler(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.get(2)); //pumps the simple expression assignment to the simple expression handler
			//simpleExpressionHandler(treverseDown(tree.children.get(1), findType(tree.children.get(1), Token.type_enum.variableDeclarationInitialize)).children.get(2), table); //pumps the simple expression assignment to the simple expression handler
		}
		
		//System.out.print(treverseDown(tree.children.get(1), 3).token.token); //outs the var name
		if(tree.children.get(2).token.type == Token.type_enum.semicolon) { //prints a new line with the ;
			System.out.println("");
		}*/
	}
	
	private static List<String> variableHelper(Ptree tree, type_enum type) {
		switch(tree.token.type) {
		case variableDeclarationList:
			List<String> list = new ArrayList<String>();
			for(Ptree t : tree.children) {
				list.addAll(variableHelper(t, type));
			}
			return list;
		case variableDeclarationInitialize:
			//add declaration
			//call simple expression if needed then set variable to %i
			//return List containing the variable being dealt with
		default:
			//TODO: error reporting, should be incapable of reaching
		}
		return null;
	}
	
	//Something like setting variables
	//Check if increment or decrement
	//Check if function call
	//Check if variable assignment +=, *=, /=, -=, or =
	//Calls simpleExpressionHandler
	private static void expressionHandler(Ptree tree) {
		
	}
	
	//Deals with math and other things involved in simple expressions
	private static String simpleExpressionHandler(Ptree tree, int i) {
		switch(tree.token.type) {
		case constant:
			return tree.children.get(0).token.token;
		case variable:
			IR.addCommand(new IRelement("set " + "%" + i + " " + tree.children.get(0).token.token));
			return null;
		case call:
			functionCallHandler(tree, i);
			return null;
		case factor:
			if(tree.children.get(0).token.type == type_enum.openParenthesis) {
				return simpleExpressionHandler(tree.children.get(1), i);
			} else {
				return simpleExpressionHandler(tree.children.get(0), i);
			}
		default:
			if(isExpression(tree)) {
				return implementExpression(tree, i);
			} else {
				return simpleExpressionHandler(tree.children.get(0), i);
			}
		}
	}
	
	private static boolean isExpression(Ptree tree) {
		switch(tree.token.type) {
		case simpleExpression:
		case andExpression:
		case bitOrExpression:
		case bitXorExpression:
		case bitAndExpression:
		case compareExpression:
		case sumExpression:
		case term:
		case notExpression:
			if(tree.children.size() > 1) {
				return true;
			}
		default:
			return false;
		}
	}
	
	private static String implementExpression(Ptree tree, int i) {
		String n, n2;
		IRelement.command c = command.set;
		if(tree.token.type != type_enum.notExpression) {
			n = simpleExpressionHandler(tree.children.get(0), i);
			n2 = simpleExpressionHandler(tree.children.get(2), i + 1);
		} else {
			n = simpleExpressionHandler(tree.children.get(1), i);
			if(n == null) {
				IR.addCommand("not %" + i);
				return null;
			} else {
				return preProcess(tree, n);
			}
		}
		switch(tree.token.type) {
		case simpleExpression:
			c = command.or;
			break;
		case andExpression:
			c = command.and;
			break;
		case bitOrExpression:
			c = command.bor;
			break;
		case bitXorExpression:
			c = command.bxor;
			break;
		case bitAndExpression:
			c = command.band;
			break;
		case compareExpression:
			if(tree.children.get(1).token.type == type_enum.equalOperator) {
				c = command.eq;
			} else {
				c = command.neq;
			}
			break;
		case sumExpression:
			if(tree.children.get(1).token.type == type_enum.additionOperator) {
				c = command.add;
			} else {
				c = command.sub;
			}
			break;
		case term:
			if(tree.children.get(1).token.type == type_enum.multiplicationOperator) {
				c = command.mul;
			} else if (tree.children.get(1).token.type == type_enum.divisionOperator){
				c = command.div;
			} else {
				c = command.mod;
			}
			break;
		default:
			//TODO: error reporting, should be unreachable
			return null;
		}
		if(n == null && n2 == null) {
			IR.addCommand(c.toString() + " %" + i + " %" + (i + 1));
		} else if(n == null && n2 != null) {
			IR.addCommand(c.toString() + " %" + i + " " + n2);
		} else if(n != null && n2 == null) {
			IR.addCommand("set %" + i + " " + n);
			IR.addCommand(c.toString() + " %" + i + " %" + (i + 1));
		} else {
			return preProcess(tree, n, n2);
		}
		return null;
	}
	
	//Handles not expressions
	private static String preProcess(Ptree tree, String v) {
		int n = ~findValue(v);
		return String.valueOf(n);		
	}
	
	//Handles every other type of expression
	private static String preProcess(Ptree tree, String v1, String v2) {
		int n = findValue(v1);
		int n2 = findValue(v2);
		switch(tree.token.type) {
		case simpleExpression:
			if(n != 0 || n2 != 0) {
				n = 1;
			}
			return String.valueOf(n);
		case andExpression:
			if(n != 0 && n2 != 0) {
				return "1";
			} else {
				return "0";
			}
		case bitOrExpression:
			return String.valueOf(n | n2);
		case bitAndExpression:
			return String.valueOf(n & n2);
		case bitXorExpression:
			return String.valueOf(n ^ n2);
		case compareExpression:
			if(tree.children.get(1).children.get(0).token.type == type_enum.equalOperator) {
				if(n == n2) {
					return "1";
				}
				return "0";
			} else {
				if(n != n2) {
					return "1";
				}
				return "0";
			}
		case sumExpression:
			if(tree.children.get(1).children.get(0).token.type == type_enum.additionOperator) {
				return String.valueOf(n + n2);
			} else {
				return String.valueOf(n - n2);
			}
		case term:
			type_enum opType = tree.children.get(1).children.get(0).token.type;
			if(opType == type_enum.multiplicationOperator) {
				return String.valueOf(n * n2);
			} else if(opType == type_enum.divisionOperator) {
				return String.valueOf(n / n2);
			} else if(opType == type_enum.modulusOperator) {
				return String.valueOf(n % n2);
			}
		default:
			//TODO: error handling
		}
		return null;
	}
	
	private static int findValue(String v) {
		if(v.charAt(0) == '\'') {
			return (int)v.charAt(1);
	 	} else {
	 		return Integer.parseInt(v);
	 	}
	}
	
	//Adds setting temp variables before function call.
	private static void functionCallHandler(Ptree tree, int i) {
		
	}
}
