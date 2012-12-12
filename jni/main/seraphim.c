#include <jni.h>
#include<android/log.h>
#include<x264.h>
#ifdef SERAPHIM_TEST_X264
#include<td_math.h>


#endif
int main(int argc,char** argv){
	int result;
	result = td_add(12,13);
	result = td_sub(12,11);
	result = td_mul(12,13);
	for(int i = 0;i<100;i++){
		i += i;
	}
#ifdef SERAPHIM_TEST_X264
	x264_t *xt;
	xt = x264_encoder_open((x264_param_t*)0);
#endif
	return 0;
}
