package com.cx.project.zhihudaliy.entity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主题类的新闻
 * @author CxiaoX
 *
 * 2014年12月5日上午10:30:19
 */
public class ThemeNew {
	private List<Story> stories;
	private String description;
	private String background;
	private int color;
	private String name;
	private String image;
	private List<Editor> editors;
	private String image_source;
	public List<Story> getStories() {
		return stories;
	}
	public void setStories(List<Story> stories) {
		this.stories = stories;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<Editor> getEditors() {
		return editors;
	}
	public void setEditors(List<Editor> editors) {
		this.editors = editors;
	}
	public String getImage_source() {
		return image_source;
	}
	public void setImage_source(String image_source) {
		this.image_source = image_source;
	}
	
	
	public static ThemeNew parse(JSONObject obj){
		ThemeNew themeNew = null;
		
		if(obj!=null){
			themeNew = new ThemeNew();
			try {
				themeNew.setStories(Story.parse( obj.getJSONArray("stories")));
				themeNew.setDescription(obj.getString("description"));
				themeNew.setBackground(obj.getString("background"));
				themeNew.setColor(obj.getInt("color"));
				themeNew.setName(obj.getString("name"));
				themeNew.setImage(obj.getString("image"));
				themeNew.setEditors(Editor.parse(obj.getJSONArray("editors")));
				themeNew.setImage_source(obj.getString("image_source"));
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		return themeNew;
		
	}
	

}
