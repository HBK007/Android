package com.example.nguyenduchai.myconverttool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ConvertActivityDistance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText edtInput;
    private TextView lblResult;
    private Spinner spnUnit;
    private int currentUnit = 0; // default meter
    private String[] arrUnit;
    //                                               meter      mile                cm              inch            yd
    private float[][] arrDistanceConvertRate = {    {1,         0.000621317f,       100f,           39.3701f,       1.09361f},// meter
                                                    {1609.339f, 1,                  160933.9f,      63359.8f,       1759.99f},// mile
                                                    {0.01f,     9.999969f,          1,              0.39369f,       0.01093f},// cm
                                                    {0.025399f, 1.5782f,            2.53999f,       1,              0.02777f},// inch
                                                    {0.91f,     0.000568f,          91.4397f,       35.999f,        1}};      // yd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_distance);

        // find view
        edtInput = (EditText) findViewById(R.id.edt_input);
        lblResult = (TextView) findViewById(R.id.lbl_result);
        spnUnit = (Spinner) findViewById(R.id.spn_unit);

        // get values of arrUnit
        arrUnit = getResources().getStringArray(R.array.array_unit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_unit, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnUnit.setAdapter(adapter);

        // check event
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                convertDistance(currentUnit, arrDistanceConvertRate);
            }
        });

        spnUnit.setOnItemSelectedListener(this);

    }

    private void convertDistance(int currentUnit, float[][] arrDistanceConvertRate){
        if(edtInput.getText().toString().trim().length() <= 0){
            lblResult.setText("Result");
            return ;
        }
        // convert
        float input = Float.parseFloat(edtInput.getText().toString().trim());
        String result = "";
        for(int i = 0; i < arrUnit.length; i++){
            if(currentUnit != i){
                result += arrUnit[i];
                result += ": ";
                result += input * arrDistanceConvertRate[currentUnit][i];
                result += "\n";
            }
            lblResult.setText(result);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentUnit = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
