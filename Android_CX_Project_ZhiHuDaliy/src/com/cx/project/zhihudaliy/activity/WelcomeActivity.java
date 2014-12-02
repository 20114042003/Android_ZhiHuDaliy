package com.cx.project.zhihudaliy.activity;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cx.project.zhihudaliy.R;
import com.cx.project.zhihudaliy.c.API;
import com.cx.project.zhihudaliy.cache.BitmapCache;
import com.cx.project.zhihudaliy.entity.StartImage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;

public class WelcomeActivity extends Activity implements AnimationListener {
	private RequestQueue mQueue;
	private NetworkImageView imgStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		initView();
		initData();
	}

	private void initView() {
		imgStart = (NetworkImageView) findViewById(R.id.img_start);
	}

	/**
	 * ��ʼ��StartView �е�����
	 */
	private void initData() {

		mQueue = Volley.newRequestQueue(getApplicationContext());
		mQueue.add(new JsonObjectRequest(Method.GET, API.getStartImageUrl(),
				null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						String json = response.toString();

						StartImage startImage = JSON.parseObject(json,
								StartImage.class);
						if (startImage != null) {
							imgStart.setImageUrl(startImage.getImg(),
									new ImageLoader(mQueue, new BitmapCache()));
						}

						// ʹ�ö�������ͼƬ
						Animation animation = new ScaleAnimation(1.0f, 1.2f,
								1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.5f); // ��ͼƬ�Ŵ�1.2���������Ŀ�ʼ����
						animation.setDuration(2000); // ��������ʱ��
						animation.setFillAfter(true); // ����������ͣ���ڽ�����λ��
						animation.setAnimationListener(WelcomeActivity.this);
						imgStart.startAnimation(animation);

					}

				}, null));

		mQueue.start();

	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	/*---------------��������---------------*/
	@Override
	public void onAnimationStart(Animation animation) {

	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
	/*---------------��������---------------*/

}
