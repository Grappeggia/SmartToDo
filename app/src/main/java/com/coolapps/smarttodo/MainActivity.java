package com.coolapps.smarttodo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.sheets.v4.SheetsScopes;

import com.google.api.services.sheets.v4.model.*;


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

import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


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
