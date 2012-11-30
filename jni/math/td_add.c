int td_add(int x,int y){
	int result = x+y;
	for(int i=0;i<12;i++){
		result +=i;
	}
	return result;
}
