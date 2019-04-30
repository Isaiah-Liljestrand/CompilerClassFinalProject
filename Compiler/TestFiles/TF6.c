foo(int i){
  return i + 1;
}

int main(){
  int i = 2;
  while(i < 5){
    i = foo(i);
  }
  return i;
}
