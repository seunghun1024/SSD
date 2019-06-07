package com.example.ssd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

import static com.example.ssd.Spec.specgrades;
import static com.example.ssd.Spec.spectoeic;
import static com.example.ssd.Spec.specopic;
import static com.example.ssd.Spec.spectoss;
import static com.example.ssd.Spec.specintern;
import static com.example.ssd.Spec.spectraining;
import static com.example.ssd.Spec.specvolunteer;

public class PassWhether extends AppCompatActivity {

    static String enterid;
    String passgrades, passtoeic, passopic, passtoss, passtraining, passintern, passvolunteer;
    String nopassgrades, nopasstoeic, nopassopic, nopasstoss, nopasstraining, nopassintern, nopassvolunteer;
    int entergrades, entertoeic, enteropic, entertoss, entertraining, enterintern, entervolunteer;
    double entersum;
    private Button btntest;
    final static double alpha = 0.01;
    static ArrayList<Double> grade = new ArrayList<Double>();
    static ArrayList<Double> toeic = new ArrayList<Double>();
    static ArrayList<Double> opic = new ArrayList<Double>();
    static ArrayList<Double> toss = new ArrayList<Double>();
    static ArrayList<Double> training = new ArrayList<Double>();
    static ArrayList<Double> intern = new ArrayList<Double>();
    static ArrayList<Double> volunteer = new ArrayList<Double>();
    static ArrayList<Double> finaleducation = new ArrayList<Double>();
    static ArrayList<Double> certificate = new ArrayList<Double>();
    static ArrayList<Integer> pass = new ArrayList<Integer>();
    int count = 0;
    static double theta0 = 0;
    static double theta1 = 1;
    static double theta2 = 1;
    static double theta3 = 1;
    static double theta4 = 1;
    static double theta5 = 1;
    static double theta6 = 1;
    static double theta7 = 1;
    ProgressDialog dialog = null;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcheck);

        enterid = "samsungSDS";

        entercheck();
        passcheck();
        nopasscheck();
        //testt();

        btntest = (Button) findViewById(R.id.buttontest);

        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        passabc();
                    }
                }).start();
            }
        });

    }

    public void entercheck() {
        class EnterCheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/entercheck.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("enterId", enterid));
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
                        entergrades = Integer.parseInt(token.nextToken());
                        entertoeic = Integer.parseInt(token.nextToken());
                        enteropic = Integer.parseInt(token.nextToken());
                        entertoss = Integer.parseInt(token.nextToken());
                        entertraining = Integer.parseInt(token.nextToken());
                        enterintern = Integer.parseInt(token.nextToken());
                        entervolunteer = Integer.parseInt(token.nextToken());
                        entersum = entergrades + entertoeic + enteropic + entertoss + entertraining + enterintern + entervolunteer;
                        System.out.println(entergrades + "~~" + entertraining + "~~" + enterintern + "~~1" + entervolunteer);




                } catch (
                        Exception e)

                {
                    // dialog.dismiss();
                    System.out.println("Exception : " + e.getMessage());
                }
                return "test!!";
            }
        }
        EnterCheck task2 = new EnterCheck();
        task2.execute();
    }

    public void passcheck() {
        class PassCheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/passcheck.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("enterId", enterid));
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

                    StringTokenizer token = new StringTokenizer(response, "-");
                    System.out.println(token.countTokens());
                    int i = token.countTokens();
                    for(int j = 0; j < i; j++){
                        String qq = token.nextToken();
                        System.out.println(qq);
                        StringTokenizer token2 = new StringTokenizer(qq, ",");
                        passgrades = token2.nextToken();
                        passtoeic = token2.nextToken();
                        passopic = token2.nextToken();
                        passtoss = token2.nextToken();
                        passtraining = token2.nextToken();
                        passintern = token2.nextToken();
                        passvolunteer = token2.nextToken();
                        grade.add((entergrades/entersum)*Double.valueOf(passgrades));
                        toeic.add((entertoeic/entersum)*Double.valueOf(passtoeic));
                        opic.add((enteropic/entersum)*Double.valueOf(passopic));
                        toss.add((entertoss/entersum)*Double.valueOf(passtoss));
                        training.add((entertraining/entersum)*Double.valueOf(passtraining));
                        intern.add((enterintern/entersum)*Double.valueOf(passintern));
                        volunteer.add((entervolunteer/entersum)*Double.valueOf(passvolunteer));
                        pass.add(1);
                        System.out.println(passgrades + "~~" + passtraining + "~~" + passintern + "~~2" + passvolunteer);
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
        PassCheck task2 = new PassCheck();
        task2.execute();
    }

    public void nopasscheck() {
        class NoPassCheck extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/nopasscheck.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("enterId", enterid));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("noResponse : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Response from PHP : " + response);
                            //dialog.dismiss();
                        }
                    });

                    StringTokenizer token = new StringTokenizer(response, "-");
                    System.out.println(token.countTokens());
                    int i = token.countTokens();
                    for(int j = 0; j < i; j++){
                        String qq = token.nextToken();
                        System.out.println(qq);
                        StringTokenizer token2 = new StringTokenizer(qq, ",");
                        nopassgrades = token2.nextToken();
                        nopasstoeic = token2.nextToken();
                        nopassopic = token2.nextToken();
                        nopasstoss = token2.nextToken();
                        nopasstraining = token2.nextToken();
                        nopassintern = token2.nextToken();
                        nopassvolunteer = token2.nextToken();
                        grade.add((entergrades/entersum)*Double.valueOf(nopassgrades));
                        toeic.add((entertoeic/entersum)*Double.valueOf(nopasstoeic));
                        opic.add((enteropic/entersum)*Double.valueOf(nopassopic));
                        toss.add((entertoss/entersum)*Double.valueOf(nopasstoss));
                        training.add((entertraining/entersum)*Double.valueOf(nopasstraining));
                        intern.add((enterintern/entersum)*Double.valueOf(nopassintern));
                        volunteer.add((entervolunteer/entersum)*Double.valueOf(nopassvolunteer));
                        pass.add(0);
                        System.out.println(nopassgrades + "~~" + nopasstraining + "~~" + nopassintern + "~~3" + nopassvolunteer);
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
        NoPassCheck task2 = new NoPassCheck();
        task2.execute();
    }

    public void passabc(){
        while (count++ < 1000) // 1000번 반복
        {
            double differential0 = 0, differential1 = 0, differential2 = 0, differential3 = 0, differential4 = 0,
                    differential5 = 0, differential6 = 0, differential7 = 0; // 미분값 계속 0으로 초기화

            for (int i = 0; i < pass.size(); i++) {
                double Beta = (theta0 + theta1 * grade.get(i)
                        + theta2 * toeic.get(i)
                        + theta3 * opic.get(i)
                        + theta4 * toss.get(i)
                        + theta5 * training.get(i)
                        + theta6 * intern.get(i)
                        + theta7 * volunteer.get(i));
                // 시그모이드 함수를 구하기 위해 Beta 값 지정
                differential0 += (pass.get(i) - Sigmoid(Beta)); // 6개의 변수에 대해 각각의 미분값을 구함
                differential1 += (pass.get(i) - Sigmoid(Beta))
                        * grade.get(i);
                differential2 += (pass.get(i) - Sigmoid(Beta))
                        * toeic.get(i);
                differential3 += (pass.get(i) - Sigmoid(Beta))
                        * opic.get(i);
                differential4 += (pass.get(i) - Sigmoid(Beta))
                        * toss.get(i);
                differential5 += (pass.get(i) - Sigmoid(Beta))
                        * training.get(i);
                differential6 += (pass.get(i) - Sigmoid(Beta))
                        * intern.get(i);
                differential7 += (pass.get(i) - Sigmoid(Beta))
                        * volunteer.get(i);
            }

            // 파라미터 갱신규칙을 확률적 경사 상승법을 이용해 갱신
            theta0 = theta0 + alpha * differential0;
            theta1 = theta1 + alpha * differential1;
            theta2 = theta2 + alpha * differential2;
            theta3 = theta3 + alpha * differential3;
            theta4 = theta4 + alpha * differential4;
            theta5 = theta5 + alpha * differential5;
            theta6 = theta6 + alpha * differential6;
            theta7 = theta7 + alpha * differential7;

            System.out.println(count + "번 학습 완료");
        }
        passdef();
    }

    public void passdef() {
        int check = 0;
        // theta들을 소수점 3자리까지 표시하기 위함
        double Theta0 = Double.parseDouble(String.format("%.3f", theta0));
        double Theta1 = Double.parseDouble(String.format("%.3f", theta1));
        double Theta2 = Double.parseDouble(String.format("%.3f", theta2));
        double Theta3 = Double.parseDouble(String.format("%.3f", theta3));
        double Theta4 = Double.parseDouble(String.format("%.3f", theta4));
        double Theta5 = Double.parseDouble(String.format("%.3f", theta5));
        double Theta6 = Double.parseDouble(String.format("%.3f", theta6));
        double Theta7 = Double.parseDouble(String.format("%.3f", theta7));
        System.out.println("y = " +  Theta1 + "x + " + Theta2 + "x2 + " + Theta3 + "x3 + " + Theta4 + "x4 + "
                + Theta5 + "x5 + " + Theta6 + "x6 + " + Theta0);
        double predictiony = Sigmoid(Theta1 * specgrades + Theta2 * spectoeic + Theta3 * specopic + Theta4 * spectoss + Theta5 * spectraining + Theta6 * specintern + theta7 * specvolunteer + Theta0);
        if(predictiony >= 0.5) {
            predictiony = 1;
            System.out.println("합격~~");
        }else{
            predictiony = 0;
            System.out.println("불합격!!");
        }
        /*for (int j = 0; j < pass.size(); j++) { // 구한 식의 theta값들로 test_set의 y값을 비교하기위한 for문
            double predictiony = Sigmoid(Theta1 * grade.get(j)
                    + Theta2 * toeic.get(j)
                    + Theta3 * opic.get(j)
                    + Theta4 * toss.get(j)
                    + Theta5 * training.get(j)
                    + Theta6 * intern.get(j)
                    + Theta7 * volunteer.get(j)
                    + Theta0);
            if (predictiony >= 0.5) { // y가 0.5이상이면 1로 바꿈
                predictiony = 1;
            } else { // 0.5이상이 아니라면 0으로 바꿈
                predictiony = 0;
            }
            if (predictiony == Double.parseDouble(pass.get(j).toString())) { // test_set의 y값과 비교 후 같으면 check변수에 1 증가
                check++;
            }
        }*/
        System.out.println("맞은 갯수 = " + check + " , 정확도 = " + check / (double) pass.size()); // check에서 전체 개수를 나누어 정확도 측정
    }

    public static double Sigmoid(double Beta){
        return 1 / (1 + Math.pow(Math.E, -Beta));
    }
}
