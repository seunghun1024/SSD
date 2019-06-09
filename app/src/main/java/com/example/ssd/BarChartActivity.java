package com.example.ssd;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
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
import static com.example.ssd.Spec.specintern;
import static com.example.ssd.Spec.specopic;
import static com.example.ssd.Spec.spectoeic;
import static com.example.ssd.Spec.spectoss;
import static com.example.ssd.Spec.spectraining;
import static com.example.ssd.Spec.specvolunteer;

public class BarChartActivity extends AppCompatActivity {

    static String enterid;
    String passgrades, passtoeic, passopic, passtoss, passtraining, passintern, passvolunteer;
    String nopassgrades, nopasstoeic, nopassopic, nopasstoss, nopasstraining, nopassintern, nopassvolunteer;
    int entergrades, entertoeic, enteropic, entertoss, entertraining, enterintern, entervolunteer;
    double entersum, avegrades, avetoeic, sumgrades, sumtoeic;
    final static double alpha = 0.01;
    static ArrayList<Double> grade = new ArrayList<Double>();
    static ArrayList<Double> toeic = new ArrayList<Double>();
    static ArrayList<Double> opic = new ArrayList<Double>();
    static ArrayList<Double> toss = new ArrayList<Double>();
    static ArrayList<Double> training = new ArrayList<Double>();
    static ArrayList<Double> intern = new ArrayList<Double>();
    static ArrayList<Double> volunteer = new ArrayList<Double>();
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
    Search search;

    private GraphicalView mChartView;
    private GraphicalView mChartView2;

    private String[] mMonth = new String[]{

            " ", " ", " "

    };

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar_chart);


        enterid = search.enterid;

        entercheck();
        passcheck();
        try{
            Thread.sleep(500);
        }catch(Exception e){

        }
        nopasscheck();
        try{
            Thread.sleep(500);
        }catch(Exception e){

        }
        passabc();

        drawChart();

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
                   // System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    //        System.out.println("Response from PHP : " + response);
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
                   // System.out.println(entergrades + "~~" + entertraining + "~~" + enterintern + "~~1" + entervolunteer);




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
                    //System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         //   System.out.println("Response from PHP : " + response);
                            //dialog.dismiss();
                        }
                    });

                    StringTokenizer token = new StringTokenizer(response, "-");
                  //  System.out.println(token.countTokens());
                    int i = token.countTokens();
                    //System.out.println(i);
                    for(int j = 0; j < i; j++){
                        String qq = token.nextToken();
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
                        sumgrades += Double.valueOf(passgrades);
                        sumtoeic += Double.valueOf(passtoeic);
                        //System.out.println(passgrades + "~~" + passtraining + "~~" + passintern + "~~2" + passvolunteer);
                    }
                    avegrades = sumgrades/i;
                    avetoeic = sumtoeic/i;




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
                    //System.out.println("noResponse : " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // System.out.println("Response from PHP : " + response);
                            //dialog.dismiss();
                        }
                    });

                    StringTokenizer token = new StringTokenizer(response, "-");
                    //System.out.println(token.countTokens());
                    int i = token.countTokens();
                    for(int j = 0; j < i; j++){
                        String qq = token.nextToken();
                       // System.out.println(qq);
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
                       // System.out.println(nopassgrades + "~~" + nopasstraining + "~~" + nopassintern + "~~3" + nopassvolunteer);
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

//            System.out.println(count + "번 학습 완료");
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
        /*System.out.println("y = " +  Theta1 + "x + " + Theta2 + "x2 + " + Theta3 + "x3 + " + Theta4 + "x4 + "
                + Theta5 + "x5 + " + Theta6 + "x6 + " + Theta0);*/
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
        System.out.println("평균학점 : " + avegrades + "  평균토익 : " + avetoeic + "  내 학점 : " + specgrades + "  내 토익 : " + spectoeic);
    }


    private void drawChart() {

        int[] x = {1,2,3};

        double[] income = {0, avegrades, 0};

        double[] expense = {0, specgrades, 0};

        double[] income2 = {0, avetoeic, 0};

        double[] expense2 = {0, spectoeic, 0};

        XYSeries incomeSeries = new XYSeries("평균학점");

        XYSeries incomeSeries2 = new XYSeries("평균토익점수");

        // Creating an XYSeries for Expense

        XYSeries expenseSeries = new XYSeries("내 학점");

        XYSeries expenseSeries2 = new XYSeries("내 토익점수");

        for (int i = 0; i < x.length; i++) {

            incomeSeries.add(i, income[i]);

            incomeSeries2.add(i, income2[i]);

            expenseSeries.add(i, expense[i]);

            expenseSeries2.add(i, expense2[i]);

        }


        // Creating a dataset to hold each series

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        XYMultipleSeriesDataset dataset2 = new XYMultipleSeriesDataset();

        // Adding Income Series to the dataset

        dataset.addSeries(incomeSeries);

        dataset2.addSeries(incomeSeries2);

        // Adding Expense Series to dataset

        dataset.addSeries(expenseSeries);

        dataset2.addSeries(expenseSeries2);

        // Creating XYSeriesRenderer to customize incomeSeries

        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();

        incomeRenderer.setColor(Color.GREEN); //color of the graph set to cyan

        incomeRenderer.setFillPoints(true);

        incomeRenderer.setLineWidth(35);

        incomeRenderer.setDisplayChartValues(true);

        incomeRenderer.setDisplayChartValuesDistance(10); //setting chart value distance


        // Creating XYSeriesRenderer to customize expenseSeries

        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();

        expenseRenderer.setColor(Color.RED);

        expenseRenderer.setFillPoints(true);

        expenseRenderer.setLineWidth(35);

        expenseRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);

        multiRenderer.setXLabels(1);

        multiRenderer.setChartTitle("합격자 평균 스펙 vs 내 스펙");

        multiRenderer.setXTitle("");

        multiRenderer.setYTitle("학점");

        XYMultipleSeriesRenderer multiRenderer2 = new XYMultipleSeriesRenderer();

        multiRenderer2.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);

        multiRenderer2.setXLabels(1);

        multiRenderer2.setChartTitle("합격자 평균 토익점수 vs 내 토익점수");

        multiRenderer2.setXTitle("");

        multiRenderer2.setYTitle("토익점수");


        /***

         * Customizing graphs

         */

        //setting text size of the title


        multiRenderer.setChartTitleTextSize(40);
        multiRenderer2.setChartTitleTextSize(35);

        multiRenderer.setAxisTitleTextSize(40);
        multiRenderer2.setAxisTitleTextSize(40);

        //setting text size of the graph lable

        multiRenderer.setLabelsTextSize(40);
        multiRenderer2.setLabelsTextSize(40);
        multiRenderer.setLegendTextSize(40);
        multiRenderer2.setLegendTextSize(30);

        //setting zoom buttons visiblity

        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer2.setZoomButtonsVisible(false);

        //setting pan enablity which uses graph to move on both axis

        multiRenderer.setPanEnabled(false, false);
        multiRenderer2.setPanEnabled(false, false);

        //setting click false on graph

        multiRenderer.setClickEnabled(false);
        multiRenderer2.setClickEnabled(false);

        //setting zoom to false on both axis

        multiRenderer.setZoomEnabled(false, false);
        multiRenderer2.setZoomEnabled(false, false);

        //setting lines to display on y axis

        multiRenderer.setShowGridY(false);
        multiRenderer2.setShowGridY(false);

        //setting lines to display on x axis

        multiRenderer.setShowGridX(false);
        multiRenderer2.setShowGridX(false);

        //setting legend to fit the screen size

        multiRenderer.setFitLegend(true);
        multiRenderer2.setFitLegend(true);

        //setting displaying line on grid

        multiRenderer.setShowGrid(false);
        multiRenderer2.setShowGrid(false);

        //setting zoom to false

        multiRenderer.setZoomEnabled(false);
        multiRenderer2.setZoomEnabled(false);

        //setting external zoom functions to false

        multiRenderer.setExternalZoomEnabled(false);
        multiRenderer2.setExternalZoomEnabled(false);

        //setting displaying lines on graph to be formatted(like using graphics)

        multiRenderer.setAntialiasing(true);
        multiRenderer2.setAntialiasing(true);

        //setting to in scroll to false

        multiRenderer.setInScroll(false);
        multiRenderer2.setInScroll(false);

        //setting to set legend height of the graph

        multiRenderer.setLegendHeight(30);
        multiRenderer2.setLegendHeight(30);

        //setting x axis label align

        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        multiRenderer2.setXLabelsAlign(Paint.Align.CENTER);

        //setting y axis label to align

        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        multiRenderer2.setYLabelsAlign(Paint.Align.LEFT);

        //setting text style

        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        multiRenderer2.setTextTypeface("sans_serif", Typeface.NORMAL);

        //setting no of values to display in y axis

        multiRenderer.setYLabels(1);
        multiRenderer2.setYLabels(100);

        multiRenderer.setYAxisMin(0);
        multiRenderer2.setYAxisMin(0);

        // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.

        // if you use dynamic values then get the max y value and set here

        multiRenderer.setYAxisMax(4.5);
        multiRenderer2.setYAxisMax(990);

        //setting used to move the graph on xaxiz to .5 to the right

        multiRenderer.setXAxisMin(0);
        multiRenderer2.setXAxisMin(0);

        //setting max values to be display in x axis

        multiRenderer.setXAxisMax(2);
        multiRenderer2.setXAxisMax(2);

        //setting bar size or space between two bars

        multiRenderer.setBarSpacing(0.5);
        multiRenderer2.setBarSpacing(0.5);

        //Setting background color of the graph to transparent

        multiRenderer.setBackgroundColor(Color.TRANSPARENT);
        multiRenderer2.setBackgroundColor(Color.TRANSPARENT);

        //Setting margin color of the graph to transparent

        multiRenderer.setMarginsColor(Color.YELLOW);
        multiRenderer2.setMarginsColor(Color.YELLOW);

        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer2.setApplyBackgroundColor(true);

        //setting the margin size for the graph in the order top, left, bottom, right

        multiRenderer.setMargins(new int[]{60, 60, 60, 60});
        multiRenderer2.setMargins(new int[]{60, 60, 60, 60});

        for (int i = 0; i < x.length; i++) {

            multiRenderer.addXTextLabel(i, mMonth[i]);
            multiRenderer2.addXTextLabel(i, mMonth[i]);

        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer

        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer

        // should be same

        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer2.addSeriesRenderer(incomeRenderer);

        multiRenderer.addSeriesRenderer(expenseRenderer);
        multiRenderer2.addSeriesRenderer(expenseRenderer);

        //this part is used to display graph on the xml

        LinearLayout layout = (LinearLayout) findViewById(R.id.chart_bar);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.chart_bar2);

        //remove any views before u paint the chart

        layout.removeAllViews();
        layout2.removeAllViews();

        //drawing bar chart

        mChartView = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);
        mChartView2 = ChartFactory.getBarChartView(this, dataset2, multiRenderer2, BarChart.Type.DEFAULT);


        layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,

                LinearLayout.LayoutParams.FILL_PARENT));

        layout2.addView(mChartView2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,

                LinearLayout.LayoutParams.FILL_PARENT));

    }

    public static double Sigmoid(double Beta){
        return 1 / (1 + Math.pow(Math.E, -Beta));
    }
}