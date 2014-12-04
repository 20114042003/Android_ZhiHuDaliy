package com.cx.project.zhihudaliy.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Editor {
	private int id;
	private String avatar;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 解析Editor 
	 * @param arrayEditor json数组对象
	 * @return Editor数组
	 */
	public static List<Editor> parse(JSONArray arrayEditor){
		List<Editor> editors = null;
		
		try {
			if(arrayEditor!=null && arrayEditor.length()>0){
				editors = new ArrayList<Editor>();
				for(int i=0;i<arrayEditor.length();i++){
					JSONObject obj = arrayEditor.getJSONObject(i);
					Editor editor = new Editor();
					
					editor.setId(obj.getInt("id"));
					editor.setAvatar(obj.getString("avatar"));
					editor.setName(obj.getString("name"));
					
					editors.add(editor);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return editors;
		
	}
	

}
