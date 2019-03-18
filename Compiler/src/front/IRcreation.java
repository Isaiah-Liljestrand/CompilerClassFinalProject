package front;

public class IRcreation {
	public void createIR(Ptree tree, SymbolTable table) {
		declarationHandler(tree, table);
		functionHandler(tree, table, true);
		whileHandler(tree, table);
		forHandler(tree, table);
		ifHandler(tree, table);
		statementHandler(tree, table);
		expressionHandler(tree, table);
	}
	
	private void declarationHandler(Ptree tree, SymbolTable table) {
		
	}
	
	private void functionHandler(Ptree tree, SymbolTable table, boolean isVoid) {
		
	}
	
	private void whileHandler(Ptree tree, SymbolTable table) {
		
	}
	
	private void forHandler(Ptree tree, SymbolTable table) {
		
	}
	
	private void ifHandler(Ptree tree, SymbolTable table) {
		
	}
	
	private void statementHandler(Ptree tree, SymbolTable table) {
		
	}
	
	private void expressionHandler(Ptree tree, SymbolTable table) {
		
	}
}
