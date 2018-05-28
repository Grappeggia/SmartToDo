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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
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
import java.util.List;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadDatabase();
        LoadTodosOnscreen();
    }

    void LoadDatabase(){
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();


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
