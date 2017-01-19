package com.example.oervald.kommunikationsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;


public class CreateNewObject extends ActionBarActivity {
    String className;
    String soundFile;
    String fileName;
    private static final int CAMERA_REQUEST = 1888;
    Boolean pictureBolean, soundBolean;

    ImageView pictureView;
    Button start, stop, play, picture, sound, save;
    Bitmap bp;
    MediaRecorder myAudioRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_object);
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        fileName = intent.getStringExtra("fileName");
        soundFile = getFilesDir().getAbsolutePath() + fileName + "3gp";

        pictureView = (ImageView) findViewById(R.id.imageView);

        pictureBolean = false;
        soundBolean = false;

        initiateButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_object, menu);
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



    public void initiateButtons() {
        save = (Button)findViewById(R.id.button_save);
        save.setEnabled(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPicture();
            }
        });

        start = (Button) findViewById(R.id.button_start);
        start.setEnabled(false);

        stop = (Button) findViewById(R.id.button_stop);
        stop.setEnabled(false);

        play = (Button) findViewById(R.id.play);
        play.setEnabled(false);

        picture = (Button) findViewById(R.id.button_picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCam();
            }
        });

        sound = (Button) findViewById(R.id.button_sound);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);

                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stop.setEnabled(true);
                        start.setEnabled(false);
                        start();
                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stop();
                        soundBolean = true;
                        checkBoleans();
                    }
                });

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            play();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    public void start() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(soundFile);
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        stop.setEnabled(false);
        play.setEnabled(true);
        start.setEnabled(true);
        soundBolean = true;
        checkBoleans();
    }

    public void play() throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {

        MediaPlayer m = new MediaPlayer();
        m.setDataSource(soundFile);
        m.prepare();
        m.start();
    }


    public void openCam() {

        PackageManager packageManager = getApplicationContext().getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getApplicationContext(), "Sorry there is no camera", Toast.LENGTH_SHORT).show();
        } else {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) if (requestCode == CAMERA_REQUEST) {
           bp = (Bitmap) data.getExtras().get("data");

            pictureView.setImageBitmap(bp);
            pictureBolean = true;
            checkBoleans();

        }
    }

    public void checkBoleans(){
        if(pictureBolean&&soundBolean){
            save.setEnabled(true);
        }
    }


    public void saveNewPicture() {

        OutputStream fOutputStream = null;

        File file = new File(getFilesDir(), fileName + "1");
        try {
            file.createNewFile();
            System.out.println("File createnewfile absolute Path  " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOutputStream = new FileOutputStream(file);
            bp.compress(Bitmap.CompressFormat.PNG, 100, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File classFile = new File(getFilesDir() + "/" + className);
        try {

            System.out.println(classFile.getAbsolutePath() + "     this is classFileAbsoluthePath()");
            BufferedWriter out = new BufferedWriter(new FileWriter(classFile, true));

            out.write(getFilesDir().getAbsolutePath()+"/"+ fileName+"1" + "," + soundFile);
            out.newLine();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), SubMenuActivity.class);
        intent.putExtra("value", className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
