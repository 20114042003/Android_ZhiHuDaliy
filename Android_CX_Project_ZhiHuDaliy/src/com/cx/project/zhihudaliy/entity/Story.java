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
	private boolean multipic;
	
	public Story() {
	}

	
	public boolean isMultipic() {
		return multipic;
	}


	public void setMultipic(boolean multipic) {
		this.multipic = multipic;
	}


	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
	
	public static List<Story> parse(JSONArray arrayStories){
		List<Story> stories = null;
		stories = new ArrayList<Story>();
		
		
		try {
			if (arrayStories != null && arrayStories.length() > 0) {
				Story StoryTitle = new Story();
				for (int i = 0 ; i < arrayStories.length() ; i++) {
					JSONObject obj = arrayStories.getJSONObject(i);
					Story story = new Story();
			
					story.setShare_url(obj.getString("share_url"));
					story.setTitle(obj.getString("title"));
					story.setType(obj.getInt("type"));
					story.setId(obj.getLong("id"));
					
					
					if(obj.has("ga_prefix")){
						story.setGa_prefix(obj.getString("ga_prefix"));
					}
					
					if(obj.has("multipic")){
						story.setMultipic(obj.getBoolean("multipic"));
					}
					
					// Í¼Æ¬Êý×é
					if(obj.has("images")){
						JSONArray array = obj.getJSONArray("images");
						if (array != null && array.length() > 0) {
							List<String> images = new ArrayList<String>();
							for (int x = 0 ; x < array.length() ; x++) {
								images.add(array.getString(x)) ;
							}
							story.setImages(images);
						}
					}
					
					
					
					stories.add(story);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stories;
		
	}

	


}
