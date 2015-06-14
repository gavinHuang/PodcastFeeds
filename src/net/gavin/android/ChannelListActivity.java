package net.gavin.android;

import java.util.List;

import net.gavin.android.database.SubscribeManager;
import net.gavin.android.model.Subscribe;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ChannelListActivity extends Activity implements OnItemClickListener {

	private SubscribeManager subscribeManager = new SubscribeManager(this);

	private String addSubscribeItemTitle = "Add Subscription...";

	String[] subscribeNames = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list);
		ListView listView = refreshListView1();
		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(addSubscribeItemTitle);
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().equalsIgnoreCase(addSubscribeItemTitle)) {

			LayoutInflater li = LayoutInflater.from(this);
			View promptsView = li.inflate(R.layout.activity_popup, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set prompts.xml to alert dialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText subscribeName = (EditText) promptsView
					.findViewById(R.id.subscribeName);
			final EditText subscribeUrl = (EditText) promptsView
					.findViewById(R.id.subscribeURL);

			alertDialogBuilder.setCancelable(true);
			alertDialogBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Subscribe subscribe = new Subscribe();
							subscribe.setName(subscribeName.getText()
									.toString().trim());
							subscribe.setUrl(subscribeUrl.getText().toString().trim());
							subscribeManager.save(subscribe);
							refreshListView1();
						}
					});
			alertDialogBuilder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	private ListView refreshListView1() {

		List<Subscribe> subscribes = subscribeManager.getAllSubscribe();

		subscribeNames = new String[subscribes.size()];
		for (int index = 0; index < subscribes.size(); index++) {
			subscribeNames[index] = subscribes.get(index).getName();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.listitem, subscribeNames);

		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int id, long arg3) {
		String itemName = subscribeNames[id];
				
		Intent myIntent = new Intent(ChannelListActivity.this, SubscribeActivity.class);
		myIntent.putExtra("name", itemName); 
		ChannelListActivity.this.startActivity(myIntent);
	}
}
