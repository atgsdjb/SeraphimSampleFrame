#include <string.h>
#include <jni.h>
#include<android/log.h>
#include<td_math.h>
#include<x264.h>
int main(int argc,char** argv){
	int result;
	result = td_add(12,13);
	result = td_sub(12,11);
	result = td_mul(12,13);
	for(int i = 0;i<100;i++){
		i += i;
	}
	x264_t *xt;
	xt = x264_encoder_open((x264_param_t*)0);
	return 0;
}
