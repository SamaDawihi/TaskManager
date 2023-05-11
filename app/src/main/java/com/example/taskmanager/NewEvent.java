package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NewEvent extends AppCompatActivity {

    EditText name, color, note, priority;
    Button type, date, time, add;

    TextView result;

    Calendar calendar;

    MyTasksDB dbHelper;

    String fName, fColor, fDateTime, fDate, fTime, fNote;
    int fType, fPriority, fRemainder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        calendar = Calendar.getInstance();

        dbHelper = new MyTasksDB(this, null, null, 1);

        findViews();
    }

    private void findViews() {
        name = findViewById(R.id.name);
        color = findViewById(R.id.color);
        note  = findViewById(R.id.note);
        priority  = findViewById(R.id.priority);
        type  = findViewById(R.id.typeId);
        date  = findViewById(R.id.date);
        time  = findViewById(R.id.time);
        add  = findViewById(R.id.add);
        result = findViewById(R.id.result);

        registerForContextMenu(type);
        date.setOnClickListener( l -> showDateSelector());
        time.setOnClickListener( l -> showTimeSelector());

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                submitData();


                if(! checkData())
                    return;

                String r = "Name: " + fName + "\n"
                        + "Type: " + fType + "\n"
                        + "Color: " + fColor + "\n"
                        + "DateTime: " + fDateTime + "\n"
                        + "Note: " + fNote + "\n"
                        + "Reminder: " + fRemainder + "\n"
                        + "Priority: " + fPriority + "\n";
                result.setText(r);



                //dbHelper.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainder, fPriority);
            }
        });
    }

    private boolean checkData() {
        if(fName == null)
            return false;
        if(fDateTime == null)
            return false;
        if(fColor == null)
            return false;
        if(fNote == null)
            return false;
        if(fPriority < 0)
            return false;
        if(fRemainder < 0)
            return false;
        return true;
    }

    private void submitData() {
        fName = name.getText().toString();
        fColor = color.getText().toString();
        fDateTime = fDate + " " + fTime;
        fNote = note.getText().toString();
        fPriority = Integer.parseInt(priority.getText().toString());
        fRemainder = 0; // for later
    }

    //ContextMenu for TypeId
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == type) {
            Log.i("TypeBtn", "selected");
            int idGroup = Menu.NONE;
            int item1 = Menu.FIRST;
            int menuItemOrder = Menu.NONE;

            List<Type> types = dbHelper.getAllTypes();

            for (Type type : types) {
                menu.add(Menu.NONE, type.getTypeId(), Menu.NONE, type.getName());
            }
        }

    }

    public boolean onContextItemSelected(MenuItem item){
        List<Type> types = dbHelper.getAllTypes();

        for (Type type : types) {
            if(item.getItemId() == type.getTypeId())
                fType = type.getTypeId();
        }
        return true;
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
}