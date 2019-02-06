package front;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
	private boolean valid;
	
	Grammar(List<Token> tokens){ // Essentially the first few parts of the grammar
		List<Token> Feed = new ArrayList<Token>();
		
		if(tokens.get(0).getType() == Token.type_enum.keyword) { //This block of ifs only checks for Int Main() at the moment
			if(tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.openParenthesis) {
				if(tokens.get(3).getType() == Token.type_enum.closedParenthesis && tokens.get(4).getType() == Token.type_enum.openCurlyBracket) {
					while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
						Feed.add(tokens.remove(0));
						if(tokens.isEmpty()) {
							System.out.println("Missing Closing Curly Brace");
						    System.out.println("False");
							return;
						}
					}
					Feed.add(tokens.remove(0));
					//for( Token tok : Feed) {
					//	System.out.println(tok.getToken());
					//}
					valid = funDeclaration(Feed);
					if(valid == true) {
						System.out.println("\n\nValid Grammar");
					}else {
						System.out.print(valid);
					}
				} else if(tokens.get(3).getType() != Token.type_enum.closedParenthesis) {
					System.out.println("Missing closing Parenthesis");
					System.out.println("False");
					return;
				}
			}
		}
	}
	
	private boolean funDeclaration(List<Token> tokens) { 
		List<Token> Feed1 = new ArrayList<Token>();
		List<Token> Feed2 = new ArrayList<Token>();
		List<Token> typeSpec = new ArrayList<Token>();
		List<Token> ID = new ArrayList<Token>();
		List<Token> params = new ArrayList<Token>();
		
		System.out.println("funDeclaration");
		
		if(tokens.get(0).getType() == Token.type_enum.keyword) {
			if(tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.openParenthesis) {
				if(tokens.get(3).getType() == Token.type_enum.closedParenthesis && tokens.get(4).getType() == Token.type_enum.openCurlyBracket) {
					while(tokens.get(0).getType() != Token.type_enum.closedParenthesis) {
						Feed1.add(tokens.remove(0));
						if(Feed1.isEmpty()) {
							System.out.println("Missing Closed Parenthisis");
							System.out.println("funDeclaration is false");
							return false;
						}
					}
					Feed1.add(tokens.remove(0)); //The function declaration up to closed parenthesis E.G. int main()
					while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
						Feed2.add(tokens.remove(0));
						if(Feed2.isEmpty()) {
							System.out.println("Missing Closing Curly Brace");
							System.out.println("funDeclaration is false");
							System.out.println("False");
							return false;
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
							return false;
						}
					}
					params.add(Feed1.remove(0));
				}
				return typeSpecifier(typeSpec) && IDfunc(ID) && params(params) && statement(Feed2);
			} else {
				ID.add(Feed1.remove(0));
				if(Feed1.get(0).getType() == Token.type_enum.openParenthesis) {
					while(Feed1.get(0).getType() != Token.type_enum.closedParenthesis) {
						params.add(Feed1.remove(0));
						if(Feed1.isEmpty()) {
							System.out.println("Missing Closed Parenthisis");
							System.out.println("funDeclaration is false");
							return false;
						}
					}
					params.add(Feed1.remove(0));
				}
				return IDfunc(ID) && params(params) && statement(Feed2);
			}
		}
		
		System.out.println("funDeclaration is false");
		return false; 
	}
	
	private boolean statement(List<Token> tokens) {
		System.out.println("statement");
		List<Token> Feed = new ArrayList<Token>();
		if(tokens.get(0).getType() == Token.type_enum.openCurlyBracket) {
			while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
				if(tokens.isEmpty()) {
					System.out.println("Missing Closed curly bracket");
					System.out.println("statement is false");
					return false;
				}
				Feed.add(tokens.remove(0));
			}
			Feed.add(tokens.remove(0));
			return compoundStmt(Feed);
		}
		
		System.out.println("statement is false");
		return false;
	}
	
	private boolean compoundStmt(List<Token> tokens) { // Checks for rtn statement
		System.out.println("compoundStmt");
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
				return false;
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
		
		return localDeclarations(tokens) && returnStmt(rtnStmt);
	}
	
	private boolean localDeclarations(List<Token> tokens) { 
		System.out.println("localDeclarations");
		//I'm about to just handle this for the basic program only, so this is gonna need further work
		if(tokens.size() == 3) {
			if(tokens.get(0).getType() == Token.type_enum.keyword && tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.semicolon) {
				return true;
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
		return false;
	}
	
	private boolean returnStmt(List<Token> tokens) {
		System.out.println("returnStmt");
		if(tokens.size() == 2) {
			if(tokens.get(0).getToken().contentEquals("return") && tokens.get(1).getType() == Token.type_enum.semicolon) {
				return true;
			}
		}
		if(tokens.size() == 3) {
			if(tokens.get(0).getToken().contentEquals("return") && tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.semicolon) {
				return true; // This actually needs to check the expression which would be the middle token
			}
		}
		System.out.println("returnStmt is false");
		return false;
	}
		
	private boolean IDfunc(List<Token> tokens) {
		System.out.println("IDfunc");
		if(tokens.size() == 1) {
			if(tokens.get(0).getType() == Token.type_enum.identifier) {
				return true;
			} else {
				System.out.println("IDfunc is false");
				return false;
			}
		}
		System.out.println("IDfunc is false");
		return false;
	}
	
	private boolean typeSpecifier(List<Token> tokens) {
		System.out.println("typeSpecifier");
		if(tokens.size() == 1) {
			if(tokens.get(0).getToken().contentEquals("int") | tokens.get(0).getToken().contentEquals("bool") | tokens.get(0).getToken().contentEquals("char") | tokens.get(0).getToken().contentEquals("float")) {
				return true;
			}
		}
		System.out.println("typeSpecifier is false");
		return false;
	}
	
	private boolean params(List<Token> tokens) { // Only handles basic program right now
		System.out.println("params");
		//for( Token tok : tokens) {
		//	System.out.println(tok.getToken());
		//}
		if(tokens.size() == 2) {
			if(tokens.get(0).getType() == Token.type_enum.openParenthesis && tokens.get(1).getType() == Token.type_enum.closedParenthesis) {
				return true;
			}
		}
		System.out.println("params is false");
		return false;
	}
	
	private boolean recDeclaration() {
		return false;
	}
	
	private boolean varDeclaration() {
		return false;
	}
	
	private boolean scopedVarDeclaration() {
		return false;
	}
	
	private boolean varDecList() {
		return false;
	}
	
	private boolean varDecInitialize() {
		return false;
	}
	
	private boolean varDecId() {
		return false;
	}
	
	private boolean scopedTypeSpecifier() {
		return false;
	}
	
	private boolean returnTypeSpecifier() {
		return false;
	}
	
	private boolean paramList() {
		return false;
	}
	
	private boolean paramTypeList() {
		return false;
	}
	
	private boolean paramIdList() {
		return false;
	}
	
	private boolean paramId() {
		return false;
	}
	
	private boolean statementList() {
		return false;
	}
	
	private boolean expressionStmt() {
		return false;
	}
	
	private boolean selectionStmt() {
		return false;
	}
	
	private boolean iterationStmt() {
		return false;
	}
	
	private boolean breakStmt() {
		return false;
	}
	
	private boolean expression() {
		return false;
	}
	
	private boolean simpleExpression() {
		return false;
	}
	
	private boolean andExpression() {
		return false;
	}
	
	private boolean unaryRelExpression() {
		return false;
	}
	
	private boolean relExpression() {
		return false;
	}
	
	private boolean relop() {
		return false;
	}
	
	private boolean sumExpression() {
		return false;
	}
	
	private boolean sumop() {
		return false;
	}
	
	private boolean term() {
		return false;
	}
	
	private boolean mulop() {
		return false;
	}
	
	private boolean unaryExpression() {
		return false;
	}
	
	private boolean unaryop() {
		return false;
	}
	
	private boolean factor() {
		return false;
	}
	
	private boolean mutable() {
		return false;
	}
	
	private boolean immutable() {
		return false;
	}
	
	private boolean call() {
		return false;
	}
	
	private boolean args() {
		return false;
	}
	
	private boolean argList() {
		return false;
	}
	
	private boolean constant() {
		return false;
	}
	
	public boolean getValid() {
		return this.valid;
	}
}
