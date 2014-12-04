package com.cx.project.zhihudaliy.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.adapter.CustomListViewAdapter;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.custom.CustomListViewForScrollView;
import com.cx.project.zhihudaliy.custom.CustomSlide;
import com.cx.project.zhihudaliy.entity.News;
import com.cx.project.zhihudaliy.entity.Story;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class MainFragment extends Fragment implements Listener<JSONObject>,
		OnRefreshListener2<ScrollView> {

	// Volley相关
	private RequestQueue mQueue;

	// 数据相关
	private News news;
	private CustomListViewAdapter adapter;
	private boolean up;

	
	//控件相关
	private CustomSlide cSlide;
	private CustomListViewForScrollView cLvNews;
	private PullToRefreshScrollView svLaTest;
	
	public static MainFragment newInstance(){
		MainFragment mf = new MainFragment();
		Bundle args = new Bundle();
		mf.setArguments(args);
		return mf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.fragment_main, container, false);

		initView(layout);
		initLatestData();

		return layout;
	}

	private void initView(View layout) {

		initSlide(layout); // 绑定幻灯
		initLvNews(layout); // 绑定新闻列表
		initsvLatest(layout); // 绑定ScrollView
	}

	/**
	 * 绑定 新闻幻灯（组合控件）
	 */
	private void initSlide(View layout) {
		cSlide = (CustomSlide) layout.findViewById(R.id.custom_slide);
	}

	/**
	 * 绑定 新闻列表
	 */
	private void initLvNews(View layout) {
		cLvNews = (CustomListViewForScrollView) layout.findViewById(R.id.lv_news);
	}

	/**
	 * 初始化 新闻ScrollView
	 */
	private void initsvLatest(View layout) {
		svLaTest = (PullToRefreshScrollView) layout.findViewById(R.id.sv_latest);
		svLaTest.setOnRefreshListener(this); // 监听上下拉
	}

	/**
	 * 初始化 新闻幻灯 和 新闻列表 的数据
	 */
	private void initLatestData() {

		mQueue = Volley.newRequestQueue(getActivity());
		mQueue.add(new JsonObjectRequest(Method.GET, API.getLatestUrl(), null,
				this, null));

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
				adapter =new CustomListViewAdapter(getActivity(), news,mQueue);
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
