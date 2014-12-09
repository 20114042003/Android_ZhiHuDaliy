package com.cx.project.zhihudaliy.updata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xml.sax.SAXException;

import com.cx.project.zhihudaliy.c.API;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
/**
 * 获取更新信息 的异步<br />
 * 含有一个内部异步类，当有更新时用来下载更新。
 * @author CxiaoX
 *
 * 2014年12月9日下午4:36:42
 */
public class UpdataTask extends AsyncTask<Void, Void, Updata> implements OnClickListener{

	
	private Context context; //创建AlertDialog 时用到
	private Updata updata;
	
	
	
	public UpdataTask(Context context) {
		this.context = context;
	}

	@Override
	protected Updata doInBackground(Void... params) {
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			MyXMLHandler hb = new MyXMLHandler();
			parser.parse(API.getUpdataUrl(),hb);
			
			return hb.getUpdata();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Updata result) {
		
		if(result!=null){
			updata = result;
			try {
				int version = context.getPackageManager()
						.getPackageInfo(context.getPackageName(), 0)
						.versionCode;
				if(result.getVersion()> version){
					//给出提示对话框。
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle(result.getTitle());
					builder.setMessage(result.getDetail().replace(";", "\r\n"));
					builder.setPositiveButton("确定", this);
					builder.setNegativeButton("取消", this);
					builder.create().show();
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
		
		}
		
	}
	
	/*-----------监听AlertDialo的点击-----------*/
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case AlertDialog.BUTTON_POSITIVE:
			//下载更新包到SD。
		//	new DownToSDTask().execute(updata.getUrl());
			//下载更新包到手机。
			new DownToPhoneTask().execute(updata.getUrl());
			break;
		case AlertDialog.BUTTON_NEGATIVE:
			Toast.makeText(context, "点击了取消", Toast.LENGTH_LONG).show();
			dialog.dismiss();
			break;
		
		}
		
	}
	/*-----------监听AlertDialo的点击-----------*/
	
	
	
	
	
	
	/**
	 * 用于下载更新至SD卡
	 * 第二个泛型了Integer 则可以显示进度条的进度
	 * @author CxiaoX
	 *
	 * 2014年12月9日下午4:50:04
	 */
	class DownToSDTask extends AsyncTask<String, Integer, File>{
		ProgressDialog progressDialog=null;
		
		
		@Override
		protected void onPreExecute() {
			 progressDialog =new ProgressDialog(context);
			progressDialog.setTitle("文件下载中...");
			
			//为了显示下载进度而设置
			progressDialog.setMax(100);  //进度条最大值
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //进度条水平显示
			
			progressDialog.show();
		}

		@Override
		protected File doInBackground(String... params) {
			InputStream is=null;
			FileOutputStream fos=null;
			
			File file=null;
			
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpParams httpParams = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if(httpResponse!=null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					
					HttpEntity httpEntity = httpResponse.getEntity();
					
					//得到输入流
				     is = 	httpEntity.getContent();
				     //得到输入流的大小。
				     long length = httpEntity.getContentLength();
				    
				     
				     
				    //文件夹地址
				    File filePath = new File(Environment.getExternalStorageDirectory() +"/cx/");
					if(!filePath.exists()) filePath.mkdir();
					
					//文件下载到的地址
					 file =new File(filePath+"/nima.apk");
					
					
					//得到输出流
					 fos = new FileOutputStream(file);
					 
					 
					//下载
					byte[] buf = new byte[1024];
					int len=0 ; //当前输入流读取长度。
					long count =0; //当前输出流下载长度
					while((len = is.read(buf) )!=-1){
						fos.write(buf, 0, len);
						count += len;
						publishProgress( (int)((count*100)/length) );
						fos.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(fos!=null) fos.close();
					if(is!=null) is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return file;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}
		
		@Override
		protected void onPostExecute(File result) {
			progressDialog.dismiss();
			
			
			//安装软件包
			if(result!=null){
				installAPKFromSD(result);
			}
		}
		
		/**
		 * 安装SD卡中的软件包
		 * @param result 安装文件
		 */
		private void installAPKFromSD(File result) {
			Intent apkIntent = new Intent(Intent.ACTION_VIEW);
			apkIntent.setDataAndType(
					Uri.parse("file://"+result.toString())
					,"application/vnd.android.package-archive");
			context.startActivity(apkIntent);
		}
		
	}
	
	/**
	 * 下载到手机内存然后安装（安装前要设置文件权限）
	 * @author CxiaoX
	 *
	 * 2014年12月9日下午6:33:29
	 */
	class DownToPhoneTask extends AsyncTask<String,Integer, File>{
		/**
		 * 安装包下载到手机的地址
		 */
		private static final String FILE_PATH = "//data//data//com.cx.project.zhihudaliy//download//";
		/**
		 * 安装包名字
		 */
		private static final String APK_NAME = "zhihudaliy.apk";

		
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {

			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("正在下载...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(100);
			progressDialog.show();
		}
		 
		@Override
		protected File doInBackground(String... params) {

			 File downloadFile = null;
			 InputStream is = null;
			 FileOutputStream fos = null;
			
			try {
				//从网络中得到输出流流
				HttpClient httpClient = new DefaultHttpClient();
				HttpParams httpParams = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity httpEntity = httpResponse.getEntity();
					if(httpEntity!=null){
						//得到了输出流
						is = httpEntity.getContent();
						long length = httpEntity.getContentLength();
						
						
						File filePath = new File(FILE_PATH);
						if(!filePath.exists()){
							filePath.mkdir();
						}
						
						downloadFile =new File(FILE_PATH+APK_NAME);
						//得到输出流
						fos = new FileOutputStream(downloadFile);
						
						//下载
						byte[] buf = new byte[1024];
						int len = 0, count = 0;
						while ((len = is.read(buf)) != -1) {
							fos.write(buf, 0, len);
							count += len;
							publishProgress((int) ((count * 100) / length));
							fos.flush();
						}
					}
				
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(is!=null){
						is.close();
					}
					if(fos!=null){
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			return downloadFile;
		}
		
	
		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}
		
		@Override
		protected void onPostExecute(File result) {
			progressDialog.dismiss();
			
			if(result!=null){
				
				// 设置文件夹的权限
				String[] args1 = { "chmod", "705", FILE_PATH };
				exec(args1);
				// 设置文件的权限
				String[] args2 = { "chmod", "604", FILE_PATH + APK_NAME };
				exec(args2);
				
				// 调用安装文件
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + FILE_PATH + APK_NAME), "application/vnd.android.package-archive");
				context.startActivity(intent);
				
			}
			
		}
		
		// 设置权限的方法
		public String exec(String[] args) {
			String result = "";
			ProcessBuilder processBuilder = new ProcessBuilder(args);
			Process process = null;
			InputStream errIs = null;
			InputStream inIs = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int read = -1;
				process = processBuilder.start();
				errIs = process.getErrorStream();
				while ((read = errIs.read()) != -1) {
					baos.write(read);
				}
				baos.write('\n');
				inIs = process.getInputStream();
				while ((read = inIs.read()) != -1) {
					baos.write(read);
				}
				byte[] data = baos.toByteArray();
				result = new String(data);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (errIs != null) {
						errIs.close();
					}
					if (inIs != null) {
						inIs.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (process != null) {
					process.destroy();
				}
			}
			return result;
		}
	}


}
