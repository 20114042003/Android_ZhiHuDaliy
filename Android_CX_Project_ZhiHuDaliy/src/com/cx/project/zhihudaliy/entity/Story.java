package com.cx.project.zhihudaliy.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.project.zhihudaliy.util.date.DateStyle;
import com.cx.project.zhihudaliy.util.date.DateUtil;



public class Story extends AbstractStory {
	private List<String> images;
	
	public Story() {
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
	
	public static List<Story> parse(News news,JSONArray arrayStories){
		List<Story> stories = null;
		stories = new ArrayList<Story>();
		
		//解析数据之前，先加标题进来。
				Story StoryTitle = new Story();
				if(news.getStories()==null){ //第一次加载
					StoryTitle.setTitle("今日热闻");
				}else{ //之前的新闻
					
					String date = news.getDate();
					String mmdd = DateUtil.StringToString(date, DateStyle.MM_DD_CN);
					String week = DateUtil.getWeek(date).getChineseName();
					StoryTitle.setTitle(String.format("%s  %s", mmdd,week));
				}
				stories.add(StoryTitle);
		
		try {
			if (arrayStories != null && arrayStories.length() > 0) {
				
				for (int i = 0 ; i < arrayStories.length() ; i++) {
					JSONObject obj = arrayStories.getJSONObject(i);
					Story story = new Story();
					story.setGa_prefix(obj.getString("ga_prefix"));
					story.setId(obj.getLong("id"));
					
					// 图片数组
					JSONArray array = obj.getJSONArray("images");
					if (array != null && array.length() > 0) {
						List<String> images = new ArrayList<String>();
						for (int x = 0 ; x < array.length() ; x++) {
							images.add(array.getString(x)) ;
						}
						story.setImages(images);
					}
					
					story.setShare_url(obj.getString("share_url"));
					story.setTitle(obj.getString("title"));
					story.setType(obj.getInt("type"));
					stories.add(story);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stories;
		
	}

	


}
