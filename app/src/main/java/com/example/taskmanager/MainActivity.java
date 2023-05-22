package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //grade_adapter=new Grade_Adapter(getApplicationContext(),R.android.R.layout.activity_list_item);
    LinearLayout linearLay;
    Button addEvent;
    ListView listView;
    MyTasksDB myTasksDB;
    List<String> list = new ArrayList<>();

    public static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(l -> startNewEvent());

        linearLay = (LinearLayout) findViewById(R.id.LinearLayout);
        listView = findViewById(R.id.listView);
        myTasksDB = new MyTasksDB(this);

        List<EventModel> eventList = myTasksDB.getAllEvents();



        for(int i = 0; i < eventList.size(); i++){
            list.add( eventList.get(i).getDateTime()+ "\n"+ eventList.get(i).getEventId() +" "+ eventList.get(i).getName());
            //listView.setBackgroundColor(eventList.get(i).getColor());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                    convertView.setBackgroundColor(eventList.get(0).getColor());
//                return view;
//            }
//        };
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                list.add(editText.getText().toString());
////                editText.setText("");
//                 adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
//                adapter.notifyDataSetChanged();
//            }
//        };
      //  button.setOnClickListener(onClickListener);
        listView.setAdapter(adapter);

        //listView.invalidateViews();

//        linearLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//                // User defined method to shuffle the array list items
//                shuffleListItems();
//            }
//        });

    }
//    public void shuffleListItems() {
//        // Shuffling the arraylist items on the basis of system time
//      //  Collections.shuffle(list, new Random(System.currentTimeMillis()));
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_item, list);
//        listView.setAdapter(arrayAdapter);
//    }

//    @Override
//    public void onResume(){
//        super.onResume();
////here you can update the adapter just call notify methods
//       // grade_adapter.notifyDataSetChanged();
//
//    }



    public void startNewEvent() {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }


}