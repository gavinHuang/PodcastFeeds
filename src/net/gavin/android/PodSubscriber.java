package net.gavin.android;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;

public class PodSubscriber {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub

		PodSubscriber pod = new PodSubscriber();
		PubItem item = pod.getLatest();
		System.out.println(item.getUrl());
		System.out.println(item.getPubDate());
		System.out.println(item.getLength());
	}
	
	public PubItem getLatest() throws ClientProtocolException, IOException{
		PubItem item = null;
		String url = "http://podcastfeeds.nbcnews.com/audio/podcast/MSNBC-Nightly.xml";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == 200){			
			item = new XMLParser().readXML(response.getEntity().getContent());
		}
		return item;
	}
}
