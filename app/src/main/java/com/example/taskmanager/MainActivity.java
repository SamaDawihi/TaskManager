package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addEvent;
    TableLayout table;
    TableRow row;
    TextView textView;
    MyTasksDB myTasksDB;

    List<EventModel> list, eventList;

    //toggle buttons
    RadioButton upcoming;
    RadioButton all;
    MainActivity context = this;

    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean allGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(l -> startNewEvent());

        upcoming = findViewById(R.id.upcoming);
        all = findViewById(R.id.all);
        table = findViewById(R.id.eventTable);
        myTasksDB = new MyTasksDB(this);

        eventList = myTasksDB.getAllEvents();

        list = new ArrayList<>();


        upcoming.setOnClickListener(m -> loadUpcoming());

        all.setOnClickListener(v -> loadAll());

        allGranted = checkPermissions();
    }
    void loadUpcoming(){
        table.removeAllViews();
        list.clear();
        for (int i = eventList.size()-5 ; i < eventList.size(); i++) {
            try{
                Log.i("list", eventList.get(i).getName());
                if(dateFormat(eventList.get(i).getDateTime(), eventList, i)) {
                    list.add(eventList.get(i));
                }

                Log.i("list2", list.get(i).getName());

            }catch (IndexOutOfBoundsException  | ParseException | NullPointerException e){}
        }
        loadList(list);
    }
    void loadAll(){
        table.removeAllViews();
        list.clear();
        for (int i = 0; i < eventList.size(); i++) {
            try{
                Log.i("list", eventList.get(i).getName());
                list.add(eventList.get(i));
                Log.i("list2", list.get(i).getName());
            }catch (IndexOutOfBoundsException  | NullPointerException e){}
        }
        loadList(list);
    }

    public void loadList(List<EventModel> list) {
        if (list.size() == 0) {
            row = new TableRow(context);
            textView = new TextView(context);
            textView.setText("There are no events yet. Click here to add a new event.");
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setTextSize(18);
            row.addView(textView);
            row.setElevation(54); // shadow
            row.setPadding(16, 16, 16, 16);
            row.setOnClickListener(l -> startNewEvent());
            table.addView(row);
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                Log.i("loading", list.get(i).getName());

                // Create the row
                row = new TableRow(context);

                textView = new TextView(context);

                int eventID = eventList.get(i).getEventId();

                textView.setText(list.get(i).getDateTime() + "\n" + list.get(i).getEventId() + " " + list.get(i).getName());
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTextSize(18);
                row.setBackgroundResource(R.drawable.rounded_background); // Apply a rounded background drawable
                row.setBackgroundColor(list.get(i).getColor());
                row.setPadding(16, 16, 16, 16);
                row.setOnClickListener(l -> viewDetails(eventID));
                row.addView(textView);
                table.addView(row);

            } catch (IndexOutOfBoundsException | NullPointerException e) {
            }
        }
    }


    public boolean dateFormat(String d, List<EventModel> list, int i) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String D = list.get(i).getDateTime();
        Log.e("date", D);
        Date date = format.parse(D);
        Date today = Calendar.getInstance().getTime();
        Log.e("today", String.valueOf(today));
        if (date.compareTo(today) < 0) {
            System.out.println("Date 1 is before Date 2");
            return false;
        } else if (date.compareTo(today) == 0) {
            System.out.println("Date 1 is equal to Date 2");
            return true;
        } else if (date.after(today)) {
            System.out.println("Date 1 is after Date 2");
            return true;
        }

        return false;
    }

    public void startNewEvent() {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }

    public void viewDetails( int eventID){

        Intent intent = new Intent(this,event_Info.class);
        intent.putExtra("eventId",eventID);
        startActivity(intent);
    }

    //Option Menu
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, (item.toString()), Toast.LENGTH_SHORT).show();
        if(item.getItemId() == R.id.viewAllEventsOption){
            Intent intent = new Intent(this, AllEvents.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.viewAllTypesOption){
            Intent intent = new Intent(this, AllTypes.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean checkPermissions() {
        boolean needPermission = false;

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_CALENDAR);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_CALENDAR);
            }

            if (!permissions.isEmpty()) {
                needPermission = true;
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            }
        }

        return needPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // All permissions granted, enableBtn();
                Toast.makeText(this, "allPermissionsGranted", Toast.LENGTH_SHORT).show();

            } else {
                int v = 0;
                for(String i : permissions) {
                    if (grantResults[v++] != PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this, i+ " NOT Granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}