package com.example.ssd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText loginId;
    private EditText loginPw;
    Button BtnLogin;
    ProgressDialog dialog = null;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginId = (EditText) findViewById(R.id.ID);
        loginPw = (EditText) findViewById(R.id.PW);
        BtnLogin = (Button) findViewById(R.id.loginButton);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MainActivity.this, "", "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        login();
                    }
                }).start();
            }
        });
    }

    void login() {
        class logincheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/logincheck.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", loginId.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Pw", loginPw.getText().toString()));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Response from PHP : " + response);
                            dialog.dismiss();
                        }
                    });

                    if (response.equalsIgnoreCase("success")) {
                        //mHandler.postDelayed(new Runnable(){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("if문 들어옴 ");
                                // Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent mainpage = new Intent(MainActivity.this, Mainpage.class);
                                    startActivity(mainpage);
                                loginId.setText("");
                                loginPw.setText("");
                            }
                        });
                    } else {
                        System.out.println("if문 못 들어옴 ");

                        Toast.makeText(MainActivity.this, "아이디, 비밀번호를 확인 해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } catch (
                        Exception e)

                {
                    dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        logincheck task = new logincheck();
        task.execute();
    }

    public void btnsignup(View v) {
        Intent signuppage = new Intent(this, SignupPage.class);
        startActivity(signuppage);
    }

    public void btnfindid(View v) {
        Intent findidpage = new Intent(this, FindId.class);
        startActivity(findidpage);
    }

    public void btnfindpw(View v) {
        Intent findpwpage = new Intent(this, FindPw.class);
        startActivity(findpwpage);
    }
}
