package net.gavin.android;

import java.io.IOException;
import java.util.List;

import net.gavin.android.model.Subscribe;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;

public class PodcastClient {

	
	Subscribe subscribe = null;
	List<PubItem> items = null;
	
	public PodcastClient(Subscribe subscribe){
		this.subscribe = subscribe ;
	}
	
	public PubItem getLatest() throws ClientProtocolException, IOException{
		PubItem item = null;
		String url = subscribe.getUrl();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == 200){
			items = new XMLParser().readXML(response.getEntity().getContent());
			item = items.get(0);
		}
		return item;
	}
}
