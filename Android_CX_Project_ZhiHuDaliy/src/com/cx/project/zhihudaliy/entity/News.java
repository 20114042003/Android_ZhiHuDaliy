package com.cx.project.zhihudaliy.entity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
				// Ω‚Œˆ stories
				news.setStories(Story.parse(news, newsObject.getJSONArray("stories")));
				// Ω‚ŒˆTopStories
				news.setTopStories(TopStory.parse(newsObject.getJSONArray("top_stories")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return news;
	}
}
