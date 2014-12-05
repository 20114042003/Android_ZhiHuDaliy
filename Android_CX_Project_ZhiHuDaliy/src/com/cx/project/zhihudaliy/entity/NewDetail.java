package com.cx.project.zhihudaliy.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewDetail {
	private String body;
	private String image_source; 
	private String title;  
	private int editor_id;  
	private String image;  
	private String editor_avatar;  
	private String share_url;  
	private List<String> js;  
	private String ga_prefix;  
	private String editor_name;  
	private int type;  
	private int id;  
	private List<String> css;
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getImage_source() {
		return image_source;
	}
	public void setImage_source(String image_source) {
		this.image_source = image_source;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getEditor_id() {
		return editor_id;
	}
	public void setEditor_id(int editor_id) {
		this.editor_id = editor_id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getEditor_avatar() {
		return editor_avatar;
	}
	public void setEditor_avatar(String editor_avatar) {
		this.editor_avatar = editor_avatar;
	}
	public String getShare_url() {
		return share_url;
	}
	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}
	public List<String> getJs() {
		return js;
	}
	public void setJs(List<String> js) {
		this.js = js;
	}
	public String getGa_prefix() {
		return ga_prefix;
	}
	public void setGa_prefix(String ga_prefix) {
		this.ga_prefix = ga_prefix;
	}
	public String getEditor_name() {
		return editor_name;
	}
	public void setEditor_name(String editor_name) {
		this.editor_name = editor_name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<String> getCss() {
		return css;
	}
	public void setCss(List<String> css) {
		this.css = css;
	}  
	
	public static NewDetail parse(JSONObject obj){
		NewDetail newDetail =null;
		
		try {
			if(obj !=null){
				newDetail = new NewDetail();
				newDetail.setBody(obj.getString("body"));
				newDetail.setImage_source(obj.getString("image_source"));
				newDetail.setTitle(obj.getString("title"));
				newDetail.setEditor_id(obj.getInt("editor_id"));
				newDetail.setImage(obj.getString("image"));
				newDetail.setEditor_avatar(obj.getString("editor_avatar"));
				newDetail.setShare_url(obj.getString("share_url"));
				newDetail.setGa_prefix(obj.getString("ga_prefix"));
				newDetail.setEditor_name(obj.getString("editor_name"));
				newDetail.setType(obj.getInt("type"));
				newDetail.setId(obj.getInt("id"));
				
				
				
				
				//½âÎöjs
				JSONArray jsArray =  obj.getJSONArray("js");
				if(jsArray!=null &&jsArray.length()>0)
				{
					List<String> jsList = new ArrayList<String>();
					for(int i=0;i<jsArray.length();i++)
					{
						jsList.add(jsArray.getString(i));
					}
					newDetail.setJs(jsList);
				}
				
				//½âÎöCss
				JSONArray cssArray = obj.getJSONArray("css");
				if(cssArray!=null &&cssArray.length()>0)
				{
					List<String> cssList = new ArrayList<String>();
					for(int i=0;i<cssArray.length();i++)
					{
						cssList.add(cssArray.getString(i));
					}
					newDetail.setCss(cssList);
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return newDetail;
	}
	   

}
