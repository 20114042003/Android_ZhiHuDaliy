package com.cx.project.zhihudaliy.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
	 * 初始化 新闻ScrollView
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
	
	/*------------------解析Json数据,并赋值------------------*/
	@Override
	public void onResponse(JSONObject response) {
		try {
			if(!up){ //刷新
				//解析news
				news = News.parse(response);//得到了实例
				
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
				news.getStories().addAll(Story.parse(news,response.getJSONArray("stories")));
				
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

}
