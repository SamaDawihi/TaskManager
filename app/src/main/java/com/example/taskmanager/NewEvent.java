package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NewEvent extends AppCompatActivity {

    EditText name, color, note;
    Button date, time, add;
    Spinner type, priority;

    TextView result;

    TaskManagerController controller;


    Calendar calendar;


    String fName, fColor, fDateTime, fDate, fTime, fNote;
    int fType, fPriority, fRemainder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        calendar = Calendar.getInstance();

        controller = new TaskManagerController(this);

        setDefault();
        findViews();
    }

    private void setDefault() {
        fName = fColor = fDateTime = fDate = fTime = fNote = null;
        fType = fPriority = fRemainder = -1;
    }

    private void findViews() {
        name = findViewById(R.id.name);
        //color = findViewById(R.id.color);
        note  = findViewById(R.id.note);
        priority  = findViewById(R.id.priorityList);
        type  = findViewById(R.id.typeList);
        date  = findViewById(R.id.date);
        time  = findViewById(R.id.time);
        add  = findViewById(R.id.add);
        result = findViewById(R.id.result);

        setTypes();
        setPriorities();

        date.setOnClickListener( l -> showDateSelector());
        time.setOnClickListener( l -> showTimeSelector());
        add.setOnClickListener(l -> add());
    }

    void setTypes(){
        List<Type> types = controller.getAllTypes();
        Type[] items = new Type[types.size()];
        int i = 0;
        for (Type t : types){
            items[i++] = t;
            Log.i("TYPE_NAME", t.getName());
        }

        ArrayAdapter<Type> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        type.setAdapter(adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type selectedType = (Type) parent.getItemAtPosition(position);
                fType = selectedType.getTypeId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
                // Your code here
            }
        });
    }
    void setPriorities(){
        Priority p1 = new Priority(NotificationManager.IMPORTANCE_HIGH, "Urgent");
        Priority p2 = new Priority(NotificationManager.IMPORTANCE_DEFAULT, "High");
        Priority p3 = new Priority(NotificationManager.IMPORTANCE_LOW, "Medium");
        Priority p4 = new Priority(NotificationManager.IMPORTANCE_MIN, "Low");

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
                        Log.i("TimePicker","time picked is: " + fTime);

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
                    Log.i("DatePicker","date picked is: " + fDate);

                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.show();

    }

    void showColorSelector(){
    }

    void add(){
        submitData();

        if(! checkData()) {
            Toast.makeText(this,"Must FIll All", Toast.LENGTH_SHORT).show();
            return;
        }
        String r = "Name: " + fName + "\n"
                + "Type: " + fType + "\n"
                + "Color: " + fColor + "\n"
                + "DateTime: " + fDateTime + "\n"
                + "Note: " + fNote + "\n"
                + "Reminder: " + fRemainder + "\n"
                + "Priority: " + fPriority + "\n";
        result.setText(r);

        controller.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainder, fPriority);
    }
    private boolean checkData() {
        if(fName == null)
            return false;
        if(fDateTime == null)
            return false;
        //if(fColor == null) return false;
        if(fNote == null)
            return false;
        if(fPriority < 0)
            return false;
        return true;
    }
    private void submitData() {
        fName = name.getText().toString();
        fColor = null; //color.getText().toString();
        if(fDate != null || fTime != null)
            fDateTime = fDate + " " + fTime;
        fNote = note.getText().toString();
        //fPriority = NotificationManager.IMPORTANCE_HIGH;
        fRemainder = 0; // for later
    }


}