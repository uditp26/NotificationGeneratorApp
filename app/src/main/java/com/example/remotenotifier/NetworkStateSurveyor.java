package com.example.remotenotifier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NetworkStateSurveyor extends Worker {

    private static final String TAG = "NetworkStateSurveyor";
    ConnectivityManager cm;
    NetworkInfo network;
    String token, output;

    public NetworkStateSurveyor(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String temp = getInputData().getString("token");
        Log.d(TAG, "Key fetch -> " + temp);
        if(checkNetworkStatus()){
            Log.d(TAG, "Connected to internet.");
            //Upload to firebase
            if(temp!=null){
                token = temp;
                uploadToFirebase(false);
            }
            else{
                token = NotifierSingleton.getObject().userToken;
                output = getInputData().getString(token);
                //upload data
                uploadToFirebase(true);
            }
            return Result.success();
        }
        else{
            Log.d(TAG, "Not connected to internet; Storing Locally... ");
            // Store to Room DB
            if(temp!=null){
                //upload token
                token = temp;
            }
            else{
                token = NotifierSingleton.getObject().userToken;
                output = getInputData().getString(token);
            }
            return Result.retry();
        }
    }

    public Boolean checkNetworkStatus(){
        cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        network = cm.getActiveNetworkInfo();
        return (network != null && network.isConnectedOrConnecting());
    }

    private void uploadToFirebase(Boolean flag){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://remotenotifier.firebaseio.com/");
//        DatabaseReference dbRef = database.getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
                Map<String, Object> ds = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + ds.keySet());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        addnewReceiver(dbRef, flag);
    }

    private void addnewReceiver(DatabaseReference dbRef, Boolean flag){
        // Receiver object receives details from login details of receiver (including receiverID).
        Receiver receiver = new Receiver("Ankit", "ankit@456.com");
        if(flag){
            receiver.optionNo = 1;
            receiver.responseVal = output;
            receiver.token = token;
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            String arr[] = timeStamp.split("_");
            receiver.uploadDate = arr[0];
            receiver.uploadTime = arr[1];
            dbRef.child("receiver").child("RId1").setValue(receiver);
        }
        else{
            receiver.token = token;
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            String arr[] = timeStamp.split("_");
            receiver.uploadDate = arr[0];
            receiver.uploadTime = arr[1];
            dbRef.child("receiver").child("RId1").child("token").setValue(token);
            dbRef.child("receiver").child("RId1").child("uploadDate").setValue(arr[0]);
            dbRef.child("receiver").child("RId1").child("uploadTime").setValue(arr[1]);
        }
    }

}
