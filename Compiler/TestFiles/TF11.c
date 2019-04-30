int foo1(int a, int b){
	return a + b;
}

int foo2(int a, int b){
	int x = foo2(a, b);
	
	return x;
}

int foo3(int a, int b, int c, int d){
	int x = a + b;
	int y = c - d;
	
	return foo2(x, y);
}

int main(){
	return foo3(10, 5, 20, 3);
}
	