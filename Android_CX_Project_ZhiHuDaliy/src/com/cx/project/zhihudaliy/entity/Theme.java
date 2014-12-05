package com.cx.project.zhihudaliy.entity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 侧滑菜单使用
 * @author CxiaoX
 *
 * 2014年12月5日上午10:27:09
 */
public class Theme {
	private int limit;
	private List<Subscribed> subscribed;
	private List<Others> others;
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public List<Subscribed> getSubscribed() {
		return subscribed;
	}
	public void setSubscribed(List<Subscribed> subscribed) {
		this.subscribed = subscribed;
	}
	public List<Others> getOthers() {
		return others;
	}
	public void setOthers(List<Others> others) {
		this.others = others;
	}
	
	public static Theme parse(JSONObject obj){
		Theme theme=null;
		
		try {
			theme = new Theme();
			theme.setLimit(obj.getInt("limit"));
			theme.setOthers(Others.parse(obj.getJSONArray("others")));
			theme.setSubscribed(Subscribed.parse(obj.getJSONArray("subscribed")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return theme;
		
	}

}
