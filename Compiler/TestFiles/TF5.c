int foo(int i){
  i = i + bar();
  return i + 1;
}

int bar(){
  return 2
}

int main(){
  int i = 1;
  i = foo(i);
  return i;
}