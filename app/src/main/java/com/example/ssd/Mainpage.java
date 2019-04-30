package com.example.ssd;

import android.app.ProgressDialog;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
    public static String selectmember = "";
    private ListView mListView;
    final static ArrayList<String> mprisename = new ArrayList<>();
    final static ArrayList<String> mpriseinfo = new ArrayList<>();
    private int realposition;
    ProgressDialog dialog = null;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("야너두");

        mListView = (ListView) findViewById(R.id.enterpriseview);

        enterpriselist();
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
                            // final ArrayList<String> menterinfo = new ArrayList<>();
                            for (int i = 0; i < countTokens; i++) {
                                String uid = str.nextToken();
                                mentername.add(uid);
                            }
                            mprisename.clear();
                            mpriseinfo.clear();
                            mprisename.addAll(mentername);
                            mpriseinfo.addAll(mentername);
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
    }


    private void dataSetting() {
        ListViewAdapter mListAdapter = new ListViewAdapter();

        mListAdapter.addItem(mprisename, mpriseinfo);

        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                realposition = position;
                selectmember = mprisename.get(position);
                mListView.setSelector(new PaintDrawable(0xffff0000));
            }
        });

    }
}
