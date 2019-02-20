package front;

import java.util.List;

import front.GrammarHelper;

import front.Token.type_enum;

public class Grammar {
	private Ptree root;
	private SymbolTable symbolTableRoot;
	private boolean valid;
	
	/**
	 * Program BNF starts at the top
	 * @param tokens All tokens in the program
	 */
	Grammar(List<Token> tokens) { // Essentially the first few parts of the grammar
		this.root = new Ptree(type_enum.program);
		valid = true;
		root.addChild(declarationList(tokens));
		if(!root.verifyChildren()) {
			valid = false;
		}
	}
	
	
	/**
	 * Checks whether the grammar is valid or not
	 * @return true if valid false if not
	 */
	public boolean getValid() {
		return this.valid;
	}
	
	
	/**
	 * If the tree root is valid, print the tree.
	 */
	public void printTree() {
		if (valid) {
			System.out.println("sanity check");
			root.printTree();
		}
	}
	
	/**
	 * splits up all variable and function declarations at the top level of code
	 * @param tokens all tokens passed down from either program of another declarationList
	 * @return tree if all subtrees are successful otherwise null
	 */
	private Ptree declarationList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.declarationList);
		int index;
		if(tokens.size() == 0) {
			return null;
		}
		tree.addChild(declaration(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		if(tokens.get(tokens.size() - 1).type == type_enum.semicolon) {
			index = GrammarHelper.findObject(tokens.subList(0, tokens.size() - 1), type_enum.semicolon, type_enum.closedCurlyBracket);
			if(index < 1) {
				return null;
			}
			index++;
			tree.addChild(declarationList(tokens.subList(0, index)));
			tree.addChild(declaration(tokens.subList(index, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		} else if (tokens.get(tokens.size() - 1).type == type_enum.closedCurlyBracket) {
			index = GrammarHelper.findMatchingBracket(tokens, tokens.size() - 1);
			if(index < 3) {
				return null;
			}
			index = GrammarHelper.findObject(tokens.subList(0, index), type_enum.semicolon, type_enum.closedCurlyBracket);
			if(index < 1) {
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
		return null;
		
	}

	/**
	 * Declaration either leads to a function or variable declaration
	 * @param tokens all tokens associated with said declaration
	 * @return declaration subtree if all subtrees are valid
	 */
	private Ptree declaration(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.declaration);
		tree.addChild(functionDeclaration(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		tree.addChild(variableDeclaration(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * Variable declaration
	 * @param tokens all tokens associated with said declaration
	 * @return variableDeclaration tree if all subtrees are valid
	 */
	private Ptree variableDeclaration(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.variableDeclaration);
		if(tokens.size() < 3) {
			return null;
		}
		tree.addChild(GrammarHelper.variableTypeSpecifier(tokens.get(0)));
		tree.addChild(variableDeclarationList(tokens.subList(1, tokens.size() - 1)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(tokens.size() - 1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * variableDeclarationList is the BNF element that splits up all declarations
	 * @param tokens either a single declaration or multiple
	 * @return variableDeclarationList subtree or null
	 */
	private Ptree variableDeclarationList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.variableDeclarationList);
		if(tokens.size() == 0) {
			return null;
		}
		tree.addChild(variableDeclarationInitialize(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		if(tokens.size() < 3) {
			return null;
		}
		int index = GrammarHelper.findObject(tokens, type_enum.comma);
		if(index < 1) {
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
	 * Either a simple variable name declaration or a declaration and assignment 
	 * @param tokens tokens passed down
	 * @return subtree that represents a declared variable
	 */
	private Ptree variableDeclarationInitialize(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.variableDeclarationInitialize);
		tree.addChild(variableDeclareID(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		if(tokens.size() < 3) {
			return null;
		}
		tree.addChild(variableDeclareID(tokens.subList(0, 1)));
		tree.addChild(GrammarHelper.equals(tokens.get(1)));
		tree.addChild(simpleExpression(tokens.subList(2, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	
	/**
	 * simply tests that the variable declaration has a valid size and is an identifier
	 * @param tokens should simply be one token that is a variable
	 * @return leaf node that represents a variable or null
	 */
	private Ptree variableDeclareID(List<Token> tokens) {
		if(tokens.size() != 1 || tokens.get(0).type != type_enum.identifier) {
			return null;
		}
		return new Ptree(tokens.get(0));
	}
	
	/**
	 * Function Declaration covers main or any other function
	 * @param tokens all tokens in a function declaration
	 * @return Ptree if the tree is valid and null if not
	 */
	private Ptree functionDeclaration(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.functionDeclaration);
		int index, index2;
		if(tokens.size() < 6) {
			return null;
		}
		tree.addChild(GrammarHelper.functionTypeSpecifier(tokens.get(0)));
		tree.addChild(GrammarHelper.identifier(tokens.get(1)));
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(2)));
		index = GrammarHelper.findMatchingParenthesis(tokens, 2);
		if (index == -1 || index > tokens.size() - 3) {
			return null;
		}
		if(index > 3) {
			tree.addChild(parameterList(tokens.subList(3, index)));
		}
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(index)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index + 1)));
		index2 = GrammarHelper.findMatchingBracket(tokens, index + 1);
		if (index2 != tokens.size() - 1) {
			return null;
		}
		if(index2 > (index + 2)) {
			tree.addChild(statementList(tokens.subList(index + 2, index2)));
		}
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index2)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}	
	
	/**
	 * list of parameters to be passed in if a function is called
	 * @param tokens tokens inside of parenthesis of a function
	 * @return Ptree if all is valid otherwise null
	 */
	private Ptree parameterList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.parameterList);
		tree.addChild(parameter(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		int index = GrammarHelper.findObject(tokens, type_enum.comma);
		if(index < 2) {
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
	 * Single parameter to be passed into a function
	 * @param tokens should be simply a variable type specifier and 
	 * @return subtree or null depending on validity
	 */
	private Ptree parameter(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.parameter);
		if(tokens.size() != 2) {
			return null;
		}
		tree.addChild(GrammarHelper.variableTypeSpecifier(tokens.get(0)));
		tree.addChild(GrammarHelper.identifier(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * Either a single statement, or a list of statement
	 * @param tokens will be either a single statement, or a statementList followed by a statement
	 * @return statementList subtree if valid, null if invalid
	 */
	private Ptree statementList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.statementList);
		int index;
		if(tokens.size() == 0) {
			return null;
		}
		
		tree.addChild(statement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		if(tokens.get(tokens.size() - 1).type == type_enum.semicolon) {
			index = GrammarHelper.findObject(tokens.subList(0, tokens.size() - 1), type_enum.semicolon, type_enum.closedCurlyBracket);
			if(index == -1) {
				return null;
			}
			index++;
			tree.addChild(statementList(tokens.subList(0, index)));
			tree.addChild(statement(tokens.subList(index, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		} else if (tokens.get(tokens.size() - 1).type == type_enum.closedCurlyBracket) {
			index = GrammarHelper.findMatchingBracket(tokens, tokens.size() - 1);
			if(tokens.get(index - 1).type == type_enum.k_else && tokens.get(index - 2).type == type_enum.closedCurlyBracket) {
				index = GrammarHelper.findMatchingBracket(tokens, index - 2);
				if(index == -1) {
					return null;
				}
				index = GrammarHelper.findObject(tokens.subList(0, index), type_enum.k_if);
				if(index == -1) {
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
			if(index == -1) {
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
	 * Separates out all the different kinds of statements
	 * @param tokens either a few statements or one depending on where it is being called from
	 * @return successful statement subtree if valid otherwise null
	 */
	private Ptree statement(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.statement);
		//goto statement
		tree.addChild(gotoStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//return statement
		tree.addChild(returnStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();

		//while statement
		tree.addChild(whileStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//break statement
		tree.addChild(breakStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		//if statement
		tree.addChild(ifStmt(tokens));
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
		tree.addChild(expressionStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		return null;
	}
	

	
	/**
	 * A goto statement.
	 * @param tokens should be a goto keyword followed by an identifier
	 * @return a gotoStmt subtree if valid, null if invalid
	 */
	private Ptree gotoStmt(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.gotoStmt);
		if(tokens.size() != 3) {
			return null;
		}
		tree.addChild(GrammarHelper.gotoFunction(tokens.get(0)));
		tree.addChild(GrammarHelper.identifier(tokens.get(1)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(2)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * A return statement.
	 * @param tokens should be a return keyword followed by either a semicolon, or a expression followed by a semicolon
	 * @return a returnStmt subtree if valid, false if invalid.
	 */
	private Ptree returnStmt(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.returnStmt);
		tree.addChild(GrammarHelper.returnFunction(tokens.get(0)));
		tree.addChild(expressionStmt(tokens.subList(1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree breakStmt(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.breakStmt);
		if(tokens.size() != 2) {
			return null;
		}
		tree.addChild(GrammarHelper.breakFunction(tokens.get(0)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * While function call
	 * @param tokens tokens that should represent a while statement
	 * @return Ptree while statement or null
	 */
	private Ptree whileStmt(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.whileStmt);
		if(tokens.size() < 6) {
			return null;
		}
		tree.addChild(GrammarHelper.whileFunction(tokens.get(0)));
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(1)));
		int index = GrammarHelper.findMatchingParenthesis(tokens, 1);
		if(index == -1 || index > tokens.size() - 3) {
			return null;
		}
		if(index > 2) {
			tree.addChild(simpleExpression(tokens.subList(2, index)));
		}
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(index)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index + 1)));
		int index2 = GrammarHelper.findMatchingBracket(tokens, index + 1);
		if(index2 != tokens.size() - 1) {
			return null;
		}
		if(index2 > index + 2) {
			tree.addChild(statementList(tokens.subList(index + 2, index2)));
		}
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index2)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree ifStmt(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.ifStmt);
		if(tokens.size() < 5) {
			return null;
		}
		tree.addChild(GrammarHelper.ifFunction(tokens.get(0)));
		tree.addChild(GrammarHelper.openParenthesis(tokens.get(1)));
		int index = GrammarHelper.findMatchingParenthesis(tokens, 1);
		if(index == -1 || index + 1 < tokens.size()) {
			return null;
		}
		tree.addChild(simpleExpression(tokens.subList(2, index)));
		tree.addChild(GrammarHelper.closedParenthesis(tokens.get(index)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index + 1)));
		int index2 = GrammarHelper.findMatchingBracket(tokens, index + 1);
		if(index2 == -1) {
			return null;
		}
		if(index + 2 != index2) {
			tree.addChild(statementList(tokens.subList(index + 2, index2)));
		}
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index2)));
		if(tokens.size() == index2 + 1) {
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		if(index2 + 3 >= tokens.size()) {
			return null;
		}
		tree.addChild(GrammarHelper.elseFunction(tokens.get(index2 + 1)));
		tree.addChild(GrammarHelper.openCurlyBracket(tokens.get(index2 + 2)));
		index = GrammarHelper.findMatchingBracket(tokens, index2 + 2);
		if(index > index2 + 3) {
			tree.addChild(statementList(tokens.subList(index2 + 3, index)));
		}
		tree.addChild(GrammarHelper.closedCurlyBracket(tokens.get(index)));
		if(index + 1 != tokens.size()) {
			return null;
		}
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}

	
	/**
	 * An identifier that a goto statement can jump to.
	 * @param tokens Should be a single identifier followed by a colon
	 * @return A gotoJumpPlace subtree if valid, null if invalid
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
	 * An expression that should theoretically stand by itself
	 * @param tokens all the tokens that should go into the expression statement
	 * @return Ptree containing the expression statement
	 */
	private Ptree expressionStmt(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.expressionStmt);
		if(tokens.size() == 0) {
			return null;
		}
		if(tokens.size() == 1) {
			tree.addChild(GrammarHelper.semicolon(tokens.get(0)));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		tree.addChild(expression(tokens.subList(0, tokens.size() - 1)));
		tree.addChild(GrammarHelper.semicolon(tokens.get(tokens.size() - 1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * Expression simple expression such as an assignment
	 * @param tokens make up an expression
	 * @return returns a Ptree that contains an expression
	 */
	private Ptree expression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.expression);
		tree.addChild(simpleExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		if(tokens.size() < 2) {
			return null;
		}
		if(tokens.size() > 2) {
			tree.addChild(GrammarHelper.identifier(tokens.get(0)));
			tree.addChild(GrammarHelper.assignmentOperator(tokens.get(1)));
			tree.addChild(simpleExpression(tokens.subList(2, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		tree.addChild(GrammarHelper.identifier(tokens.get(0)));
		tree.addChild(GrammarHelper.increment(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		tree.addChild(GrammarHelper.identifier(tokens.get(0)));
		tree.addChild(GrammarHelper.decrement(tokens.get(1)));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}

	/**
	 * Checks whether the or operator is present
	 * @param tokens tokens to be split and analyzed
	 * @return Ptree containing the correct subtrees or null if invalid
	 */
	private Ptree simpleExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.simpleExpression);
		if(tokens.size() == 0) {
			return null;
		}
		tree.addChild(andExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
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
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree andExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.andExpression);
		if(tokens.size() == 0) { 
			return null;
		}
		tree.addChild(unaryRelExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		int index = GrammarHelper.findObject(tokens, type_enum.andLogicOperator);
		if(index < 0) {
			return tree;
		}
		tree.addChild(andExpression(tokens.subList(0, index)));
		tree.addChild(GrammarHelper.logicAnd(tokens.get(index)));
		tree.addChild(unaryRelExpression(tokens.subList(index + 1, tokens.size())));
		return null;
	}
	
	/**
	 * Checks if the not operator is part of the current expression being evaluated
	 * @param tokens to be checked for unary expression not
	 * @return Ptree containing an expression or null if invalid
	 */
	private Ptree unaryRelExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.unaryRelExpression);
		if(tokens.size() == 0) {
			return null;
		}
		tree.addChild(GrammarHelper.notToken(tokens.get(0)));
		tree.addChild(relExpression(tokens.subList(1, tokens.size())));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		tree.addChild(relExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree relExpression(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.relExpression);
		tree.addChild(sumExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChildren();
		
		int index = GrammarHelper.findObject(tokens, type_enum.equalsOperator, type_enum.notEqualOperator);
		if(index < 0) {
			return null;
		}
		tree.addChild(sumExpression(tokens.subList(0, index)));
		tree.addChild(compareOperator(tokens.get(index)));
		tree.addChild(,treein);
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree compareOperator(Token token) {
		Ptree tree = new Ptree(type_enum.compareOperator);
		if(a)
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree sumExpression(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree sumop(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree term(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree mulOp(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree factor(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree call(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree args(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree argList(List<Token> tokens) {
		return null;
	}
}
	