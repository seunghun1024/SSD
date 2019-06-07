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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
                Intent intent = new Intent(getApplicationContext(),InputSpec.class);
                startActivity(intent);
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
        class IdOverlapCheck extends AsyncTask<String, Void, String> {
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
        IdOverlapCheck task2 = new IdOverlapCheck();
        task2.execute();
    }

    //-----------------------------회원가입 눌렀을 시 메서드---------------------------------------------------
    public void insert() {
        String Id = editTextId.getText().toString();
        String Pw = editTextPw.getText().toString();
        String Name = editTextName.getText().toString();
        String Phone = editTextPhone.getText().toString();

        if (!validate) {
            AlertDialog.Builder alert4 = new AlertDialog.Builder(SignupPage.this);
            alert4.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert4.setMessage("아이디 중복체크를 하지 않았습니다.");
            alert4.show();
            return;
        }

        if (Id.equals("") || Pw.equals("") || Name.equals("") || Phone.equals("")) {
            AlertDialog.Builder alert5 = new AlertDialog.Builder(SignupPage.this);
            alert5.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert5.setMessage("빈 칸을 다 채워주시기 바랍니다.");
            alert5.show();
            return;
        }

        insertoToDatabase(Id, Pw, Name, Phone);
    }

    private void insertoToDatabase(String Id, String Pw, String Name, String Phone) {
        class InsertData extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //  loading = ProgressDialog.show(SignupPage2.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                System.out.println("백그라운드 끝");
                Intent mainpage = new Intent(SignupPage.this, MainActivity.class);
                startActivity(mainpage);
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];
                    String Name = (String) params[2];
                    String Phone = (String) params[3];

                    String link = "http://203.234.62.96:7979/signup.php";
                    String data = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");
                    data += "&" + URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8");
                    data += "&" + URLEncoder.encode("Phone", "UTF-8") + "=" + URLEncoder.encode(Phone, "UTF-8");

                    System.out.println(Id + Pw + Name + Phone);

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id, Pw, Name, Phone);
    }
}
