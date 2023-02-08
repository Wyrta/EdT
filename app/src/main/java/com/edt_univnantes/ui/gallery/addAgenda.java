package com.edt_univnantes.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edt_univnantes.Date_parser;
import com.edt_univnantes.R;

public class addAgenda extends AppCompatActivity {

    private Button   bpAgendaOK;
    private Button   bpAgendaNOK;
    private EditText agendaDate;
    private EditText agendaDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);

        bpAgendaOK  = findViewById(R.id.bpAgendaOK);
        bpAgendaNOK = findViewById(R.id.bpAgendaNOK);
        agendaDate  = findViewById(R.id.agendaDate);
        agendaDesc  = findViewById(R.id.agendaDesc);

        int year, month, day;
        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        String str = date.toString();

        String yearStr = "" + str.charAt(0) + str.charAt(1) + str.charAt(2) + str.charAt(3);
        year = Integer.parseInt(yearStr);

        String monthStr = "" + str.charAt(5) + str.charAt(6);
        month = Integer.parseInt(monthStr);

        String dayStr = "" + str.charAt(8) + str.charAt(9);
        day = Integer.parseInt(dayStr);
        agendaDate.setText("" +year + "/" + month  + "/" + day);

        bpAgendaOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = agendaDate.getText().toString();
                String desc = agendaDesc.getText().toString();

                Intent result = new Intent();
                result.putExtra("KEY_DATE", date);
                result.putExtra("KEY_DESC", desc);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        bpAgendaNOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}