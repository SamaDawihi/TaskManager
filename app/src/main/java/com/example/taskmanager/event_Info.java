package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class event_Info extends AppCompatActivity {
    private Integer eventID;
    MyTasksDB myTasksDB;
    protected EventModel desiredEvent ;

    private TextView name, type, dateAndTime, priority;
    private CheckBox[] tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        Intent intent = getIntent();
        if (intent != null)
            eventID = intent.getIntExtra("eventId",-1);
            myTasksDB = new MyTasksDB(this);
            List<EventModel> eventList = myTasksDB.getAllEvents();
            for (int i=0 ; i<eventList.size();i++){
                if(eventList.get(i).getEventId()==eventID)
                    desiredEvent = eventList.get(i);
            }
        if(eventID != -1){
            name.setText(name.getText()+desiredEvent.getName());
        }
    }

}