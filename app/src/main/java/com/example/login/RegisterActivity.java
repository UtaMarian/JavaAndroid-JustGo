package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Model.User;
import com.example.login.service.RetrofitService;
import com.example.login.service.UserClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button loginBtn = findViewById(R.id.loginbtnRegister);
        Button registerBtn = findViewById(R.id.registerBtnRegister);
        EditText username = findViewById(R.id.usernameRegister);
        EditText password1 = findViewById(R.id.passwordRegister);
        EditText password2 = findViewById(R.id.password2Register);
        TextView errorView = findViewById(R.id.errormsgRegister);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameTxt = username.getText().toString();
                String passwd1Txt = password1.getText().toString();
                String passwd2Txt = password2.getText().toString();
                if ((usernameTxt.isEmpty() || passwd2Txt.isEmpty() || passwd1Txt.isEmpty())) {
                    errorView.setText("Complete all fields!");
                } else if (!passwd1Txt.equals(passwd2Txt)) {
                    errorView.setText("Password doesn't match");
                } else {
                    callRegisterAPI(usernameTxt, passwd1Txt, errorView);
                }
            }
        });
    }

    void navgatetoHomeActivity(User user){
        finish();
        Intent intent=new Intent(RegisterActivity.this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void callRegisterAPI(String uname, String pwd, TextView error) {
        Map<String, String> formData = new HashMap<>();
        formData.put("Email", uname);
        formData.put("Password", pwd);

        final Boolean[] failure = {false};
        Runnable runn = new Runnable() {
            @Override
            public void run() {
                Call<User> call = RetrofitService.getClient().create(UserClient.class).registerJWT(formData);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {

                            assert response.body() != null;

                            User user = new User();
                            user.setId(response.body().getId());
                            user.setEmail(response.body().getEmail());
                            user.setName(response.body().getName());
                            user.setToken(response.body().getToken());
                            //System.out.println(user.getToken());
                            navgatetoHomeActivity(user);
                        } else {
                            // Handle error
                            //Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();

                            assert response.errorBody() != null;
                            error.setText("Response failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        Thread mythread = new Thread(runn);
        mythread.start();

    }

}