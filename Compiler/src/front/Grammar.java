package front;

import java.util.List;

public class Grammar {
	
	Grammar(List<Token> tokens){
		program(tokens);
	}
	
	private boolean program(List<Token> tokens) {
		if(tokens.isEmpty()) {
			return false;
		} else if(tokens.size() == 1) {
			return declaration(tokens);
		} else {
			return declarationList(tokens);
		}
	}
	
	private boolean declarationList(List<Token> tokens) {
		if(tokens.isEmpty()) {
			return false;
		} else if(tokens.size() == 1) {
			return declaration(tokens);
		} else{
			return declarationList(tokens); 
		}
	}
	
	private boolean declaration(List<Token> tokens) {
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
	
	private boolean typeSpecifier() {
		return false;
	}
	
	private boolean returnTypeSpecifier() {
		return false;
	}
	
	private boolean funDeclaration() {
		return false;
	}
	
	private boolean params() {
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
	
	private boolean statement() {
		return false;
	}
	
	private boolean compoundStmt() {
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
	
	private boolean returnStmt() {
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
