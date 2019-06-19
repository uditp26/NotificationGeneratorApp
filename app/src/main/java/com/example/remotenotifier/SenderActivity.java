package com.example.remotenotifier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class SenderActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SenderActivity";
    private static final String CHANNEL_ID = "RID";
    private static final String testReceiverId = "cirv69qWfgk:APA91bFdnwJLjIPWAbaFji0NuYqYgkhjp3rKqlwNd8dbJAjvDmUWxmwaTuQsFy6hUuzkz7dF2K8_G9Wkf5c8gGGYGzcJ815wM_QkB77-2sEp3YhuV0XJ5Nmu_rAfRAAfmWfQTXeKy-cL";
    Button clearButton, notifierButton;
    RadioGroup optionsGroup;
    int query;
    int id;
    Boolean networkStatus;
    ConnectivityManager cm;
    NetworkInfo network;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);
        notifierButton = findViewById(R.id.notifyButton);
        notifierButton.setOnClickListener(this);
        optionsGroup = findViewById(R.id.optionsRadioGroup);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "senderDB").allowMainThreadQueries().build();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.clearButton){
            optionsGroup.clearCheck();
            Log.d(TAG, "RadioGroup cleared!");
        }
        if(v.getId()==R.id.notifyButton){
            id = optionsGroup.getCheckedRadioButtonId();
            if(id==R.id.optRadioButton1){
                query = 1;
                Log.d(TAG, "Option1 selected!");
                initNotifier();
            }
            else if(id==R.id.optRadioButton2){
                query = 2;
                Log.d(TAG, "Option2 selected!");
                initNotifier();
            }
            else{
                Toast.makeText(this, "No option selected!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Boolean checkNetworkStatus(){
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = cm.getActiveNetworkInfo();
        return (network != null && network.isConnectedOrConnecting());
    }

    public void initNotifier(){
        networkStatus = checkNetworkStatus();
        if(networkStatus){
            Log.d(TAG, "Internet connection available.");
            uploadToFirebase();
        }
        else{
            Toast.makeText(this, "Internet not available!", Toast.LENGTH_SHORT).show();
            uploadToLocalDB();
        }
    }

    private void uploadToFirebase(){
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
        addnewSender(dbRef);
    }

    private void addnewSender(DatabaseReference dbRef){
        // Sender object receives details from login details of sender (including senderID && token value of receiver).
        Sender sender = new Sender("Udit", "udit@123.com");
        sender.optionNo = query;
        sender.optionVal = "option1";
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
        String arr[] = timeStamp.split("_");
        sender.uploadDate = arr[0];
        sender.uploadTime = arr[1];
        sender.receiverId = testReceiverId;
        dbRef.child("sender").child("Id1").setValue(sender);
    }

    private void uploadToLocalDB(){
        SenderEntity entity = new SenderEntity();
        entity.sid = "Id1";
        entity.email = "udit@123";
        entity.name = "Udit";
        entity.rid = testReceiverId;
        entity.choice = "Yes";
//        db.senderDao().delete(entity);
        db.senderDao().insert(entity);
        SenderEntity storedEntity = db.senderDao().loadBySid(entity.sid);
        Log.d(TAG, "Stored to localDb value: " + storedEntity.name);
        db.senderDao().delete(entity);
    }


}
