package com.example.oervald.kommunikationsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends Activity {

    ImageButton keyboardButton,conversationHistory,phoneGallery;
    TextView latestMessage;
    Vibrator vibo;
    static final public String[] fileNames = {"Personer","Begivenheder","Fysiskebehov", "Steder","Følelser","Mad og drikke","Aktiviteter", "Beskrive","Input Output","Conversations"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");


        vibo = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        latestMessage = (TextView)findViewById(R.id.textView_newText);
        latestMessage.setTextSize(20);



        GridView grid = (GridView) findViewById(R.id.gridview);
        ViewGroup.LayoutParams layoutParams = grid.getLayoutParams();
        layoutParams.height = 900;
        grid.setLayoutParams(layoutParams);
        grid.setAdapter(new ImageAdapter(this));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                vibo.vibrate(30);
                getSubMenu(position);
            }
        });


        keyboardButton =  (ImageButton)findViewById(R.id.keyboardButton);
        keyboardButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        conversationHistory = (ImageButton)findViewById(R.id.conversationHistory);
        conversationHistory.setScaleType(ImageView.ScaleType.CENTER_CROP);
        conversationHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibo.vibrate(30);
                Intent history = new Intent(getApplicationContext(), ConversationHistory.class);
                startActivity(history);
            }
        });

        phoneGallery = (ImageButton)findViewById(R.id.pictures);
        phoneGallery.setScaleType(ImageView.ScaleType.CENTER_CROP);

        phoneGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibo.vibrate(30);
                getSubMenu(101);
            }
        });



        keyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibo.vibrate(30);
                Intent intent = new Intent(getApplicationContext(), ConversationTodayActivity.class);
                startActivity(intent);

            }
        });

        checkAndCreate();
        loadToday();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadToday();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

   

    public void getSubMenu(int position){

        switch(position){
            case 0:
                createNewSubmenuWithParam(fileNames[0]);
                break;
            case 1:

                createNewSubmenuWithParam(fileNames[1]);
                break;
            case 2:

                createNewSubmenuWithParam(fileNames[2]);
                break;
            case 3:

                createNewSubmenuWithParam(fileNames[3]);
                break;
            case 4:

                createNewSubmenuWithParam(fileNames[4]);
                break;
            case 5:

                createNewSubmenuWithParam(fileNames[5]);
                break;
            case 6:

                createNewSubmenuWithParam(fileNames[6]);
                break;
            case 7:

                createNewSubmenuWithParam(fileNames[7]);
                break;
            case 8:

                createNewSubmenuWithParam(fileNames[8]);
                break;
            case 100:

                break;
            case 101:

                Intent intent = new Intent(Intent.ACTION_VIEW, null);
                intent.setType("image/*");
                startActivity(intent);
                break;

        }
    }

    public void createNewSubmenuWithParam(String className){
        Intent intent = new Intent(this,SubMenuActivity.class);
        intent.putExtra("value", className);
        startActivity(intent);
    }

    public void checkAndCreate(){

        File [] fileList = getApplicationContext().getFilesDir().listFiles();

        ArrayList<String> fileNameArrayList = new ArrayList<String>();

        for(File f : fileList){
            fileNameArrayList.add(f.getName());
        }

        for(String fileName : fileNames){
            if(!fileNameArrayList.contains(fileName)){

                File newFile = new File(getFilesDir(),fileName);
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),newFile.getName(),Toast.LENGTH_SHORT).show();
            }
        }


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
                       latestMessage.setText(message);
                  }else{latestMessage.setText("Ingen beskeder i dag");}

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


