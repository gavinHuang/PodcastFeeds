package net.gavin.android;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.gavin.android.database.SubscribeManager;
import net.gavin.android.model.Subscribe;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SubscribeActivity extends Activity {

	private static final String LOG_TAG = Constants.APP_NAME;
	
	private SubscribeManager subscribeManager = new SubscribeManager(this);

	private ProgressBar progressBar;
	
	private String[] files;
	
	private String selectedFileName;
	
	Subscribe subscribe = null;
	
	String rootPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PODCASTS).getAbsolutePath() + "/" + Constants.APP_NAME;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE);
		
		Intent intent = getIntent();
		String value = intent.getStringExtra("name"); 
		rootPath += ("/" + value);
		subscribe = subscribeManager.getSubscribeByName(value);
		
		ListView listView = refreshListView1();
		registerForContextMenu(listView);
		
		//update title
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Refresh");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Refresh")){
			refresh();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.listView1) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    	    selectedFileName = files[info.position];
    		String[] menuItems = getResources().getStringArray(R.array.menu); 
    		for (int i = 0; i<menuItems.length; i++) {
    			menu.add(Menu.NONE, i, i, menuItems[i]);
			}
    	}
    }	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    int menuItemIndex = item.getItemId();
		String[] menuItems = getResources().getStringArray(R.array.menu);
		String menuItemName = menuItems[menuItemIndex];
	    if (menuItemName.equalsIgnoreCase("play")){
	    	//play 	    	
	    	String fileUrl = "file:///"+ rootPath + "/" + selectedFileName;
	    	
	    	Intent playAudioIntent = new Intent(Intent.ACTION_VIEW);
	    	playAudioIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	Uri url = Uri.parse(fileUrl);
	    	playAudioIntent.setDataAndType(url, "audio/mp3");
	    	startActivity(playAudioIntent);
	    	return true;
	    }
	    if (menuItemName.equalsIgnoreCase("delete")){
	    	//delete 	    	
	    	File mediaFile = new File(rootPath + "/" + selectedFileName);
	    	mediaFile.delete();
	    	
	    	refreshListView1();
	    	return true;
	    }		
		return super.onContextItemSelected(item);
	}
	
	
	@Override
	protected void onResume() {
		refreshListView1();
		super.onResume();
	}

	private ListView refreshListView1(){
		File mediaStorageDir = new File(rootPath);
		if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d(LOG_TAG, "failed to create directory");	            
	        }
	    }
		files = mediaStorageDir.list();
		if (files == null){
			files = new String[]{"no file permission!"};
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem, files);
		ListView list = (ListView)findViewById(R.id.listView1);
		list.setAdapter(adapter);
		return list;
	}
	
	private void refresh(){
		PodcastClient podSubscribeClient = new PodcastClient(subscribe);
		try {
			PubItem item = podSubscribeClient.getLatest();
			boolean latest = false;
			for(String fileName:files){
				if (fileName.equalsIgnoreCase(item.getFileName())){
					latest = true;
				}
			}
			if (!latest){
				//refresh
				progressBar.setVisibility(View.VISIBLE);
				FileDownloader downloadFIle=new FileDownloader();
				downloadFIle.execute(item.getUrl(), item.getFileName());				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	class FileDownloader extends AsyncTask<String,Integer,String>{		
		
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
					File output = new File(rootPath + "/" + name);
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
			Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			progressBar.setVisibility(View.INVISIBLE);
		}
		
		

	}
}
