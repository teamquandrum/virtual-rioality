package com.example.infomap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.quandrum.atos.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FilterSelector extends Activity implements OnClickListener {
	
	Button button;
    ListView listView;
    ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_selector);
		
		listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.testbutton);
        String[] types = getResources().getStringArray(R.array.Types);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, types);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        button.setOnClickListener(this);
	}
	
	public void onClick(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }
        String[] outputStrArr = new String[selectedItems.size()];
        
        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
        }

String types="";
        for(int i=0;i<outputStrArr.length;i++){
        	types+=outputStrArr[i];
        	if(i!=outputStrArr.length-1)
        	types+="|";
        }//close for
        Intent returnIntent = new Intent();
    	String result=types;
    	try {
			result= URLEncoder.encode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	returnIntent.putExtra("result",result);
    	 setResult(RESULT_OK,returnIntent);     
    	 finish();
       
    }
        

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_selector, menu);
		return true;
	}*/



}
