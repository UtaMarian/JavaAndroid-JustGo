package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.login.Model.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SettingsActivity extends AppCompatActivity {

    private User user=null;
    TextView email,password1,password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            user= (User) bundle.getSerializable("user");
        }
        email=findViewById(R.id.email);
        password1=findViewById(R.id.password1);
        password2=findViewById(R.id.password2);

        Button saveBtn=findViewById(R.id.saveBioBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveCredentials();
                } catch (GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
                Intent intent =new Intent(SettingsActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        Button backBtn=findViewById(R.id.backBtnList3);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(SettingsActivity.this,HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

    }

    private void saveCredentials() throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                this,
                "my_encrypted_cred",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(password1.getText().toString().equals(password2.getText().toString())){
            editor.putString("email", email.getText().toString());
            editor.putString("password", password1.getText().toString());
        }

        editor.apply();
    }
}