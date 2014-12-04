package com.cx.project.zhihudaliy.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TopStory extends AbstractStory {
	private String image;
	
	public TopStory() {
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	/**
	 * 解析topStory ,使用该方法解析因为是静态了，一写一个方法就比较占用内存
	 * @param arrayTopStories  topStory 的Json数组对象
	 * @return  topStory 集合
	 */
	public static List<TopStory> parse(JSONArray arrayTopStories ){
		List<TopStory> topStories = null;
		// 封装TopStory
		try {
			if (arrayTopStories != null && arrayTopStories.length() > 0) {
				topStories = new ArrayList<TopStory>();
				for (int i = 0 ; i < arrayTopStories.length() ; i++) {
					JSONObject obj = arrayTopStories.getJSONObject(i);
					TopStory topStory = new TopStory();
					topStory.setGa_prefix(obj.getString("ga_prefix"));
					topStory.setId(obj.getLong("id"));
					topStory.setImage(obj.getString("image"));
					topStory.setShare_url(obj.getString("share_url"));
					topStory.setTitle(obj.getString("title"));
					topStory.setType(obj.getInt("type"));
					topStories.add(topStory);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return topStories;
		
	}

}
