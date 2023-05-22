package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
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

        List<EventModel> eventList = myTasksDB.getAllEvents();
        List<String> list = new ArrayList<>();


        for(int i = 0; i < eventList.size(); i++){
            list.add( eventList.get(i).getDateTime()+ "\n"+ eventList.get(i).getEventId() +" "+ eventList.get(i).getName());
            listView.setBackgroundColor(eventList.get(i).getColor());

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);


    }



    public void startNewEvent() {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }

    public void viewDetails(View v, int eventID){
        Intent intent = new Intent(this,event_Info.class);
        intent.putExtra("eventId",eventID);
        startActivity(intent);
    }
}