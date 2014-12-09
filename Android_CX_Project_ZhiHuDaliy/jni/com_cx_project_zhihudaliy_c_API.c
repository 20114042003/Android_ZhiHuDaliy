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

JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getNewsDetail
  (JNIEnv *env, jclass thiz){
	return (*env)->NewStringUTF(env,"http://news-at.zhihu.com/api/3/story/%s");
}

JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getNewsDetailExtra
  (JNIEnv *env, jclass thiz ){
	return (*env)->NewStringUTF(env,"http://news-at.zhihu.com/api/3/story-extra/%s");
}


JNIEXPORT jstring JNICALL Java_com_cx_project_zhihudaliy_c_API_getUpdataUrl
  (JNIEnv *env, jclass thiz){
	return (*env)->NewStringUTF(env,"http://10.2.156.39:8080/apk/zhihudaliy/updata.xml");

}
