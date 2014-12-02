package com.cx.project.zhihudaliy.activity;

import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.adapter.CustomListViewAdapter;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.custom.CustomListViewForScrollView;
import com.cx.project.zhihudaliy.custom.CustomSlide;
import com.cx.project.zhihudaliy.custom.CustomTitle;
import com.cx.project.zhihudaliy.entity.Latest;
import com.cx.project.zhihudaliy.task.LatestTask;
import com.cx.project.zhihudaliy.task.LatestTask.LatestCallBack;

import android.os.Bundle;
import android.widget.ScrollView;
import android.app.Activity;

public class MainActivity extends Activity {
	private CustomTitle cTitle;
	private CustomSlide cSlide;
	private CustomListViewForScrollView cLvNews;
	private ScrollView svLaTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initTitle();
		initSlide();
		initLvNews();
		initsvLatest();
		
	}

	

	/**
	 * ��ʼ�� ��Slide ��ListView��  ScrollView
	 */
	private void initsvLatest() {
		svLaTest = (ScrollView) findViewById(R.id.sv_latest);
		
		
	}



	private void initLvNews() {
		cLvNews = (CustomListViewForScrollView) findViewById(R.id.lv_news);
	}



	private void initTitle() {
		cTitle =(CustomTitle) findViewById(R.id.custom_title);
		cTitle.setTitle("��ҳ");
		
	}
	
	/**
	 * ��ʼ�� �õ�
	 */
	private void initSlide() {
		
		cSlide =(CustomSlide) findViewById(R.id.custom_slide);
		
		LatestTask task =new LatestTask(new LatestCallBack() {
			/**
			 * �ӿڻص� ���� Latest��
			 */
			@Override
			public void onPostExecute(Latest latest) { 
				
				//��ʼ�� �õ�
				cSlide.initSlide(latest);
				//��ʼ��listView
				CustomListViewAdapter adapter =new CustomListViewAdapter(MainActivity.this, latest);
				cLvNews.setAdapter(adapter);
				
				svLaTest.smoothScrollTo(0, 0);
				
				
			}
		});
		task.execute(API.getLatestUrl());
		
		
		
//		cSlide.init(API.getLatestUrl());
	}


}
