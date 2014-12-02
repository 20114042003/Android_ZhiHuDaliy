package com.cx.project.zhihudaliy.custom;

import com.cx.project.zhihudaliy.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomTitle extends FrameLayout{
	private TextView txTitle;

	public CustomTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//绑定布局
		LayoutInflater.from(context).inflate(R.layout.custom_title, this);
		initTitle();
	}

	/**
	 * 初始化标题
	 */
	private void initTitle() {
		txTitle = (TextView) findViewById(R.id.tx_title);
		
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title) {
		txTitle.setText(title);
	}

}
