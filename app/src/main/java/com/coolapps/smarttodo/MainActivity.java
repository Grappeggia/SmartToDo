package com.coolapps.smarttodo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadTodosOnscreen();
    }

    void LoadTodosOnscreen(){
        //Call the main layout from App XML
        final LinearLayout MainLayoutView = (LinearLayout) findViewById(R.id.mainLayout);

        String todoList[] = LoadElements();

        //Draw all UI elements, extracted from a ToDo list
        for (String todoItem : todoList){
            //Create a view to inflate the main layout
            View tempView = getLayoutInflater().inflate(R.layout.layout_text, MainLayoutView,false);
            TextView tempText = (TextView) tempView.findViewById(R.id.text_item_id);
            tempText.setText(todoItem);
            //Add the view to the main layout
            MainLayoutView.addView(tempView);
        }


    }

    public String[] LoadElements(){

        String todoList[] = {   "1-make todo app",
                "2-create todo list",
                "2-create todo list",
                "2-create todo list",
                "2-create todo list",
                "2-create todo list",
                "2-create todo list",
                "3-run todo app",
                "3-run todo app",
                "3-run todo app",
                "3-run todo app",
                "3-run todo app",
                "3-run todo app"};

        return todoList;
    }


}
