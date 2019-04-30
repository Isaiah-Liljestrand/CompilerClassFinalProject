int main(){
	int i = 0;
	int y = 10;
	int a, b, c, d;
	
	a = 5;
	b = 10;
	c = 2;
	d = 4;
	
	while(i < 10){
		a = a + b;
		
		while(y > 0){
			c = d + a;
			y--;
		}
		y = 10;
		b = a + c;
		i++;
	}
	
	return b;
}