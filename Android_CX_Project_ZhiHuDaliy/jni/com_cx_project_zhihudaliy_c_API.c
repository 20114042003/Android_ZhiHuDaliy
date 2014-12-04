#include "com_cx_project_zhihudaliy_c_API.h"

JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getThemesUrl
  (JNIEnv *env, jclass thiz){

	return  (*env)->NewStringUTF(env,"http://news-at.zhihu.com/api/3/themes");
}

JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getStartImageUrl
  (JNIEnv *env, jclass thiz){
	return (*env)->NewStringUTF(env, "http://news-at.zhihu.com/api/3/start-image/480*728");
}

JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getLatestUrl
  (JNIEnv *env, jclass thiz){
	return (*env)->NewStringUTF(env,"http://news-at.zhihu.com/api/3/stories/latest");
}

JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getBeforeUrl
  (JNIEnv *env, jclass thiz){
	return (*env)->NewStringUTF(env,"http://news-at.zhihu.com/api/3/stories/before/%s");
}

JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getTheme
  (JNIEnv *env, jclass thiz){
	return (*env)->NewStringUTF(env,"http://news-at.zhihu.com/api/3/theme/%s");
}


