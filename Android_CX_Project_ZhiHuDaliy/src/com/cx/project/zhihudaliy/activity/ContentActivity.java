package com.cx.project.zhihudaliy.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
 * 
 * @author CxiaoX
 *
 *         2014年12月5日下午4:29:14
 */
@SuppressLint("SetJavaScriptEnabled")
public class ContentActivity extends Activity implements OnClickListener,
		Listener<JSONObject> {

	// 参数相关
	private Long id;

	// 控件相关
	private NetworkImageView imgTitle;
	private WebView wvContent;
	private RelativeLayout rlHead; // 用于在没有头部数据的时候，隐藏头部。

	private Button btnShare;

	// Volley
	private RequestQueue mQueue;

	// 数据相关
	private NewDetail detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_content);

		initParams();
		initView();

		// 初始化数据
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

		btnShare = (Button) findViewById(R.id.btn_share);
		btnShare.setOnClickListener(this);

		rlHead = (RelativeLayout) findViewById(R.id.rl_head);
		rlHead.setVisibility(View.GONE);

		imgTitle = (NetworkImageView) findViewById(R.id.img_thumb);

		wvContent = (WebView) findViewById(R.id.wv_content);

	}

	/**
	 * 初始化 主体详情新闻的数据
	 */
	private void initData() {
		mQueue = Volley.newRequestQueue(this);
		mQueue.add(new JsonObjectRequest(Method.GET, String.format(
				API.getNewsDetail(), id), null, this, null));

	}

	/*-----------------网络请求-----------------*/
	@Override
	public void onResponse(JSONObject response) {
		if (response != null) {
			detail = NewDetail.parse(response);

			// 主页新闻才有头部数据
			if (detail.getImage() != null) {
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

		imgTitle.setImageUrl(detail.getImage(), new ImageLoader(mQueue,
				new BitmapCache()));

		TextView txSource = (TextView) findViewById(R.id.tx_image_source);
		txSource.setText(detail.getImage_source());

		TextView txTitle = (TextView) findViewById(R.id.tx_title);
		txTitle.setText(detail.getTitle());

	}

	/*-----------------网络请求-----------------*/

	private void initWebViewData() {
		WebSettings ws = wvContent.getSettings();

		// 布局适应屏幕
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 支持Javascript脚本
		ws.setJavaScriptEnabled(true);

		// 加样式进来
		StringBuffer sbCss = new StringBuffer();
		if (detail.getCss() != null) {
			for (int i = 0; i < detail.getCss().size(); i++) {
				sbCss.append("<link rel=\"stylesheet\" href=\"");
				sbCss.append(detail.getCss().get(0));
				sbCss.append("\"> \n");
			}
		}

		// 有的新闻没有 Body
		if (detail.getBody() != null) {
			String body = String.format("%s \n %s ", sbCss.toString(),
					detail.getBody());
			wvContent.loadDataWithBaseURL(null, body, "text/html", "UTF-8",
					null);
		} else { // 没有Body的直接 加 share_url
			wvContent.loadUrl(detail.getShare_url());
			wvContent.setWebViewClient(new MyWebViewClient());

		}

	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;
		}

	}

	/*-------------按钮点击事件-------------*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share:
			showShare();

			Toast.makeText(this, "点击了分享按钮", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字
		 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		 oks.setTitleUrl("http://sharesdk.cn");
		
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
//		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
//		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
	}

}
