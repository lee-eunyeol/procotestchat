package com.dmsduf.socketio_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dmsduf.socketio_test.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
    EditText nickname_edittext;
    EditText useridx_edittext;
    SharedSettings sharedSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        nickname_edittext = findViewById(R.id.main_nickname);
        useridx_edittext = findViewById(R.id.main_room_idx);
        sharedSettings = new SharedSettings(this,"user_info");
    }
    public void go_chat(View view){
        sharedSettings.set_something_int("user_idx",Integer.parseInt(useridx_edittext.getText().toString()));
        sharedSettings.set_something_string("user_nickname",nickname_edittext.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
