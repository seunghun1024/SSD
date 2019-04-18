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

public class FindPw extends AppCompatActivity {

    private EditText Id;
    private EditText Name;
    private EditText PhoneNumber;
    private Button btnFindPw;
    ProgressDialog dialog = null;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pw);

        Id = (EditText) findViewById(R.id.findpwid);
        Name = (EditText) findViewById(R.id.findpwname);
        PhoneNumber = (EditText) findViewById(R.id.findpwphonenumber);
        btnFindPw = (Button) findViewById(R.id.findpwbutton);

        btnFindPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(FindPw.this, "", "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        FindPW();
                    }
                }).start();
            }
        });
    }

    public void FindPW() {
        class FindPW extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/findpw.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", Id.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Name", Name.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("PhoneNumber", PhoneNumber.getText().toString()));
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
                                AlertDialog.Builder alert2 = new AlertDialog.Builder(FindPw.this);
                                alert2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                alert2.setMessage("해당 정보에 해당하는 비밀번호는 [" + response + "]입니다.");
                                alert2.show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alert3 = new AlertDialog.Builder(FindPw.this);
                                alert3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                alert3.setMessage("해당 정보에 맞는 비밀번호가 없습니다.");
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
        FindPW task2 = new FindPW();
        task2.execute();
    }
}
