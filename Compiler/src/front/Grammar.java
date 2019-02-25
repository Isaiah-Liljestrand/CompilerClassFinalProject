package front;

import java.util.List;

import front.GrammarHelper;

import front.Token.type_enum;

/**
 * Grammar class creates the Parse tree while checking that the input matches the provided grammar 
 * @author Isaiah-Liljestrand with some help from Jacob
 */
public class Grammar {
	public Ptree root;
	//public SymbolTable symTable;
	public boolean valid;
	
	/**
	 * Program → declarationList
	 * @param tokens All tokens in the program
	 */
	Grammar(List<Token> tokens) {
		this.root = new Ptree(type_enum.program);
		valid = true;
		root.addChild(declarationList(tokens));
		if(!root.verifyChildren()) {
			System.out.println("Grammar returned False");
			valid = false;
		}
	}
	
	
	/**
	 * Returns whether the grammar is valid or not
	 * @return true if valid false if not
	 */
	public boolean getValid() {
		return this.valid;
	}
	
	/**
	 * Returns ptree at the root of the parse tree
	 * @return program parse tree node
	 */
	public Ptree getPtree() {
		return root;
	}
	
	
	/**
	 * If the tree root is valid, print the tree.
	 */
	public void printTree() {
		if (valid) {
			//System.out.println("sanity check");
			root.printTree();
		}
	}
	
	
	/**
	 * declarationList → declarationList declaration | declaration
	 * @param tokens passed from parent grammar
	 * @return declarationList tree if valid, null if invalid
	 */
	private Ptree declarationList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.declarationList);
		int index;
		
		//Invalid if the list size is 0
		if(tokens.size() == 0) {
			return null;
		}
		
		//Checks if the passed in tokens represent a single declaration
		tree.addChild(declaration(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where the lowest current declaration ends with a semicolon, splits the input appropriately
		if(tokens.get(tokens.size() - 1).type == type_enum.semicolon) {
			index = GrammarHelper.findObject(tokens.subList(0, tokens.size() - 1), type_enum.semicolon, type_enum.closedCurlyBracket);
			if(index < 0) {
				return null;
			}
			index++;
			tree.addChild(declarationList(tokens.subList(0, index)));
			tree.addChild(declaration(tokens.subList(index, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
			
		//Case where the lowest current declaration ends with a closed curly bracket, splits the input appropriately
		} else if (tokens.get(tokens.size() - 1).type == type_enum.closedCurlyBracket) {
			index = GrammarHelper.findMatchingBracket(tokens, tokens.size() - 1);
			if(index < 0) {
				return null;
			}
			index = GrammarHelper.findObject(tokens.subList(0, index), type_enum.semicolon, type_enum.closedCurlyBracket);
			if(index < 0) {
				return null;
			}
			index++;
			tree.addChild(declarationList(tokens.subList(0, index)));
			tree.addChild(declaration(tokens.subList(index, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		
		//Failed to satisfy any pre-existing constructs
		return null;
	}

	
	/**
	 * declaration → variableDeclaration | functionDeclaration
	 * @param tokens passed from parent grammar
	 * @return declaration tree if valid, null if invalid
	 */
	private Ptree declaration(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.declaration);
		
		//Checks if declaration is a function declaration
		tree.addChild(functionDeclaration(tokens));
		if(tree.verifyChildren()) {
		//	System.out.println("check3");
			return tree;
		}
		tree.removeChildren();
		
		//Checks if the declaration is a function declaration
		tree.addChild(variableDeclaration(tokens));
		if(tree.verifyChildren()) {
		//	System.out.println("check3");
			return tree;
		}
		System.out.println("Declaration Did Nothing");
		return null;
	}
	
	
	/**
	 * variableDeclaration → variableTypeSpecifier variableDeclarationList ;
	 * @param tokens passed from parent grammar
	 * @return declaration tree if valid, null if invalid
	 */
	private Ptree variableDeclaration(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.variableDeclaration);
		if(tokens.size() < 3) {
			return null;
		}
		tree.addChild(variableTypeSpecifier(tokens.get(0)));
		tree.addChild(variableDeclarationList(tokens.subList(1, tokens.size() - 1)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(tokens.size() - 1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * variableTypeSpecifier → int | char
	 * @param token passed from parent grammar should be int or char
	 * @return variableTypeSpecifier tree if valid, null if invalid
	 */
	public static Ptree variableTypeSpecifier(Token token) {
		switch (token.type) {
		case k_int:
		case k_char:
			Ptree tree = new Ptree(type_enum.variableTypeSpecifier);
			tree.addChild(new Ptree(token));
			return tree;
		default:
			return null;
		}
	}
	
	
	/**
	 * variableDeclarationList → variableDeclarationList , variableDeclarationInitialize | variableDeclarationInitialize
	 * @param tokens passed from parent grammar
	 * @return variableDeclarationList tree if valid, null if invalid
	 */
	private Ptree variableDeclarationList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.variableDeclarationList);
		if(tokens.size() == 0) {
			return null;
		}
		
		//Single variable declaration
		tree.addChild(variableDeclarationInitialize(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Verifies that the size is large enough to be valid for multiple declarations
		if(tokens.size() < 3) {
			return null;
		}
		
		//Splits input based on farthest right comma
		int index = GrammarHelper.findObject(tokens, type_enum.comma);
		if(index < 0) {
			return null;
		}
		tree.addChild(variableDeclarationList(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.comma(tokens.get(index)));
		tree.addChild(variableDeclarationInitialize(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * variableDeclarationInitialize → variableDeclarationID | variableDeclarationID = simpleExpression 
	 * @param tokens passed from parent grammar
	 * @return variableDeclarationInitialize tree if valid, null if invalid
	 */
	private Ptree variableDeclarationInitialize(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.variableDeclarationInitialize);
		
		//Invalid input size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Variable initialization with no value
		tree.addChild(variableDeclarationID(tokens.get(0)));
		if(tree.verifyChildren() && tokens.size() == 1) {
			return tree;
		}
		
		//Verifies legitimate size for assignment declaration
		if(tokens.size() < 3) {
			return null;
		}
		
		//Assignment operation
		tree.addChild(GrammarHelper.equals(tokens.get(1)));
		tree.addChild(simpleExpression(tokens.subList(2, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * variableDeclarationID → ID
	 * @param token passed from parent grammar
	 * @return variableDeclarationID tree if valid, null if invalid
	 */
	private Ptree variableDeclarationID(Token token) {
		if(token.type != type_enum.identifier) {
			return null;
		}
		Ptree tree = new Ptree(type_enum.variableDeclarationID);
		//symTable.AddEntry(token.getToken(), token.getType().toString());
		tree.addChild(new Ptree(token));
		return tree;
	}
	
	
	/**
	 * functionDeclaration → functionTypeSpecifier functionDeclarationID ( parameterList ) { statementList }
	 * @param tokens passed from parent grammar should be one function declaration
	 * @return functionDeclaration tree if valid, null if invalid
	 */
	private Ptree functionDeclaration(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.functionDeclaration);
		int index, index2;
		
		//Verifies there are at least the minimum possible tokens for a function declaration
		if(tokens.size() < 6) {
			System.out.println("FunctionDeclaration did nothing");
			return null;
		}
		tree.addChild(functionTypeSpecifier(tokens.get(0)));
		tree.addChild(GrammarHelper.identifier(tokens.get(1)));
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(2)));
		index = GrammarHelper.findMatchingParenthesis(tokens, 2);
		if (index == -1 || index > tokens.size() - 3) {
			return null;
		}
		
		//If there are parameters in the function declaration calling parameterList
		if(index > 3) {
			tree.addChild(parameterList(tokens.subList(3, index)));
		}
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(index)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index + 1)));
		index2 = GrammarHelper.findMatchingBracket(tokens, index + 1);
		
		//Verifies legitimacy of index2 values
		if (index2 != tokens.size() - 1 || index2 < (index + 3)) {
			return null;
		}

		
		tree.addChild(statementList(tokens.subList(index + 2, index2)));
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index2)));
		
		//Verifies ligitimacy of the whole statement
		if(tree.verifyChildren()) {
			return tree;
		}
		System.out.println("functionDeclaration did nothing");
		return null;
	}
	
	
	/**
	 * functionTypeSpecifier → int | char | void
	 * @param token passed from parent grammar should be int, char, or void
	 * @return functionDeclaration tree if valid, null if invalid
	 */
	public static Ptree functionTypeSpecifier(Token token) {
		switch (token.type) {
		case k_int:
		case k_char:
		case k_void:
			Ptree tree = new Ptree(type_enum.functionTypeSpecifier);
			tree.addChild(new Ptree(token));
			return tree;
		default:
			System.out.println("FunctionTypeSpecifier did nothing");
			return null;
		}
	}
	
	
	/**
	 * parameterList → parameterList , parameter | parameter
	 * @param tokens passed from parent grammar
	 * @return parameterList tree if valid, null if invalid
	 */
	private Ptree parameterList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.parameterList);
		if(tokens.size() == 0) {
			return null;
 		}
		
		//Case where only one parameter is passed in
		tree.addChild(parameter(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//finds comma that should seperate parameters
		int index = GrammarHelper.findObject(tokens, type_enum.comma);
		if(index < 0) {
			return null;
		}
		tree.addChild(parameterList(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.comma(tokens.get(index)));
		tree.addChild(parameter(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * parameter → variableTypeSpecifier ID
	 * @param tokens passed from parent grammar should be one parameter
	 * @return parameter tree if valid, null if invalid
	 */
	private Ptree parameter(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.parameter);
		if(tokens.size() != 2) {
			return null;
		}
		tree.addChild(variableTypeSpecifier(tokens.get(0)));
		tree.addChild(GrammarHelper.identifier(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * statementList → statementList statement | statement
	 * @param tokens passed from parent grammar should be a list of statements or one statement
	 * @return statementList tree if valid, null if invalid
	 */
	private Ptree statementList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.statementList);
		int index;
		
		//Invalid size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Single statment
		tree.addChild(statement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Multiple statements where the last statement ends with a semicolon
		if(tokens.get(tokens.size() - 1).type == type_enum.semicolon) {
			index = GrammarHelper.findObject(tokens.subList(0, tokens.size() - 1), type_enum.semicolon, type_enum.closedCurlyBracket);
			if(index < 0) {
				return null;
			}
			index++;
			tree.addChild(statementList(tokens.subList(0, index)));
			tree.addChild(statement(tokens.subList(index, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		
		//Multiple statements where the last statement ends with a curly bracket
		} else if (tokens.get(tokens.size() - 1).type == type_enum.closedCurlyBracket) {
			index = GrammarHelper.findMatchingBracket(tokens, tokens.size() - 1);
			//Case where the last statement is a if else block
			if(tokens.get(index - 1).type == type_enum.k_else && tokens.get(index - 2).type == type_enum.closedCurlyBracket) {
				index = GrammarHelper.findMatchingBracket(tokens, index - 2);
				if(index < 0) {
					return null;
				}
				index = GrammarHelper.findObject(tokens.subList(0, index), type_enum.k_if);
				if(index < 0) {
					return null;
				}
				tree.addChild(statementList(tokens.subList(0, index)));
				tree.addChild(statement(tokens.subList(index, tokens.size())));
				if(tree.verifyChildren()) {
					return tree;
				}
				return null;
			}
			index = GrammarHelper.findObject(tokens.subList(0, index), type_enum.closedCurlyBracket, type_enum.semicolon);
			if(index < 0) {
				return null;
			}
			index++;
			tree.addChild(statementList(tokens.subList(0, index)));
			tree.addChild(statement(tokens.subList(index, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		return null;
	}
	

	/**
	 * statement → gotoStmt | returnStmt | whileStmt | breakStmt | ifStmt | varDeclaration | gotoJumpPlace | expressionStmt
	 * @param tokens passed from parent grammar should be any type of statement
	 * @return statement tree if valid, null if invalid
	 */
	private Ptree statement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.statement);
		//goto statement
		tree.addChild(gotoStatement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//return statement
		tree.addChild(returnStatement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();

		//while statement
		tree.addChild(whileStatement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//break statement
		tree.addChild(breakStatement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//if statement
		tree.addChild(ifStatement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//variable declaration statement
		tree.addChild(variableDeclaration(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();

		//goto jump location statement
		tree.addChild(gotoJumpPlace(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();

		//expression statement
		tree.addChild(expressionStatement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		return null;
	}
	
	
	/**
	 * gotoStatement → goto ID ;
	 * @param tokens passed from parent grammar should be a goto statement
	 * @return gotoStatement tree if valid, null if invalid
	 */
	private Ptree gotoStatement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.gotoStatement);
		
		//Verifies legitimacy of size
		if(tokens.size() != 3) {
			return null;
		}
		tree.addChild(GrammarHelper.gotoFunction(tokens.get(0)));
		if(!tree.verifyChildren()) {
			return null;
		}
		tree.addChild(GrammarHelper.identifier(tokens.get(1)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(2)));
		if(tree.verifyChildren()) {
			return tree;
		}
		//TODO: error reporting
		return null;
	}
	
	
	/**
	 * gotoJumpPlace → ID :
	 * @param tokens passed from parent grammar goto destination
	 * @return gotoJumpPlace tree if valid, null if invalid
	 */
	private Ptree gotoJumpPlace(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.gotoJumpPlace);
		if(tokens.size() != 2) {
			return null;
		}
		tree.addChild(GrammarHelper.identifier(tokens.get(0)));
		tree.addChild(GrammarHelper.colon(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * returnStatement → return expressionStatement
	 * @param token passed from parent grammar should be a return statement
	 * @return returnStatement tree if valid, null if invalid
	 */
	private Ptree returnStatement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.returnStatement);
		tree.addChild(GrammarHelper.returnFunction(tokens.get(0)));
		if(!tree.verifyChildren()) {
			return null;
		}
		tree.addChild(simpleExpression(tokens.subList(1, tokens.size() - 1)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(tokens.size() - 1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		//TODO: add error reporting
		return null;
	}
	
	
	/**
	 * breakStatement → break ;
	 * @param tokens passed from parent grammar should be break statement
	 * @return breakStatement tree if valid, null if invalid
	 */
	private Ptree breakStatement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.breakStatement);
		
		//Verifies legitimacy of size
		if(tokens.size() != 2) {
			return null;
		}
		tree.addChild(GrammarHelper.breakFunction(tokens.get(0)));
		if(!tree.verifyChildren()) {
			return null;
		}
		tree.addChild(GrammarHelper.semicolon(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		//TODO: error reporting
		return null;
	}
	
	
	/**
	 * whileStatement → while ( simpleExpression ) { statementList }
	 * @param tokens passed from parent grammar should be while statement
	 * @return whileStatement tree if valid, null if invalid
	 */
	private Ptree whileStatement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.whileStatement);
		
		//Checks for minimum legitimate size
		if(tokens.size() < 7) {
			return null;
		}
		tree.addChild(GrammarHelper.whileFunction(tokens.get(0)));
		if(tree.verifyChildren()) {
			return null;
		}
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(1)));
		int index = GrammarHelper.findMatchingParenthesis(tokens, 1);
		
		//Checks parenthesis location output
		if(index < 0 || index > tokens.size() - 3 || !(index > 2)) {
			//TODO: error reporting
			return null;
		}
		
		//expression that deals with the loop logic
		tree.addChild(simpleExpression(tokens.subList(2, index)));
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(index)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index + 1)));
		int index2 = GrammarHelper.findMatchingBracket(tokens, index + 1);
		
		//checks bracket location output
		if(index2 != tokens.size() - 1 || !(index2 > index + 2)) {
			//TODO: error reporting
			return null;
		}
		
		//deals with statements inside while statement
		tree.addChild(statementList(tokens.subList(index + 2, index2)));
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index2)));
		if(tree.verifyChildren()) {
			return tree;
		}
		//TODO: error reporting
		return null;
	}
	
	
	/**
	 * ifStmt → if ( simpleExpression ) { statementList } | if ( simpleExpression ) { statementList } else { statementList }
	 * @param tokens passed from parent grammar should be if statement
	 * @return ifStatement tree if valid, null if invalid
	 */
	private Ptree ifStatement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.ifStatement);
		
		//Verifies minimum possible size
		if(tokens.size() < 5) {
			return null;
		}
		tree.addChild(GrammarHelper.ifFunction(tokens.get(0)));
		if(tree.verifyChildren()) {
			return null;
		}
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(1)));
		int index = GrammarHelper.findMatchingParenthesis(tokens, 1);
		
		//Checks parenthesis location output
		if(index < 3 || !(index + 1 < tokens.size())) {
			//TODO: error reporting
			return null;
		}
		tree.addChild(simpleExpression(tokens.subList(2, index)));
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(index)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index + 1)));
		int index2 = GrammarHelper.findMatchingBracket(tokens, index + 1);
		
		//Checks that the bracket location is legitimate
		if(index2 < 0 || !(index + 2 < index2)) {
			//TODO: error reporting
			return null;
		}
		
		//Internal statements
		tree.addChild(statementList(tokens.subList(index + 2, index2)));
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index2)));
		
		//Case where the statement has no else
		if(tokens.size() == index2 + 1) {
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		
		//Checks index of index2
		if(index2 + 4 >= tokens.size()) {
			return null;
		}
		
		//Case where an else function is involved
		tree.addChild(GrammarHelper.elseFunction(tokens.get(index2 + 1)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index2 + 2)));
		index = GrammarHelper.findMatchingBracket(tokens, index2 + 2);
		
		//Checks if the bracket index is a legitimate size
		if(index < 0 || !(index2 + 3 < index) || index != tokens.size()) {
			//TODO: error reporting
			return null;
		}
		
		//Statements inside else function
		tree.addChild(statementList(tokens.subList(index2 + 3, index)));
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index)));
		if(tree.verifyChildren()) {
			return tree;
		}
		//TODO: error reporting
		return null;
	}
	
	
	/**
	 * expressionStatement → expression ; | ;
	 * @param tokens passed from parent grammar should be an expression statement
	 * @return expressionStatement tree if valid, null if invalid
	 */
	private Ptree expressionStatement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.expressionStatement);
		if(tokens.size() == 0) {
			return null;
		}
		
		//Statment composed of one semicolon
		if(tokens.size() == 1) {
			tree.addChild(GrammarHelper.semicolon(tokens.get(0)));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		
		//Statement composed of an expression ending in a semicolon
		tree.addChild(expression(tokens.subList(0, tokens.size() - 1)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(tokens.size() - 1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * expression → ID assignmentOperator simpleExpression | call | ID++ | ID--
	 * @param tokens passed from parent grammar should be an expression
	 * @return expression tree if valid, null if invalid
	 */
	private Ptree expression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.expression);
		if(tokens.size() == 0) {
			return null;
		}
		
		//simple expression
		tree.addChild(call(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		if(tokens.size() < 2) {
			return null;
		}
		
		//variable reassignment
		if(tokens.size() > 2) {
			tree.addChild(variable(tokens.get(0)));
			tree.addChild(GrammarHelper.assignmentOperator(tokens.get(1)));
			tree.addChild(simpleExpression(tokens.subList(2, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		
		//variable++
		tree.addChild(variable(tokens.get(0)));
		tree.addChild(GrammarHelper.increment(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//variable--
		tree.addChild(variable(tokens.get(0)));
		tree.addChild(GrammarHelper.decrement(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}

	
	/**
	 * simpleExpression → simpleExpression or(||) andExpression | andExpression
	 * @param tokens passed from parent grammar to be split by logical ors
	 * @return expression tree if valid, null if invalid
	 */
	private Ptree simpleExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.simpleExpression);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Case with no logical or at this level
		tree.addChild(andExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where logical or is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.orLogicOperator);
		if(index < 0) {
			return null;
		}
		tree.addChild(simpleExpression(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.logicOr(tokens.get(index)));
		tree.addChild(andExpression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * andExpression → andExpression and(&&) bitOrExpression | bitOrExpression
	 * @param tokens passed from parent grammar to be split by logical ands
	 * @return andExpression tree if valid, null if invalid
	 */
	private Ptree andExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.andExpression);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) { 
			return null;
		}
		
		//Case with no logic and at this level
		tree.addChild(bitOrExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where the logical and operator is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.andLogicOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(andExpression(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.logicAnd(tokens.get(index)));
		tree.addChild(bitOrExpression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * bitOrExpression → bitOrExpression or (|) bitXorExpression | bitXorExpression
	 * @param tokens passed from parent grammar to be split by bitwise ors
	 * @return bitOrExpression tree if valid, null if invalid
	 */
	private Ptree bitOrExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.bitOrExpression);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Case where no bitwise or operator is used at this level
		tree.addChild(bitXorExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where bitwise or operator is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.orOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(bitOrExpression(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.bitOr(tokens.get(index)));
		tree.addChild(bitXorExpression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * bitXorExpression → bitXorExpression xor (^) bitAndExpression | bitAndExpression
	 * @param tokens passed from parent grammar to be split by bitwise xors
	 * @return bitXorExpression tree if valid, null if invalid
	 */
	private Ptree bitXorExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.bitXorExpression);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Case where no bitwise xor operator is used at this level
		tree.addChild(bitAndExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where bitwise or operator is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.xorOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(bitXorExpression(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.bitOr(tokens.get(index)));
		tree.addChild(bitAndExpression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * bitAndExpression → bitAndExpression and (&) compareExpression | bitAndExpression
	 * @param tokens passed from parent grammar to be split by bitwise ands
	 * @return bitAndExpression tree if valid, null if invalid
	 */
	private Ptree bitAndExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.bitAndExpression);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Case where no bitwise and operator is used at this level
		tree.addChild(compareExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where bitwise and operator is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.andOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(bitAndExpression(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.bitAnd(tokens.get(index)));
		tree.addChild(compareExpression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}

	
	/**
	 * compareExpression → sumExpression compareOperator sumExpression | sumExpression
	 * @param tokens passed from parent grammar to be split by comparison operators
	 * @return compareExpression tree if valid, null if invalid
	 */
	private Ptree compareExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.compareExpression);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Case where no comparison operator is used at this level
		tree.addChild(sumExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where comparison operator is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.equalOperator, type_enum.notEqualOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(sumExpression(tokens.subList(0, index)));
		tree.addChild(compareOperator(tokens.get(index)));
		tree.addChild(sumExpression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * compareOperator → == | !=
	 * @param token passed from parent grammar should be either == or !=
	 * @return compareOperator tree if valid, null if invalid
	 */
	private Ptree compareOperator(Token token) {
		switch(token.type) {
		case equalOperator:
		case notEqualOperator:
			Ptree tree = new Ptree(type_enum.compareOperator);
			tree.addChild(new Ptree(token));
			return tree;
		default:
			return null;
		}
	}

	
	/**
	 * sumExpression → sumExpression sumoperator term | term
	 * @param tokens passed from parent grammar to be split by summation operators
	 * @return sumExpression tree if valid, null if invalid
	 */
	private Ptree sumExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.sumExpression);
			
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
			
		//Case where no summation operator is used at this level
		tree.addChild(term(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
			
		//Case where summation operator is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.additionOperator, type_enum.subtractionOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(sumExpression(tokens.subList(0, index)));
		tree.addChild(sumOperator(tokens.get(index)));
		tree.addChild(term(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * sumOperator → + | -
	 * @param token passed from parent grammar should be either + or -
	 * @return sumOperator tree if valid, null if invalid
	 */
	private Ptree sumOperator(Token token) {
		switch(token.type) {
		case additionOperator:
		case subtractionOperator:
			Ptree tree = new Ptree(type_enum.sumOperator);
			tree.addChild(new Ptree(token));
			return tree;
		default:
			return null;
		}
	}

	
	/**
	 * term → term productOperator notExpression | notExpression
	 * @param tokens passed from parent grammar to be split by multiplicitive operators
	 * @return term tree if valid, null if invalid
	 */
	private Ptree term(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.term);
			
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
			
		//Case where no multiplicative operator is used at this level
		tree.addChild(notExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
			
		//Case where multiplicative operator is used at this level
		int index = GrammarHelper.findObject(tokens, type_enum.multiplicationOperator, type_enum.divisionOperator, type_enum.modulusOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(term(tokens.subList(0, index)));
		tree.addChild(productOperator(tokens.get(index)));
		tree.addChild(notExpression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * productOperator → * | / | %
	 * @param token passed from parent grammar should be either *, /, or %
	 * @return productOperator tree if valid, null if invalid
	 */
	private Ptree productOperator(Token token) {
		switch(token.type) {
		case multiplicationOperator:
		case divisionOperator:
		case modulusOperator:
			Ptree tree = new Ptree(type_enum.productOperator);
			tree.addChild(new Ptree(token));
			return tree;
		default:
			return null;
		}
	}
	
	
	/**
	 * notExpression → !notExpression | factor
	 * @param tokens passed from parent grammar should be ! operator
	 * @return notExpression tree if valid, null if invalid
	 */
	private Ptree notExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.notExpression);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Case where not ! unary operator was used
		tree.addChild(factor(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Case where the ! unary operator was used
		tree.addChild(GrammarHelper.notToken(tokens.get(0)));
		tree.addChild(factor(tokens.subList(1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * factor → ID | ( expression ) | call | constant
	 * @param tokens passed from parent grammar should be a factor in an expression
	 * @return factor tree if valid, null if invalid
	 */
	private Ptree factor(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.factor);
		
		//Checks for a legitimate size
		if(tokens.size() == 0) {
			return null;
		}
		
		//Checks if the factor is an identifier
		if(tokens.size() == 1) {
			tree.addChild(variable(tokens.get(0)));
			if(tree.verifyChildren()) {
				return tree;
			}
			tree.removeChildren();
			
			//Checks if the factor is a constant
			tree.addChild(constant(tokens.get(0)));
			if(tree.verifyChildren()) {
				return tree;
			}
			tree.removeChildren();
			return null;
		}
		//Checks if factor is expression inside parenthesis
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(0)));
		tree.addChild(expression(tokens.subList(1, tokens.size() - 1)));
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(tokens.size() - 1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//Checks if factor is a function call
		tree.addChild(call(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * call → ID ( args )
	 * @param tokens passed from parent grammar should be a function call
	 * @return call tree if valid, null if invalid
	 */
	private Ptree call(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.call);
		
		//verifies legitimacy of size
		if(tokens.size() < 3) {
			return null;
		}
		tree.addChild(GrammarHelper.identifier(tokens.get(0)));
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(1)));
		if(tokens.size() > 3) {
			tree.addChild(argList(tokens.subList(2, tokens.size())));
		}
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(tokens.size() - 1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * argList → argList, expression | expression
	 * @param tokens passed from parent grammar should be a function call
	 * @return argList tree if valid, null if invalid
	 */
	private Ptree argList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.argList);
		
		//Checks if there is one argument being passed in
		tree.addChild(expression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//seperates out input by a comma
		int index = GrammarHelper.findObject(tokens, type_enum.comma);
		if(index < 0) {
			return null;
		}
		tree.addChild(argList(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.comma(tokens.get(index)));
		tree.addChild(expression(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * constant → number | character
	 * @param token passed from parent grammar should either be a number or letter
	 * @return constant tree if valid, null if invalid
	 */
	private Ptree constant(Token token) {
		switch(token.type) {
		case number:
		case character:
			Ptree tree = new Ptree(type_enum.constant);
			tree.addChild(new Ptree(token));
			return tree;
		default:
			return null;
		}
	}
	
	/**
	 * Variable call that has been defined elsewhere
	 * @param token passed from parent should be a variable call
	 * @return variable tree if valid, null if invalid
	 */
	private Ptree variable(Token token) {
		Ptree tree = new Ptree(type_enum.variable);
		tree.addChild(GrammarHelper.identifier(token));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
}
	