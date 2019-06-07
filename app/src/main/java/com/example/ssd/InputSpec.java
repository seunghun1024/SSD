package com.example.ssd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class InputSpec extends AppCompatActivity {
    private Spinner spinner;
    ArrayList<String> arrayAcademic;
    ArrayAdapter<String> adapterAcademic;
    ArrayList<String> arrayMajor;
    ArrayAdapter<String> adapterMajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_spec);

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }
}
