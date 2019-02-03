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
		equalsOperator,
		incrementOperator,
		decrementOperator,
		subtractionAssignmentOperator,
		additionAssignmentOperator,
		multiplicationAssignmentOperator,
		divisionAssignmentOperator,

		//logic operators
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
		variable,
		identifier,
		function,
	};
	
	
	Token(){
		setToken("");
	}
	Token(String token){
		this.token = token;
	}
	Token(String token, type_enum type){
		this.token = token;
		this.type = type;
	}
	
	public void addTokenIdentity() {
		char t = this.token.charAt(0);
		switch(t) {
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
			this.type = type_enum.notOperator;
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
		String keywords[] = {"auto", "break"};//...
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
