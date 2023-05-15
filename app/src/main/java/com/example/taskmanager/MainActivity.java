package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button addEvent, dbState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(l -> startNewEvent());

        dbState = findViewById(R.id.dbState);
        dbState.setOnClickListener(l -> viewDBState());
    }

    private void viewDBState() {
        Intent intent = new Intent(this, DBStateActivity.class);
        startActivity(intent);
    }


    public void startNewEvent() {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }
}
//timeline (kadi)
//View (lina)
//edit (manal)
//add -> (sama)
// to calendar - set notification (hind)

