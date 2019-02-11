package front;

public class Token {
	String token;
	type_enum type;
	int lineNumber;
	enum type_enum {
		
		//basic arithmatic
		additionOperator,
		subtractionOperator,
		divisionOperator,
		multiplicationOperator,
		modulusOperator,

		//assignment arithmatic
		assignmentOperator,
		incrementOperator,
		decrementOperator,
		subtractionAssignmentOperator,
		additionAssignmentOperator,
		multiplicationAssignmentOperator,
		divisionAssignmentOperator,

		//logic operators
		notEqualOperator,
		equalsOperator,
		orLogicOperator,
		andLogicOperator,
		
		//binary operations
		notOperator,
		orOperator,
		andOperator,
		xorOperator,
		
		//brackets and semicolon
		openCurlyBracket,
		closedCurlyBracket,
		openParenthesis,
		closedParenthesis,
		semicolon,
		colon,
		comma,
		epsilon,
		
		//identifiers and keywords
		number,
		identifier,
		typeSpecifier,
		
		//keywords
		k_break,
		k_char,
		k_else,
		k_goto,
		k_if,
		k_int,
		k_return,
		k_void,
		k_while,
		
		//Nodes of parse tree
		program,
		declarationList,
		declaration,
		functionDeclaration,
		variableDeclaration,
		variableDeclarationList,
		variableDeclarationInitialize,
		parameterList,
		parameter,
		statement
	};
	
	
	Token(type_enum type) {
		this.type = type;
	}
	
	Token(String token, int lineNumber){
		this.token = token;
		this.lineNumber = lineNumber;
		this.addTokenIdentity();
	}


	private void addTokenIdentity() {
		this.type = null;
		char t = this.token.charAt(0);
		switch(t) {
		case ';':
			this.type = type_enum.semicolon;
			break;
		case ':':
			this.type = type_enum.colon;
		case ',':
			this.type = type_enum.comma;
			break;
		case '(':
			this.type = type_enum.openParenthesis;
			break;
		case ')':
			this.type = type_enum.closedParenthesis;
			break;
		case '{':
			this.type = type_enum.openCurlyBracket;
			break;
		case '}':
			this.type = type_enum.closedCurlyBracket;
			break;
		case '+':
			if(this.token.equals("+=")) {
				this.type = type_enum.additionAssignmentOperator;
			} else if(this.token.equals("++")) {
				this.type = type_enum.incrementOperator;
			} else {
				this.type = type_enum.additionOperator;
			}
			break;
		case '-':
			if(this.token.equals("-=")) {
				this.type = type_enum.subtractionAssignmentOperator;
			} else if(this.token.equals("--")) {
				this.type = type_enum.decrementOperator;
			} else {
				this.type = type_enum.subtractionOperator;
			}
			break;
		case '*':
			if(this.token.equals("*=")) {
				this.type = type_enum.multiplicationAssignmentOperator;
			} else {
				this.type = type_enum.multiplicationOperator;
			}
			break;
		case '/':
			if(this.token.equals("/=")) {
				this.type = type_enum.divisionAssignmentOperator;
			} else {
				this.type = type_enum.divisionOperator;
			}
			break;
		case '%':
			this.type = type_enum.modulusOperator;
			break;
		case '=':
			if(this.token.equals("==")) {
				this.type = type_enum.equalsOperator;
			} else {
				this.type = type_enum.assignmentOperator;
			}
			break;
		case '!':
			if(this.token.equals("!=")) {
				this.type = type_enum.notEqualOperator;
			} else {
				this.type = type_enum.notOperator;
			}
			break;
		case '^':
			this.type = type_enum.xorOperator;
			break;
		case '|':
			if(this.token.equals("||")) {
				this.type = type_enum.orLogicOperator;
			} else {
				this.type = type_enum.orOperator;
			}
			break;
		case '&':
			if(this.token.equals("&&")) {
				this.type = type_enum.andLogicOperator;
			} else {
				this.type = type_enum.andOperator;
			}
			break;
		}
		if(this.type != null) {
			return;
		}
		//list will be shortened at some point as many of these won't be used
		//String keywords[] = {/*"auto",*/ "break", /*"case",*/ "char", /*"const", "continue",*/
		//		/*"default", "do", "double",*/ "else", /*"enum", "extern", "float", "for",*/
		//		"goto", "if", "int", /*"long", "register", */"return", /*"short", "signed",*/
		//		/*"sizeof", "static", "struct", "switch", "typedef", "union", "unsigned",*/
		//		"void", /*"volatile",*/ "while"};
		switch(this.token) {
		case "break":
			this.type = type_enum.k_break;
			break;
		case "char":
			this.type = type_enum.k_char;
			break;
		case "goto":
			this.type = type_enum.k_goto;
			break;
		case "if":
			this.type = type_enum.k_if;
			break;
		case "int":
			this.type = type_enum.k_int;
			break;
		case "return":
			this.type = type_enum.k_return;
			break;
		case "void":
			this.type = type_enum.k_void;
			break;
		case "while":
			this.type = type_enum.k_while;
		}
		//could be written better, temporary fix
		if(this.token.charAt(0) >= '0' && this.token.charAt(0) <= '9') {
			this.type = type_enum.number;
			if(this.token.subSequence(0, 1).equals("0x")) {
				this.token = String.valueOf(Integer.parseInt(this.token, 16));
			}
			return;
		}
		//function or variable
		this.type = type_enum.identifier;
	}
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public type_enum getType() {
		return type;
	}
	public void setType(type_enum type) {
		this.type = type;
	}
}
