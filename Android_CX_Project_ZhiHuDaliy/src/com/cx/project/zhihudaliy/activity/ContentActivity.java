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
 * �����������
 * 
 * @author CxiaoX
 *
 *         2014��12��5������4:29:14
 */
@SuppressLint("SetJavaScriptEnabled")
public class ContentActivity extends Activity implements OnClickListener,
		Listener<JSONObject> {

	// �������
	private Long id;

	// �ؼ����
	private NetworkImageView imgTitle;
	private WebView wvContent;
	private RelativeLayout rlHead; // ������û��ͷ�����ݵ�ʱ������ͷ����

	private Button btnShare;

	// Volley
	private RequestQueue mQueue;

	// �������
	private NewDetail detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_content);

		initParams();
		initView();

		// ��ʼ������
		initData();
	}

	/**
	 * ��ʼ������
	 */
	private void initParams() {
		id = getIntent().getLongExtra("id", 0);
	}

	/**
	 * ��ʼ���ؼ�
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
	 * ��ʼ�� �����������ŵ�����
	 */
	private void initData() {
		mQueue = Volley.newRequestQueue(this);
		mQueue.add(new JsonObjectRequest(Method.GET, String.format(
				API.getNewsDetail(), id), null, this, null));

	}

	/*-----------------��������-----------------*/
	@Override
	public void onResponse(JSONObject response) {
		if (response != null) {
			detail = NewDetail.parse(response);

			// ��ҳ���Ų���ͷ������
			if (detail.getImage() != null) {
				initHeadData();
				rlHead.setVisibility(View.VISIBLE);
			}
			initWebViewData();
		}
	}

	/**
	 * ��ʼ�� ͷ��������
	 */
	private void initHeadData() {

		imgTitle.setImageUrl(detail.getImage(), new ImageLoader(mQueue,
				new BitmapCache()));

		TextView txSource = (TextView) findViewById(R.id.tx_image_source);
		txSource.setText(detail.getImage_source());

		TextView txTitle = (TextView) findViewById(R.id.tx_title);
		txTitle.setText(detail.getTitle());

	}

	/*-----------------��������-----------------*/

	private void initWebViewData() {
		WebSettings ws = wvContent.getSettings();

		// ������Ӧ��Ļ
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// ֧��Javascript�ű�
		ws.setJavaScriptEnabled(true);

		// ����ʽ����
		StringBuffer sbCss = new StringBuffer();
		if (detail.getCss() != null) {
			for (int i = 0; i < detail.getCss().size(); i++) {
				sbCss.append("<link rel=\"stylesheet\" href=\"");
				sbCss.append(detail.getCss().get(0));
				sbCss.append("\"> \n");
			}
		}

		// �е�����û�� Body
		if (detail.getBody() != null) {
			String body = String.format("%s \n %s ", sbCss.toString(),
					detail.getBody());
			wvContent.loadDataWithBaseURL(null, body, "text/html", "UTF-8",
					null);
		} else { // û��Body��ֱ�� �� share_url
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

	/*-------------��ť����¼�-------------*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share:
			showShare();

			Toast.makeText(this, "����˷���ť", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������
		 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle(getString(R.string.share));
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
//		 oks.setTitleUrl("http://sharesdk.cn");
		
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText("���Ƿ����ı�");
		 
		 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
//		 oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
//		 oks.setUrl("http://sharesdk.cn");
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
//		 oks.setComment("���ǲ��������ı�");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
//		 oks.setSite(getString(R.string.app_name));
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
//		 oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		 oks.show(this);
	}

}
