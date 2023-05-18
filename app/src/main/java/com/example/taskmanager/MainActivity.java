package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addEvent;
    ListView listView;
    MyTasksDB myTasksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(l -> startNewEvent());

        listView = findViewById(R.id.listView);
        myTasksDB = new MyTasksDB(this);

        List<String> list = myTasksDB.getAllEvents();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);

    }



    public void startNewEvent() {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }
}