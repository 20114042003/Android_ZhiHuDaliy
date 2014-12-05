package com.cx.project.zhihudaliy.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cx.project.zhihudaliy.entity.NewDetail;
/**
 * 新闻详情界面
 * @author CxiaoX
 *
 * 2014年12月5日下午4:29:14
 */
@SuppressLint("SetJavaScriptEnabled")
public class ContentActivity extends Activity implements Listener<JSONObject>{
	
	
	//参数相关
	private Long id;
	
	//控件相关
	private NetworkImageView imgTitle;
	private WebView wvContent; 
	private RelativeLayout rlHead; //用于在没有头部数据的时候，隐藏头部。
	
	
	//Volley
	private RequestQueue mQueue;
	
	//数据相关
	private NewDetail detail;
	
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
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		rlHead =(RelativeLayout) findViewById(R.id.rl_head);
		rlHead.setVisibility(View.GONE);
		
		imgTitle = (NetworkImageView) findViewById(R.id.img_thumb);
		
		wvContent = (WebView) findViewById(R.id.wv_content);
		
	}

	
	/**
	 * 初始化 主体详情新闻的数据
	 */
	private void initData() {
		mQueue = Volley.newRequestQueue(this);
		mQueue.add(new JsonObjectRequest(Method.GET, String.format(API.getNewsDetail(), id), null,this, null));
		
	}


	/*-----------------网络请求-----------------*/
	@Override
	public void onResponse(JSONObject response) {
		if(response !=null){
			 detail = NewDetail.parse(response);
			
			 //主页新闻才有头部数据
			 if(detail.getImage()!=null){
				 initHeadData();
				 rlHead.setVisibility(View.VISIBLE);
			 }
			 initWebViewData();
		}
	}
	
	/**
	 * 初始化 头部的数据
	 */
	private void initHeadData() {
		
		
		imgTitle.setImageUrl(detail.getImage(), new ImageLoader(mQueue, new BitmapCache()));
		
		TextView txSource = (TextView) findViewById(R.id.tx_image_source);
		txSource.setText(detail.getImage_source());
		
		TextView txTitle = (TextView) findViewById(R.id.tx_title);
		txTitle.setText(detail.getTitle());
		
		
	}


	/*-----------------网络请求-----------------*/


	private void initWebViewData() {
		WebSettings ws =  wvContent.getSettings();
		
		//布局适应屏幕
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		//支持Javascript脚本
		ws.setJavaScriptEnabled(true);
		
		
		
		// 加样式进来
		StringBuffer sbCss = new StringBuffer();
		if(detail.getCss()!=null){
			for(int i=0;i<detail.getCss().size();i++){
				sbCss.append("<link rel=\"stylesheet\" href=\"");
				sbCss.append(detail.getCss().get(0));
				sbCss.append("\"> \n");
			}
		}
		
		//有的新闻没有 Body
		if(detail.getBody()!=null){ 
			String body = String.format("%s \n %s ", sbCss.toString(),detail.getBody());
			wvContent.loadDataWithBaseURL(null,body , "text/html", "UTF-8", null);
		}else{  //没有Body的直接 加 share_url
			wvContent.loadUrl(detail.getShare_url());
			wvContent.setWebViewClient(new MyWebViewClient());
			
		}
		
	}
	
	
	private class MyWebViewClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			view.loadUrl(url);
			return true;
		}
		
	}

}
