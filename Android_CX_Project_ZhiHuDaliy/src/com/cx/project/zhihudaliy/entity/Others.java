package com.cx.project.zhihudaliy.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 侧滑菜单中的主题的项
 * @author CxiaoX
 *
 * 2014年12月5日上午10:32:36
 */
public class Others {
	private int color;
	private String image;
	private String description;
	private int id;
	private String  name;
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static List<Others> parse(JSONArray arrayOthers){
		List<Others> othersList=null;
		
		try {
			if(arrayOthers!=null && arrayOthers.length()>0){
				othersList =new ArrayList<Others>();
				
				//手动加一个首页
				Others others = new Others();
				others.setName("首页");
				othersList.add(others);
				
				for(int i=0;i<arrayOthers.length();i++){
					JSONObject obj = arrayOthers.getJSONObject(i);
					others = new Others();
					others.setColor(obj.getInt("color"));
					others.setImage(obj.getString("image"));
					others.setDescription(obj.getString("description"));
					others.setId(obj.getInt("id"));
					others.setName(obj.getString("name"));
					
					othersList.add(others);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return othersList;
		
	}

}
