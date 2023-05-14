package com.example.login;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Model.User;

import com.example.login.service.RetrofitService;
import com.example.login.service.UserClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

//    IAuthAPI iAuthAPI;

    String bioEmail="",bioPassword="";
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;

    Button biometricBtn;
    TextView error;

    private static String token;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView usernameTxt =(TextView) findViewById(R.id.username);
        TextView passwordTxt =(TextView) findViewById(R.id.password);
        error=(TextView) findViewById(R.id.errormsg);

        MaterialButton loginbtn= findViewById(R.id.loginbtn);
        biometricBtn= findViewById(R.id.biometric_btn);
        googleBtn = findViewById(R.id.google_btn);

        //Google OAuth2
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , connectionResult -> {})
                .addApi(Auth.CREDENTIALS_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        gsc = GoogleSignIn.getClient(this,gso);
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if(acct!=null){
//            navgatetoHomeActivity(null);
//        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signIn();
            }
        });

        biometricBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricLogin();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                error.setText("");
                String email = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                callLoginApi(email,password,"Credentiale invalide!");

            }
        });

        Button registerBtn=findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void BiometricLogin()
    {
        checkBiometricSupported();
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt=new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Authentication error: "+errString, Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                try {
                    getCredentials();
                } catch (GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
                if(bioEmail.isEmpty() || bioPassword.isEmpty()){
                    error.setText("Autentificarea biometrica nu a fost setata in aplicatie");
                }
                else{
                    callLoginApi(bioEmail,bioPassword,"Credentiale invalide! Va rugam sa va setati logarea biometrica in aplicatie");
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Authentication failed
                Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        biometricBtn.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promptInfo=dialogMetric();
            promptInfo.setDeviceCredentialAllowed(true);
            biometricPrompt.authenticate(promptInfo.build());
        });
    }
    androidx.biometric.BiometricPrompt.PromptInfo.Builder dialogMetric(){
        return new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login").setSubtitle("Login using your biometric credential");
    }

    private void checkBiometricSupported(){
        String info="";
        BiometricManager manager= BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG))
        {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info="App can authenticate using biometrics";
                enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info="No biometric features avaible on this device";
                enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info="Biometric features are currently unavaible";
                enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info="Need register at least one fingerprint";
                enableButton(false,true);
                break;
            default:
                info="Unknown cause";
                break;
        }
        TextView txtinfo=findViewById(R.id.infotext);
        txtinfo.setText(info);

    }

    void enableButton(boolean enable)
    {
        biometricBtn.setEnabled(true);
    }
    void enableButton(boolean enable,boolean enroll)
    {
        enableButton(enable);
        if(!enroll) return;
        Intent enrollIntent=new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,BiometricManager.Authenticators.BIOMETRIC_STRONG |
                BiometricManager.Authenticators.BIOMETRIC_WEAK);
        startActivity(enrollIntent);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle result from Google Sign-In API
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result!=null) {
                handleSignInResult(result);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;

            String email = acct.getEmail();
            String name = acct.getDisplayName();
            String id = acct.getId();

            System.out.println(acct.getAccount());
            System.out.println(acct.getIdToken());
            System.out.println(acct.getServerAuthCode());
            User user=new User();
            user.setName(name);
            user.setEmail(email);
            user.setId(id);
            user.setToken(acct.getIdToken());
            navgatetoHomeActivity(user);
            // TODO: Save user information or authenticate with your backend

        } else {
            // Signed out, show unauthenticated UI
        }
    }
   void navgatetoHomeActivity(User user){
       System.out.println(user.getEmail());
       System.out.println(user.getToken());
        finish();
        Intent intent=new Intent(MainActivity.this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);
   }

    public void saveSharePreferences(User user) {
        SharedPreferences sp;
        sp = getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("jwt", user.getToken());
        editor.putString("email", user.getEmail());
        editor.putString("username", user.getName());
        editor.putString("userid", user.getId());
        editor.apply();
    }

    public User readSharedPreferences() {
        SharedPreferences sp;
        sp = getSharedPreferences("sharedPref", MODE_PRIVATE);
        User user=new User();
        user.setToken(sp.getString("jwt", ""));
        user.setEmail(sp.getString("email", ""));
        user.setName(sp.getString("username", ""));
        user.setId(sp.getString("userid", ""));
        return user;
    }

    private void getCredentials() throws GeneralSecurityException, IOException {
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

        bioEmail = sharedPreferences.getString("email", "");
        bioPassword=sharedPreferences.getString("password", "");
    }

    public void callLoginApi(String email,String password,String errorMsg){
        Map<String, String> formData = new HashMap<>();
        formData.put("Email", email);
        formData.put("Password", password);

        final Boolean[] failure = {false};
        Runnable runn=new Runnable() {
            @Override
            public void run() {
                Call<User> call = RetrofitService.getClient().create(UserClient.class).loginJWT(formData);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {

                            assert response.body() != null;

                            User user=new User();
                            user.setId(response.body().getId());
                            user.setEmail(response.body().getEmail());
                            user.setName(response.body().getName());
                            user.setToken(response.body().getToken());
                            //System.out.println(user.getToken());
                            navgatetoHomeActivity(user);
                        } else {
                            // Handle error
                            error.setText(errorMsg);
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Handle failure
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        failure[0] =true;
                    }
                });

            }
        };

        Thread mythread = new Thread(runn);
        mythread.start();


    }
}