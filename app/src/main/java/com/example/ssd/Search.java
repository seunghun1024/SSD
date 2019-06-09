package com.example.ssd;
// 검색 페이지
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class Search extends AppCompatActivity {

    public static String selectprise = "";
    private ListView ListViewtext;
    private EditText search;
    private Button btnsearch;
    final static ArrayList<String> searchname = new ArrayList<>();
    ProgressDialog dialog = null;
    private int realposition;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        search = (EditText) findViewById(R.id.search);
        ListViewtext = (ListView) findViewById(R.id.listViewsearch);
        btnsearch = (Button) findViewById(R.id.search_btn);

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listviewtest();
            }
        });
    }

    public void listviewtest() {
        class ListViewTest extends AsyncTask<String, Void, String> {
            @SuppressLint("WrongThread")
            protected String doInBackground(String... params) {
                try {
                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://203.234.62.96:7979/searchlistview.php");
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("searchtext", search.getText().toString()));
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
                                //muid.add(uid);
                                System.out.println("uid값 1 : " + uid);
                                mentername.add(uid);
                            }
                            searchname.clear();
                            searchname.addAll(mentername);
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
        ListViewTest task = new ListViewTest();
        task.execute();
    }

    private void dataSetting() {
        ListViewAdapter2 mListAdapter = new ListViewAdapter2();

        mListAdapter.addItem(searchname);

        ListViewtext.setAdapter(mListAdapter);

        ListViewtext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                realposition = position;
                selectprise = searchname.get(position);
                ListViewtext.setSelector(new PaintDrawable(0xffff0000));
            }
        });


    }
}
