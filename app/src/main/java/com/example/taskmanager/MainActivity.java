package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

    //toggle buttons
    RadioButton upcoming;
    RadioButton all;
    MainActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(l -> startNewEvent());
        //toggle buttons
        upcoming = findViewById(R.id.upcoming);
        all = findViewById(R.id.all);
        table = findViewById(R.id.eventTable);
        myTasksDB = new MyTasksDB(this);
        List<EventModel> eventList = myTasksDB.getAllEvents();

        List<EventModel> list = new ArrayList<>();


        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.removeAllViews();
                list.clear();
                for (int i = 0; i < 5; i++) {
                    try{
                        Log.e("list", eventList.get(i).getName());

                        if(dateFormat(eventList.get(i).getDateTime(), eventList, i)) {
                            list.add(eventList.get(i));
                        }

                        Log.e("list2", list.get(i).getName());
                            int finalI = i;
                            int finalI1 = i;


                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Toast.makeText(MainActivity.this, "rowClicked", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), event_Info.class);
                                intent.putExtra("eventId", eventList.get(finalI1).getEventId());
                                startActivity(intent);
                            }
                        });
                    }catch (IndexOutOfBoundsException  |ParseException | NullPointerException e){}
                }
                loadList(list);
            }

        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.removeAllViews();
                list.clear();
                for (int i = 0; i < eventList.size(); i++) {
                    try{
                        Log.e("list", eventList.get(i).getName());
                            list.add(eventList.get(i));

                        Log.e("list2", list.get(i).getName());
                        int finalI = i;
                        int finalI1 = i;


                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Toast.makeText(MainActivity.this, "rowClicked", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), event_Info.class);
                                intent.putExtra("eventId", eventList.get(finalI1).getEventId());
                                startActivity(intent);
                            }
                        });
                    }catch (IndexOutOfBoundsException  | NullPointerException e){}
                }
                loadList(list);
            }

        });

    }


    public void loadList(List<EventModel> list){
        for (int i = 0; i < list.size(); i++){
            try {

                Log.e("loading", list.get(i).getName());
                row = new TableRow(context);
                textView = new TextView(context);

//                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//                Date date = format.parse(list.get(i).getDateTime());
//                Log.e("datec", list.get(i).getDateTime());
//                Date today = Calendar.getInstance().getTime();
//                if (date.after(today) || date.equals(today)) {
//                    Log.e("after", list.get(i).getDateTime());
                    textView.setText(list.get(i).getDateTime() + "\n" + list.get(i).getEventId() + " " + list.get(i).getName());
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                    textView.setTextSize(18);
                    row.addView(textView);
                    row.setBackgroundColor(list.get(i).getColor());
                    row.setElevation(54);//shadow
                    row.setPadding(16,16,16,16);

                    table.addView(row);
             //   }
            }catch (IndexOutOfBoundsException |NullPointerException  e){}
        }
    }

    public boolean dateFormat(String d, List<EventModel> list, int i) throws ParseException {
        Log.e("enetred", list.get(i).getDateTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("date", list.get(i).getDateTime());
        String D = list.get(i).getDateTime();
        Date date = format.parse(D);
        Log.e("datec", "h");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date today = Calendar.getInstance().getTime();
//        int diff = date.compareTo(today);
//        Log.e("diff", String.valueOf(diff));
        if (date.compareTo(today) >= 0) {
                    return true;
                }
                    Log.e("after", list.get(i).getDateTime());
                return false;
    }




    public void startNewEvent() {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }

    public void viewDetails(View v, int eventID){
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
}