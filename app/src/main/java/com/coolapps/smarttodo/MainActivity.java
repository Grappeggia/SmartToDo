package com.coolapps.smarttodo;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //Main array used for storing/adding the todo items
    public ArrayList<String> todolistArray;
    public String userID = "tempUser3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTodosFromDatabase(userID);

        TextView createText = findViewById(R.id.createText);
        createText.setVisibility(View.GONE);

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        /**
         * Monitors the keyboard for actions that may indicate the user is done with their to do
         * creation
         */
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                createNewTodo();
                return true;
            case KeyEvent.KEYCODE_ESCAPE:
                createNewTodo();
                return true;
            case KeyEvent.KEYCODE_BACK:
                createNewTodo();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_OUT:
                createNewTodo();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }


    public void loadTodosFromDatabase(String userID){
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tempDoc = db.collection("users").document(userID);

        // Reads the todolist from the document belonging to the respective user
        Task<DocumentSnapshot> tempTask = tempDoc.get();
        tempTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> todolistHashmap;
                        todolistHashmap = document.getData();
                        todolistArray = (ArrayList<String>) todolistHashmap.get("todolist");
                        Log.d(TAG, "DocumentSnapshot data: " + todolistArray);
                        if (todolistArray != null){
                            // Once all elements are loaded, call function to draw UI
                            drawLayoutFromTodolist(todolistArray);
                        }
                    } else{
                        Log.d(TAG, "No such document, creating");
                        // First time this user users, so needs to create document
                        saveTodosInDatabase();


                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void saveTodosInDatabase(){
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tempDoc = db.collection("users").document(userID);

        //Writes new To do inside Database, using global todolistArray
        Map<String, Object> docData = new HashMap<>();
        docData.put("todolist", todolistArray);
        tempDoc.set(docData);
    }

    public void drawLayoutFromTodolist(ArrayList<String> todoList){
        /**
         * Call the main layout from App XML
         */
        final LinearLayout MainLayoutView = (LinearLayout) findViewById(R.id.todolistLinearView);

        int elementArrayPosition = 0; //Position tracker to allow for element wise operations

        MainLayoutView.removeAllViews();

        //Draw all UI elements, extracted from a ToDo list
        for (String todoItem : todoList){
            //Create a view to inflate the main layout
            final View tempView = getLayoutInflater().inflate(R.layout.layout_text, MainLayoutView,false);
            TextView tempText = (TextView) tempView.findViewById(R.id.text_item_id);
            tempText.setText(todoItem);

            //Set listener to capture swiping action
            tempView.setId(elementArrayPosition);
            elementArrayPosition++;
            tempView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
                public void onSwipeRight(){
                    deleteTodo(tempView.getId());

                }
                public void onSwipeLeft(){
                    deleteTodo(tempView.getId());
                }
            });

            //Add the view to the main layout
            MainLayoutView.addView(tempView);
        }


    }

    public void loadCreationScreen(View view) {

        // Hide button to create todo, show text box to type contents
        TextView createText = findViewById(R.id.createText);
        createText.setVisibility(View.VISIBLE);
        FloatingActionButton createButton = findViewById(R.id.createButton);
        createButton.setVisibility(View.INVISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }

    public void createNewTodo(){
        // Show button to create todo, hide text box to type contents
        TextView createText = findViewById(R.id.createText);
        createText.setVisibility(View.GONE);
        FloatingActionButton createButton = findViewById(R.id.createButton);
        createButton.setVisibility(View.VISIBLE);

        // Gets text for new todo, insert on top of current todo list
        String textNewTodo = createText.getText().toString();
        if(todolistArray == null ) todolistArray = new ArrayList<>();

        todolistArray.add(0,textNewTodo);

        // Draw new to do
        drawLayoutFromTodolist(todolistArray);


        // Write new entry to database
        saveTodosInDatabase();

        // Clean up Text for creating new todos, ready for next
        createText.setText("");
    }

    public void deleteTodo(int elementArrayPosition){
        Toast.makeText(MainActivity.this, "id" + elementArrayPosition , Toast.LENGTH_SHORT).show();
    }

}
