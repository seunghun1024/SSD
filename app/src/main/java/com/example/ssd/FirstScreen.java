package com.example.ssd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstScreen extends AppCompatActivity {
    Button BtnFirstSignup;
    Button BtnFirstLogin;
    Button BtnFirstMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

        BtnFirstSignup = (Button) findViewById(R.id.first_signup);

        BtnFirstSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupPage.class);
                startActivity(intent);
            }
        });

        BtnFirstLogin = (Button) findViewById(R.id.first_login);

        BtnFirstLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        BtnFirstMain= (Button) findViewById(R.id.first_main);

        BtnFirstMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Mainpage.class);
                startActivity(intent);
            }
        });

    }

    public void btnsignup(View v) {
        Intent signuppage = new Intent(this, SignupPage.class);
        startActivity(signuppage);
    }

    public void btnlogin(View v) {
        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
    }

    public void btnmain(View v) {
        Intent main = new Intent(this, Mainpage.class);
        startActivity(main);
    }
}
