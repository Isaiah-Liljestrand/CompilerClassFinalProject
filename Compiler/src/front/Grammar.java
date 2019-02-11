package front;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
	private PTree root;
	private boolean valid;
	
	/**
	 * Effectivly Main function of Grammar as a builder?
	 * @param tokens
	 */
	Grammar(List<Token> tokens){ // Essentially the first few parts of the grammar
					while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
						Feed.add(tokens.remove(0));
						if(tokens.isEmpty()) {
							System.out.println("Missing Closing Curly Brace");
						    //System.out.println("False");
							return;
						}
						
					}
					
					Feed.add(tokens.remove(0));
					//for( Token tok : Feed) {
						//System.out.println(tok.getToken());
					//}
					
					root.addChild(funDeclaration(Feed));
					root.printTree();
						/**if(valid == true) {
							System.out.println("\n\nValid Grammar");
						}else {
							System.out.print(valid);
						}*/
				} else if(tokens.get(3).getType() != Token.type_enum.closedParenthesis) {
					System.out.println("Missing closing Parenthesis");
					//System.out.println("False");
					return;
				}
			}
		}
	}
	
		List<Token> Feed1 = new ArrayList<Token>();
		List<Token> Feed2 = new ArrayList<Token>();
		List<Token> typeSpec = new ArrayList<Token>();
		List<Token> ID = new ArrayList<Token>();
		List<Token> params = new ArrayList<Token>();
		PTree tree = new PTree("funDeclaration");
		
		//System.out.println("funDeclaration");
		
		if(tokens.get(0).getType() == Token.type_enum.keyword) {
			if(tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.openParenthesis) {
				if(tokens.get(3).getType() == Token.type_enum.closedParenthesis && tokens.get(4).getType() == Token.type_enum.openCurlyBracket) {
					while(tokens.get(0).getType() != Token.type_enum.closedParenthesis) {
						Feed1.add(tokens.remove(0));
						if(Feed1.isEmpty()) {
							System.out.println("Missing Closed Parenthisis");
							System.out.println("funDeclaration is false");
							return null;
						}
					}
					Feed1.add(tokens.remove(0)); //The function declaration up to closed parenthesis E.G. int main()
					while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
						if(Feed2.isEmpty()) {
							System.out.println("Missing Closing Curly Brace");
							System.out.println("funDeclaration is false");
							//System.out.println("False");
							return null;
						}
					}
					
					Feed2.add(tokens.remove(0)); //The statement for { to }
				}
	 */
			}
		}
		
		if(!Feed1.isEmpty()) {
			if(Feed1.get(0).getType() == Token.type_enum.keyword && Feed1.get(1).getType() == Token.type_enum.identifier && Feed1.get(2).getType() == Token.type_enum.openParenthesis) {
				typeSpec.add(Feed1.remove(0));
				ID.add(Feed1.remove(0));
				//System.out.println(ID.get(0).getToken());
				if(Feed1.get(0).getType() == Token.type_enum.openParenthesis) {
					while(Feed1.get(0).getType() != Token.type_enum.closedParenthesis) {
						params.add(Feed1.remove(0));
						if(Feed1.isEmpty()) {
							System.out.println("Missing Closed Parenthisis");
						}
					params.add(Feed1.remove(0));
		if(tokens.get(tokens.size() - 1).type == type_enum.semicolon) {
				i--;
				if(i < 0) {
				}
				tree.addChild(statement(Feed2));
				return tree;
			} else {
				ID.add(Feed1.remove(0));
				if(Feed1.get(0).getType() == Token.type_enum.openParenthesis) {
					while(Feed1.get(0).getType() != Token.type_enum.closedParenthesis) {
						params.add(Feed1.remove(0));
						if(Feed1.isEmpty()) {
							System.out.println("Missing Closed Parenthisis");
							System.out.println("funDeclaration is false");
							return null;
						}
					params.add(Feed1.remove(0));
			return null;
		} else if (tokens.get(tokens.size() - 1).type == type_enum.closedCurlyBracket) {
			i = findMatchingBracket(tokens, tokens.size() - 1);
			while (true) {
				if(i < 0) {
					return null;
				}
				tree.addChild(params(params));
				tree.addChild(statement(Feed2));
				return tree;
			}
		}
		
		System.out.println("funDeclaration is false");
		return null; 
	}
	
	private PTree statement(List<Token> tokens) {
		//System.out.println("statement");
		List<Token> Feed = new ArrayList<Token>();
		PTree leaf = new PTree("statement");
		
		if(tokens.get(0).getType() == Token.type_enum.openCurlyBracket) {
			while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
				if(tokens.isEmpty()) {
					return null;
				Feed.add(tokens.remove(0));
			}
			Feed.add(tokens.remove(0));
			//return compoundStmt(Feed);
			leaf.addChild(compoundStmt(Feed));
			return leaf;
		if(tokens.size() < 3) {
		}
		
		System.out.println("statement is false");
		return null;
	}
	
		
		//for(Token tok : tokens) {
	//		System.out.println(tok.getToken());
		//}
		List<Token> rtnStmt = new ArrayList<Token>();
		int i = 0;
		
		while(!tokens.get(i).getToken().contentEquals("return")) {
			i++;
	private Ptree variableDeclarationList(List<Token> tokens) {
		}
		while(tokens.get(i).getType() != Token.type_enum.semicolon) {
			if(tokens.isEmpty()) {
				System.out.println("Expected semicolon at end of return statement");
				System.out.println("compoundStmt is false");
				return null;
			}
		rtnStmt.add(tokens.remove(i));
		}
		if(tokens.get(0).getType() == Token.type_enum.openCurlyBracket) {
			tokens.remove(0);
		}
		
		leaf.addChild(localDeclarations(tokens));
		//return localDeclarations(tokens) && returnStmt(rtnStmt);
		return leaf;
		
	}
	
	private PTree localDeclarations(List<Token> tokens) { 
		//System.out.println("localDeclarations");
		PTree leaf = new PTree("localDeclarations");
		
		//I'm about to just handle this for the basic program only, so this is gonna need further work
		if(tokens.size() == 3) {
			if(tokens.get(0).getType() == Token.type_enum.keyword && tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.semicolon) {
				return leaf;
			}
		} else if(tokens.size() > 3) {
			if(tokens.get(0).getType() == Token.type_enum.keyword && tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.semicolon) {
				tokens.remove(0);
				tokens.remove(0);
				tokens.remove(0);
				return localDeclarations(tokens);
			}
		}
		return null;
	}
	
			if(tokens.get(0).getToken().contentEquals("return") && tokens.get(1).getType() == Token.type_enum.semicolon) {
				//return true;
				return leaf;
			}
		}
		if(tokens.size() == 3) {
			if(tokens.get(0).getToken().contentEquals("return") && tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.semicolon) {
			}
	private Ptree functionDeclaration(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.functionDeclaration);
		int index, index2;
		if (index == -1) {
			return null;
		}
			tree.addChild(parameterList(tokens.subList(3, index)));
		}
		tree.addChild(closedParenthesis(tokens.get(index)));
		tree.addChild(openCurlyBracket(tokens.get(index + 1)));
		index2 = findMatchingBracket(tokens, index + 1);
		if (index2 == -1) {
			return null;
		}
		System.out.println("returnStmt is false");
		return null;
	}
		
	private PTree IDfunc(List<Token> tokens) {
		PTree leaf = new PTree("IDfunc");
	}	
	
	/**
	 * @param tokens tokens inside of parenthesis of a function
	 * @return Ptree if all is valid otherwise null
	 */
	private Ptree parameterList(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.parameterList);
		tree.addChild(parameter(tokens));
		if(tree.verifyChildren(1)) {
		
		if(tokens.size() == 1) {
			if(tokens.get(0).getType() == Token.type_enum.identifier) {
				return leaf;
			} else {
				System.out.println("IDfunc is false");
				return null;
			}
		}
		System.out.println("IDfunc is false");
		return null;
	}
	
	private PTree typeSpecifier(List<Token> tokens) {
		//System.out.println("typeSpecifier");
		PTree leaf = new PTree("typeSpecifier");
		
		
		if(tokens.size() == 1) {
			if(tokens.get(0).getToken().contentEquals("int") | tokens.get(0).getToken().contentEquals("bool") | tokens.get(0).getToken().contentEquals("char") | tokens.get(0).getToken().contentEquals("float")) {
				return leaf;
	/**
	 * Single parameter to be passed into a function
		}
		System.out.println("typeSpecifier is false");
		return null;
	}
	
	private PTree params(List<Token> tokens) { // Only handles basic program right now
		//System.out.println("params");
		PTree leaf = new PTree("params");
		//for( Token tok : tokens) {
		//	System.out.println(tok.getToken());
		//}
		if(tokens.size() == 2) {
			if(tokens.get(0).getType() == Token.type_enum.openParenthesis && tokens.get(1).getType() == Token.type_enum.closedParenthesis) {
				return leaf;
			}
		}
		if(tree.verifyChildren(1)) {
			return tree;
		}
		System.out.println("params is false");
		return null;
	}
	
	private PTree recDeclaration() {
		if(statement(tokens) != null) {
			tree.addChild(statement(tokens));
			if(tree.verifyChildren(1)) {
			while (true) {
				if(i < 0) {
					return null;
			tree.addChild(statementList(tokens.subList(0, i)));
			tree.addChild(statement(tokens.subList(i, tokens.size())));
			if(tree.verifyChildren(2)) {
		return null;
	}
	
	/**
	 * A goto statement.
		}
		if (tokens.get(1).getType() == type_enum.identifier) {
			tree.addChildFromToken(tokens.get(1));
		return null;
	}
	
	/**
	 * A return statement.
			tree.addChildFromToken(tokens.get(1));
			if(tree.verifyChildren(2)) {
				return tree;
		return null;
	}
	
	private PTree varDecList() {
	 */
	private Ptree breakStmt(List<Token> tokens) {
		return null;
	}
	
	private PTree varDecInitialize() {
	 */
	private Ptree whileStmt(List<Token> tokens) {
		return null;
	}
	
	private PTree varDecId() {
	private Ptree ifStmt(List<Token> tokens) {
		return null;
	}
	
	private PTree scopedTypeSpecifier() {
		return null;
	}
	
		return null;
	}
	
	 * @param tokens Should be a single identifier followed by a colon
	 * @return A gotoJumpPlace subtree if valid, null if invalid
	 */
		}
		if(tree.verifyChildren(2)) {
			return tree;
		return null;
	}
	
	private PTree paramTypeList() {
	 * @param tokens
	 * @return
	 */
		return null;
	}
	
	private PTree paramIdList() {
		Ptree tree = new Ptree(type_enum.expression);
		tree.addChild(simpleExpression(tokens));
		if(tree.verifyChildren(1)) {
		return null;
	}
	
	private PTree paramId() {
	private Ptree simpleExpression(List<Token> tokens) {
		//Very incorrect. Only used temporarily for testing.
		Ptree tree = new Ptree(type_enum.expression);
		return null;
	}
	
	/**
	 * Placeholder function. Will complete in future.
		return null;
	}
	
	private Ptree unaryRelExpression(List<Token> tokens) {
		return null;
	}
	
	private PTree selectionStmt() {
	 */
	private Ptree relExpression(List<Token> tokens) {
		return null;
	}
	
	private PTree iterationStmt() {
	 */
	private Ptree compareOp(List<Token> tokens) {
		return null;
	}
	
	private PTree breakStmt() {
	 */
	private Ptree sumExpression(List<Token> tokens) {
		return null;
	}
	
	private PTree expression() {
	 */
	private Ptree sumop(List<Token> tokens) {
		return null;
	}
	
	private PTree simpleExpression() {
		return null;
	}
	
	private PTree andExpression() {
	 * @param tokens
		return null;
	}
	
	private PTree unaryRelExpression() {
		return null;
	}
	
		return null;
	}
	
	private PTree relop() {
		return null;
	}
	
		return null;
	}
	
	private PTree sumop() {
	private Ptree constant(List<Token> tokens) {
		Ptree tree = new Ptree(type_enum.constant);
		if (tokens.get(0).getType() == type_enum.number) {
		return null;
	}
	
	private PTree term() {
		return null;
	
		return null;
			return new Ptree(token);
		default:
			return null;
	}
	
	private PTree unaryExpression() {
		return null;
	private Ptree functionTypeSpecifier(Token token) {
		switch (token.getType()) {
		case k_int:
	}
	
	/**
	 * Simply checks if passed in token is an identifier
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an open parenthesis
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an closed parenthesis
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an open curly bracket
		if(token.type == type_enum.openCurlyBracket) {
			return new Ptree(token);
		}
		return null;
	}
	
	private PTree call() {
		return null;
	}
	
	private PTree args() {
		return null;
	}
	
	private PTree argList() {
		return null;
	}
	
	private PTree constant() {
		return null;
	}
	
	public boolean getValid() {
		return this.valid;
	}
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
			//i++;
		} else if (tokens.get(startindex).getType() == type_enum.closedCurlyBracket) {
			forward = false;
			otherBracket = type_enum.openCurlyBracket;
			//i--;
		}
		while(i < tokens.size() && i > -1) {
			if(forward) {
				i++;
			} else {
				i--;
			}
			if(tokens.get(i).getType() == matchingBracket) {
				numBrackets++;
			} else if(tokens.get(i).getType() == otherBracket) {
				numBrackets--;
				if(numBrackets == 0) {
					return i;
				}
			}
			/*if(forward) {
				i++;
			} else {
				i--;
			}*/
		}

		return -1;
	}
}
