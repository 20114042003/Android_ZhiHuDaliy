package com.cx.project.zhihudaliy.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * �໬�˵���ʹ��
 * @author CxiaoX
 *
 * 2014��12��5������10:33:08
 */
public class Subscribed {
	
	
	public static List<Subscribed> parse(JSONArray arraySubscribed){
		List<Subscribed> list=null;
		
		
			if(arraySubscribed!=null && arraySubscribed.length()>0){
				list =new ArrayList<Subscribed>();
				
			}
		
		
		return list;
		
	}

}
