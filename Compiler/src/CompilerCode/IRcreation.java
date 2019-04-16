package CompilerCode;

import CompilerCode.IRelement.command;
import CompilerCode.Token.type_enum;
import java.util.ArrayList;
import java.util.List;

public class IRcreation {
	private static int whilecount;
	private static int forcount;
	private static int ifcount;
	
	/**
	 * Begins the process to create the IR
	 * @param tree top 'program' node
	 */
	public static void createIR(Ptree tree) {
		whilecount = 0;
		forcount = 0;
		ifcount = 0;
		declarationHandler(tree);
	}
	
	/**
	 * Handles all global variable declarations and function declarations.
	 * @param node in the tree being handled
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
			ErrorHandler.addError("default Case reached in declarationHandler");
		}
	}
	

	//Use findTree function from Ptree instead of traverseDown or findType
	
	/**
	 * Creates a list of parameters to be declared in a function declaration
	 * @param tree any tree that comes from parameterList
	 * @return list of all parameters found
	 */
	private static List<String> paramGetter(Ptree tree) {
		List<String> list = new ArrayList<String>();
		if(tree.token.type == Token.type_enum.parameter) {
			list.add(tree.children.get(0).children.get(0).token.token);
			list.add(tree.children.get(1).token.token);
		} else {
			for(Ptree t: tree.children) {
				list.addAll(paramGetter(t));
			}
		}
		return list;
	}
	
	
	/**
	 * Called to handle each function declaration
	 * @param tree function declaration node
	 */
	private static void functionHandler(Ptree tree) {
		//Gets this ParseTree bit - functionTypeSpecifier functionDeclarationID ( parameterList ) { statementList } parameter list is optional
		//Create IR with command "function" and a list of parameters that are type followed by ID.
		//Create the parameters string array from parameterList
		//Something like this -> IR.addCommand(IRelement.command.function, new String[] {"int", "x", "int", "y"});
		//Take the name of the function and it's list of parameters and make an IR.
		//Check each top level statement in statementList. If none of them is a return statement, add one at the end.
		
		//List to be passed into addCommand
		List<String> list = new ArrayList<String>();
		int index = 5;
		Ptree tree2 = tree.children.get(3);
		
		//Adding the function type
		list.add(tree.children.get(0).children.get(0).token.token);
		
		//Adding the name of the function
		list.add(tree.children.get(1).token.token);
		
		//Adding parameters if they exist
		if(tree2.token.type == type_enum.parameterList) {
			list.addAll(paramGetter(tree2));
			index++;
		}
		
		//tree of the statementList
		tree2 = tree.children.get(index);
		
		//Creates necessary IR
		IR.addCommand(command.function, list);
		
		//Calls the statementHandler to handle internals and then destroy all declared variables
		destroyVars(statementHandler(tree2));
		
		//Adds a return statement if it is necessary
		if(!returnStatementExists(tree2)) {
			if(tree.children.get(0).children.get(0).token.type == type_enum.k_void) {
				IR.addCommand(IRelement.command.ret);
			} else {
				IR.addCommand(IRelement.command.ret, "0");
			}
		}
		IR.addCommand(IRelement.command.endfunction);
	}
	
	/**
	 * Checks that there is necessarily a return statement to be hit in the function
	 * @param tree node being dealt with
	 * @return true if there is a return statement to be hit, false if not
	 */
	private static boolean returnStatementExists(Ptree tree) {
		switch(tree.token.type) {
		case statementList:
		case statement:
			for(Ptree t: tree.children) {
				if(returnStatementExists(t)) {
					return true;
				}
			}
			return false;
		case returnStatement:
			return true;
		case ifStatement:
			if(tree.children.size() < 10) {
				return false;
			}
			//Case where there is a return statement is in both sections of an if else statement
			if(returnStatementExists(tree.children.get(5)) && returnStatementExists(tree.children.get(9))) {
				return true;
			}
		default:
			return false;
		}
	}
	
	
	/**
	 * Deals with all statements.
	 * Calls whileHandler, forHandler, ifHandler, varDecHandler, expressionHandler, and any others we need to add
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
			if(tree.children.size() == 2) {
				IR.addCommand(IRelement.command.ret);
			} else {
				String n = simpleExpressionHandler(tree.children.get(1), 1);
				if(n == null) {
					IR.addCommand(IRelement.command.ret, "%1");
				} else {
					IR.addCommand(IRelement.command.ret, n);
				}
			}
			break;
		case gotoJumpPlace:
			IR.addCommand(IRelement.command.gotolabel, tree.children.get(0).token.token);
			break;
		case gotoStatement:
			IR.addCommand(IRelement.command.goto_, tree.children.get(1).token.token);
			break;
		case breakStatement:
			IR.addCommand(IRelement.command.break_);
			break;
		case expressionStatement:
			expressionHandler(tree);
			break;
		default:
			ErrorHandler.addError("Under statementhandler default was called");
			//expressionHandler(tree, table); //If we have other unhandled cases (like returnStatement) that I can't think of
			for(Ptree t: tree.children) { //If we have garbage
				declarationHandler(t);
			}
		}
		return vars;
	}
	
	/**
	 * Add destroy statements for all variables declared in a block
	 * @param varnames list of variable names to be destroyed
	 */
	private static void destroyVars(List<String> varnames) {
		if (varnames.size() > 0) {
			for(String name : varnames) {
				IR.addCommand(IRelement.command.destroy, name);
			}
		}
	}
	
	/**
	 * Deals with a while statement by calling subfunctions and dealing with structure
	 * @param tree should be a whileStatement node of a parse tree
	 */
	private static void whileHandler(Ptree tree) {
		//General IR structure:
		//Start label
		//Conditional step
		//Conditional jump to end label
		//statementList body
		//Jump unconditionally to start label
		//Destroy variables
		//End label
		
		if (tree.children.size() != 7) {
			ErrorHandler.addError("tree children size incorrect in whilehandler");
			return;
		}
		String expression, wc = Integer.toString(whilecount);
		whilecount++;
		
		//Add the initial while label which is unconditionally jumped to at the end of each while loop.
		IR.addCommand(IRelement.command.label, "whilestart" + wc);
		
		//Temporary variable %1 should be equal to the result of the simple expression
		if((expression = simpleExpressionHandler(tree.children.get(2), 1)) != null) {
			IR.addCommand(IRelement.command.set, "%1 " + expression);
		}
		IR.addCommand(IRelement.command.not, "%1");
		
		//Test variable %1 and jump to whileend label if the test fails
		IR.addCommand(IRelement.command.jmpcnd, "whileend" + wc);
		
		//Handle the statements inside the while loop.
		//The returned list of strings includes the names of all variables created in the statement list.
		destroyVars(statementHandler(tree.children.get(5)));
		
		//Unconditionally jump back to the start of the while loop
		IR.addCommand(IRelement.command.jmp, "whilestart" + wc);
		
		//Add a label for the end of the while loop.
		IR.addCommand(IRelement.command.label, "whileend" + wc);
	}
	

	/**
	 * forHandler takes in a parse tree string with a forStatement
	 * @param tree forStatement parse tree node
	 */
	private static void forHandler(Ptree tree) {
		//General IR structure:
		//Initialization step
		//Start label
		//Conditional step
		//Conditional jump to end label
		//statementList body
		//Destroy variables
		//Increment step
		//Jump unconditionally to start label
		//End label
		//Destroy initialization step stuff
		
		//Handle the initialization part of the for loop.
		//Has to account for 3 options: an expression, variable declaration, or nothing.
		int next = 3; //This value adjusts which children are looked at depending on if the initialization step is empty.
		List<String> forvars = new ArrayList<String>();
		if (tree.children.get(2).token.type == Token.type_enum.expressionStatement) {
			expressionHandler(tree.children.get(2));
		} else if (tree.children.get(2).token.type == Token.type_enum.variableDeclaration) {
			forvars.addAll(variableDeclarationHandler(tree.children.get(2)));
		}
		
		//Add a label for the start of the for loop.
		String expression, fc = Integer.toString(forcount);
		forcount++;
		IR.addCommand(IRelement.command.label, "forstart" + fc);
		
		//Handle the condition of the for loop
		if((expression = simpleExpressionHandler(tree.children.get(next), 1)) != null) {
			IR.addCommand(IRelement.command.set, "%1 " + expression);
		}
		IR.addCommand(IRelement.command.not, "%1");
		
		//Jump conditionally to the end if the condition fails
		IR.addCommand(IRelement.command.jmpcnd, "forend" + fc);
		
		//Handle the statementList inside the for loop body
		destroyVars(statementHandler(tree.children.get(next + 5)));
		
		//Handle the incrementation part of the for loop
		expressionHandler(tree.children.get(next + 2));
		
		//Jump unconditionally back to the start of the for loop
		IR.addCommand(IRelement.command.jmp, "forstart" + fc);
		
		//Add a label for the end of the for loop.
		IR.addCommand(IRelement.command.label, "forend" + fc);
		
		//Destroy the variables created in the for loop body
		destroyVars(forvars);
	}
	
	/**
	 * Handles if statement, can deal with one else statement at most
	 * @param tree ifStatement node in a parse tree
	 */
	private static void ifHandler(Ptree tree) {
		/*
		With else
		Conditional
		jmpcnd to else
		if body
		jmp to endlabel
		elselabel
		else body
		endlabel
		destroy vars

		Without else
		Conditional
		jmpcnd to end
		if body
		endlabel
		destroy vars
		*/
		
		String expression, ic = Integer.toString(ifcount);
		ifcount++;
		
		if (tree.children.size() > 7) { //Has else
			
			//Handle the condition of the if statement
			if((expression = simpleExpressionHandler(tree.children.get(2), 1)) != null) {
				IR.addCommand(IRelement.command.set, "%1 " + expression);
			}
			IR.addCommand(IRelement.command.not, "%1");
			
			//Jump conditionally to the else statement if the condition fails
			IR.addCommand(IRelement.command.jmpcnd, "else" + ic);
			
			IR.addCommand(IRelement.command.label, "ifstart" + ic);
			
			//Handle the statementList inside the if statement body and deletes variables
			destroyVars(statementHandler(tree.children.get(5)));
			
			//Jump unconditionally to the ifend label
			IR.addCommand(IRelement.command.jmp, "ifend" + ic);
			
			//Add a label for the else statement
			IR.addCommand(IRelement.command.label, "else" + ic);
			
			//Handle the statementList inside the else statement body
			destroyVars(statementHandler(tree.children.get(9)));
			
			//Add a label for the end of the if statement
			IR.addCommand(IRelement.command.label, "ifend" + ic);
			
		} else { //Doesn't have else			
			
			//Handle the condition of the if statement
			if((expression = simpleExpressionHandler(tree.children.get(2), 1)) != null) {
				IR.addCommand(IRelement.command.set, "%1 " + expression);
			}
			IR.addCommand(IRelement.command.not, "%1");
			
			//Jump conditionally to the end if the condition fails
			IR.addCommand(IRelement.command.jmpcnd, "ifend" + ic);
			
			//Indicates start of the if statement
			IR.addCommand(IRelement.command.label, "ifstart" + ic);
			
			//Handle the statementList inside the if statement body
			destroyVars(statementHandler(tree.children.get(5)));
			
			//Add a label for the end of the if statement
			IR.addCommand(IRelement.command.label, "ifend" + ic);
		}
	}
	
	//Calls simpleExpressionHandler
	//Please use recursion as much as possible. 
	private static List<String> variableDeclarationHandler(Ptree tree) {
		//1. store variable type
		type_enum type = tree.children.get(0).children.get(0).token.type;
		
		//2. pass type to recursive function that goes to each instance of variableDeclarationInitialize
		return variableHelper(tree.children.get(1), type);
		
		//3. do stuff in subfunction
	}
	
	private static List<String> variableHelper(Ptree tree, type_enum type) {
		List<String> list = new ArrayList<String>();
		switch(tree.token.type) {
		case variableDeclarationList:
			for(Ptree t : tree.children) {
				list.addAll(variableHelper(t, type));
			}
			return list;
		case variableDeclarationInitialize:
			String expression, ID = tree.children.get(0).children.get(0).token.token;
			IR.addCommand(IRelement.command.declare, ID);
			if(tree.children.size() == 3) {
				if((expression = simpleExpressionHandler(tree.children.get(2), 1)) == null) {
					IR.addCommand(IRelement.command.set, ID + " %1");
				} else {
					IR.addCommand(IRelement.command.set, ID + " " + expression);
				}
			}
			list.add(ID);
			return list;
		case comma:
			return list;
			//add declaration
			//call simple expression if needed then set variable to %i
			//return List containing the variable being dealt with
		default:
			ErrorHandler.addError("default case reached in variableHelper");
		}
		return null;
	}
	
	/**
	 * Handles expression statments. possible expressions involve increment decrement assignments or function calls
	 * @param tree should be an expressionStatement node in a parse tree
	 */
	private static void expressionHandler(Ptree tree) {
		if(tree.token.type == type_enum.expressionStatement) {
			tree = tree.children.get(0);
		}
		String n;
		if(tree.children.get(0).token.type == type_enum.call) {
			functionCallHandler(tree.children.get(0), 0);
			return;
		}
		
		String ID = tree.findTree(type_enum.identifier).token.token;
		switch(tree.children.get(1).token.type) {
		case incrementOperator:
			IR.addCommand(IRelement.command.add, ID + " 1");
			break;
		case decrementOperator:
			IR.addCommand(IRelement.command.sub, ID + " 1");
			break;
		case additionAssignmentOperator:
			if((n = simpleExpressionHandler(tree.children.get(2), 1)) == null) {
				IR.addCommand(IRelement.command.add, ID + " %1");
			} else {
				IR.addCommand(IRelement.command.add, ID + " " + n);
			}
			break;
		case subtractionAssignmentOperator:
			if((n = simpleExpressionHandler(tree.children.get(2), 1)) == null) {
				IR.addCommand(IRelement.command.sub, ID + " %1");
			} else {
				IR.addCommand(IRelement.command.sub, ID + " " + n);
			}
			break;
		case multiplicationAssignmentOperator:
			if((n = simpleExpressionHandler(tree.children.get(2), 1)) == null) {
				IR.addCommand(IRelement.command.mul, ID + " %1");
			} else {
				IR.addCommand(IRelement.command.mul, ID + " " + n);
			}
			break;
		case divisionAssignmentOperator:
			if((n = simpleExpressionHandler(tree.children.get(2), 1)) == null) {
				IR.addCommand(IRelement.command.div, ID + " %1");
			} else {
				IR.addCommand(IRelement.command.div, ID + " " + n);
			}
			break;
		case assignmentOperator:
			if((n = simpleExpressionHandler(tree.children.get(2), 1)) == null) {
				IR.addCommand(IRelement.command.set, ID + " %1");
			} else {
				IR.addCommand(IRelement.command.set, ID + " " + n);
			}
			break;
		default:
			ErrorHandler.addError("default case reached in expressionHandler");
		}
	}
	
	/**
	 * Deals with all simple expressions
	 * @param tree parse tree node of wherever is being dealt with
	 * @param i intermediate variable in usage
	 * @return String of whatever variable is being passed up or null in the case of an intermediate variable
	 */
	private static String simpleExpressionHandler(Ptree tree, int i) {
		switch(tree.token.type) {
		case constant:
			return tree.children.get(0).token.token;
		case variable:
			IR.addCommand(IRelement.command.set, "%" + i + " " + tree.children.get(0).token.token);
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
	
	/**
	 * Tests if the current node of the tree being scanned is an expression or just an intermediary step
	 * @param tree node being evaluated
	 * @return true if the expression is a used expression or false if just an intermediary
	 */
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
			//Situation where expression node is actually used rather than being passed through
			if(tree.children.size() > 1) {
				return true;
			}
		default:
			return false;
		}
	}
	
	/**
	 * Creates IR and preprocesses input for simple expression handler
	 * @param tree tree of whatever expression is being evaluated
	 * @param i index of whatever temporary variable is being used at the moment
	 * @return String of the preprocessed value or null if it evaluates to an intermediate variable
	 */
	private static String implementExpression(Ptree tree, int i) {
		String n, n2;
		IRelement.command c = command.set;
		if(tree.token.type != type_enum.notExpression) {
			n = simpleExpressionHandler(tree.children.get(0), i);
			n2 = simpleExpressionHandler(tree.children.get(2), i + 1);
		} else {
			n = simpleExpressionHandler(tree.children.get(1), i);
			if(n == null) {
				IR.addCommand(IRelement.command.not, "%" + i);
				return null;
			} else {
				return preProcess(n);
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
			if(tree.children.get(1).children.get(0).token.type == type_enum.equalOperator) {
				c = command.eq;
			} else {
				c = command.neq;
			}
			break;
		case sumExpression:
			if(tree.children.get(1).children.get(0).token.type == type_enum.additionOperator) {
				c = command.add;
			} else {
				c = command.sub;
			}
			break;
		case term:
			if(tree.children.get(1).children.get(0).token.type == type_enum.multiplicationOperator) {
				c = command.mul;
			} else if (tree.children.get(1).token.type == type_enum.divisionOperator){
				c = command.div;
			} else {
				c = command.mod;
			}
			break;
		default:
			ErrorHandler.addError("default condition met in simple expressionhandler");
			return null;
		}
		if(n == null && n2 == null) {
			IR.addCommand(c, "%" + i + " %" + (i + 1));
		} else if(n == null && n2 != null) {
			IR.addCommand(c, "%" + i + " " + n2);
		} else if(n != null && n2 == null) {
			IR.addCommand(IRelement.command.set, "%" + i + " " + n);
			IR.addCommand(c, "%" + i + " %" + (i + 1));
		} else {
			return preProcess(tree, n, n2);
		}
		return null;
	}
	
	/**
	 * Preprocesses not values
	 * @param v value of the constant to be reversed
	 * @return the string 1 or 0
	 */
	private static String preProcess(String v) {
		int n = ~findValue(v);
		return String.valueOf(n);		
	}
	
	/**
	 * Preprocesses values in the case where two constants are being operated on
	 * @param tree the expression to be evaluated
	 * @param v1 the left constant
	 * @param v2 the right constant
	 * @return a string of whatever the preprocessed value is
	 */
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
			ErrorHandler.addError("default case reached in preProcess");
		}
		return null;
	}
	
	/**
	 * Returns the integer value of whatever is being handled for preprocessing
	 * @param v value being evaluated
	 * @return integer that is representative of the string passed in
	 */
	private static int findValue(String v) {
		if(v.charAt(0) == '\'') {
			return (int)v.charAt(1);
	 	} else {
	 		return Integer.parseInt(v);
	 	}
	}
	
	//Adds setting temp variables before function call.
	private static void functionCallHandler(Ptree tree, int i) {
		String ID = tree.children.get(0).token.token;
		String name, n;
		
		if(tree.children.size() == 3) {
			name = " %" + String.valueOf(i);
			if(i == 0) {
				name = "";
			}
			IR.addCommand(IRelement.command.call, ID + name);
		} else {
			name = String.valueOf(i);
			if(i == 0) {
				i++;
				name = null;
			}
			List<Ptree> trees = new ArrayList<Ptree>();
			List<String> list = new ArrayList<String>();
			tree.children.get(2).findTrees(trees, type_enum.simpleExpression);
			for(Ptree t : trees) {
				n = simpleExpressionHandler(t, i);
				if(n == null) {
					list.add("%" + i);
					i++;
				} else {
					list.add(n);
				}
			}
			if(name != null) {
				list.add(name);
			}
			IR.addCommand(command.call, list);
		}
	}
}
