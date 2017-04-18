package com.caccommonmodule.util;
//package com.m104parttime.util;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.SocketException;
//import java.net.SocketTimeoutException;
//import java.net.URLEncoder;
//import java.nio.charset.Charset;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ConnectTimeoutException;
//import org.apache.http.conn.HttpHostConnectException;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.CoreProtocolPNames;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.protocol.HttpContext;
//import org.apache.http.util.EntityUtils;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Base64;
//import android.util.Base64OutputStream;
//import android.util.Log;
//
//import com.e104.BaseProxy;
//import com.e104.QueryKey;
//import com.m104.MainApp;
//import com.m104parttime.util.CustomMultiPartEntity.ProgressListener;
//
//@SuppressWarnings("static-access")
//public class AsyncDataUploadUtil extends AsyncTask<Object, Integer, String> {
//
//	private String upLoadServerUri = BaseProxy.API_PROTOCOL + BaseProxy.API_SERVER
//			+ "/api/1.0/user/resume/attachment_upload.php";
//
//	private Context context;
//	private String filePath;
//	private String version_no;
//	private String file_name;
//	private String file_name_ext;
//
//	// private int index;
//	// private boolean isTransform = false;
//
//	// private double fileLimitSize1 = 5 * 1024 * 1024; // 5M
//	// private double fileLimitSize2 = 7.5 * 1024 * 1024; // 7.5M
//	//
//	// private int angle;
//	private String encode = HTTP.UTF_8;
//	private long totalSize;
//	// private String[] files;
////	private FormBodyPart[] parts;
//	private CallBack mCallBack;
//	private CallBackMsg mCallBackMsg;
//	private boolean isSccuess = true;
//	private HttpPost httpPost;
//	private HttpClient httpClient;
//	private boolean isCancel;
//	private boolean hasEvent;
//	
//	public AsyncDataUploadUtil(Context context, String filePath,
//			String version_no, String file_name, String file_name_ext) {
//		this.context = context;
//		this.filePath = filePath;
//		this.version_no = version_no;
//		this.file_name = file_name;
//		this.file_name_ext = file_name_ext;
//	}
//
//	@SuppressWarnings("resource")
//	private String getBase64Data() {
//		String data = "";
//
//		InputStream inputStream = null;// You can get an inputStream using any
//										// IO API
//		try {
//			inputStream = new FileInputStream(filePath);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		byte[] buffer = new byte[8192];
//		int bytesRead;
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		Base64OutputStream output64 = new Base64OutputStream(output,
//				Base64.DEFAULT);
//		try {
//			while ((bytesRead = inputStream.read(buffer)) != -1) {
//				output64.write(buffer, 0, bytesRead);
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			output64.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		data = output.toString();
//
//		return data;
//	}
//
//	@SuppressWarnings({ "resource", "unused", "unchecked" })
//	@Override
//	protected String doInBackground(Object... arg0) {
////		MainApp.getInstance().cancelUpload = false;
//
//		upLoadServerUri += "?"
//				+ QueryKey.DEVICE_TYPE + "=" + MainApp.getInstance().device_type
//				+ "&" + QueryKey.APP_VERSION + "="
//				+ MainApp.getInstance().versionName + "&" + QueryKey.ID_CK
//				+ "=" + MainApp.getInstance().user.getIdCk() + "&"
//				+ QueryKey.DEVICE_ID + "="
//				+ MainApp.getInstance().device_id_hash;
//
//		
//		//timeout setting 20151001
////		HttpParams httpParameters = new BasicHttpParams();
////		int timeoutConnection = 120 * 1000;
////		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
////		// Set the default socket timeout (SO_TIMEOUT) 
////		// in milliseconds which is the timeout for waiting for data.
////		int timeoutSocket = 120 * 1000;
////		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//		
//		String serverResponse = "";
//		
//		httpClient = new DefaultHttpClient();
//		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
//                System.getProperty("http.agent"));
//		HttpContext httpContext = new BasicHttpContext();
//		httpPost = new HttpPost(upLoadServerUri);
//		
////		try {
////			file_name = URLEncoder.encode(
////					file_name, "utf-8");
////		} catch (UnsupportedEncodingException e1) {
////			e1.printStackTrace();
////		}
//		
//		try {
//			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(
//					HttpMultipartMode.BROWSER_COMPATIBLE/**,
//														  null,
//														  Charset.forName(
//														  encode)**/,
//														 
//					new ProgressListener() {
//						@Override
//						public void transferred(long num) {
//							publishProgress((int) ((num / (float) totalSize) * 100));
//						}
//					});
//
//			// add other parts
//			// for (FormBodyPart part : parts) {
//			// multipartContent.addPart(part);
//			// }
//			
//			multipartContent.addPart("version_no", new StringBody(version_no));
//			multipartContent.addPart("file_name", new StringBody(file_name, Charset.forName(encode)));
//			multipartContent.addPart("file_name_ext", new StringBody(
//					file_name_ext));
//			multipartContent.addPart("file_base64", new StringBody(
//					getBase64Data()));
//
//			totalSize = multipartContent.getContentLength();
//
//			// Send it
//			httpPost.setEntity(multipartContent);
//			HttpResponse response = httpClient.execute(httpPost, httpContext);
//			serverResponse = EntityUtils.toString(response.getEntity());
//			return serverResponse;
//		} catch (SocketTimeoutException e){
//			isSccuess = false;
//			serverResponse = "上傳檔案逾時，請檢查網路狀態";
//		} catch (ConnectTimeoutException e){
//			isSccuess = false;
//			serverResponse = "上傳檔案逾時，請檢查網路狀態";
//		}catch(HttpHostConnectException e){
//			isSccuess = false;
////			isCancel = true;
////			hasEvent = true;
//			serverResponse = "網路異常";
//		}catch(SocketException e){
//			isSccuess = false;
////			isCancel = true;
////			hasEvent = true;
////			isCancel = true;
//			serverResponse = "關閉上傳";
//		}catch (IOException e) {
//			isSccuess = false;
//			isCancel = false;
//			hasEvent = true;
//			serverResponse = "網路異常";
//		} catch (Exception e) {
////			isSccuess = false;
////			isCancel = true;
//			serverResponse = "上傳失敗，請重新上傳。";
//		}
//		
//		return serverResponse;
//	}
//
//	@Override
//	protected void onProgressUpdate(Integer... progress) {
//		if (mCallBack != null) {
//			mCallBack.update(progress[0]);
//		}
//	}
//
//	@Override
//	protected void onPostExecute(String param) {
//		Log.d("execute", param + "");
//		if(!isCancel){
//			if (mCallBackMsg != null) {
//				mCallBackMsg.msg(isSccuess, hasEvent, param);
//			}
//		}
//	}
//
//	public void setCallBack(CallBack mCallBack) {
//		this.mCallBack = mCallBack;
//	}
//
//	public void setCallBackMsg(CallBackMsg mCallBackMsg) {
//		this.mCallBackMsg = mCallBackMsg;
//	}
//
//	public interface CallBack {
//		public void update(Integer i);
//	}
//
//	public interface CallBackMsg {
//		public void msg(boolean isSccuess, boolean hasEvent, String msg);
//	}
//
//	public void close(){
//		if(httpPost != null)
//			httpPost.abort();
//		if(httpClient != null && httpClient.getClass()!= null)
//		{
//			httpClient.getConnectionManager().shutdown();
//			httpClient = null;
//		}
//		
//	}
//
//	/**
//	 * 
//	 * @param path
//	 *            文件夾路徑
//	 */
//	public static void isExist(String path) {
//		File file = new File(path);
//		// 判斷文件夾是否存在,如果不存在則建立文件夾
//		if (!file.exists()) {
//			file.mkdir();
//		}
//	}
//
//	private void deleteFile() {
//		File file = new File(filePath);// 複製檔案
//		if (file.exists())
//			file.delete();
//		file = null;
//	}
//
//}
