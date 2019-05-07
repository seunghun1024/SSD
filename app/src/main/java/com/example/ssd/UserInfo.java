package com.example.ssd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.StringTokenizer;

import static com.example.ssd.MainActivity.sttid;

public class UserInfo extends AppCompatActivity {

    private EditText editTextId, editTextPw, editTextName, editTextPhone;
    private Button btnModify, btnLeave;
    static boolean infoboolean;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    String tokenId, tokenPw, tokenName, tokenPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        editTextId = (EditText) findViewById(R.id.ID);
        editTextPw = (EditText) findViewById(R.id.PW);
        editTextName = (EditText) findViewById(R.id.NAME);
        editTextPhone = (EditText) findViewById(R.id.PHONE);
        btnModify = (Button) findViewById(R.id.infomodify);
        btnLeave = (Button) findViewById(R.id.infoleave);
        infoboolean = true;

        InfoCheck();

        btnModify.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(infoboolean == true) {
                    editTextPw.setEnabled(true);
                    editTextName.setEnabled(true);
                    editTextPhone.setEnabled(true);
                    btnModify.setText("취소");
                    btnLeave.setText("저장");
                    infoboolean = false;
                }else {
                    editTextPw.setEnabled(false);
                    editTextName.setEnabled(false);
                    editTextPhone.setEnabled(false);
                    btnModify.setText("수정");
                    btnLeave.setText("탈퇴");
                    infoboolean = true;
                }
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (infoboolean == true) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(UserInfo.this);
                    alert_confirm.setMessage("정말 계정을 탈퇴하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    InfoLeave();
                                }
                            }).setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }else{
                    InfoModify();
                }
            }
        });
    }

    public void InfoCheck() {
        class InfoCheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/infocheck.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", sttid));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Response from PHP : " + response);
                            //dialog.dismiss();
                        }
                    });

                    StringTokenizer token = new StringTokenizer(response, ",");
                    tokenId = token.nextToken();
                    tokenPw = token.nextToken();
                    tokenName = token.nextToken();
                    tokenPhone = token.nextToken();

                    editTextId.setText(tokenId);
                    editTextPw.setText(tokenPw);
                    editTextName.setText(tokenName);
                    editTextPhone.setText(tokenPhone);
                    editTextId.setEnabled(false);
                    editTextPw.setEnabled(false);
                    editTextName.setEnabled(false);
                    editTextPhone.setEnabled(false);
                } catch (
                        Exception e)

                {
                    dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        InfoCheck task2 = new InfoCheck();
        task2.execute();
    }

    public void InfoModify() {
        class InfoModify extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/modifyInfo.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", sttid));
                    nameValuePairs.add(new BasicNameValuePair("Pw", editTextPw.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Name", editTextName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Phone", editTextPhone.getText().toString()));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Response from PHP~~~ : " + response);
                            //dialog.dismiss();
                            editTextPw.setEnabled(false);
                            editTextName.setEnabled(false);
                            editTextPhone.setEnabled(false);
                            btnModify.setText("수정");
                            btnLeave.setText("탈퇴");
                            infoboolean = true;
                        }
                    });
                } catch (
                        Exception e)

                {
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        InfoModify task = new InfoModify();
        task.execute();
    }

    public void InfoLeave() {
        class InfoLeave extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/leaveInfo.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", sttid));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Response from PHP~~~ : " + response);

                            Intent MainActivity = new Intent(UserInfo.this, MainActivity.class);
                            startActivity(MainActivity);
                        }
                    });
                } catch (
                        Exception e)

                {
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        InfoLeave task = new InfoLeave();
        task.execute();
    }
}
