package com.cx.project.zhihudaliy.c;

/**
 * 
 * @description  ʹ��DNK��װ�Ľӿ��� <br />
 * @author cx  <br />
 * Created on 2014-12-1����1:19:20
 *
 */
public class API {
	static{
		System.loadLibrary("api");
	}
	/**
	 * ����
	 * @return
	 */
	public static native String getThemesUrl();
	
	/**
	 * ��ӭͼƬ
	 * @return
	 */
	public static native String getStartImageUrl();
	
	/**
	 * ��������
	 * @return
	 */
	public static native String getLatestUrl();
	
	/**
	 * ��ʷ����<br />
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getBefore(), "20141201");
	 * @return
	 */
	public static native String getBeforeUrl();
	
	/**
	 * �����б�<br />
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getTheme(), "11");
	 * @return
	 */
	public static native String getTheme();
	
	/**
	 * ��������
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getNewsDetail(), "4353728");
	 * @return
	 */
	public static native String getNewsDetail();
	
	/**
	 * ��������������Ŀ�͵�����
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getNewsDetailExtra(), "4353728");
	 * @return
	 */
	public static native String getNewsDetailExtra();
}
