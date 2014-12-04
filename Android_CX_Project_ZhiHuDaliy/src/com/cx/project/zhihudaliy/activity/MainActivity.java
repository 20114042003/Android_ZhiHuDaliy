package com.cx.project.zhihudaliy.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.adapter.CustomListViewAdapter;
import com.cx.project.zhihudaliy.adapter.ThemeAdapter;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.custom.CustomListViewForScrollView;
import com.cx.project.zhihudaliy.custom.CustomSlide;
import com.cx.project.zhihudaliy.custom.CustomTitle;
import com.cx.project.zhihudaliy.entity.News;
import com.cx.project.zhihudaliy.entity.Story;
import com.cx.project.zhihudaliy.entity.Theme;
import com.cx.project.zhihudaliy.entity.TopStory;
import com.cx.project.zhihudaliy.util.date.DateStyle;
import com.cx.project.zhihudaliy.util.date.DateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity implements OnRefreshListener2<ScrollView>,Listener<JSONObject>{
	private CustomTitle cTitle;
	private CustomSlide cSlide;
	private CustomListViewForScrollView cLvNews;
	private PullToRefreshScrollView svLaTest;
	
	//Volley相关
	private RequestQueue mQueue;
	
	//数据相关
	private News news;
	private CustomListViewAdapter adapter;
	private boolean up ;

	
	//侧滑菜单相关
	private SlidingMenu menu;
	private ListView lvThems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initTitle();
		initSlide();
		initLvNews();
		initsvLatest(); //绑定它，是为了调节ScrollView 的初始位置
		
		initSlidingMenu();
		
		initLatestData();
		initSlideingMenuData();
		
	}

	
	/**
	 * 初始化侧滑菜单中的数据
	 */
	private void initSlideingMenuData() {
		mQueue =Volley.newRequestQueue(this);
		mQueue.add(new JsonObjectRequest(Method.GET, API.getThemesUrl(), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Theme theme = Theme.parse(response);
				
				ThemeAdapter adapter = new ThemeAdapter(MainActivity.this, theme);
				lvThems.setAdapter(adapter);
			}
		}, null));
		
	}

	/**
	 * 初始化侧滑菜单
	 */
	private void initSlidingMenu() {
		
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setShadowWidth(10);
		menu.setBehindOffset(50);
		menu.setBehindWidth(350);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.sliding_menu);
		
		lvThems = (ListView) findViewById(R.id.lv_thems);
		
//		List<String> array = new ArrayList<String>();
//		for(int i = 0;i<15;i++){
//			array.add(String.format("item %s",i));
//		}
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array );
//		lvThems.setAdapter(adapter);
		
		
	}


	/**
	 * 绑定 新闻列表
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
		
		cTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});
	}
	
	/**
	 * 初始化 存Slide 和ListView的  ScrollView
	 */
	private void initsvLatest() {
		svLaTest = (PullToRefreshScrollView) findViewById(R.id.sv_latest);
		svLaTest.setOnRefreshListener(this); //监听上下拉
	}
	
	/**
	 * 绑定 新闻幻灯（组合控件）
	 */
	private void initSlide() {
		cSlide =(CustomSlide) findViewById(R.id.custom_slide);
	}
	
	/**
	 * 初始化 新闻幻灯 和 新闻列表 的数据
	 */
	private void initLatestData() {
		
		mQueue = Volley.newRequestQueue(this);
		mQueue.add( new JsonObjectRequest(Method.GET, API.getLatestUrl(), null, this, null));
		
	}
	
	/*------------------解析Json数据,并赋值------------------*/
	@Override
	public void onResponse(JSONObject response) {
		try {
			if(!up){ //刷新
				news = new News();
				//解析latest
				parserLatest(response); //得到了实例
				
				//初始化 幻灯
				cSlide.initSlide(news,mQueue);
			
				//更新新闻列表(今日热闻)
				adapter =new CustomListViewAdapter(MainActivity.this, news,mQueue);
				cLvNews.setAdapter(adapter);
				
				//ScrollView 到顶部
				svLaTest.getRefreshableView().smoothScrollTo(0, 0);
				
				
				
			}else{ //加载
				
				news.setDate(response.getString("date"));
				//解析 stories,并添加到原来的news
				news.getStories().addAll(parserStories(response.getJSONArray("stories")));
				//更新新闻列表（新增 以前的新闻）
				adapter.notifyDataSetChanged();
				up=false;
			}
			
			svLaTest.onRefreshComplete();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*------------------解析Json数据,并赋值------------------*/
	

	/*---------------监听PullToRefreshScrollView上下拉---------------*/
	/**
	 *  下拉刷新。
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		//清空数据
		cSlide.cancel();
		news.getStories().clear();
		news.getTopStories().clear();
		//重新加载数据
		initLatestData();
		
		
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		//该地址加载数据,昨天的新闻（用今天的data）。
		up=true;
		mQueue.add( new JsonObjectRequest(Method.GET,String.format(API.getBeforeUrl(),news.getDate() ) , null, this, null));
		
	}
	/*---------------监听PullToRefreshScrollView上下拉---------------*/


	
	
	/*------------------解析 新闻的Json字符串------------------*/
	/**
	 * 解析latest
	 * @param latestObject
	 * @throws JSONException
	 */
	private void parserLatest(JSONObject latestObject) throws JSONException {
		
		news.setDate(latestObject.getString("date"));
		//解析 stories
		news.setStories(parserStories(latestObject.getJSONArray("stories")));
		//解析TopStories
		news.setTopStories(TopStory.parse(latestObject.getJSONArray("top_stories")));
//		news.setTopStories(parserTopStories(latestObject.getJSONArray("top_stories")));
		
	}

	
	/**
	 * 解析 Stories
	 * @param jsonArray
	 * @return
	 * @throws JSONException 
	 */
	private List<Story> parserStories(JSONArray arrayStories) throws JSONException {
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
		return stories;
	}
	
//	/**
//	 * 解析 TopStories
//	 * @param jsonArray
//	 * @return
//	 * @throws JSONException 
//	 */
//	private List<TopStory> parserTopStories(JSONArray arrayTopStories) throws JSONException {
//		List<TopStory> topStories = null;
//		
//		// 封装TopStory
//		if (arrayTopStories != null && arrayTopStories.length() > 0) {
//			topStories = new ArrayList<TopStory>();
//			for (int i = 0 ; i < arrayTopStories.length() ; i++) {
//				JSONObject obj = arrayTopStories.getJSONObject(i);
//				TopStory topStory = new TopStory();
//				topStory.setGa_prefix(obj.getString("ga_prefix"));
//				topStory.setId(obj.getLong("id"));
//				topStory.setImage(obj.getString("image"));
//				topStory.setShare_url(obj.getString("share_url"));
//				topStory.setTitle(obj.getString("title"));
//				topStory.setType(obj.getInt("type"));
//				topStories.add(topStory);
//			}
//		}
//		
//		return topStories;
//	}
	/*------------------解析 新闻的Json字符串------------------*/

}
