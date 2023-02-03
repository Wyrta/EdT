package com.edt_univnantes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    private Button   bp_nextDay;
    private Button   bp_previousDay;
    private ListView classesList;
    private TextView txt_date;

    public RequestQueue queue;
    private ArrayAdapter<Classe> adapter;
    private ArrayList<Classe> EdT;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        String str = date.toString();

        String yearStr = "" + str.charAt(0) + str.charAt(1) + str.charAt(2) + str.charAt(3);
        year = Integer.parseInt(yearStr);

        String monthStr = "" + str.charAt(5) + str.charAt(6);
        month = Integer.parseInt(monthStr);

        String dayStr = "" + str.charAt(8) + str.charAt(9);
        day = Integer.parseInt(dayStr);

        bp_nextDay      = findViewById(R.id.bp_nextday);
        bp_previousDay  = findViewById(R.id.bp_previousDay);
        classesList     = findViewById(R.id.classesList);
        txt_date        = findViewById(R.id.txt_date);

        txt_date.setText(new String("" + year + "/" + month + "/" + day) );

        queue = Volley.newRequestQueue(this);

        EdT = new ArrayList<Classe>();
        ArrayAdapter<Classe> adapter = new ArrayAdapter<Classe>(this, android.R.layout.simple_list_item_1, new ArrayList<Classe>());
        classesList.setAdapter(adapter);


        String url = "https://edt.univ-nantes.fr/iut_nantes/p1002715.ics";

        queue.add(new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean isOK = false;
                        String DTSTART = "";
                        String DTEND = "";
                        String UID = "";
                        String SUMMARY = "";
                        String LOCATION = "";
                        String DESCRIPTION = "";
                        String CATEGORIES = "";

                        adapter.clear();
                        EdT.clear();

                        Scanner scanner = new Scanner(response);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();

                            if (line.startsWith("BEGIN:")) {
                                if (line.contains("VEVENT"))
                                    isOK = true;
                                else
                                    isOK = false;
                            } else if (line.startsWith("DTSTART:")) {
                                DTSTART = line.replace("DTSTART:", "");
                            } else if (line.startsWith("DTEND:")) {
                                DTEND = line.replace("DTEND:", "");
                            } else if (line.startsWith("UID:")) {
                                UID = line.replace("UID:", "");
                            } else if (line.startsWith("SUMMARY:")) {
                                SUMMARY = line.replace("SUMMARY:", "");
                            } else if (line.startsWith("LOCATION:")) {
                                LOCATION = line.replace("LOCATION:", "");
                            } else if (line.startsWith("DESCRIPTION:")) {
                                DESCRIPTION = line.replace("DESCRIPTION:", "");
                            } else if (line.startsWith("CATEGORIES:")) {
                                CATEGORIES = line.replace("CATEGORIES:", "");
                            } else if (line.startsWith("END:")) {
                                if (line.contains("VEVENT") && isOK) {
                                    EdT.add(new Classe(DTSTART.toString(), DTEND.toString(), UID.toString(), SUMMARY.toString(), LOCATION.toString(), DESCRIPTION.toString(), CATEGORIES.toString()));

                                    Classe lastItem = EdT.get(EdT.size() - 1);

                                    if ((lastItem.year_start == year) && (lastItem.month_start == month) && (lastItem.day_start == day)) {
                                        adapter.add(lastItem);
                                    }

                                } else
                                    isOK = false;

                            }
                        }
                        scanner.close();
                        txt_date.setText(new String("" + year + "/" + month + "/" + day) );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ));

        bp_nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();

                if ((month == 2) && (day == (28 + (((year%4) == 0) ? 1 : 0) )) ) {
                    day = 1;
                    month++;
                }
                if (day == 30) {
                    switch (month) {
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            day = 1;
                            month++;
                            break;
                        default:
                            day++;
                            break;
                    }

                } else if (day == 31) {
                    if (month == 12) {
                        day   = 1;
                        month = 1;
                        year++;
                    } else {
                        day = 1;
                        month++;
                    }

                } else {
                    day++;
                }


                for (Classe item : EdT) {
                    if ((item.year_start == year) && (item.month_start == month) && (item.day_start == day))
                        adapter.add(item);
                }
                txt_date.setText(new String("" + year + "/" + month + "/" + day) );
            }
        });

        bp_previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();

                if (day == 1) {
                    switch (month) {
                        case 1:
                            day = 31;
                            month = 12;
                            year--;
                            break;
                        case 3:
                            day = 28;
                            month--;
                            break;
                        case 2:
                            day = 31;
                            month--;
                            break;
                        case 5:
                        case 7:
                        case 10:
                        case 12:
                            day = 30;
                            month--;
                            break;
                        case 4:
                        case 6:
                        case 8:
                        case 9:
                        case 11:
                            day = 31;
                            month--;
                            break;
                        default:
                            break;
                    }
                } else {
                    day--;

                }

                for (Classe item : EdT) {
                    if ((item.year_start == year) && (item.month_start == month) && (item.day_start == day))
                        adapter.add(item);
                }
                txt_date.setText(new String("" + year + "/" + month + "/" + day) );
            }
        });

    }
}


/* https://edt.univ-nantes.fr/iut_nantes/p1002715.ics */