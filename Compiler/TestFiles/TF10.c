int foo(int a, int b){

	if(a > 0){
		a = a - b;
	} else{
		foo(a, b);
	}
	
	return a + b;
}

int main(){

	return foo(100, 10);
	
}