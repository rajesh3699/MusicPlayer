package com.example.myplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String>  arrayList;
    ArrayAdapter<String> adapter;
    Cursor songCursor;
    Uri uri;
    Uri filename;

    public static final int MY_PERMISSION_REQUEST=1;
    public MediaPlayer mMediaPlayer;
    int currentPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {

                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

            }


        }else
        {

            result();
        }


       mMediaPlayer=new MediaPlayer();

        Button Playb = findViewById(R.id.PLAY);

        Playb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mMediaPlayer!=null){
                    String name =songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    Toast.makeText(getBaseContext(), name, Toast.LENGTH_SHORT).show();

                    mMediaPlayer.seekTo(currentPos);
                    mMediaPlayer.start();

                }


            }
        });



        Button Pause = (Button)findViewById(R.id.PAUSE);
        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mMediaPlayer!=null){
                    mMediaPlayer.pause();
                    currentPos=mMediaPlayer.getCurrentPosition();
                }
            }
        });


        Button Previous = (Button)findViewById(R.id.PREVIOUS);
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mMediaPlayer!=null &&  songCursor.moveToPrevious()&& songCursor!=null){

                    String name =songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String fileName=songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    Uri u = Uri.parse(fileName);

                    Toast.makeText(getBaseContext(), name, Toast.LENGTH_SHORT).show();

                    mMediaPlayer.reset();

                    try {

                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.setDataSource(getApplicationContext(),u);
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();

                    } catch (IOException e) {
                        e.printStackTrace();

                    }


                }
            }
        });

        Button Next = (Button)findViewById(R.id.NEXT);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mMediaPlayer!=null && songCursor.moveToNext() && songCursor!=null){

                    String name =songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String fileName=songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Toast.makeText(getBaseContext(), name, Toast.LENGTH_SHORT).show();
                    Uri u = Uri.parse(fileName);



                    mMediaPlayer.reset();

                    try {

                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.setDataSource(getApplicationContext(),u);
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();

                    } catch (IOException e) {
                        e.printStackTrace();

                    }


                }
            }
        });


    }






    public void getMusic ()
    {

        ContentResolver contentResolver =getContentResolver();
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
         songCursor=contentResolver.query(uri,null,null,null,null);
        if(songCursor!=null && songCursor.moveToFirst())
        {
            int songTitle=songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            do {
                String song=songCursor.getString(songTitle);
                arrayList.add(song);


            }while (songCursor.moveToNext());




        }
    }
    public void result(){
        listView=findViewById(R.id.okay);
        arrayList=new ArrayList<>();
        getMusic();
        adapter=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {

                songCursor.moveToPosition(position);
                Toast.makeText(getBaseContext(), arrayList.get(position), Toast.LENGTH_SHORT).show();
                String filename=songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Uri u = Uri.parse(filename);

                mMediaPlayer.reset();
                try
                {

                    mMediaPlayer.setDataSource(getApplicationContext(),u);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                }
                catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();}
               catch (IOException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }


        });
    }
    public void onRequestPermissionsResult(int requestCode ,String[] permissions, int[] grantResults){

        switch(requestCode){
            case MY_PERMISSION_REQUEST:{
                if(grantResults.length> 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"permission de diya",Toast.LENGTH_SHORT).show();
                        result();
                    }else{

                        Toast.makeText(this,"permission nhi hai",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

        }



    }


}