package com.cx.project.zhihudaliy.c;

/**
 * 
 * @description  使用DNK封装的接口类 <br />
 * @author cx  <br />
 * Created on 2014-12-1下午1:19:20
 *
 */
public class API {
	static{
		System.loadLibrary("api");
	}
	/**
	 * 主题
	 * @return
	 */
	public static native String getThemesUrl();
	
	/**
	 * 欢迎图片
	 * @return
	 */
	public static native String getStartImageUrl();
	
	/**
	 * 今日热闻
	 * @return
	 */
	public static native String getLatestUrl();
	
	/**
	 * 历史新闻<br />
	 * 返回的字符串需要使用String.format();方法格式化<br />
	 * 例如：String.format(API.getBefore(), "20141201");
	 * @return
	 */
	public static native String getBeforeUrl();
}
