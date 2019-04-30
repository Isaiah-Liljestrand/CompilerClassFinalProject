int main(){
	int i = 0;
	int y = 10;
	
	int a, b, c, d;
	
	a = 10;
	b = 3;
	c = 5;
	d = 20;
	
	while(i < 10){
		if(i > 5){
			y = 10;
			while(y > 0){
				c = c + d;
				y--;
			}
		} else{
			while(y > 0){
				a = a + b;
				y--;
			}
			y = 10;
		}
		i++;
	}
	
	return a + c;
}