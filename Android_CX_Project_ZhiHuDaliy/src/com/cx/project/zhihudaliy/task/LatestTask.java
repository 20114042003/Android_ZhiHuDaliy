package com.cx.project.zhihudaliy.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cx.project.zhihudaliy.entity.News;
import com.cx.project.zhihudaliy.entity.Story;
import com.cx.project.zhihudaliy.entity.TopStory;
import com.cx.project.zhihudaliy.util.NetUtil;

import android.os.AsyncTask;

public class LatestTask extends AsyncTask<String, Void, Void>{
	
	private LatestCallBack latestIml;
	private News latest = null;
	
	

	public LatestTask(LatestCallBack latestIml) {
		this.latestIml = latestIml;
	}

	@Override
	protected Void doInBackground(String... params) {
		try {
			String json = NetUtil.getJson(params[0]);
			if (json != null) {
				JSONObject latestObject = new JSONObject(json);
				latest = new News();
				//解析latest
				parserLatest(latestObject);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		
		latestIml.onPostExecute(latest);
	}
	
	
	public interface LatestCallBack{
		void onPostExecute(News latest);
	}
	
	
	/**
	 * 解析latest
	 * @param latestObject
	 * @throws JSONException
	 */
	private void parserLatest(JSONObject latestObject) throws JSONException {
		
		latest.setDate(latestObject.getString("date"));
		//解析 stories
		latest.setStories(parserStories(latestObject.getJSONArray("stories")));
		//解析TopStories
		latest.setTopStories(parserTopStories(latestObject.getJSONArray("top_stories")));
		
	}

	

	/**
	 * 解析 Stories
	 * @param jsonArray
	 * @return
	 * @throws JSONException 
	 */
	private List<Story> parserStories(JSONArray arrayStories) throws JSONException {
		List<Story> stories = null;
		if (arrayStories != null && arrayStories.length() > 0) {
			stories = new ArrayList<Story>();
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
		return stories;
	}
	
	/**
	 * 解析 TopStories
	 * @param jsonArray
	 * @return
	 * @throws JSONException 
	 */
	private List<TopStory> parserTopStories(JSONArray arrayTopStories) throws JSONException {
		List<TopStory> topStories = null;
		
		// 封装TopStory
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
		
		return topStories;
	}

}
