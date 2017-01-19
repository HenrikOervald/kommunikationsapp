package com.example.oervald.kommunikationsapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ConversationTodayActivity extends ActionBarActivity {
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems;
    HashMap<String,List<String>> conversations;
    EditText speak;
    ListView text;
    List<String> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setTitle("Beskeder");

        speak =  (EditText) findViewById(R.id.editText_Conversation);
        text = (ListView) findViewById(R.id.list);
        messages = new ArrayList<>();
        conversations = new HashMap<>();
        listItems = new ArrayList<>();


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);
        text.setAdapter(adapter);

        loadToday();

        text.post(new Runnable() {
            @Override
            public void run() {

                text.setSelection(adapter.getCount()-1);
            }
        });


        speak.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (!speak.getText().toString().isEmpty()) {

                        listItems.add(speak.getText().toString());
                        adapter.notifyDataSetChanged();
                        Date date = new Date();
                        // messages.add(speak.getText().toString());

                        saveConversation(speak.getText().toString());


                        speak.setText("");
                        text.post(new Runnable() {
                            @Override
                            public void run() {

                                text.setSelection(adapter.getCount() - 1);
                            }
                        });

                        return true;
                    }
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conversation, menu);
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


    public void loadToday(){
        File f = new File(getFilesDir(), "Conversations");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            String text;

            while ((line = br.readLine()) != null) {
                text = line;
                String[] bits = text.split("#DATE#");
                String date = bits[0];
                String message = bits[1];
                Date toDay = new Date();
                String toDayString = DateFormat.getDateInstance().format(toDay);
                if(date.equals(toDayString)){
                    if(!conversations.containsKey(date)){
                        messages.add(message);
                        conversations.put(date,messages);
                    }else {

                        conversations.get(date).add(message);
                    }
                }

            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found in loadObjects");
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(conversations.get(DateFormat.getDateInstance().format(new Date())) != null) {
            for (String s : conversations.get(DateFormat.getDateInstance().format(new Date()))) {
                    listItems.add(s);
                    adapter.notifyDataSetChanged();
            }
        }
    }

    public void saveConversation(String message){
             File f = new File(getFilesDir(), "Conversations");
        Date date = new Date();
        String stringDate = DateFormat.getDateInstance().format(date);

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(f, true));

        out.write(stringDate + "#DATE#" + message);
        out.newLine();
        out.flush();
        out.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
