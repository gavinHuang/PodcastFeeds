package net.gavin.android;


import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String LOG_TAG = Constants.APP_NAME;
	
	private Button btnDownload;
	
	private String[] files;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnDownload=(Button) this.findViewById(R.id.button1);
		btnDownload.setOnClickListener(this);
		
		ListView list = (ListView)findViewById(R.id.listView1);
		
		//prepare for data
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
		list.setAdapter(adapter);
		registerForContextMenu(list);
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

}
