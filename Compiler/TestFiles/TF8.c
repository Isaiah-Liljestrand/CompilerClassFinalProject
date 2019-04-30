int math(int a, int b){
	return (a + b) / b;
}

int main(){
	int i = 0;
	int x = 2;
	int y = 3;
	
	while(i < 10){
		y = math(x, y);
		i++;
	}
	
	return y;
}