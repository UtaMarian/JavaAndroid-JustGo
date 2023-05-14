package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.login.Model.User;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private User user=null;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name,email;
//    Button signOutBtn;


    //navbar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeMenu:
                Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ticketMenu:
                navigateToActivity(SearchRoutesActivity.class);
                break;
            case R.id.logoutMenu:
                navigateToActivity(MainActivity.class);
                break;
            case R.id.settingsMenu:
                navigateToActivity(SettingsActivity.class);
                break;
            case R.id.mapMenu:
                navigateToActivity(MapsActivity.class);
                break;
            case R.id.pannelMenu:
                navigateToActivity(PanelActivity.class);
                break;
            case R.id.myticketsMenu:
                navigateToActivity(TicketsActivity.class);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void navigateToActivity(Class<?> c){
        Intent intent=new Intent(HomeActivity.this,c);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //navbar
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            user= (User) bundle.getSerializable("user");

        }
        else{
            user=readSharedPreferences();

        }




        ImageView btn=findViewById(R.id.openbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //email from nav header
        View header = navigationView.getHeaderView(0);
        TextView email=header.findViewById(R.id.emailDisplay);
        if(user!=null){
            email.setText(user.getEmail());
        }
        else{
            user=readSharedPreferences();
            email.setText(user.getEmail());
        }


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        //if(acct!=null){
            //String personName = acct.getDisplayName();
            //String personEmail = acct.getEmail();
            //name.setText(personName);
            //email.setText(personEmail);
        //}

//        signOutBtn.setOnClickListener(v -> signOut());
    }
    void signOut(){
        gsc.signOut().addOnCompleteListener(task -> {
            finish();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

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


}