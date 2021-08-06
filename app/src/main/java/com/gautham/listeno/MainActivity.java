package com.gautham.listeno;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.Manifest;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview= findViewById(R.id.listviewsong);
        runtimePremission();

    }
    public void runtimePremission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
               displaysongs();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                 permissionToken.cancelPermissionRequest();
            }
        }).check();

    }
    public ArrayList<File> findsong(File file){
        ArrayList<File> arraylist= new ArrayList<>();
        File[] files=file.listFiles();
        for(File singlefile:files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arraylist.addAll(findsong(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){
                    arraylist.add(singlefile);
                }
            }
        }
        return arraylist;
    }
    public void displaysongs(){
        final ArrayList<File> mysong= findsong(Environment.getExternalStorageDirectory());
        items=new String[mysong.size()];
        for(int i=0;i<mysong.size();i++){
            items[i]=mysong.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
       // ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        //listview.setAdapter(myAdapter);
        customadapter ca=new customadapter();
        listview.setAdapter(ca);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songname=(String)listview.getItemAtPosition(i);
                startActivity(new Intent(getApplicationContext(),playerActivity.class)
                .putExtra("songs",mysong)
                        .putExtra("songname",songname)
                        .putExtra("pos",i)
                );
            }
        });
    }
    class customadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View mview=getLayoutInflater().inflate(R.layout.listitem,null);
            TextView songname=mview.findViewById(R.id.songname);
            songname.setSelected(true);
            songname.setText(items[i]);
            return mview;
        }
    }
}