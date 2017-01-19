package com.example.oervald.kommunikationsapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ConversationHistoryDateScreen extends  ActionBarActivity {

    ListView listView;
    List<String> listItems;
    ArrayAdapter<String> adapter;
    String date;
    String [] dates;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_history_date_screen);

        intent = getIntent();
        date = intent.getStringExtra("date");
        setTitle(date);
        listItems = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listViewOneDate);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        
        loadToday(date);

        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(adapter.getCount()-1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conversation_history_date_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadToday(String date){
        File f = new File(getFilesDir(), "Conversations");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            String text;

            while ((line = br.readLine()) != null) {
                text = line;
                String[] bits = text.split("#DATE#");
                String dateIn = bits[0];
                String message = bits[1];
                if(dateIn.equals(date)){
                       listItems.add(message);
                       adapter.notifyDataSetChanged();
                }

            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found in loadObjects");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
