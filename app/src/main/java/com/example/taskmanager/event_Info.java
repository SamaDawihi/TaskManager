package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class event_Info extends AppCompatActivity {
    Integer eventID;
    //MyTasksDB myTasksDB;
    EventModel eventModel ;
    TextView name, type, dateAndTime, priority;
    // CheckBox[] tasksCheckboxes;
    TaskManagerController taskcontroller;
    Type[] types;
    String typeString;
    Task[] tasks;
    //TableRow toBeRemovedRow, newRow;
    //TableLayout parentOfRow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        taskcontroller = new TaskManagerController(this);
        name = findViewById(R.id.tvname);
        type = findViewById(R.id.tvtype);
        dateAndTime = findViewById(R.id.tvdatetime);
        priority = findViewById(R.id.tvpriority);
        Intent intent = getIntent();
        if (intent != null) {
            eventID = intent.getIntExtra("eventId", -1);
            eventModel = taskcontroller.getEventById(eventID);

            types = taskcontroller.getAllTypes().toArray(new Type[0]);
            //finding the type string
            for (int i=0; i<types.length;i++){
                if (types[i].getTypeId()==eventModel.getTypeId())
                    typeString = types[i].getName();
            }
            //we have the type string now
            tasks = taskcontroller.getTasksByEventId(eventID).toArray(new Task[0]);
           /* if (tasks.length == 0){
                TableLayout parent = findViewById(R.id.tablelayout);
                TableRow buttonsrow = findViewById(R.id.targetrow);
                int index = parent.indexOfChild(buttonsrow);
                TableRow space = new TableRow(this);
                space.setPadding(0,0,0,100);
                parent.addView(space,index);
            }
            else{

            }*/

            //printing to the screen
            name.setText("Name:     "+eventModel.getName());
            type.setText("Type:     "+typeString);
            dateAndTime.setText("Date&Time:     "+eventModel.getDateTime());
        }

    }
           /* myTasksDB = new MyTasksDB(this);
            List<EventModel> eventList = myTasksDB.getAllEvents();
            for (int i=0 ; i<eventList.size();i++){
                if(eventList.get(i).getEventId()==eventID)
                    desiredEvent = eventList.get(i);
            }
        if(eventID != -1){
            name.setText(name.getText()+desiredEvent.getName());
            //type.setText(type.getText()+desiredEvent.getTypeId());
            dateAndTime.setText(dateAndTime.getText()+desiredEvent.getDateTime());
            //priority.setText(priority.getText()+desiredEvent.getPriority());
        }*/
    /*if(tasks != null){
                toBeRemovedRow = (TableRow) findViewById(R.id.toberemoved);
                parentOfRow = (TableLayout) findViewById(R.id.tablelayout);
                parentOfRow.removeView(toBeRemovedRow);
                for (int i=0; i<tasks.length; i++){
                    if(tasks[i]!=null){
                       newRow = new TableRow(this);
                        View targetRow = findViewById(R.id.targetrow);
                        int targetIndex = parentOfRow.indexOfChild(targetRow);
                        parentOfRow.addView(newRow, targetIndex);
                        newRow.setBackgroundColor(Color.BLUE);

                    }
                }
            }*/
}