package com.example.ssd;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import android.widget.ViewFlipper;

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

public class Mainpage extends AppCompatActivity {
    public static String selectprise = "";
    private ListView mListView;
    final static ArrayList<String> mprisename = new ArrayList<>();
    final static ArrayList<String> mpriseinfo = new ArrayList<>();
    final static ArrayList<String> muid = new ArrayList<>();
    private int realposition;
    ProgressDialog dialog = null;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    View editSearch;
    ImageButton infoBtn;
    ViewFlipper v_fllipper;
    Button specBtn, questionBtn, logoutBtn;
    TextView home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("야너두");

        home = (TextView)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Mainpage.class);
                startActivity(intent);
            }
        });


        editSearch = (View) findViewById(R.id.main_search);


        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Search.class);
                startActivity(intent);
            }
        });


        infoBtn = (ImageButton) findViewById(R.id.info_btn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info = new Intent(getApplicationContext(),UserInfo.class);
                startActivity(info);
            }
        });

        specBtn = (Button) findViewById(R.id.spec_btn);
        specBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info = new Intent(getApplicationContext(),Spec.class);
                startActivity(info);
            }
        });

        questionBtn = (Button) findViewById(R.id.question_btn);
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent info = new Intent(getApplicationContext(),UserInfo.class);
                //startActivity(info);
            }
        });

        logoutBtn = (Button) findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        int images[] = {
                R.drawable.home1,
                R.drawable.home2,
                R.drawable.home3
        };

        v_fllipper = findViewById(R.id.image_slide);

        for(int image : images) {
            fllipperImages(image);
        }



        mListView = (ListView) findViewById(R.id.enterpriseview);

        Spec spec = new Spec();
        spec.SpecCheck();

        enterpriselist();
    }

    // 이미지 슬라이더 구현 메서드
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(2000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



    public void enterpriselist() {
        class Enterpriselistsub extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/enterpriselist.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("sttid", sttid));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);
                    System.out.println("받아온 관리자 값 : " + sttid);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringTokenizer str = new StringTokenizer(response, ",");
                            int countTokens = str.countTokens();
                            System.out.println("토큰 수 : " + countTokens);
                            final ArrayList<String> mentername = new ArrayList<>();
                            for (int i = 0; i < countTokens; i++) {
                                    String uid = str.nextToken();
                                    muid.add(uid);
                                    System.out.println("uid값 1 : " + uid);
                                    mentername.add(uid);
                            }
                            mprisename.clear();
                            mprisename.addAll(mentername);
                            dataSetting();
                        }
                    });
                } catch (
                        Exception e) {
                    dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
                System.out.println("끝!");
                return "test!!";
            }
        }
        Enterpriselistsub task = new Enterpriselistsub();
        task.execute();
    }/*

    private String passorfail() {
        class Passorfailsub extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... params) {
                try {

                    httpclient2 = new DefaultHttpClient();
                    httppost2 = new HttpPost("http://203.234.62.96:7979/speccheck.php");
                    nameValuePairs2 = new ArrayList<NameValuePair>(2);
                    nameValuePairs2.add(new BasicNameValuePair("Id", sttid));
                    httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
                    response2 = httpclient2.execute(httppost2);

                    ResponseHandler<String> responseHandler2 = new BasicResponseHandler();
                    final String response2 = httpclient2.execute(httppost2, responseHandler2);
                    System.out.println("Response2 : " + response2);

                    StringTokenizer token = new StringTokenizer(response2, ",");
                    university = token.nextToken();
                    department = token.nextToken();
                    grades = Double.parseDouble(token.nextToken()) / 4.5;
                    toeic = Double.parseDouble(token.nextToken()) / 990;
                    opic = token.nextToken();
                    toss = Double.parseDouble(token.nextToken()) / 8;
                    training = Double.parseDouble(token.nextToken());
                    intern = Double.parseDouble(token.nextToken());
                    volunteer = Double.parseDouble(token.nextToken());

                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/passorfail.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("muid", muid.get(0)));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);
                    System.out.println("받아온 관리자 값 : " + muid.get(0));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringTokenizer str = new StringTokenizer(response, ",");
                            int countTokens = str.countTokens();
                            System.out.println("토큰 수 : " + countTokens);

                            grades2 = Integer.parseInt(str.nextToken());
                            toeic2 = Integer.parseInt(str.nextToken());
                            opic2 = Integer.parseInt(str.nextToken());
                            toss2 = Integer.parseInt(str.nextToken());
                            training2 = Integer.parseInt(str.nextToken());
                            intern2 = Integer.parseInt(str.nextToken());
                            volunteer2 = Integer.parseInt(str.nextToken());
                            finaleducation2 = Integer.parseInt(str.nextToken());
                            certificate2 = Integer.parseInt(str.nextToken());

                            comsum = (grades2 + toeic2 + opic2 + toss2 + training2 + intern2 + volunteer2 + finaleducation2 + certificate2) / 100d;
                            passfail = (grades * (grades2/comsum)) + (toeic * (toeic2/comsum)) + (toss * (toss2/comsum)) + (training * (training2/comsum)) + (intern * (intern2/comsum)) + (volunteer * (volunteer2/comsum) *//* finale~~~~ 추가해야됨 ~~~~~~~~~*//*);
                            System.out.println(passfail);

                            *//*for (int i = 0; i < countTokens; i++) {
                                if(i == 0) {
                                    String uid = str.nextToken();
                                    System.out.println("uid값 1 : " + uid);
                                }else {
                                    String uid = str.nextToken();
                                    System.out.println("uid값 2 : " + uid);
                                }
                            }*//*
                        }
                    });


                } catch (
                        Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }
                System.out.println("끝!");
                return "test!!";
            }
        }
        Passorfailsub task = new Passorfailsub();
        task.execute();

        return "결과값";
    }
*/

    private void dataSetting() {
        ListViewAdapter mListAdapter = new ListViewAdapter();

        mListAdapter.addItem(mprisename);

        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                realposition = position;
                selectprise = mprisename.get(position);
                mListView.setSelector(new PaintDrawable(0xF5DA81));
            }
        });

    }

    public void logOut() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mainpage.this);
        alert_confirm.setMessage("로그아웃 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sttid = null;
                        Intent MainActivity = new Intent(Mainpage.this, MainActivity.class);
                        startActivity(MainActivity);
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
    }

}
