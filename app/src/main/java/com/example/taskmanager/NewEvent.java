package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewEvent extends AppCompatActivity implements NewTypeDialog.NewTypeDialogListener{


    //---------------------------Views-----------------------------
    TableLayout table;
    EditText name, note, reminderDuration, sub1, sub2, sub3, sub4;
    Button addNewType, date, time, add, addTask1, removeTask2, removeTask3, removeTask4;
    Spinner type, priority, reminderUnit;
    TextView dateTV, timeTV, result;
    TableRow row1,row2, row3,row4;
    //---------------------------Views-----------------------------

    List<String> visibleRows;
    List<String> errors;

    TaskManagerController controller;

    Calendar calendar;



    //---------------------------Outputs Variables-----------------------------
    String fName, fDateTime, fDate, fTime, fNote,fReminderUnit, fNewTypeName, fNewTypeColor, fNewTypeIcon;
    String fSubTask1, fSubTask2, fSubTask3, fSubTask4;
    int fTypeId, fPriority, fReminderDuration, fColor;
    //---------------------------Outputs Variables-----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);


        calendar = Calendar.getInstance();
        controller = new TaskManagerController(this);


        setDefault();
        findViews();
        hideSubTasksRows();
        setUnitsSpinner();
        setTypesSpinner();
        setPrioritiesSpinner();
        setOnClickListeners();

    }
    private void setDefault() {
        fName = fDateTime = fDate = fTime = fNote = fReminderUnit = fNewTypeName = fNewTypeColor = fNewTypeIcon = fSubTask1 = fSubTask2 = fSubTask3 = fSubTask4 = null;
        fTypeId = fPriority = fReminderDuration = -1;
        fColor = Color.BLUE;
    }
    private void findViews() {
        name = findViewById(R.id.name);
        note  = findViewById(R.id.note);
        reminderDuration = findViewById(R.id.reminderDuration);
        reminderUnit = findViewById(R.id.reminderUnit);
        priority  = findViewById(R.id.priorityList);
        type  = findViewById(R.id.typeList);
        date  = findViewById(R.id.date);
        dateTV = findViewById(R.id.dateTV);
        timeTV = findViewById(R.id.timeTV);
        time  = findViewById(R.id.time);

        addNewType = findViewById(R.id.addType);

        table  = findViewById(R.id.table);

        row1  = findViewById(R.id.r1);
        row2  = findViewById(R.id.r2);
        row3  = findViewById(R.id.r3);
        row4  = findViewById(R.id.r4);

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

    private void hideSubTasksRows() {
        row2.setVisibility(View.GONE);
        row3.setVisibility(View.GONE);
        row4.setVisibility(View.GONE);
        visibleRows = new ArrayList<>();
    }
    private void setUnitsSpinner() {
        String[] items = new String[]{"Minutes", "Hours", "Day", "Weak", "month" };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        reminderUnit.setAdapter(adapter);

        reminderUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fReminderUnit = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
                // Your code here
            }
        });
    }
    void setTypesSpinner(){
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
    void setPrioritiesSpinner(){
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
    private void setOnClickListeners() {
        addNewType.setOnClickListener( l -> showAddTypeDialog());
        date.setOnClickListener( l -> showDateSelector());
        time.setOnClickListener( l -> showTimeSelector());
        add.setOnClickListener(l -> add());

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

    private void showAddTypeDialog() {
        NewTypeDialog dialogFragment = new NewTypeDialog();
        dialogFragment.setListener(this);
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        NewTypeDialog d = (NewTypeDialog) dialog;
        if(name == null)
            return;
        fNewTypeName = name;
        fNewTypeIcon = d.getIcon();
        fNewTypeColor = d.getColor();
        Log.i("dcolor", d.getColor());
        controller.addType(fNewTypeName, fNewTypeIcon, fNewTypeColor);
        setTypesSpinner();
    }

    public void onDialogNegativeClick(DialogFragment dialog) {
        // Handle negative button click in the activity
        Toast.makeText(this, "Add new type cancels", Toast.LENGTH_SHORT).show();
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
                    dateTV.setText(fDate);
                    Log.i("DatePicker","date picked is: " + fDate);

                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.show();

    }


    boolean add(){

        //Toast.makeText(this, "typeId = " + fTypeId * 100, Toast.LENGTH_SHORT).show();
        errors = new ArrayList<>();

        submitData();

        checkData();

        if (!errors.isEmpty()) {
            displayErrors();
            return false;
        }


        String r = "Name: " + fName + "\n"
                + "Type: " + fTypeId + "\n"
                + "Color: " + fColor + "\n"
                + "DateTime: " + fDateTime + "\n"
                + "Note: " + fNote + "\n"
                + "Reminder: " + fReminderDuration + " " + fReminderUnit + (fReminderDuration > 1? "s" : "") + "\n"
                + "Priority: " + fPriority + "\n";
        result.setText(r);
        result.setTextColor(fColor);
        Log.i("ADDED_EVENT", r);

        int eventId = controller.addEvent(fName, fTypeId, fColor, fDateTime, fNote, fReminderDuration, fReminderUnit, fPriority, this);
        if(eventId == -1) {
            errors.add("Failed to add new event");
            displayErrors();
            return false;
        }

        if(fSubTask1 != null && fSubTask1 != "")
            controller.addTask(eventId, fSubTask1);

        if(fSubTask2 != null && fSubTask2 != "")
            controller.addTask(eventId, fSubTask2);

        if(fSubTask3 != null && fSubTask3 != "")
            controller.addTask(eventId, fSubTask3);

        if(fSubTask4 != null && fSubTask4 != "")
            controller.addTask(eventId, fSubTask4);

        return true;
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
        if(fName == null || fName == "") {
            errors.add("SET NAME");
        }
        if(fTypeId < 0 ){//|| controller.doesTypeExist(fTypeId)) {
            errors.add("SELECT TYPE");
        }
        if(fDateTime == null) {
            errors.add("SET DATE AND TIME");
        }
        if(fNote == null || fNote == "") {
            fNote = "";
        }
        if(fReminderUnit == null) {
            errors.add("SELECT REMINDER UNIT");
        }
        if(fReminderDuration < 0) {
            errors.add("SET Reminder Duration");
        }
        if(fPriority < 0) {
            errors.add("SELECT PRIORITY");
        }
        return true;
    }

}