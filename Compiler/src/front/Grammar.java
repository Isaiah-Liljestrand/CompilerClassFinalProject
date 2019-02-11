package front;

import java.util.List;

import front.Token.type_enum;

public class Grammar {
	private Ptree root;
	private boolean valid;
	
	/**
	 * Effectively Main function of Grammar starts the program tree
	 * @param tokens All tokens in the program
	 */
	Grammar(List<Token> tokens) { // Essentially the first few parts of the grammar
		this.root = new Ptree(type_enum.program);
		valid = true;
		root.addChild(declarationList(tokens));
		if(!root.verifyChildren(1)) {
			valid = false;
		}
		//Potential to add funDeclarationList in order to support more than just main
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
		if(declaration(tokens) != null) {
			tree.addChild(declaration(tokens));
			if(tree.verifyChildren(1)) {
				return tree;
			}
			return null;
		}
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
			if(tree.verifyChildren(2)) {
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
			if(tree.verifyChildren(2)) {
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
		if(tree.getChildren().get(0) != null) {
			return tree;
		}
		tree.removeChild();
		tree.addChild(variableDeclaration(tokens));
		if(tree.getChildren().get(0) != null) {
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
		if(tree.verifyChildren(3)) {
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
		while(tokens.get(index).getType() == type_enum.comma) {
			index--;
			if(index == 0) {
				return null;
			}
		}
		tree.addChild(variableDeclarationList(tokens.subList(0, index)));
		tree.addChild(comma(tokens.get(index)));
		tree.addChild(variableDeclarationInitialize(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren(3)) {
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
		if(tree.getChildren().get(0) != null) {
			return tree;
		}
		tree.removeChild();
		
		if(tokens.size() < 3) {
			return null;
		}
		tree.addChild(variableDeclareID(tokens.subList(0, 1)));
		tree.addChild(equals(tokens.get(1)));
		tree.addChild(expression(tokens.subList(2, tokens.size())));
		if(tree.verifyChildren(3)) {
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
		if(tokens.size() != 1 || tokens.get(0).getType() != type_enum.identifier) {
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
		index = findMatchingBracket(tokens, 2);
		if (index == -1) {
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
			tree.addChild(statement(tokens.subList(index + 2, index2)));
		}
		tree.addChild(closedCurlyBracket(tokens.get(index2)));
		if(tree.verifyChildren(6) || tree.verifyChildren(7) || tree.verifyChildren(8)) {
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
		if(tree.verifyChildren(1)) {
			return tree;
		}
		tree.removeChild();
		
		int index = tokens.size() - 1;
		while(tokens.get(index).getType() != type_enum.comma) {
			index--;
			if(index == 0) {
				return null;
			}
		}
		tree.addChild(parameterList(tokens.subList(0, index)));
		tree.addChild(comma(tokens.get(index)));
		tree.addChild(parameter(tokens.subList(index + 1, tokens.size())));
		if(tree.verifyChildren(3)) {
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
		if(tree.verifyChildren(2)) {
			return tree;
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
	}
	

	
	
	
	private Ptree expression(List<Token> tokens) {
		return null;
	}
	
	private Ptree simpleExpression() {
		return null;
	}
	
	private Ptree andExpression() {
		return null;
	}
	
	private Ptree unaryRelExpression() {
		return null;
	}
	
	private Ptree relExpression() {
		return null;
	}
	
	private Ptree relop() {
		return null;
	}
	
	private Ptree sumExpression() {
		return null;
	}
	
	private Ptree sumop() {
		return null;
	}
	
	private Ptree term() {
		return null;
	}
	
	private Ptree mulop() {
		return null;
	}
	
	private Ptree unaryExpression() {
		return null;
	}
	
	private Ptree unaryop() {
		return null;
	}
	
	private Ptree factor() {
		return null;
	}
	
	private Ptree mutable() {
		return null;
	}
	
	private Ptree immutable() {
		return null;
	}
	
	private Ptree call() {
		return null;
	}
	
	private Ptree args() {
		return null;
	}
	
	private Ptree argList() {
		return null;
	}
	
	private Ptree constant() {
		return null;
	}
	
	/**
	 * Checks whether the token passed in is a valid variable type specifier
	 * @param token should be either int or char
	 * @return Ptree containing the token if the token was an int or char null otherwise
	 */
	private Ptree variableTypeSpecifier(Token token) {
		switch (token.getType()) {
		case k_int:
		case k_char:
			return new Ptree(token);
		default:
			return null;
		}
	}
	
	/**
	 * Checks whether the token passed in is a valid function type specifier
	 * @param token should be either int char or void
	 * @return Ptree containing the token if the token was an int, char, or void null otherwise
	 */
	private Ptree functionTypeSpecifier(Token token) {
		switch (token.getType()) {
		case k_int:
		case k_char:
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
	 * simply checks if passed in token is an equals sign
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
	 * Finds the matching curly bracket in a list of tokens
	 * @param tokens list of tokens from an undefined source
	 * @param startindex index of the curly bracket to be matched
	 * @return the index of the bracket that matches the one passed in
	 */
	private int findMatchingBracket(List<Token> tokens, int startindex) {
		int numBrackets = 1, i = startindex;
		boolean forward = true;
		type_enum matchingBracket = tokens.get(startindex).getType();
		type_enum otherBracket = null;
		if(tokens.get(startindex).getType() == type_enum.openCurlyBracket) {
			otherBracket = type_enum.closedCurlyBracket;
			i++;
		} else if (tokens.get(startindex).getType() == type_enum.closedCurlyBracket) {
			forward = false;
			otherBracket = type_enum.openCurlyBracket;
			i--;
		}
		while(i < tokens.size() && i > -1) {
			if(tokens.get(i).getType() == matchingBracket) {
				numBrackets++;
			} else if(tokens.get(i).getType() == otherBracket) {
				numBrackets--;
				if(numBrackets == 0) {
					return i;
				}
			}
			if(forward) {
				i++;
			} else {
				i--;
			}
		}

		return -1;
	}
}
