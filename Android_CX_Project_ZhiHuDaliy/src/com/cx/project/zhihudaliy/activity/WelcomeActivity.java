package com.cx.project.zhihudaliy.activity;

import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.task.StartImageTask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class WelcomeActivity extends Activity{
	private ImageView imgStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		initView();
		initData();
	}
	
	

	private void initView() {
		imgStart = (ImageView) findViewById(R.id.img_start);
	}
	
	/**
	 * 初始化StartView 中的数据
	 */
	private void initData() {
		StartImageTask task = new StartImageTask(this,imgStart);
		task.execute();
		
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	

}
