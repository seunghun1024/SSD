package com.example.ssd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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

public class Spec extends AppCompatActivity {

    private EditText editTextGrade, editTextTOEIC, editTextOPIC, editTextTOS;
    private Button btnSpecModify, btnSpecReset;
    private RadioGroup radioGroupstudytrain, radioGroupintern, radioGroupvolunteer;
    private RadioButton studytrainbuttonyes, studytrainbuttonno,internbuttonyes, internbuttonno, volunteerbuttonyes, volunteerbuttonno;
    Spinner spinnerSchool, spinnerDepartment;
    static boolean specboolean;
    static String School, Department, Studytrain, Intern, Volunteer;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    String specuniversity, specdepartment;
    static double specgrades, spectoeic, specopic, spectoss, spectraining, specintern, specvolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec);

        radioGroupstudytrain = (RadioGroup) findViewById(R.id.studytrainradiogroup);
        studytrainbuttonyes = (RadioButton) findViewById(R.id.studytrainyes);
        studytrainbuttonno = (RadioButton) findViewById(R.id.studytrainno);
        radioGroupintern = (RadioGroup) findViewById(R.id.internradiogroup);
        internbuttonyes = (RadioButton) findViewById(R.id.internyes);
        internbuttonno = (RadioButton) findViewById(R.id.internno);
        radioGroupvolunteer = (RadioGroup) findViewById(R.id.volunteerradiogroup);
        volunteerbuttonyes = (RadioButton) findViewById(R.id.volunteeryes);
        volunteerbuttonno = (RadioButton) findViewById(R.id.volunteerno);
        spinnerSchool = (Spinner) findViewById(R.id.spinner);
        spinnerDepartment = (Spinner) findViewById(R.id.spinner2);
        editTextGrade = (EditText) findViewById(R.id.grade);
        editTextTOEIC = (EditText) findViewById(R.id.toeic);
        editTextOPIC = (EditText) findViewById(R.id.opic);
        editTextTOS = (EditText) findViewById(R.id.toeicspeaking);
        btnSpecModify = (Button) findViewById(R.id.specmodify);
        btnSpecReset = (Button) findViewById(R.id.specreset);
        specboolean = true;

        SpecCheck();

        btnSpecModify.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(specboolean == true) {
                    editTextGrade.setEnabled(true);
                    editTextTOEIC.setEnabled(true);
                    editTextOPIC.setEnabled(true);
                    editTextTOS.setEnabled(true);
                    studytrainbuttonyes.setEnabled(true);
                    studytrainbuttonno.setEnabled(true);
                    internbuttonyes.setEnabled(true);
                    internbuttonno.setEnabled(true);
                    volunteerbuttonyes.setEnabled(true);
                    volunteerbuttonno.setEnabled(true);
                    btnSpecModify.setText("취소하기");
                    btnSpecReset.setText("저장하기");
                    specboolean = false;
                }else {
                    editTextGrade.setEnabled(false);
                    editTextTOEIC.setEnabled(false);
                    editTextOPIC.setEnabled(false);
                    editTextTOS.setEnabled(false);
                    studytrainbuttonyes.setEnabled(false);
                    studytrainbuttonno.setEnabled(false);
                    internbuttonyes.setEnabled(false);
                    internbuttonno.setEnabled(false);
                    volunteerbuttonyes.setEnabled(false);
                    volunteerbuttonno.setEnabled(false);
                    btnSpecModify.setText("수정하기");
                    btnSpecReset.setText("스펙 초기화");
                    specboolean = true;
                }
            }
        });

        btnSpecReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (specboolean == true) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Spec.this);
                    alert_confirm.setMessage("스펙을 초기화 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //SpecReset();
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
                    SpecModify();
                    editTextGrade.setEnabled(false);
                    editTextTOEIC.setEnabled(false);
                    editTextOPIC.setEnabled(false);
                    editTextTOS.setEnabled(false);
                    studytrainbuttonyes.setEnabled(false);
                    studytrainbuttonno.setEnabled(false);
                    internbuttonyes.setEnabled(false);
                    internbuttonno.setEnabled(false);
                    volunteerbuttonyes.setEnabled(false);
                    volunteerbuttonno.setEnabled(false);
                    btnSpecModify.setText("수정하기");
                    btnSpecReset.setText("스펙 초기화");
                    specboolean = true;
                }
            }
        });

        radioGroupstudytrain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Studytrain = String.valueOf((RadioButton) group.findViewById(checkedId));
            }
        });

        radioGroupintern.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Intern = String.valueOf((RadioButton) group.findViewById(checkedId));
            }
        });

        radioGroupvolunteer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Volunteer = String.valueOf((RadioButton) group.findViewById(checkedId));
            }
        });

        spinnerSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                School = String.valueOf(parent.getItemAtPosition(position));
                System.out.println(parent.getItemAtPosition(position) + " 선택!!!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Department = String.valueOf(parent.getItemAtPosition(position));
                System.out.println(parent.getItemAtPosition(position) + " 선택2!!!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void SpecCheck() {
        class SpecCheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/speccheck.php");
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
                    specuniversity = token.nextToken();
                    specdepartment = token.nextToken();
                    specgrades = Double.valueOf(token.nextToken());
                    spectoeic = Double.valueOf(token.nextToken());
                    specopic = Double.valueOf(token.nextToken());
                    spectoss = Double.valueOf(token.nextToken());
                    spectraining = Double.valueOf(token.nextToken());
                    specintern = Double.valueOf(token.nextToken());
                    specvolunteer = Double.valueOf(token.nextToken());


                    editTextGrade.setText(Double.toString(specgrades));
                    editTextTOEIC.setText(Double.toString(spectoeic));
                    editTextOPIC.setText(Double.toString(specopic));
                    editTextTOS.setText(Double.toString(spectoss));
                    editTextGrade.setEnabled(false);
                    editTextTOEIC.setEnabled(false);
                    editTextOPIC.setEnabled(false);
                    editTextTOS.setEnabled(false);
                    studytrainbuttonyes.setEnabled(false);
                    studytrainbuttonno.setEnabled(false);
                    internbuttonyes.setEnabled(false);
                    internbuttonno.setEnabled(false);
                    volunteerbuttonyes.setEnabled(false);
                    volunteerbuttonno.setEnabled(false);
                    System.out.println(spectraining + "~!!!!!!~" + specintern + "~~" + specvolunteer);

                    if(spectraining == 1) {
                        studytrainbuttonyes.setChecked(true);
                    }else{
                        studytrainbuttonno.setChecked(true);
                    }
                    if(specintern == 1) {
                        internbuttonyes.setChecked(true);
                    }else{
                        internbuttonno.setChecked(true);
                    }
                    if(specvolunteer == 1){
                        volunteerbuttonyes.setChecked(true);
                    }else{
                        volunteerbuttonno.setChecked(true);
                    }
                } catch (
                        Exception e)

                {
                   // dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        SpecCheck task2 = new SpecCheck();
        task2.execute();
    }

    public void SpecModify() {
        class SpecModify extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    int a = 0, b = 0, c = 0;

                    if(Studytrain.contains("studytrainyes")){
                        a = 1;
                    }else{
                        a = 0;
                    }
                    if(Intern.contains("internbuttonyes")){
                        b = 1;
                    }else{
                        b = 0;
                    }
                    if(Volunteer.contains("volunteerbuttonyes")){
                        c = 1;
                    }else{
                        c = 0;
                    }


                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/specmodify.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("Id", sttid));
                    nameValuePairs.add(new BasicNameValuePair("Grade", editTextGrade.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Toeic", editTextTOEIC.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Opic", editTextOPIC.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Toss", editTextTOS.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Training", Integer.toString(a)));
                    nameValuePairs.add(new BasicNameValuePair("Intern", Integer.toString(b)));
                    nameValuePairs.add(new BasicNameValuePair("Volunteer", Integer.toString(c)));
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

                    editTextGrade.setEnabled(false);
                    editTextTOEIC.setEnabled(false);
                    editTextOPIC.setEnabled(false);
                    editTextTOS.setEnabled(false);
                    studytrainbuttonyes.setEnabled(false);
                    studytrainbuttonno.setEnabled(false);
                    internbuttonyes.setEnabled(false);
                    internbuttonno.setEnabled(false);
                    volunteerbuttonyes.setEnabled(false);
                    volunteerbuttonno.setEnabled(false);
                    System.out.println(spectraining + "~!!!!!!~" + specintern + "~~" + specvolunteer);
                } catch (
                        Exception e)

                {
                    // dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        SpecModify task2 = new SpecModify();
        task2.execute();
    }
}
