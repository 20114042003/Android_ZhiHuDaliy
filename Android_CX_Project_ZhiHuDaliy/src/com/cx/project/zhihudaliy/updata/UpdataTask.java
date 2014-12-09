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
 * ��ȡ������Ϣ ���첽<br />
 * ����һ���ڲ��첽�࣬���и���ʱ�������ظ��¡�
 * @author CxiaoX
 *
 * 2014��12��9������4:36:42
 */
public class UpdataTask extends AsyncTask<Void, Void, Updata> implements OnClickListener{

	
	private Context context; //����AlertDialog ʱ�õ�
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
					//������ʾ�Ի���
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle(result.getTitle());
					builder.setMessage(result.getDetail().replace(";", "\r\n"));
					builder.setPositiveButton("ȷ��", this);
					builder.setNegativeButton("ȡ��", this);
					builder.create().show();
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
		
		}
		
	}
	
	/*-----------����AlertDialo�ĵ��-----------*/
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case AlertDialog.BUTTON_POSITIVE:
			//���ظ��°���SD��
		//	new DownToSDTask().execute(updata.getUrl());
			//���ظ��°����ֻ���
			new DownToPhoneTask().execute(updata.getUrl());
			break;
		case AlertDialog.BUTTON_NEGATIVE:
			Toast.makeText(context, "�����ȡ��", Toast.LENGTH_LONG).show();
			dialog.dismiss();
			break;
		
		}
		
	}
	/*-----------����AlertDialo�ĵ��-----------*/
	
	
	
	
	
	
	/**
	 * �������ظ�����SD��
	 * �ڶ���������Integer �������ʾ�������Ľ���
	 * @author CxiaoX
	 *
	 * 2014��12��9������4:50:04
	 */
	class DownToSDTask extends AsyncTask<String, Integer, File>{
		ProgressDialog progressDialog=null;
		
		
		@Override
		protected void onPreExecute() {
			 progressDialog =new ProgressDialog(context);
			progressDialog.setTitle("�ļ�������...");
			
			//Ϊ����ʾ���ؽ��ȶ�����
			progressDialog.setMax(100);  //���������ֵ
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //������ˮƽ��ʾ
			
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
					
					//�õ�������
				     is = 	httpEntity.getContent();
				     //�õ��������Ĵ�С��
				     long length = httpEntity.getContentLength();
				    
				     
				     
				    //�ļ��е�ַ
				    File filePath = new File(Environment.getExternalStorageDirectory() +"/cx/");
					if(!filePath.exists()) filePath.mkdir();
					
					//�ļ����ص��ĵ�ַ
					 file =new File(filePath+"/nima.apk");
					
					
					//�õ������
					 fos = new FileOutputStream(file);
					 
					 
					//����
					byte[] buf = new byte[1024];
					int len=0 ; //��ǰ��������ȡ���ȡ�
					long count =0; //��ǰ��������س���
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
			
			
			//��װ�����
			if(result!=null){
				installAPKFromSD(result);
			}
		}
		
		/**
		 * ��װSD���е������
		 * @param result ��װ�ļ�
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
	 * ���ص��ֻ��ڴ�Ȼ��װ����װǰҪ�����ļ�Ȩ�ޣ�
	 * @author CxiaoX
	 *
	 * 2014��12��9������6:33:29
	 */
	class DownToPhoneTask extends AsyncTask<String,Integer, File>{
		/**
		 * ��װ�����ص��ֻ��ĵ�ַ
		 */
		private static final String FILE_PATH = "//data//data//com.cx.project.zhihudaliy//download//";
		/**
		 * ��װ������
		 */
		private static final String APK_NAME = "zhihudaliy.apk";

		
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {

			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("��������...");
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
				//�������еõ��������
				HttpClient httpClient = new DefaultHttpClient();
				HttpParams httpParams = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity httpEntity = httpResponse.getEntity();
					if(httpEntity!=null){
						//�õ��������
						is = httpEntity.getContent();
						long length = httpEntity.getContentLength();
						
						
						File filePath = new File(FILE_PATH);
						if(!filePath.exists()){
							filePath.mkdir();
						}
						
						downloadFile =new File(FILE_PATH+APK_NAME);
						//�õ������
						fos = new FileOutputStream(downloadFile);
						
						//����
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
				
				// �����ļ��е�Ȩ��
				String[] args1 = { "chmod", "705", FILE_PATH };
				exec(args1);
				// �����ļ���Ȩ��
				String[] args2 = { "chmod", "604", FILE_PATH + APK_NAME };
				exec(args2);
				
				// ���ð�װ�ļ�
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + FILE_PATH + APK_NAME), "application/vnd.android.package-archive");
				context.startActivity(intent);
				
			}
			
		}
		
		// ����Ȩ�޵ķ���
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
