package com.example.ssd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PredictResult extends AppCompatActivity {
    CompanyInfo cominfo;
    TextView result_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predict_result);

        result_text = (TextView)findViewById(R.id.Textview_predict_result);
        result_text.setText("축하드립니다."+cominfo.predict_result+"입니다.");
    }
}
