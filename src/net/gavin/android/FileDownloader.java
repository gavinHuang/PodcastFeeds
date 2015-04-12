package net.gavin.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Environment;

public class FileDownloader extends AsyncTask<String,String,String>{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String doInBackground(String... arg0) {
		String url = arg0[0];
		String name = arg0[1];
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200){			
				InputStream is = response.getEntity().getContent();
				File output = new File(Environment.getExternalStoragePublicDirectory(
			              Environment.DIRECTORY_PODCASTS).getAbsolutePath() + "/" + Constants.APP_NAME + "/" + name);
				FileOutputStream fos = new FileOutputStream(output);
				byte b[] = new byte[1024];
				int content = 0;
				while ((content = is.read(b)) > 0){
					fos.write(b,0,content);
				}
				fos.close();
				is.close();
			}			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
	}

}
