package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_EVENT_REQUEST_CODE = 111;
    Button addEvent;
    TableLayout table;
    TableRow row;
    TextView textView, textView2;
    MyTasksDB myTasksDB;

    TaskManagerController controller;

    List<EventModel> list, eventList;

    //toggle buttons
    RadioButton upcoming;
    RadioButton all;
    MainActivity context = this;
    CreateNotificationChannels createNotificationChannels;

    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean allGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannels = new CreateNotificationChannels(this);

        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(l -> startNewEvent());

        upcoming = findViewById(R.id.upcoming);
        all = findViewById(R.id.all);
        table = findViewById(R.id.eventTable);
        myTasksDB = new MyTasksDB(this);
        controller = new TaskManagerController(this);

        eventList = myTasksDB.getAllEvents();

        list = new ArrayList<>();


        upcoming.setOnClickListener(m -> loadUpcoming());

        all.setOnClickListener(v -> loadAll());

        allGranted = checkPermissions();

        addCalendar();

    }



    void loadUpcoming(){
        table.removeAllViews();
        list.clear();
        List<EventModel> listUnordered = new ArrayList<>();
        for (int i = eventList.size()-1 ; i >=0 ; i--) {
            try{
                if(dateFormat(eventList.get(i).getDateTime(), eventList, i)) {
                    if(listUnordered.size()< 5) {
                        listUnordered.add(eventList.get(i));
                    }
                }

            }catch (IndexOutOfBoundsException  | ParseException | NullPointerException e){}
        }
        for (int i = 0; i < listUnordered.size(); i++) {
            list.add(listUnordered.get(listUnordered.size() - 1 - i));
        }
        loadList(list);
    }
    void loadAll(){
        table.removeAllViews();
        list.clear();
        for (int i = 0; i < eventList.size(); i++) {
            try{
                list.add(eventList.get(i));
            }catch (IndexOutOfBoundsException  | NullPointerException e){}
        }
        loadList(list);
    }

    public void loadList(List<EventModel> list) {
        if (list.size() == 0) {
            row = new TableRow(context);
            textView = new TextView(context);
            textView.setText("There are no events yet. Click here to add a new event.");
            textView.setTextSize(14);
            row.addView(textView);
            row.setElevation(54); // shadow
            row.setPadding(16, 16, 16, 16);
            row.setOnClickListener(l -> startNewEvent());
            table.addView(row);
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                Log.i("loading", "EventName " + list.get(i).getName());

                // Create the row
                row = new TableRow(context);

                textView = new TextView(context);
                textView2 = new TextView(context);


                int eventID = list.get(i).getEventId();

                textView.setText("    " + list.get(i).getEventId() + " " + list.get(i).getName() + "    ");
                textView.setPadding(8, 0, 8, 0);
                textView2.setText("    " + list.get(i).getDateTime() + "    ");
                textView.setPadding(8, 0, 8, 0);
                textView.setTextColor(list.get(i).getColor());
                textView2.setTextColor(list.get(i).getColor());
                textView.setTextSize(18);
                textView2.setTextSize(18);
                row.setBackgroundResource(R.drawable.rounded_background); // Apply a rounded background drawable
                //row.setBackgroundColor(list.get(i).getColor());
                row.setPadding(16, 16, 16, 16);
                row.setOnClickListener(l -> viewDetails(eventID));
                row.addView(textView);
                row.addView(textView2);
                table.addView(row);

            } catch (IndexOutOfBoundsException | NullPointerException e) {
            }
        }
    }


    public boolean dateFormat(String d, List<EventModel> list, int i) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String D = list.get(i).getDateTime();
        Date date = format.parse(D);
        Date today = Calendar.getInstance().getTime();
        if (date.compareTo(today) < 0) {
            return false;
        } else if (date.compareTo(today) == 0) {
            return true;
        } else if (date.after(today)) {
            return true;
        }

        return false;
    }

    public void startNewEvent() {
        Intent startNewEventActivity = new Intent(this, NewEvent.class);
        startActivity(startNewEventActivity);
    }

    private void addCalendar() {
        if(getIntent().getIntExtra("addCalendar", -1) > -1){
            int eventId = getIntent().getIntExtra("eventId", -1);
            EventModel eventToCal = controller.getEventById(eventId);
            if(eventToCal == null) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            DialogInterface.OnClickListener addEventToCalendar = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date eventDate = null;
                        try {
                            eventDate = format.parse(eventToCal.getDateTime());

                            Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
                            calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                            calendarIntent.putExtra(CalendarContract.Events.TITLE, eventToCal.getName());
                            calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, eventToCal.getNote());
                            calendarIntent.putExtra(CalendarContract.Events.EVENT_COLOR, eventToCal.getColor());// Set the event color here
                            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventDate.getTime());
                            calendarIntent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                            calendarIntent.putExtra(CalendarContract.Events.ALL_DAY, true);
                            calendarIntent.putExtra(CalendarContract.Events.HAS_ALARM, false);


                            // Check if there is a calendar app available to handle the intent
                            if (getIntent().resolveActivity(getPackageManager()) != null) {
                                // Start the activity with the intent
                                Log.i("AddEventController", "Will start Calendar Intent");
                                startActivity(calendarIntent);
                                Toast.makeText(context, "calendar app found", Toast.LENGTH_SHORT).show();

                            } else {
                                // Handle the case where no calendar app is available
                                Toast.makeText(context, "No calendar app found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            Log.i("PARSE_ERROR", e.toString());
                        }
                    }
                };

            builder.setTitle("Calendar")
                    .setMessage("Do you want to add this event to the calendar")
                    .setPositiveButton("Yes", addEventToCalendar)
                    .setNegativeButton("No", null);

                // Show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public void viewDetails(int eventID){

        Intent intent = new Intent(this,event_Info.class);
        intent.putExtra("eventId",eventID);
        startActivity(intent);
    }
    private boolean checkPermissions() {
        boolean needPermission = false;

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions.add(Manifest.permission.POST_NOTIFICATIONS);
                }
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

            /*
            if (allPermissionsGranted) {
                Toast.makeText(this, "allPermissionsGranted", Toast.LENGTH_SHORT).show();

            } else {
                int v = 0;
                for(String i : permissions) {
                    if (grantResults[v++] != PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this, i+ " NOT Granted", Toast.LENGTH_SHORT).show();
                }
            }

             */
        }
    }


}