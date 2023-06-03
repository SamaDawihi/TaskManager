package com.example.taskmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class UpdateEvent extends AppCompatActivity {

    //-------------intent-----------
    Intent startedIntent;
    int eventId;

    //---------------------------Views-----------------------------
    TableLayout table;
    EditText name, note, reminderDuration, sub1, sub2, sub3, sub4, newTypeName;
    Button addNewType, date, time, add, addTask1, removeTask2, removeTask3, removeTask4, newTypeColor, submitNewType, save_btn, cancle_btn;
    Spinner type, priority, reminderUnit;
    TextView dateTV, timeTV, result;
    TableRow row1,row2, row3,row4, newTypeRow;
    //---------------------------Views-----------------------------
    TaskManagerController taskcontroller;

    List<String> visibleRows;
    List<String> errors;
    Calendar calendar;
    Task[] task;
    EventModel selectedEvent;

    TaskManagerController controller;

    //---------------------------Outputs Variables-----------------------------
    String fName, fDateTime, fDate, fTime, fNote,fReminderUnit, fNewTypeName, fNewTypeIcon;
    String fSubTask1, fSubTask2, fSubTask3, fSubTask4;
    int fTypeId, fPriority, fReminderDuration, fColor, fNewTypeColor;
    //---------------------------Outputs Variables-----------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        taskcontroller = new TaskManagerController(this);
        calendar = Calendar.getInstance();
        controller = new TaskManagerController(this);
        errors = new ArrayList<>();

        startedIntent = getIntent();
        eventId = startedIntent.getIntExtra("eventId", -1);

        findViews();
        hideSubTasksAndNewTypeRows();
        getTaskByEventId(eventId);
        getEvent(eventId);

        setTypesSpinner();
        setUnitsSpinner();
        setPrioritiesSpinner();
        setOnClickListeners();

    }

    public void getTaskByEventId(int id) {
        task = taskcontroller.getTasksByEventId(id).toArray(new Task[0]);

    }

    private void findViews() {
        name = findViewById(R.id.eventName);
        note  = findViewById(R.id.note);
        reminderDuration = findViewById(R.id.reminderDuration);
        reminderUnit = findViewById(R.id.reminderUnit);
        priority  = findViewById(R.id.priorityList);
        type  = findViewById(R.id.typeList);
        newTypeName = findViewById(R.id.newTypeName);
        date  = findViewById(R.id.date);
        dateTV = findViewById(R.id.dateTV);
        timeTV = findViewById(R.id.timeTV);
        time  = findViewById(R.id.time);
        cancle_btn=findViewById(R.id.cancle_btn);
        save_btn=findViewById(R.id.save_btn);
        addNewType = findViewById(R.id.addType);
        newTypeColor = findViewById(R.id.newTypeColor);
        submitNewType = findViewById(R.id.submitNewType);

        table  = findViewById(R.id.table);

        row1  = findViewById(R.id.r1);
        row2  = findViewById(R.id.r2);
        row3  = findViewById(R.id.r3);
        row4  = findViewById(R.id.r4);
        newTypeRow = findViewById(R.id.newTypeRow);

        sub1 = findViewById(R.id.subTask1);
        sub2 = findViewById(R.id.subTask2);
        sub3 = findViewById(R.id.subTask3);
        sub4 = findViewById(R.id.subTask4);

        addTask1 = findViewById(R.id.addSub1);
        removeTask2 = findViewById(R.id.removeSub2);
        removeTask3 = findViewById(R.id.removeSub3);
        removeTask4 = findViewById(R.id.removeSub4);

        add  = findViewById(R.id.add);
        result = findViewById(R.id.result);
    }
    private void hideSubTasksAndNewTypeRows() {
        row2.setVisibility(View.GONE);
        row3.setVisibility(View.GONE);
        row4.setVisibility(View.GONE);
        newTypeRow.setVisibility(View.GONE);
        visibleRows = new ArrayList<>();
    }
    public void getEvent(int id){
        selectedEvent = controller.getEventById(id);

        name.setText(selectedEvent.getName()) ;
        note.setText(selectedEvent.getNote());
        reminderDuration.setText(selectedEvent.getReminderDuration() == -1? "" : selectedEvent.getReminderDuration() + "");


        dateTV = findViewById(R.id.dateTV);

        dateTV.setText(selectedEvent.getDateTime().substring(0,selectedEvent.getDateTime().indexOf(' ')));
        fDate=selectedEvent.getDateTime().substring(0,selectedEvent.getDateTime().indexOf(' '));
        timeTV.setText(selectedEvent.getDateTime().substring(selectedEvent.getDateTime().indexOf(' '))+1);
        fTime=selectedEvent.getDateTime().substring(selectedEvent.getDateTime().indexOf(' '))+1;

        if(task.length>0)
        {
            sub1.setText(task[0].getDescription());
        }
        if(task.length>1)
        {
            row2.setVisibility(View.VISIBLE);
            visibleRows.add("Row2");
            sub2.setText(task[1].getDescription());
        }
        if(task.length>2)
        {
            row3.setVisibility(View.VISIBLE);
            visibleRows.add("Row3");
            sub3.setText(task[2].getDescription());
        }
        if(task.length>3)
        {
            row4.setVisibility(View.VISIBLE);
            visibleRows.add("Row4");
            sub4.setText(task[3].getDescription());
        }



    }
    void setTypesSpinner(){
        List<Type> types = controller.getAllTypes();

        Type[] items = new Type[types.size()];
        int i = 1;
        for (Type t : types){
            if(t.getTypeId() == selectedEvent.getTypeId())
                items[0] = t;
            else
                items[i++] = t;
        }

        ArrayAdapter<Type> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        type.setAdapter(adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type selectedType = (Type) parent.getItemAtPosition(position);
                fTypeId = selectedType.getTypeId();
                fColor = selectedType.getColor();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
                // Your code here
                fTypeId = -1;
            }
        });
    }
    private void setUnitsSpinner() {
        String[] items = new String[]{"At time of event", "Minutes", "Hours", "Days", "Weeks"}; //modified

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        reminderUnit.setAdapter(adapter);

        reminderUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fReminderUnit = (String) parent.getItemAtPosition(position);
                if(fReminderUnit.equals("At time of event")){
                    reminderDuration.setVisibility(View.GONE);
                }else{
                    reminderDuration.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
                // Your code here
            }
        });
    }
    void setPrioritiesSpinner(){
        Priority p1 = new Priority(NotificationCompat.PRIORITY_HIGH, "Urgent");
        Priority p2 = new Priority(NotificationCompat.PRIORITY_DEFAULT, "High");
        Priority p3 = new Priority(NotificationCompat.PRIORITY_LOW, "Medium");
        Priority p4 = new Priority(NotificationCompat.PRIORITY_MIN, "Low");

        Priority[] items = new Priority[]{p1,p2,p3, p4};

        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        priority.setAdapter(adapter);

        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Priority selectedP = (Priority) parent.getItemAtPosition(position);
                fPriority = selectedP.getPriority();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
                // Your code here
            }
        });
    }
    private void setOnClickListeners() {
        addNewType.setOnClickListener( l -> {
            if(newTypeRow.getVisibility() == View.GONE) {
                newTypeRow.setVisibility(View.VISIBLE);
                addNewType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.baseline_remove_24);

            } else{
                newTypeRow.setVisibility(View.GONE);
                newTypeName.setText("");
                fNewTypeColor = Color.BLUE;
                addNewType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.baseline_add_24);

            }
        });
        newTypeColor.setOnClickListener(l -> showColorPicker());
        submitNewType.setOnClickListener(l -> submitNewType());
        date.setOnClickListener( l -> showDateSelector());
        time.setOnClickListener( l -> showTimeSelector());
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UpdateEvent.this,MainActivity.class));
                finish();

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitChanages();
                if (checkData()){
                    updateEvent(eventId);
                }
                else
                {
                    displayErrors();
                }

            }
        });

        addTask1.setOnClickListener( l -> {
            if(!visibleRows.contains("Row2")) {
                row2.setVisibility(View.VISIBLE);
                visibleRows.add("Row2");
            }
            else if(!visibleRows.contains("Row3")) {
                row3.setVisibility(View.VISIBLE);
                visibleRows.add("Row3");
            }
            else if(!visibleRows.contains("Row4")) {
                row4.setVisibility(View.VISIBLE);
                visibleRows.add("Row4");
            }else {
                Toast.makeText(this, "Max is 4 subtasks", Toast.LENGTH_SHORT).show();
            }
        });
        removeTask2.setOnClickListener(l -> {
            row2.setVisibility(View.GONE);
            visibleRows.remove("Row2");
            fSubTask2="";

        });
        removeTask3.setOnClickListener(l -> {
            row3.setVisibility(View.GONE);
            visibleRows.remove("Row3");
            fSubTask3="";

        });
        removeTask4.setOnClickListener(l -> {
            row4.setVisibility(View.GONE);
            visibleRows.remove("Row4");
            fSubTask4="";

        });



    }
    private void showColorPicker() {
        AmbilWarnaDialog aWDialog = new AmbilWarnaDialog(this, fNewTypeColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // color is the color selected by the user.
                fNewTypeColor = color;
                newTypeColor.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });
        aWDialog.show();
    }
    private void submitNewType() {
        fNewTypeName = newTypeName.getText().toString();
        if(fNewTypeName == "" || fNewTypeName == null){
            Toast.makeText(this, "Faild To add new Type, Make Sure To Write a name", Toast.LENGTH_SHORT).show();
            fNewTypeName = null;
        }else {
            Toast.makeText(this, "New Type " + fNewTypeName + " Added Successfully", Toast.LENGTH_SHORT).show();
            newTypeRow.setVisibility(View.GONE);
            controller.addType(fNewTypeName, fNewTypeColor);
            setTypesSpinner();
        }
    }
    void showTimeSelector(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        // HH:mm:ss
                        String min = String.valueOf(minute);
                        if(minute < 10) min = "0" + min;

                        String hour = String.valueOf(hourOfDay);
                        if(hourOfDay < 10) hour = "0" + hour;
                        fTime = hour + ":" + min + ":00";
                        timeTV.setText(fTime);

                    }
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);

        timePickerDialog.show();
    }
    void showDateSelector(){
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // HH:mm:ss
                    fDate = dateFormat.format(calendar.getTime());
                    dateTV.setText(fDate);

                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.show();

    }
    private void displayErrors() {
        // Concatenate the error messages
        StringBuilder errorMessage = new StringBuilder();
        for (String error : errors) {
            errorMessage.append("- ").append(error).append("\n");
        }

        // Create and configure the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Errors")
                .setMessage(errorMessage.toString())
                .setPositiveButton("OK", null);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        errors.clear();
    }
    private boolean checkData() {
        if(fName == null || fName == "") {
            errors.add("SET NAME");
            return false;
        }
        if(fTypeId < 0 ){//|| controller.doesTypeExist(fTypeId)) {
            errors.add("SELECT TYPE");
            return false;
        }
        if(fDateTime == null) {
            errors.add("SET DATE AND TIME");
            return false;
        }
        if(fNote == null || fNote == "") {
            fNote = "";
            return false;
        }
        if(fReminderUnit == null) {
            errors.add("SELECT REMINDER UNIT");
        }
        if(fReminderDuration < 0) {
            if(!fReminderUnit.equals("At time of event"))
                errors.add("SET Reminder Duration");
        }
        if(fPriority < -2 || fPriority > 1) {
            errors.add("SELECT PRIORITY");
        }
        return true;
    }
    public void  updateEvent(int id){
        taskcontroller.updateEventById(id,fName,fTypeId,fColor,fDateTime,fNote,fReminderDuration,fReminderUnit,fPriority, this);


        Toast.makeText(this, "change saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UpdateEvent.this,MainActivity.class));
        finish();


    }
    @SuppressLint("SuspiciousIndentation")
    public void submitChanages(){
        fName = name.getText().toString();
        if(fDate != null && fTime != null)
            fDateTime = fDate + " " + fTime;
        else
            fDateTime = null;

        fNote = note.getText().toString();

        String reminderD = reminderDuration.getText().toString();
        if ( !reminderD.equals(""))
            fReminderDuration = Integer.parseInt(reminderD.replaceAll("\\s", ""));

        fSubTask1 = fSubTask2 = fSubTask3 = fSubTask4 = null;

        fSubTask1 = sub1.getText().toString();
        if(visibleRows.contains("Row2"))
            fSubTask2 = sub2.getText().toString();
        if(visibleRows.contains("Row3"))
            fSubTask3 = sub3.getText().toString();
        if(visibleRows.contains("Row4"))
            fSubTask4 = sub4.getText().toString();



        //add, update or remove task1
        if("".equalsIgnoreCase(fSubTask1) || fSubTask1==null)
        {
            if(task.length >= 1) {
                taskcontroller.removeTask(task[0].getTaskId());
                Toast.makeText(this, "removeTask", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(task.length >= 1) {
                taskcontroller.updateTaskById(task[0].getTaskId(),fSubTask1+"");
                Toast.makeText(this, "updateTaskById", Toast.LENGTH_SHORT).show();

            }
            else {
                taskcontroller.addTask(eventId,fSubTask1+"");
                Toast.makeText(this, "addTask", Toast.LENGTH_SHORT).show();
            }
        }

        /////////////////////

        if("".equalsIgnoreCase(fSubTask2) || fSubTask2 == null)
        {
            if(task.length>=2)
            {

                taskcontroller.removeTask (task[1].getTaskId());
                Toast.makeText(this, "removeTask 2", Toast.LENGTH_SHORT).show();
            }


        }
        else
        {
            if(task.length>=2)
            {
                taskcontroller.updateTaskById(task[1].getTaskId(),fSubTask2+"");
                Toast.makeText(this, "updateTaskById", Toast.LENGTH_SHORT).show();
            }
            else
            {
                taskcontroller.addTask(eventId,fSubTask2+"");
                Toast.makeText(this, "addTask", Toast.LENGTH_SHORT).show();
            }
        }

        if("".equalsIgnoreCase(fSubTask3) || fSubTask3 == null)
        {
            if(task.length>=3)
            {
                taskcontroller.removeTask(task[2].getTaskId());
                Toast.makeText(this, "removeTask 2", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(task.length>=2)
            {
                taskcontroller.updateTaskById(task[2].getTaskId(),fSubTask3+"");
                Toast.makeText(this, "updateTaskById", Toast.LENGTH_SHORT).show();
            }
            else
            {
                taskcontroller.addTask(eventId,fSubTask3+"");
                Toast.makeText(this, "addTask", Toast.LENGTH_SHORT).show();
            }



        }

        if("".equalsIgnoreCase(fSubTask4) || fSubTask4==null)
        {
            if(task.length>=4)
            {

                taskcontroller.removeTask(task[3].getTaskId());
                Toast.makeText(this, "removeTask 2", Toast.LENGTH_SHORT).show();
            }


        }
        else
        {

            if(task.length>=4)
            {

                taskcontroller.updateTaskById(task[3].getTaskId(),fSubTask4+"");
                Toast.makeText(this, "updateTaskById", Toast.LENGTH_SHORT).show();

            }
            else
            {
                taskcontroller.addTask(eventId,fSubTask4+"");
                Toast.makeText(this, "addTask", Toast.LENGTH_SHORT).show();
            }
        }

    }



}