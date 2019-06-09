package com.example.ssd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class CompanyInfo extends AppCompatActivity {
    Button predictBtn,interestBtn,urlBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_info);

        predictBtn = (Button) findViewById(R.id.predict_btn);
        interestBtn = (Button) findViewById(R.id.interest_btn);
        urlBtn = (Button) findViewById(R.id.url_btn);

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PredictResult.class);
                startActivity(intent);
            }
        });

        interestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //관심기업
            }
        });

        urlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //url 가져오기
            }
        });



    }
}
