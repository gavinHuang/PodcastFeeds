package net.gavin.android;


import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String LOG_TAG = Constants.APP_NAME;
	
	private Button btnDownload;
	
	private String[] files;
	
	private String selectedFileName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnDownload=(Button) this.findViewById(R.id.button1);
		btnDownload.setOnClickListener(this);
		ListView listView = refreshListView1();
		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {	
		PodSubscriber podSubscriber = new PodSubscriber();
		try {
			PubItem item = podSubscriber.getLatest();
			boolean latest = false;
			for(String fileName:files){
				if (fileName.equalsIgnoreCase(item.getFileName())){
					latest = true;
				}
			}
			if (!latest){
				//refresh
				FileDownloader downloadFIle=new FileDownloader();
				downloadFIle.execute(item.getUrl(), item.getFileName());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
	    	String fileUrl = "file://"+Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PODCASTS).getAbsolutePath() + "/" + Constants.APP_NAME + "/" + selectedFileName;
	    	
	    	Intent playAudioIntent = new Intent(Intent.ACTION_VIEW);
	    	playAudioIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	Uri url = Uri.parse(fileUrl);
	    	playAudioIntent.setDataAndType(url, "audio/mp3");
	    	startActivity(playAudioIntent);
	    	return true;
	    }
	    if (menuItemName.equalsIgnoreCase("delete")){
	    	//delete 	    	
	    	File mediaFile = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PODCASTS).getAbsolutePath() + "/" + Constants.APP_NAME + "/" + selectedFileName);
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
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PODCASTS), Constants.APP_NAME);
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
	
	

}
