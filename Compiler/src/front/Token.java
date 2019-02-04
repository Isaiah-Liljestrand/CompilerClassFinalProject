package front;

public class Token {
	String token;
	type_enum type;
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
		openSquareBracket,
		closedSquareBracket,
		openParenthesis,
		closedParenthesis,
		semicolon,
		
		//Words ???? I don't know what to call this section
		//keyword needs to be split up into sections such as identifier, and loops and such
		variable,
		number,
		function,
		keyword
	};
	
	
	Token(){
		setToken("");
	}
	Token(String token){
		this.token = token;
		this.type = Interpret(token);
	}
	Token(String token, type_enum type){
		this.token = token;
		this.type = type;
	}

	type_enum Interpret(String Token){ //interpreting what each token is
		return type_enum.keyword;
	}

	public void addTokenIdentity() {
		this.type = null;
		char t = this.token.charAt(0);
		switch(t) {
		case ';':
			this.type = type_enum.semicolon;
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
		case '[':
			this.type = type_enum.openSquareBracket;
			break;
		case ']':
			this.type = type_enum.closedSquareBracket;
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
			if(this.token.contentEquals("&&")) {
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
		String keywords[] = {"auto", "break", "case", "char", "const", "continue",
				"default", "do", "double", "else", "enum", "extern", "float", "for",
				"goto", "if", "int", "long", "register", "return", "short", "signed",
				"sizeof", "static", "struct", "switch", "typedef", "union", "unsigned",
				"void", "volatile", "while"};
		for(String keyword : keywords) {
			if(keyword.equals(this.token)) {
				//intent to make enum of every keyword individually and account for it here
				this.type = type_enum.keyword;
				return;
			}
		}
		//could be written better, temporary fix
		if(this.token.charAt(0) >= '0' && this.token.charAt(0) <= '9') {
			this.type = type_enum.number;
			if(this.token.subSequence(0, 1).equals("0x")) {
				//todo: convert hex number to decimal form
			}
			return;
		}
		//function support to be added
		this.type = type_enum.variable;
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
