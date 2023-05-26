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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addEvent;
    //ListView listView;
    TableLayout table;
    TableRow row;
    TextView textView;

    MyTasksDB myTasksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(l -> startNewEvent());

        //listView = findViewById(R.id.listView);
        table = findViewById(R.id.eventTable);


        myTasksDB = new MyTasksDB(this);

        List<EventModel> eventList = myTasksDB.getAllEvents();
        List<String> list = new ArrayList<>();


        for(int i = 0; i < eventList.size(); i++){
            row = new TableRow(this);
            textView = new TextView(this);
            //list.add( eventList.get(i).getDateTime()+ "\n"+ eventList.get(i).getEventId() +" "+ eventList.get(i).getName());
            //listView.setBackgroundColor(eventList.get(i).getColor());
            textView.setText(eventList.get(i).getDateTime()+ "\n"+ eventList.get(i).getEventId() +" "+ eventList.get(i).getName());
            row.addView(textView);
            row.setBackgroundColor(eventList.get(i).getColor());
            table.addView(row);
            int finalI = i;
            int finalI1 = i;
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(MainActivity.this, "rowClicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), event_Info.class);
                    intent.putExtra("eventId",eventList.get(finalI1).getEventId());
                    startActivity(intent);
                }
            });
        }
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        //listView.setAdapter(adapter);


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