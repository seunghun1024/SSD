package com.example.ssd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

import static com.example.ssd.SignupPage.Id;

public class InputSpec extends AppCompatActivity {
    private Spinner spinner;
    private Button btnsignup;
    private EditText ingrades, intoeic, inopic, intoss;
    private RadioGroup radioGroupstudytrain, radioGroupintern, radioGroupvolunteer;
    private RadioButton studytrainbuttonyes, studytrainbuttonno,internbuttonyes, internbuttonno, volunteerbuttonyes, volunteerbuttonno;
    static String Studytrain2, Intern2, Volunteer2;
    String Academic, Major;
    ArrayList<String> arrayAcademic;
    ArrayAdapter<String> adapterAcademic;
    ArrayList<String> arrayMajor;
    ArrayAdapter<String> adapterMajor;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_spec);

        radioGroupstudytrain = (RadioGroup) findViewById(R.id.studytrainradiogroup2);
        studytrainbuttonyes = (RadioButton) findViewById(R.id.studytrainyes2);
        studytrainbuttonno = (RadioButton) findViewById(R.id.studytrainno2);
        radioGroupintern = (RadioGroup) findViewById(R.id.internradiogroup2);
        internbuttonyes = (RadioButton) findViewById(R.id.internyes2);
        internbuttonno = (RadioButton) findViewById(R.id.internno2);
        radioGroupvolunteer = (RadioGroup) findViewById(R.id.volunteerradiogroup2);
        volunteerbuttonyes = (RadioButton) findViewById(R.id.volunteeryes2);
        volunteerbuttonno = (RadioButton) findViewById(R.id.volunteerno2);
        btnsignup = (Button) findViewById(R.id.save);
        ingrades = (EditText) findViewById(R.id.insertgrades);
        intoeic = (EditText) findViewById(R.id.inserttoeic);
        inopic = (EditText) findViewById(R.id.insertopic);
        intoss = (EditText) findViewById(R.id.inserttoss);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        aa();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                }).start();
            }
        });

        radioGroupstudytrain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Studytrain2 = String.valueOf((RadioButton) group.findViewById(checkedId));
            }
        });

        radioGroupintern.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Intern2 = String.valueOf((RadioButton) group.findViewById(checkedId));
            }
        });

        radioGroupvolunteer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Volunteer2 = String.valueOf((RadioButton) group.findViewById(checkedId));
            }
        });


        arrayAcademic = new ArrayList<>();
        arrayAcademic.add("고등학교 졸업");
        arrayAcademic.add("전문대 재학중");
        arrayAcademic.add("전문대 졸업");
        arrayAcademic.add("4년제 대학 재학중");
        arrayAcademic.add("4년제 대학 졸업");

        adapterAcademic = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayAcademic);

        spinner = (Spinner)findViewById(R.id.spinner_Academic);
        spinner.setAdapter(adapterAcademic);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),arrayAcademic.get(i)+"가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
                Academic = arrayAcademic.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        arrayMajor = new ArrayList<>();
        arrayMajor.add("소프트웨어융합공학과");
        arrayMajor.add("컴퓨터공학과");
        arrayMajor.add("전자공학과");

        adapterMajor = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayMajor);

        spinner = (Spinner)findViewById(R.id.spinner_major);
        spinner.setAdapter(adapterMajor);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),arrayMajor.get(i)+"가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
                Major = arrayMajor.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }





    public void aa() {
        class passGHI extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    int a = 0, b = 0, c = 0;

                    System.out.println(Studytrain2 + " --- " + Intern2 + " --- " + Volunteer2);

                    if(Studytrain2.contains("studytrainyes2")){
                        a = 1;
                    }else{
                        a = 0;
                    }
                    if(Intern2.contains("internyes2")){
                        b = 1;
                    }else{
                        b = 0;
                    }
                    if(Volunteer2.contains("volunteeryes2")){
                        c = 1;
                    }else{
                        c = 0;
                    }

                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/insertspec.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", Id));
                    nameValuePairs.add(new BasicNameValuePair("Academic", Academic));
                    nameValuePairs.add(new BasicNameValuePair("Major", Major));
                    nameValuePairs.add(new BasicNameValuePair("inGrades", ingrades.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("inToeic", intoeic.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("inOpic", inopic.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("inToss", intoss.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Training", Integer.toString(a)));
                    nameValuePairs.add(new BasicNameValuePair("Intern", Integer.toString(b)));
                    nameValuePairs.add(new BasicNameValuePair("Volunteer", Integer.toString(c)));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                   // ResponseHandler<String> responseHandler = new BasicResponseHandler();
                   // final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("noResponse : " + response);
                    System.out.println(Academic + "-" + Major + "-" + ingrades.getText().toString() + "-" + a + b);


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
