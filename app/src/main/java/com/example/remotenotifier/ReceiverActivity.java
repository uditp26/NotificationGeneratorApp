package com.example.remotenotifier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class ReceiverActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ReceiverActivity";
    Button submit;
    EditText input;
    Editable output;
    ConnectivityManager cm;
    NetworkInfo network;
    String token;
    TextView notifDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(this);
        input = findViewById(R.id.inputEditText);
        notifDisplay = findViewById(R.id.notifDisplay);
        initMessagingService();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submitButton){
            output = input.getText();
            if(output.toString().equals("")){
                Toast.makeText(this,"No input provided!", Toast.LENGTH_LONG).show();
            }
            else{
                Log.d(TAG, output.toString());
                initUpload();
            }
        }
    }

    private void initMessagingService(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }
                token = task.getResult().getToken();
                Log.d(TAG, "initService token ->" + token);
                NotifierSingleton.getObject().userToken = token;
                String msg = getString(R.string.msg_token_fmt, token);
                Toast.makeText(ReceiverActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        });
        initTokenUpload();
    }

    private void initTokenUpload(){
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        Log.d(TAG, "Token -> "+NotifierSingleton.getObject().userToken );
        Data tokenData = new Data.Builder().putString("token", NotifierSingleton.getObject().userToken).build();
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(NetworkStateSurveyor.class).setConstraints(constraints).setInputData(tokenData).addTag("tokenUp").build();
        WorkManager.getInstance().enqueue(uploadWorkRequest);
    }

    public void initUpload(){
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        Data inputData = new Data.Builder().putString(NotifierSingleton.getObject().userToken, output.toString()).build();
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(NetworkStateSurveyor.class).setConstraints(constraints).setInputData(inputData).addTag("dataUp").build();
        WorkManager.getInstance().enqueue(uploadWorkRequest);
    }
}
