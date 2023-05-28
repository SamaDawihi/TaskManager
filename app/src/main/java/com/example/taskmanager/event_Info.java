package com.example.taskmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class event_Info extends AppCompatActivity {
    Integer eventID;
    EventModel eventModel ;
    TextView name, type, dateAndTime,note, priority, state;
    // CheckBox[] tasksCheckboxes;
    TaskManagerController taskcontroller;
    Type[] types;
    String typeString, priorityString;
    Task[] tasks;
    //TableRow toBeRemovedRow, newRow;
    //TableLayout parentOfRow;
    Button editButton, deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("EVENTINFO", "INFO");
        setContentView(R.layout.activity_event_info);
        taskcontroller = new TaskManagerController(this);
        name = findViewById(R.id.tvname);
        type = findViewById(R.id.tvtype);
        dateAndTime = findViewById(R.id.tvdatetime);
        note = findViewById(R.id.tvnote);
        priority = findViewById(R.id.tvpriority);
        state= findViewById(R.id.tvstate);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
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

            int pInt = eventModel.getPriority();
            switch (pInt){
                case 4:
                    priorityString = "Urgent";
                    break;
                case 3:
                    priorityString = "High";
                    break;
                case 2:
                    priorityString = "Medium";
                    break;
                case 1:
                    priorityString = "Low";
                    break;
            }
            tasks = taskcontroller.getTasksByEventId(eventID).toArray(new Task[0]);
            boolean done;
            if (tasks!=null){
                TableRow row;
                int nor = tasks.length;
                if(nor!=0) {
                    row = findViewById(R.id.row1);
                    row.setVisibility(row.VISIBLE);
                    CheckBox chB1 = (CheckBox) row.getChildAt(0);
                    chB1.setText(tasks[0].getDescription());
                    done = tasks[0].isDone();
                    if (done == true)
                        chB1.setChecked(true);
                    boolean finalDone = done;
                    chB1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            taskcontroller.changeTaskStatus(tasks[0].getTaskId(),!finalDone);
                        }
                    });
                    nor--;
                }
                if (nor!=0){
                    row = findViewById(R.id.row2);
                    row.setVisibility(row.VISIBLE);
                    CheckBox chB2 =(CheckBox) row.getChildAt(0);
                    chB2.setText(tasks[1].getDescription());
                    done = tasks[1].isDone();
                    if (done)
                        chB2.setChecked(true);
                    boolean finalDone1 = done;
                    chB2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            taskcontroller.changeTaskStatus(tasks[1].getTaskId(),!finalDone1);
                        }
                    });
                    nor --;
                }
                if (nor!=0){
                    row = findViewById(R.id.row3);
                    row.setVisibility(row.VISIBLE);
                    CheckBox chB3 =(CheckBox) row.getChildAt(0);
                    chB3.setText(tasks[2].getDescription());
                    done = tasks[2].isDone();
                    if (done==true)
                        chB3.setChecked(true);
                    boolean finalDone2 = done;
                    chB3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            taskcontroller.changeTaskStatus(tasks[2].getTaskId(),!finalDone2);
                        }
                    });
                    nor --;
                }
                if (nor!=0){
                    row = findViewById(R.id.row4);
                    row.setVisibility(row.VISIBLE);
                    CheckBox chB4 =(CheckBox) row.getChildAt(0);
                    chB4.setText(tasks[3].getDescription());
                    done = tasks[3].isDone();
                    if (done==true)
                        chB4.setChecked(true);
                    boolean finalDone3 = done;
                    chB4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            taskcontroller.changeTaskStatus(tasks[3].getTaskId(),!finalDone3);
                        }
                    });
                }
            }
            int checkFromNotification = intent.getIntExtra("fromNotification",-1);
            if (checkFromNotification != -1){
                deleteButton.setText("confirm");
                editButton.setText("dismiss");
            }

            //printing to the screen
            name.setText("Name:     "+eventModel.getName());
            type.setText("Type:     "+typeString);
            dateAndTime.setText("Date&Time:     "+eventModel.getDateTime());
            note.setText("note:     "+eventModel.getNote());
            priority.setText("priority:     "+priorityString);
            state.setText("state:     "+eventModel.getState());
        }

    }
    public void buttonActions (View view){
        if (((Button)view).getText().equals("delete")){
            AlertDialog.Builder builder = new AlertDialog.Builder(event_Info.this);

            builder.setTitle("Confirmation")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            taskcontroller.removeEvent(eventID, getApplicationContext());
                            Intent intent = new Intent(event_Info.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null) // null to dismiss the dialog without performing any action
                    .show();
        }
        else if(((Button)view).getText().equals("edit")){
            view.setOnClickListener(v -> {
                Intent EUIntent = new Intent(this, UpdateEvent.class);
                EUIntent.putExtra("eventId", eventID);
                startActivity(EUIntent);
            });
        }
        else if(((Button)view).getText().equals("confirm")){
            taskcontroller.changeEventState(eventID,"confirmed");
        }
        else if(((Button)view).getText().equals("dismiss")){
            taskcontroller.changeEventState(eventID,"dismissed");
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
    /*     TableLayout parent = findViewById(R.id.tablelayout);
                TableRow buttonsrow = findViewById(R.id.targetrow);
                int index = parent.indexOfChild(buttonsrow);
                TableRow space = new TableRow(this);
                space.setPadding(0,0,0,1000);
                parent.addView(space,index);*/
}