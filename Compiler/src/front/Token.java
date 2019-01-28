package front;

public class Token {
	String token;
	int type;
	Token left;
	Token right;
	
	Token(){
		
	}
	Token(String token){
		this.token = token;
	}
	Token(String token, int type){
		this.token = token;
		this.type = type;
	}
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Token getLeft() {
		return left;
	}
	public void setLeft(Token left) {
		this.left = left;
	}
	public Token getRight() {
		return right;
	}
	public void setRight(Token right) {
		this.right = right;
	}

}
