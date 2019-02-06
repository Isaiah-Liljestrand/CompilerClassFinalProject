package front;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
	private boolean valid;
	
	Grammar(List<Token> tokens){ // Essentially the first few parts of the grammar
		List<Token> Feed = new ArrayList<Token>();
		
		if(tokens.get(0).getType() == Token.type_enum.identifier) {
			if(tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.openParenthesis) {
				if(tokens.get(3).getType() == Token.type_enum.closedParenthesis && tokens.get(4).getType() == Token.type_enum.openCurlyBracket) {
					while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
						Feed.add(tokens.remove(0));
					}
					Feed.add(tokens.remove(0));
					valid = funDeclaration(Feed);
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
		
		if(tokens.get(0).getType() == Token.type_enum.identifier) {
			if(tokens.get(1).getType() == Token.type_enum.identifier && tokens.get(2).getType() == Token.type_enum.openParenthesis) {
				if(tokens.get(3).getType() == Token.type_enum.closedParenthesis && tokens.get(4).getType() == Token.type_enum.openCurlyBracket) {
					while(tokens.get(0).getType() != Token.type_enum.closedParenthesis) {
						Feed1.add(tokens.remove(0));
					}
					Feed1.add(tokens.remove(0)); //The function declaration up to closed paranthesis E.G. int main()
					while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
						Feed2.add(tokens.remove(0));
					}
					Feed2.add(tokens.remove(0)); //The statement for { to }
				}
			}
		}
		
		if(!Feed1.isEmpty()) {
			if(Feed1.get(0).getType() == Token.type_enum.identifier && Feed1.get(1).getType() == Token.type_enum.identifier && Feed1.get(2).getType() == Token.type_enum.openParenthesis) {
				typeSpec.add(Feed1.remove(0));
				ID.add(Feed1.remove(0));
				if(Feed1.get(0).getType() == Token.type_enum.openParenthesis) {
					while(Feed1.get(0).getType() != Token.type_enum.closedParenthesis) {
						params.add(Feed1.remove(0));
					}
					params.add(Feed1.remove(0));
				}
				return typeSpecifier(typeSpec) && IDfunc(ID) && params(params) && statement(Feed2);
			} else {
				ID.add(Feed1.remove(0));
				if(Feed1.get(0).getType() == Token.type_enum.openParenthesis) {
					while(Feed1.get(0).getType() != Token.type_enum.closedParenthesis) {
						params.add(Feed1.remove(0));
					}
					params.add(Feed1.remove(0));
				}
				return IDfunc(ID) && params(params) && statement(Feed2);
			}
		}
		
		return false; 
	}
	
	private boolean statement(List<Token> tokens) {
		List<Token> Feed = new ArrayList<Token>();
		if(tokens.get(0).getType() == Token.type_enum.openCurlyBracket) {
			while(tokens.get(0).getType() != Token.type_enum.closedCurlyBracket) {
				Feed.add(tokens.remove(0));
			}
			Feed.add(tokens.remove(0));
			return compoundStmt(Feed);
		}
		
		return false;
	}
	
	private boolean compoundStmt(List<Token> tokens) {
		List<Token> rtnStmt = new ArrayList<Token>();
		int i = 0;
		
		while(tokens.get(i).getToken() != "return") {
			i++;
		}
		while(tokens.get(i).getType() != Token.type_enum.semicolon) {
			rtnStmt.add(tokens.remove(i));
		}
		rtnStmt.add(tokens.remove(i));
		
		
		
		return returnStmt(rtnStmt);
	}
	
	private boolean returnStmt(List<Token> tokens) {
		return false;
	}
		
	private boolean IDfunc(List<Token> tokens) {
		if(tokens.size() == 1) {
			if(tokens.get(0).getType() == Token.type_enum.identifier) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	private boolean typeSpecifier(List<Token> tokens) {
		return false;
	}
	
	private boolean params(List<Token> tokens) {
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
	
	private boolean localDeclarations() {
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
}
