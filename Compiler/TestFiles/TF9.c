int math(int a, int b, int c){
	return b + b - a;
}

int loop(int a, int b){
	int i = 10;
	int c = 12;
	while(i > 0){
		c = math(a, b, c);
		i--;
	}
	
	return c;
}

int main(){
	int i = 0;
	int a = 2;
	int b = 3;
	while(i < 10){
		a = loop(a, b);
		i++;
	}
	
	return a;
}