package com.example.ssd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class SignupPage extends AppCompatActivity {
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextName;
    private EditText editTextPhone;
    private boolean validate = false;
    Button BtnIdOverlapCheck, btninsert;
    ProgressDialog dialog = null;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        editTextId = (EditText) findViewById(R.id.ID);
        editTextPw = (EditText) findViewById(R.id.PW);
        editTextName = (EditText) findViewById(R.id.NAME);
        editTextPhone = (EditText) findViewById(R.id.Phone);
        BtnIdOverlapCheck = (Button) findViewById(R.id.IdOverlapCheck);
        btninsert = (Button) findViewById(R.id.signupButton);

        btninsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              // insert();
            }
        });

        BtnIdOverlapCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(SignupPage.this, "", "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        IdOverlapCheck();
                    }
                }).start();
            }
        });
    }

    //--------------------------아이디 중복체크 메서드--------------------------------------
    public void IdOverlapCheck() {
        class OverlapCheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/idoverlap.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", editTextId.getText().toString()));
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

                    if (!response.equalsIgnoreCase("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("if문 들어옴 ");
                                // Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alert2 = new AlertDialog.Builder(SignupPage.this);
                                alert2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                alert2.setMessage("이미 존재하는 아이디입니다. 다시 적어주세요.");
                                editTextId.setText("");
                                alert2.show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("if문 못 들어옴 ");
                                AlertDialog.Builder alert3 = new AlertDialog.Builder(SignupPage.this);
                                alert3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                alert3.setMessage("사용 가능한 아이디입니다.");
                                validate = true;
                                editTextId.setEnabled(false);
                                alert3.show();
                            }
                        });
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
        OverlapCheck task2 = new OverlapCheck();
        task2.execute();
    }
}
