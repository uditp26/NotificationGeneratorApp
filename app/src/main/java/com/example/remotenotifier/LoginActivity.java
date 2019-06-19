package com.example.remotenotifier;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    Button sender, receiver;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sender = findViewById(R.id.sendButton);
        receiver = findViewById(R.id.recButton);
        sender.setOnClickListener(this);
        receiver.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sendButton){
            intent = new Intent(this, SenderActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.recButton){
            intent = new Intent(this, ReceiverActivity.class);
            startActivity(intent);
        }
    }

}
