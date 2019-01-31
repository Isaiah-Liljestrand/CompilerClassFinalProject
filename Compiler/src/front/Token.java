package front;

public class Token {
	String token;
	int type;
	
	Token(){
		
	}
	Token(String token){
		this.token = token;
		this.type = Interpret(token);
	}
	Token(String token, int type){
		this.token = token;
		this.type = type;
	}
	
	//need to do
	int Interpret(String Token){ //interpreting what each token is
		return 0;
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
}
