package com.cx.project.zhihudaliy.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.adapter.CustomListViewAdapter;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.custom.CustomListViewForScrollView;
import com.cx.project.zhihudaliy.custom.CustomSlide;
import com.cx.project.zhihudaliy.custom.CustomTitle;
import com.cx.project.zhihudaliy.entity.Latest;
import com.cx.project.zhihudaliy.entity.Story;
import com.cx.project.zhihudaliy.entity.TopStory;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class MainActivity extends Activity {
	private CustomTitle cTitle;
	private CustomSlide cSlide;
	private CustomListViewForScrollView cLvNews;
	private PullToRefreshScrollView svLaTest;
	
	//Volley相关
	private RequestQueue mQueue;
	
	//数据相关
	private Latest latest;
	
	private PullToRefreshScrollView ptsSv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initTitle();
		initSlide();
		initLvNews();
		initsvLatest(); //绑定它，是为了调节ScrollView 的初始位置
		
		initLatestData();
		
	}

	
	/**
	 * 初始化存热点新闻的ListView
	 */
	private void initLvNews() {
		cLvNews = (CustomListViewForScrollView) findViewById(R.id.lv_news);
	}

	/**
	 * 初始化标题
	 */
	private void initTitle() {
		cTitle =(CustomTitle) findViewById(R.id.custom_title);
		cTitle.setTitle("首页");
		
	}
	
	/**
	 * 初始化 存Slide 和ListView的  ScrollView
	 */
	private void initsvLatest() {
		svLaTest = (PullToRefreshScrollView) findViewById(R.id.sv_latest);
	}
	
	/**
	 * 初始化 幻灯
	 */
	private void initSlide() {
		
		cSlide =(CustomSlide) findViewById(R.id.custom_slide);
	}


	/**
	 * 初始化Slide 和listView 的数据
	 */
	private void initLatestData() {
		
		mQueue = Volley.newRequestQueue(this);
		mQueue.add( new JsonObjectRequest(Method.GET, API.getLatestUrl(), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					latest = new Latest();
					//解析latest
					parserLatest(response); //得到了实例
					
					
					//初始化 幻灯
					cSlide.initSlide(latest,mQueue);
				
					//初始化listView
					CustomListViewAdapter adapter =new CustomListViewAdapter(MainActivity.this, latest,mQueue);
					cLvNews.setAdapter(adapter);
					
					//ScrollView 到顶部
//					svLaTest.smoothScrollTo(0, 0);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
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

		}, null));
		
	}


}
