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
	 * ����topStory ,ʹ�ø÷���������Ϊ�Ǿ�̬�ˣ�һдһ�������ͱȽ�ռ���ڴ�
	 * @param arrayTopStories  topStory ��Json�������
	 * @return  topStory ����
	 */
	public static List<TopStory> parse(JSONArray arrayTopStories ){
		List<TopStory> topStories = null;
		// ��װTopStory
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
