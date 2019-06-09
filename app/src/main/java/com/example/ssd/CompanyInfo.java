package com.example.ssd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.StringTokenizer;

import static com.example.ssd.MainActivity.sttid;
import static com.example.ssd.Spec.specgrades;
import static com.example.ssd.Spec.specintern;
import static com.example.ssd.Spec.specopic;
import static com.example.ssd.Spec.spectoeic;
import static com.example.ssd.Spec.spectoss;
import static com.example.ssd.Spec.spectraining;
import static com.example.ssd.Spec.specvolunteer;
import static java.lang.Thread.sleep;


public class CompanyInfo extends AppCompatActivity {
    Button predictBtn,interestBtn,urlBtn;
    TextView companyName,companyInfo;
    Search search;
    ProgressDialog dialog = null;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    static String cominfo, comurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_info);

        companyName = (TextView)findViewById(R.id.textView_enterprisename);
        companyInfo = (TextView)findViewById(R.id.textView_enterpriseinfo);

        companyName.setText(search.enterid);
        bring();

        predictBtn = (Button) findViewById(R.id.predict_btn);
        interestBtn = (Button) findViewById(R.id.interest_btn);
        urlBtn = (Button) findViewById(R.id.url_btn);


        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override //예측페이지로 넘어가야함
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(),PredictResult.class);
                        startActivity(intent);
                    }
                }).start();

            }
        });


        interestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        passghi();
                    }
                }).start();
            }
        });

        urlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //url 가져오기
                Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse("http://"+ comurl));
                startActivity(intent);
            }
        });

    }

    void bring() {
        class logincheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/bringcominfo.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("enterid", search.enterid));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Response from PHP : " + response);
                        }
                    });

                    StringTokenizer token = new StringTokenizer(response, "`");
                    cominfo = token.nextToken();
                    comurl = token.nextToken();
                    companyInfo.setText(cominfo);

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

    public void passghi() {
        class passGHI extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/addenterprise.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("sttid", sttid));
                    nameValuePairs.add(new BasicNameValuePair("enterId", search.enterid));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);


                } catch (
                        Exception e)

                {
                    // dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        passGHI task2 = new passGHI();
        task2.execute();
    }

}
