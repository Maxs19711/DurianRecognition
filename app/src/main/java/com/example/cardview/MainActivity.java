package com.example.cardview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<Pojo> list;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        listShow();
        myAdapter = new MyAdapter(this,list);
        listView.setAdapter(myAdapter);

        ImageButton b1 = (ImageButton)findViewById(R.id.imagebutton1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyDialogFragment().show(getSupportFragmentManager(), "MyDialogFragment");
            }
        });
    }

    private void listShow(){
        list = new ArrayList<>();
        list.add(new Pojo(R.drawable.a,"BMerah","Bunga Merah"));
        list.add(new Pojo(R.drawable.b,"D168","IOI"));
        list.add(new Pojo(R.drawable.c,"D197","Musang king"));
    }

}