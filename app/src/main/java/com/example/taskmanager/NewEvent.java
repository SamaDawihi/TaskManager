package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;

public class NewEvent extends AppCompatActivity {
    //---------------------------Views-----------------------------
    EditText name, note, reminderDuration, sub1, sub2, sub3, sub4, newTypeName;
    Button addNewType, date, time, add, addTask1, removeTask2, removeTask3, removeTask4, newTypeColor, submitNewType;
    Spinner type, priority, reminderUnit;
    TextView dateTV, timeTV;
    LinearLayout row1,row2, row3,row4, newTypeRow;
    //---------------------------Views-----------------------------

    List<String> visibleRows;
    List<String> errors;

    TaskManagerController controller;

    Calendar calendar;

    //---------------------------Outputs Variables-----------------------------
    String fName, fDateTime, fDate, fTime, fNote,fReminderUnit, fNewTypeName, fNewTypeIcon;
    String fSubTask1, fSubTask2, fSubTask3, fSubTask4;
    int fTypeId, fPriority, fReminderDuration, fColor, fNewTypeColor;
    //---------------------------Outputs Variables-----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        calendar = Calendar.getInstance();
        controller = new TaskManagerController(this);

        setDefault();
        findViews();
        hideSubTasksAndNewTypeRows();
        setTypesSpinner();
        setUnitsSpinner();
        setPrioritiesSpinner();
        setOnClickListeners();

    }
    private void setDefault() {
        fName = fDateTime = fDate = fTime = fNote = fReminderUnit = fNewTypeName  = fNewTypeIcon = fSubTask1 = fSubTask2 = fSubTask3 = fSubTask4 = null;
        fTypeId = fPriority = fReminderDuration = -1;
        fNewTypeColor = fColor = Color.BLUE;

        Calendar initial = Calendar.getInstance();
        initial.add(Calendar.HOUR, 5);

        // Create a SimpleDateFormat object to format the date in the desired format.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Set the text of the dateTextView to the formatted date.
        fDate = dateFormat.format(initial.getTime());
        fTime = timeFormat.format(initial.getTime());
    }
    private void findViews() {
        name = findViewById(R.id.name);
        note  = findViewById(R.id.note);
        reminderDuration = findViewById(R.id.reminderDuration);
        reminderUnit = findViewById(R.id.reminderUnit);
        priority  = findViewById(R.id.priorityList);
        type  = findViewById(R.id.typeList);
        newTypeName = findViewById(R.id.newTypeName);
        date  = findViewById(R.id.date);
        time  = findViewById(R.id.time);
        dateTV = findViewById(R.id.dateTV);
        timeTV = findViewById(R.id.timeTV);

        if(fDate != null)
            dateTV.setText(fDate);
        if(fTime != null)
            timeTV.setText(fTime);

        addNewType = findViewById(R.id.addType);
        newTypeColor = findViewById(R.id.newTypeColor);
        submitNewType = findViewById(R.id.submitNewType);


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
    }

    private void hideSubTasksAndNewTypeRows() {
        row2.setVisibility(View.GONE);
        row3.setVisibility(View.GONE);
        row4.setVisibility(View.GONE);
        newTypeRow.setVisibility(View.GONE);
        visibleRows = new ArrayList<>();
    }

    void setTypesSpinner(){
        List<Type> types = controller.getAllTypes();
        Type[] items = new Type[types.size()];
        int i = 0;
        for (Type t : types){
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
        String[] items = new String[]{"At time of event", "Minutes before", "Hours before", "Days before", "Weeks before"}; //modified

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
        add.setOnClickListener(l -> {
            int eventId = add();
            if(eventId > -1) {
                Intent toMain = new Intent(this, MainActivity.class);
                toMain.putExtra("addCalendar", 1);
                toMain.putExtra("eventId", eventId);
                startActivity(toMain);
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
        });
        removeTask3.setOnClickListener(l -> {
            row3.setVisibility(View.GONE);
            visibleRows.remove("Row3");
        });
        removeTask4.setOnClickListener(l -> {
            row4.setVisibility(View.GONE);
            visibleRows.remove("Row4");
        });
    }

    private void showColorPicker() {
        AmbilWarnaDialog aWDialog = new AmbilWarnaDialog(this, fNewTypeColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // color is the color selected by the user.
                fNewTypeColor = color;
                newTypeColor.setTextColor(color);
                newTypeName.setTextColor(color);
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
            Toast.makeText(this, "Failed To add new Type, Make Sure To Write a name", Toast.LENGTH_SHORT).show();
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


    int add(){

        errors = new ArrayList<>();

        submitData();

        checkData();

        if (!errors.isEmpty()) {
            displayErrors();
            return -1;
        }


        String r = "Name: " + fName + "\n"
                + "Type: " + fTypeId + "\n"
                + "Color: " + fColor + "\n"
                + "DateTime: " + fDateTime + "\n"
                + "Note: " + fNote + "\n"
                + "Reminder: " + fReminderDuration + " " + fReminderUnit + (fReminderDuration > 1? "s" : "") + "\n"
                + "Priority: " + fPriority + "\n";
        Log.i("ADDED_EVENT", r);

        int eventId = controller.addEvent(fName, fTypeId, fColor, fDateTime, fNote, fReminderDuration, fReminderUnit, fPriority,  this);
        if(eventId == -1) {
            errors.add("Failed to add new event Try again");
            displayErrors();
            return -1;
        }


        if(fSubTask1 != null && fSubTask1.length() > 0)
            controller.addTask(eventId, fSubTask1);

        if(fSubTask2 != null && fSubTask2.length() > 0)
            controller.addTask(eventId, fSubTask2);

        if(fSubTask3 != null && fSubTask3.length() > 0)
            controller.addTask(eventId, fSubTask3);

        if(fSubTask4 != null && fSubTask4.length() > 0)
            controller.addTask(eventId, fSubTask4);

        Toast.makeText(this, "Event is added successfully", Toast.LENGTH_SHORT).show();


        return eventId;
    }



    private void displayErrors() {
        // Concatenate the error messages
        StringBuilder errorMessage = new StringBuilder();
        for (String error : errors) {
            errorMessage.append("- ").append(error).append("\n");
        }

        // Create and configure the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent((Context) dialog, MainActivity.class));
            }
        };
        builder.setTitle("Errors")
                .setMessage(errorMessage.toString())
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel Adding", listener);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        errors.clear();
    }

    private void submitData() {
        fName = name.getText().toString();
        if(fDate != null && fTime != null)
            fDateTime = fDate + " " + fTime;
        else
            fDateTime = null;

        fNote = note.getText().toString();

        String reminderD = reminderDuration.getText().toString();
        if (!(reminderD == null || reminderD.isEmpty()))
            fReminderDuration = Integer.parseInt(reminderD);

        fSubTask1 = fSubTask2 = fSubTask3 = fSubTask4 = null;

        fSubTask1 = sub1.getText().toString();

        if(visibleRows.contains("Row2"))
            fSubTask2 = sub2.getText().toString();
        if(visibleRows.contains("Row3"))
            fSubTask3 = sub3.getText().toString();
        if(visibleRows.contains("Row4"))
            fSubTask4 = sub4.getText().toString();
    }
    private boolean checkData() {
        if(fName == null || fName.equals("")) {
            errors.add("SET NAME");
        }
        if(fTypeId < 0 ){
            errors.add("SELECT TYPE");
        }
        if(fDateTime == null) {
            errors.add("SET DATE AND TIME");
        }else{
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // specify the format of the string date
                Date date = sdf.parse(fDateTime); // parse the string into a date object
                Calendar cal = Calendar.getInstance(); // get a calendar instance
                cal.setTime(date); // set the calendar date to the parsed date

                Calendar currentCalendar = Calendar.getInstance(); // get the current calendar

                // check if the user's calendar is before the current calendar
                boolean result = cal.before(currentCalendar);

                if (result) {
                    // user's calendar is before current calendar
                    errors.add("you cant enter a date of time in the past");
                }
            }catch (ParseException e){
                Log.i("ParseExceptionATAdd", e.toString());
            }
        }
        if(fNote == null || fNote.equals("")) {
            fNote = "";
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
}