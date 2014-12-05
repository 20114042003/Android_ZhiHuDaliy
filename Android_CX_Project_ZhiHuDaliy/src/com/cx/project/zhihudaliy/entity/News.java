package com.cx.project.zhihudaliy.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cx.project.zhihudaliy.util.date.DateStyle;
import com.cx.project.zhihudaliy.util.date.DateUtil;

/**
 * 首页中的新闻
 * @author CxiaoX
 *
 * 2014年12月5日上午10:28:49
 */
public class News {
	private String date;
	private List<Story> stories;
	private List<TopStory> top_stories;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

	public List<TopStory> getTopStories() {
		return top_stories;
	}

	public void setTopStories(List<TopStory> topStories) {
		this.top_stories = topStories;
	}

	public static News parse(JSONObject newsObject) {
		News news = null;

		try {
			if (newsObject != null) {
				news = new News();
				news.setDate(newsObject.getString("date"));
				
				
				List<Story> stories =   new ArrayList<Story>();
				
				//解析数据之前，先加标题进来。
				Story StoryTitle = new Story();
				if(news.getStories()==null){ //第一次加载
					StoryTitle.setTitle("今日热闻");
				}else{ //以前的新闻
					
					String date = news.getDate();
					String mmdd = DateUtil.StringToString(date, DateStyle.MM_DD_CN);
					String week = DateUtil.getWeek(date).getChineseName();
					StoryTitle.setTitle(String.format("%s  %s", mmdd,week));
				}
				stories.add(StoryTitle);
				
				// 解析 stories
				stories.addAll(Story.parse(newsObject.getJSONArray("stories")));
				news.setStories(stories);
			
				// 解析TopStories
				news.setTopStories(TopStory.parse(newsObject.getJSONArray("top_stories")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return news;
	}
}
