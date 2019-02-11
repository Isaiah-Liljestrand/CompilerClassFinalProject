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
		this.root = new PTree();
		root.setType("root");
		List<Token> Feed = new ArrayList<Token>();
		
		if(tokens.get(0).getType() == Token.type_enum.keyword) { //This block of ifs only checks for Int Main() at the moment
			if(tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.openParenthesis) {				
				if(tokens.get(3).getType() == Token.type_enum.closedParenthesis && tokens.get(4).getType() == Token.type_enum.openCurlyBracket) {
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
	
	private PTree funDeclaration(List<Token> tokens) { 
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
						Feed2.add(tokens.remove(0));
						if(Feed2.isEmpty()) {
							System.out.println("Missing Closing Curly Brace");
							System.out.println("funDeclaration is false");
							//System.out.println("False");
							return null;
						}
					}
					
					Feed2.add(tokens.remove(0)); //The statement for { to }
				}
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
							System.out.println("funDeclaration is false");
							return null;
						}
					}
					params.add(Feed1.remove(0));
				}
				//return typeSpecifier(typeSpec) && IDfunc(ID) && params(params) && statement(Feed2);
				
				tree.setType("funDeclaration");
				tree.addChild(typeSpecifier(typeSpec));
				tree.addChild(IDfunc(ID));
				tree.addChild(params(params));
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
					}
					params.add(Feed1.remove(0));
				}
				//return IDfunc(ID) && params(params) && statement(Feed2);
				tree.setType("funDeclaration");
				tree.addChild(IDfunc(ID));
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
					System.out.println("Missing Closed curly bracket");
					System.out.println("statement is false");
					return null;
				}
				Feed.add(tokens.remove(0));
			}
			Feed.add(tokens.remove(0));
			//return compoundStmt(Feed);
			leaf.addChild(compoundStmt(Feed));
			return leaf;
		}
		
		System.out.println("statement is false");
		return null;
	}
	
	private PTree compoundStmt(List<Token> tokens) { // Checks for rtn statement
		//System.out.println("compoundStmt");
		PTree leaf = new PTree("compoundStmt");
		
		//for(Token tok : tokens) {
	//		System.out.println(tok.getToken());
		//}
		List<Token> rtnStmt = new ArrayList<Token>();
		int i = 0;
		
		while(!tokens.get(i).getToken().contentEquals("return")) {
			i++;
		}
		while(tokens.get(i).getType() != Token.type_enum.semicolon) {
			if(tokens.isEmpty()) {
				System.out.println("Expected semicolon at end of return statement");
				System.out.println("compoundStmt is false");
				return null;
			}
			rtnStmt.add(tokens.remove(i));
		}
		
		rtnStmt.add(tokens.remove(i));
		
		//This next part is bad because we are just removing curly brackets and assuming the remaining
		//Tokens are local declarations
		
		if(tokens.get(i).getType() == Token.type_enum.closedCurlyBracket) {
			tokens.remove(i);
		}
		if(tokens.get(0).getType() == Token.type_enum.openCurlyBracket) {
			tokens.remove(0);
		}
		
		leaf.addChild(localDeclarations(tokens));
		leaf.addChild(returnStmt(rtnStmt));
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
		System.out.println("localDeclarations is false");
		return null;
	}
	
	private PTree returnStmt(List<Token> tokens) {
		//System.out.println("returnStmt");
		PTree leaf = new PTree("returnStmt");
		
		if(tokens.size() == 2) {
			if(tokens.get(0).getToken().contentEquals("return") && tokens.get(1).getType() == Token.type_enum.semicolon) {
				//return true;
				return leaf;
			}
		}
		if(tokens.size() == 3) {
			if(tokens.get(0).getToken().contentEquals("return") && tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.semicolon) {
				return leaf; // This actually needs to check the expression which would be the middle token
			}
		}
		System.out.println("returnStmt is false");
		return null;
	}
		
	private PTree IDfunc(List<Token> tokens) {
		//System.out.println("IDfunc");
		PTree leaf = new PTree("IDfunc");
		
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
				//System.out.println(leaf.getType());
				return leaf;
			}
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
		System.out.println("params is false");
		return null;
	}
	
	private PTree recDeclaration() {
		return null;
	}
	
	private PTree varDeclaration() {
		return null;
	}
	
	private PTree scopedVarDeclaration() {
		return null;
	}
	
	private PTree varDecList() {
		return null;
	}
	
	private PTree varDecInitialize() {
		return null;
	}
	
	private PTree varDecId() {
		return null;
	}
	
	private PTree scopedTypeSpecifier() {
		return null;
	}
	
	private PTree returnTypeSpecifier() {
		return null;
	}
	
	private PTree paramList() {
		return null;
	}
	
	private PTree paramTypeList() {
		return null;
	}
	
	private PTree paramIdList() {
		return null;
	}
	
	private PTree paramId() {
		return null;
	}
	
	private PTree statementList() {
		return null;
	}
	
	private PTree expressionStmt() {
		return null;
	}
	
	private PTree selectionStmt() {
		return null;
	}
	
	private PTree iterationStmt() {
		return null;
	}
	
	private PTree breakStmt() {
		return null;
	}
	
	private PTree expression() {
		return null;
	}
	
	private PTree simpleExpression() {
		return null;
	}
	
	private PTree andExpression() {
		return null;
	}
	
	private PTree unaryRelExpression() {
		return null;
	}
	
	private PTree relExpression() {
		return null;
	}
	
	private PTree relop() {
		return null;
	}
	
	private PTree sumExpression() {
		return null;
	}
	
	private PTree sumop() {
		return null;
	}
	
	private PTree term() {
		return null;
	}
	
	private PTree mulop() {
		return null;
	}
	
	private PTree unaryExpression() {
		return null;
	}
	
	private PTree unaryop() {
		return null;
	}
	
	private PTree factor() {
		return null;
	}
	
	private PTree mutable() {
		return null;
	}
	
	private PTree immutable() {
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
