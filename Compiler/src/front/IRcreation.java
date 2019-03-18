package front;

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
	
	//Global variable declarations and function declarations. Everything passed on to further functions.
	//Calls functionHandler, and variableDeclarationHandler
	private void declarationHandler(Ptree tree, SymbolTable table) {
		
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
