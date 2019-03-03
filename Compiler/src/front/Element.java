package front;

public class Element extends Token {
	
	/**
	 * incase the element has a value (num constant)
	 */
	protected Integer value;

	Element(type_enum type, String token) {
		super(type);
		this.token = token;
		if(isNumeric(token)){ //If token is a num constant
			this.value = Integer.parseInt(token);
		}
	}
	
	Element(type_enum type, String token, int value) {
		super(type);
		this.token = token;
		this.value = value;
	}
	
	Element(Token token){
		super(token.type);
		this.token = token.token;
		if(isNumeric(token.token)){ //If token is a num constant
			this.value = Integer.parseInt(token.token);
		}
	}
	
	public static boolean isNumeric(String str) { 
		try {  
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}
	}
}
