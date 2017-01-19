package com.example.oervald.kommunikationsapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;


public class ConversationHistory extends ActionBarActivity {
    ListView list;
    HashMap<String,List<String>> conversations;
    List<String> messages, listItems;
    ArrayAdapter<String> adapter;
    List<String> dates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_history);
        setTitle("Samtalehistorik");
        dates = new ArrayList<>();
        list = (ListView)findViewById(R.id.listView);
        conversations = new HashMap<>();
        messages = new ArrayList<>();
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToHistory = new Intent(getApplicationContext(), ConversationHistoryDateScreen.class);
                String dateThis = dates.get(position);
                goToHistory.putExtra("date",dateThis);
                startActivity(goToHistory);
            }
        });

        loadHistory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conversation_history, menu);
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

    public void loadHistory(){
        File f = new File(getFilesDir(), "Conversations");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            String text;
            int i = 0;
            while ((line = br.readLine()) != null) {
                text = line;
                String[] bits = text.split("#DATE#");
                String date = bits[0];
                String message = bits[1];

                    if(!dates.contains(date)){
                        dates.add(date);
                        listItems.add(date);
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


        if(conversations.keySet() != null) {
            for (String s: conversations.keySet()) {
                listItems.add(s);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
