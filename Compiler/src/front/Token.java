package front;

public class Token {
	String token;
	int type;
	
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
}
