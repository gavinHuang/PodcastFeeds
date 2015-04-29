package net.gavin.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;

public class FileDownloader extends AsyncTask<String,Integer,String>{

	private ProgressBar progressBar;
	
	public FileDownloader(ProgressBar progressBar){
		this.progressBar = progressBar;
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
				long totalSize = response.getEntity().getContentLength();
				long currentSize = 0;
				InputStream is = response.getEntity().getContent();				
				File output = new File(Environment.getExternalStoragePublicDirectory(
			              Environment.DIRECTORY_PODCASTS).getAbsolutePath() + "/" + Constants.APP_NAME + "/" + name);
				FileOutputStream fos = new FileOutputStream(output);
				byte b[] = new byte[102400];
				int content = 0;
				while ((content = is.read(b)) > 0){
					fos.write(b,0,content);
					currentSize+=content;
					publishProgress((int)((currentSize / (float) totalSize) * 100));  
				}
				fos.close();
				is.close();
			}			
		} catch (Exception e) {
			return e.getMessage();
		}
		return "OK";
	}

	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		progressBar.setProgress(values[0]);
	}


	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressBar.setVisibility(View.INVISIBLE);
	}
	
	

}
