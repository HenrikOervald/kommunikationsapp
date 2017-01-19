package com.example.oervald.kommunikationsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class SubMenuActivity extends ActionBarActivity {

    ImageButton keyboardButton, phoneGallery, plus;
    String classTitle;
    Intent intent;
    Vibrator vibo;
    HashMap<String, String> objects = new HashMap<>();
    SubMenuImageAdapter subMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);
        intent = (Intent) getIntent();
        classTitle = intent.getStringExtra("value");
        setTitle(classTitle);

        vibo = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        GridView grid = (GridView) findViewById(R.id.gridViewSubView);
        ViewGroup.LayoutParams layoutParams = grid.getLayoutParams();
        layoutParams.height = 900;
        grid.setLayoutParams(layoutParams);
        grid.setAdapter( subMenu =new SubMenuImageAdapter(this, classTitle));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vibo.vibrate(30);
              String name = subMenu.itemList.get(position);
                try {
                    playSound(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        initComponents();
        loadObjects();
    }

    private void initComponents() {
        keyboardButton = (ImageButton) findViewById(R.id.keyboardButton);
        keyboardButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        keyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibo.vibrate(30);
                Intent intent = new Intent(getApplicationContext(), ConversationTodayActivity.class);
                startActivity(intent);
            }
        });

        plus = (ImageButton) findViewById(R.id.plus);
        plus.setScaleType(ImageView.ScaleType.CENTER_CROP);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibo.vibrate(30);

                makeNewObjectText();
            }
        });

        phoneGallery = (ImageButton) findViewById(R.id.pictures);
        phoneGallery.setScaleType(ImageView.ScaleType.CENTER_CROP);

        phoneGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibo.vibrate(30);
                Intent intent = new Intent(Intent.ACTION_VIEW, null);
                intent.setType("image/*");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_menu, menu);
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


    public void makeNewObjectText() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText text = new EditText(this);
        builder.setTitle("Opret nyt objekt");
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(text);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CreateNewObject.class);
                intent.putExtra("className", classTitle);
                intent.putExtra("fileName", text.getText().toString());
                startActivity(intent);
            }
        });

        builder.show();
    }

    public void loadObjects() {

        File file = new File(getFilesDir(), classTitle);
        System.out.println(file.getAbsolutePath());

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String text;

            while ((line = br.readLine()) != null) {
                text = line;
                String[] bits = text.split(",");
                objects.put(bits[0], bits[1]);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found in loadObjects");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playSound(String name) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {
        String soundName = objects.get(name);
        MediaPlayer m = new MediaPlayer();
        m.setDataSource(soundName);
        m.prepare();
        m.start();
    }
}




