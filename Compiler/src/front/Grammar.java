package front;

import java.util.List;

import front.Token.type_enum;

public class Grammar {
	private Ptree root;
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
	 * If the tree root is valid, print the tree.
	 */
	public void printTree() {
		if (valid) {
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
		int i;
		if(tokens.size() == 0) {
			return null;
		}
		tree.addChild(declaration(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();
		
		if(tokens.get(tokens.size() - 1).type == type_enum.semicolon) {
			i = tokens.size() - 2;
			while(tokens.get(i).type != type_enum.semicolon && tokens.get(i).type != type_enum.closedCurlyBracket) {
				i--;
				if(i < 0) {
					return null;
				}
			}
			i++;
			tree.addChild(declarationList(tokens.subList(0, i)));
			tree.addChild(declaration(tokens.subList(i, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		} else if (tokens.get(tokens.size() - 1).type == type_enum.closedCurlyBracket) {
			i = findMatchingBracket(tokens, tokens.size() - 1);
			i--;
			while (true) {
				if(i < 0) {
					return null;
				}
				if(tokens.get(i).type == type_enum.semicolon || tokens.get(i).type == type_enum.closedCurlyBracket) {
					break;
				}
				i--;
			}
			i++;
			tree.addChild(declarationList(tokens.subList(0, i)));
			tree.addChild(declaration(tokens.subList(i, tokens.size())));
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
		tree.removeChild();
		
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
		tree.addChild(variableTypeSpecifier(tokens.get(0)));
		tree.addChild(variableDeclarationList(tokens.subList(1, tokens.size() - 1)));
		tree.addChild(semicolon(tokens.get(tokens.size() - 1)));
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
		if(tree.getChildren().get(0) != null) {
			return tree;
		}
		tree.removeChild();
		if(tokens.size() < 3) {
			return null;
		}
		int index = tokens.size() - 1;
		while(tokens.get(index).type == type_enum.comma) {
			index--;
			if(index == 0) {
				return null;
			}
		}
		tree.addChild(variableDeclarationList(tokens.subList(0, index)));
		tree.addChild(comma(tokens.get(index)));
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
		tree.removeChild();
		
		if(tokens.size() < 3) {
			return null;
		}
		tree.addChild(variableDeclareID(tokens.subList(0, 1)));
		tree.addChild(equals(tokens.get(1)));
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
		tree.addChild(functionTypeSpecifier(tokens.get(0)));
		tree.addChild(identifier(tokens.get(1)));
		tree.addChild(openParenthesis(tokens.get(2)));
		index = findMatchingParenthesis(tokens, 2);
		if (index == -1 || index + 1 >= tokens.size()) {
			return null;
		}
		if(index > 3) {
			tree.addChild(parameterList(tokens.subList(3, index)));
		}
		tree.addChild(closedParenthesis(tokens.get(index)));
		tree.addChild(openCurlyBracket(tokens.get(index + 1)));
		index2 = findMatchingBracket(tokens, index + 1);
		if (index2 == -1) {
			return null;
		}
		if(index2 > (index + 2)) {
			tree.addChild(statementList(tokens.subList(index + 2, index2)));
		}
		tree.addChild(closedCurlyBracket(tokens.get(index2)));
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
		tree.removeChild();
		
		int index = tokens.size() - 1;
		while(tokens.get(index).type != type_enum.comma) {
			index--;
			if(index == 0) {
				return null;
			}
		}
		tree.addChild(parameterList(tokens.subList(0, index)));
		tree.addChild(comma(tokens.get(index)));
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
		tree.addChild(variableTypeSpecifier(tokens.get(0)));
		tree.addChild(identifier(tokens.get(1)));
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
		int i;
		if(tokens.size() == 0) {
			return null;
		}
		
		tree.addChild(statement(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();
		
		if(tokens.get(tokens.size() - 1).type == type_enum.semicolon) {
			i = tokens.size() - 2;
			while(tokens.get(i).type != type_enum.semicolon && tokens.get(i).type != type_enum.closedCurlyBracket) {
				i--;
				if(i < 0) {
					return null;
				}
			}
			i++;
			tree.addChild(statementList(tokens.subList(0, i)));
			tree.addChild(statement(tokens.subList(i, tokens.size())));
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		} else if (tokens.get(tokens.size() - 1).type == type_enum.closedCurlyBracket) {
			i = findMatchingBracket(tokens, tokens.size() - 1);
			i--;
			if(tokens.get(i).type == type_enum.k_else) {
				if(tokens.get(i - 1).type == type_enum.closedCurlyBracket) {
					i = findMatchingBracket(tokens, i - 1);
					i--;
					while(tokens.get(i).type != type_enum.semicolon && tokens.get(i).type != type_enum.closedCurlyBracket) {
						i--;
						if(i < 0) {
							return null;
						}
					}
					i++;
					tree.addChild(statementList(tokens.subList(0, i)));
					tree.addChild(statement(tokens.subList(i, tokens.size())));
					if(tree.verifyChildren()) {
						return tree;
					}
					return null;
				}
			}
			while(tokens.get(i).type != type_enum.semicolon && tokens.get(i).type != type_enum.closedCurlyBracket) {
				i--;
				if(i < 0) {
					return null;
				}
			}
			i++;
			tree.addChild(statementList(tokens.subList(0, i)));
			tree.addChild(statement(tokens.subList(i, tokens.size())));
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
		tree.removeChild();
		
		//return statement
		tree.addChild(returnStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();

		//while statement
		tree.addChild(whileStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();
		
		//break statement
		tree.addChild(breakStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();
		
		//if statement
		tree.addChild(ifStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();
		
		//variable declaration statement
		tree.addChild(variableDeclaration(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();

		//goto jump location statement
		tree.addChild(gotoJumpPlace(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();

		//expression statement
		tree.addChild(expressionStmt(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		tree.removeChild();

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
		tree.addChild(gotoFunction(tokens.get(0)));
		tree.addChild(identifier(tokens.get(1)));
		tree.addChild(semicolon(tokens.get(2)));
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
		tree.addChild(returnFunction(tokens.get(0)));
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
		tree.addChild(breakFunction(tokens.get(0)));
		tree.addChild(semicolon(tokens.get(1)));
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
		tree.addChild(whileFunction(tokens.get(0)));
		tree.addChild(openParenthesis(tokens.get(1)));
		int index = findMatchingParenthesis(tokens, 1);
		if(index == -1 || index > tokens.size() - 3) {
			return null;
		}
		if(index > 2) {
			tree.addChild(simpleExpression(tokens.subList(2, index)));
		}
		tree.addChild(closedParenthesis(tokens.get(index)));
		tree.addChild(openCurlyBracket(tokens.get(index + 1)));
		int index2 = findMatchingBracket(tokens, index + 1);
		if(index2 == -1 || index2 != tokens.size() - 1) {
			return null;
		}
		if(index2 > index + 2) {
			tree.addChild(statementList(tokens.subList(index + 2, index2)));
		}
		tree.addChild(closedCurlyBracket(tokens.get(index2)));
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
		tree.addChild(ifFunction(tokens.get(0)));
		tree.addChild(openParenthesis(tokens.get(1)));
		int index = findMatchingParenthesis(tokens, 1);
		if(index == -1 || index + 1 < tokens.size()) {
			return null;
		}
		tree.addChild(simpleExpression(tokens.subList(2, index)));
		tree.addChild(closedParenthesis(tokens.get(index)));
		tree.addChild(openCurlyBracket(tokens.get(index + 1)));
		int index2 = findMatchingBracket(tokens, index + 1);
		if(index2 == -1) {
			return null;
		}
		if(index + 2 != index2) {
			tree.addChild(statementList(tokens.subList(index + 2, index2)));
		}
		tree.addChild(closedCurlyBracket(tokens.get(index2)));
		if(tokens.size() == index2 + 1) {
			if(tree.verifyChildren()) {
				return tree;
			}
			return null;
		}
		if(index2 + 3 >= tokens.size()) {
			return null;
		}
		tree.addChild(elseFunction(tokens.get(index2 + 1)));
		tree.addChild(openCurlyBracket(tokens.get(index2 + 2)));
		index = findMatchingBracket(tokens, index2 + 2);
		if(index > index2 + 3) {
			tree.addChild(statementList(tokens.subList(index2 + 3, index)));
		}
		tree.addChild(closedCurlyBracket(tokens.get(index)));
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
		tree.addChild(identifier(tokens.get(0)));
		tree.addChild(colon(tokens.get(1)));
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
	private Ptree expressionStmt(List<Token> tokens) {
		return null;
	}
	
	private Ptree expression(List<Token> tokens) {
		//Not accounting for anything but simpleExpression yet
		Ptree tree = new Ptree(type_enum.expression);
		tree.addChild(simpleExpression(tokens));
		if(tree.verifyChildren()) {
			return tree;
		}
		return null;
	}

	private Ptree simpleExpression(List<Token> tokens) {
		//Very incorrect. Only used temporarily for testing.
		Ptree tree = new Ptree(type_enum.expression);
		tree.addChild(constant(tokens.get(0)));
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
		return null;
	}
	
	private Ptree unaryRelExpression(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree relExpression(List<Token> tokens) {
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
	 * @param tokens
	 * @return
	 */
	private Ptree compareOp(List<Token> tokens) {
		return null;
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
	
	/**
	 * Tests that the passed in token is a goto keyword
	 * @param token should be goto
	 * @return tree node or null
	 */
	private Ptree gotoFunction(Token token) {
		if(token.type == type_enum.k_goto) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * tests if the token passed in is an if keyword
	 * @param token should be if
	 * @return either a tree node or null
	 */
	private Ptree ifFunction(Token token) {
		if(token.type == type_enum.k_if) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * checks if the token passed in is the else keyword
	 * @param token should be else
	 * @return Ptree with else token or null
	 */
	private Ptree elseFunction(Token token) {
		if(token.type == type_enum.k_else) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token passed in is a while function keyword
	 * @param token should be the while keyword
	 * @return Ptree node containing the keyword or null
	 */
	private Ptree whileFunction(Token token) {
		if(token.type == type_enum.k_while) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token is a constant
	 * @param token should be either a char constant or an int constant
	 * @return Ptree node if the statment evaluates to true
	 */
	private Ptree constant(Token token) {
		if (token.type == type_enum.number) {
			return new Ptree(token);
		}
		return null;
	}
	
	
	/**
	 * Checks whether the token passed in is a valid variable type specifier
	 * @param token should be either int or char
	 * @return Ptree containing the token if the token was an int or char null otherwise
	 */
	private Ptree variableTypeSpecifier(Token token) {
		switch (token.type) {
		case k_int:
		case k_char:
			return new Ptree(token);
		default:
			return null;
		}
	}
	
	
	/**
	 * Checks that the passed in token is a return call
	 * @param token should be a return statement
	 * @return Ptree containing return statement or null
	 */
	private Ptree returnFunction(Token token) {
		if(token.type == type_enum.k_return) {
			return new Ptree(token);
		}
		return null;
	}
	
	
	/**
	 * Tests if the token passed in is a break keyword
	 * @param token should be break
	 * @return new node or null
	 */
	private Ptree breakFunction(Token token) {
		if(token.type == type_enum.k_break) {
			return new Ptree(token);
		}
		return null;
	}
	
	
	
	
	
	/**
	 * Checks whether the token passed in is a valid function type specifier
	 * @param token should be either int char or void
	 * @return Ptree containing the token if the token was an int, char, or void null otherwise
	 */
	private Ptree functionTypeSpecifier(Token token) {
		switch (token.type) {
		case k_int:
			return new Ptree(token);
		case k_char:
			return new Ptree(token);
		case k_void:
			return new Ptree(token);
		default:
			return null;
		}
	}
	
	/**
	 * Simply checks if passed in token is an identifier
	 * @param token should be identifier
	 * @return Ptree identifier leaf node or null if not valid
	 */
	private Ptree identifier(Token token) {
		if(token.type == type_enum.identifier) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an open parenthesis
	 * @param token should be an open parenthesis
	 * @return Ptree node with an open parenthesis or null
	 */
	private Ptree openParenthesis(Token token) {
		if(token.type == type_enum.openParenthesis) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an closed parenthesis
	 * @param token should be an closed parenthesis
	 * @return Ptree node with an closed parenthesis or null
	 */
	private Ptree closedParenthesis(Token token) {
		if(token.type == type_enum.closedParenthesis) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an open curly bracket
	 * @param token should be an open curly bracket
	 * @return Ptree leaf node or null depending on validity
	 */
	private Ptree openCurlyBracket(Token token) {
		if(token.type == type_enum.openCurlyBracket) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is a closed curly bracket
	 * @param token should be an closed curly bracket
	 * @return Ptree leaf node or null depending on validity
	 */
	private Ptree closedCurlyBracket(Token token) {
		if(token.type == type_enum.closedCurlyBracket) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is a semicolon
	 * @param token should be a semicolon
	 * @return Ptree leaf node with a semicolon or null depending on validity
	 */
	private Ptree semicolon(Token token) {
		if(token.type == type_enum.semicolon) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * simply checks if passed in token is a comma
	 * @param token should be a comma
	 * @return Ptree leaf node with a semicolon or null depending on validity
	 */
	private Ptree comma(Token token) {
		if(token.type == type_enum.comma) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is a colon
	 * @param token should be colon
	 * @return Ptree node containing Ptree or null
	 */
	private Ptree colon(Token token) {
		if(token.type == type_enum.colon) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if passed in token is an equals sign
	 * @param token should be an equals sign
	 * @return Ptree leaf node with an equals sign depending on validity
	 */
	private Ptree equals(Token token) {
		if(token.type == type_enum.assignmentOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks whether the grammar is valid or not
	 * @return true if valid false if not
	 */
	public boolean getValid() {
		return this.valid;
	}
	
	/**
	 * Finds the matching parenthesis in a list of tokens
	 * @param tokens list of tokens from an undefined source
	 * @param startindex index of the parenthesis to be matched
	 * @return the index of the parenthesis that matches the one passed in
	 */
	private int findMatchingParenthesis(List<Token> tokens, int startindex) {
		int numBrackets = 1, i = startindex;
		boolean forward = true;
		type_enum matchingBracket = tokens.get(startindex).type;
		type_enum otherBracket = null;
		if(tokens.get(startindex).type == type_enum.openParenthesis) {
			otherBracket = type_enum.closedParenthesis;
		} else if (tokens.get(startindex).type == type_enum.closedParenthesis) {
			forward = false;
			otherBracket = type_enum.openParenthesis;
		}
		while(i < tokens.size() && i > -1) {
			if(forward) {
				i++;
			} else {
				i--;
			}
			if(tokens.get(i).type == matchingBracket) {
				numBrackets++;
			} else if(tokens.get(i).type == otherBracket) {
				numBrackets--;
				if(numBrackets == 0) {
					return i;
				}
			}
		}

		return -1;
	}
	
	/**
	 * Finds the matching curly bracket in a list of tokens
	 * @param tokens list of tokens from an undefined source
	 * @param startindex index of the curly bracket to be matched
	 * @return the index of the bracket that matches the one passed in
	 */
	private int findMatchingBracket(List<Token> tokens, int startindex) {
		int numBrackets = 1, i = startindex;
		boolean forward = true;
		type_enum matchingBracket = tokens.get(startindex).type;
		type_enum otherBracket = null;
		if(tokens.get(startindex).type == type_enum.openCurlyBracket) {
			otherBracket = type_enum.closedCurlyBracket;
		} else if (tokens.get(startindex).type == type_enum.closedCurlyBracket) {
			forward = false;
			otherBracket = type_enum.openCurlyBracket;
		}
		while(i < tokens.size() && i > -1) {
			if(forward) {
				i++;
			} else {
				i--;
			}
			if(tokens.get(i).type == matchingBracket) {
				numBrackets++;
			} else if(tokens.get(i).type == otherBracket) {
				numBrackets--;
				if(numBrackets == 0) {
					return i;
				}
			}
		}

		return -1;
	}
}
