package com.cx.project.zhihudaliy.activity;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.c.API;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class ContentActivity extends Activity{
	
	
	//参数相关
	private Long id;
	
	//控件相关
	private WebView wvContent; 
	
	//Volley
	private RequestQueue mQueue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_content);
		
		initParams();
		initView();
		
		//初始化数据
		initData();
	}


	/**
	 * 初始化参数
	 */
	private void initParams() {
		id = getIntent().getLongExtra("id", 0);
		Toast.makeText(this, String.valueOf(id), Toast.LENGTH_LONG).show();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		
		wvContent = (WebView) findViewById(R.id.wv_content);
		
	}

	
	/**
	 * 初始化 主体详情新闻的数据
	 */
	private void initData() {
		mQueue = Volley.newRequestQueue(this);
//		mQueue.add(new JsonObjectRequest(Method.GET, String.format(API.get, args), null, listener, null));
		
	}

}
