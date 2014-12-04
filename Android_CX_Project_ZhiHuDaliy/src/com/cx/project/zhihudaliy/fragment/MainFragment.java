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

	// Volley���
	private RequestQueue mQueue;

	// �������
	private News news;
	private CustomListViewAdapter adapter;
	private boolean up;

	
	//�ؼ����
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

		initSlide(layout); // �󶨻õ�
		initLvNews(layout); // �������б�
		initsvLatest(layout); // ��ScrollView
	}

	/**
	 * �� ���Żõƣ���Ͽؼ���
	 */
	private void initSlide(View layout) {
		cSlide = (CustomSlide) layout.findViewById(R.id.custom_slide);
	}

	/**
	 * �� �����б�
	 */
	private void initLvNews(View layout) {
		cLvNews = (CustomListViewForScrollView) layout.findViewById(R.id.lv_news);
	}

	/**
	 * ��ʼ�� ����ScrollView
	 */
	private void initsvLatest(View layout) {
		svLaTest = (PullToRefreshScrollView) layout.findViewById(R.id.sv_latest);
		svLaTest.setOnRefreshListener(this); // ����������
	}

	/**
	 * ��ʼ�� ���Żõ� �� �����б� ������
	 */
	private void initLatestData() {

		mQueue = Volley.newRequestQueue(getActivity());
		mQueue.add(new JsonObjectRequest(Method.GET, API.getLatestUrl(), null,
				this, null));

	}



	/*------------------����Json����,����ֵ------------------*/
	@Override
	public void onResponse(JSONObject response) {
		try {
			if(!up){ //ˢ��
				//����news
				news = News.parse(response);//�õ���ʵ��
				
				//��ʼ�� �õ�
				cSlide.initSlide(news,mQueue);
			
				//���������б�(��������)
				adapter =new CustomListViewAdapter(getActivity(), news,mQueue);
				cLvNews.setAdapter(adapter);
			
				//ScrollView ������
				svLaTest.getRefreshableView().smoothScrollTo(0, 0);
				
				
			}else{ //����
				
				news.setDate(response.getString("date"));
				//���� stories,����ӵ�ԭ����news
				news.getStories().addAll(Story.parse(news,response.getJSONArray("stories")));
				
				//���������б����� ��ǰ�����ţ�
				adapter.notifyDataSetChanged();
				up=false;
			}
			
			svLaTest.onRefreshComplete();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*------------------����Json����,����ֵ------------------*/
	
	/*---------------����PullToRefreshScrollView������---------------*/
	/**
	 *  ����ˢ�¡�
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		//�������
		cSlide.cancel();
		news.getStories().clear();
		news.getTopStories().clear();
		//���¼�������
		initLatestData();
		
		
	}

	/**
	 * ��������
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		//�õ�ַ��������,��������ţ��ý����data����
		up=true;
		mQueue.add( new JsonObjectRequest(Method.GET,String.format(API.getBeforeUrl(),news.getDate() ) , null, this, null));
		
	}
	/*---------------����PullToRefreshScrollView������---------------*/

}
