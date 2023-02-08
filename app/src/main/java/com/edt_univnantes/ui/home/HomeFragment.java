package com.edt_univnantes.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edt_univnantes.Classe;
import com.edt_univnantes.Parameters;
import com.edt_univnantes.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Scanner;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private Button bp_nextDay;
    private Button   bp_previousDay;
    private Button   bpToDay;
    private ListView classesList;
    private TextView txt_date;

    public RequestQueue queue;
    private StringRequest request;

    private ArrayAdapter<Classe> adapter;
    private ArrayList<Classe> EdT;
    private int year, month, day;

    Parameters parameters = new Parameters();

    private String url = "";//https://edt.univ-nantes.fr/iut_nantes/p1002715.ics";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        String str = date.toString();

        String yearStr = "" + str.charAt(0) + str.charAt(1) + str.charAt(2) + str.charAt(3);
        year = Integer.parseInt(yearStr);

        String monthStr = "" + str.charAt(5) + str.charAt(6);
        month = Integer.parseInt(monthStr);

        String dayStr = "" + str.charAt(8) + str.charAt(9);
        day = Integer.parseInt(dayStr);

        bp_nextDay      = binding.bpNextday;
        bp_previousDay  = binding.bpPreviousDay;
        bpToDay         = binding.bpToDay;
        classesList     = binding.classesList;
        txt_date        = binding.txtDate;

        txt_date.setText(new String("" + year + "/" + month + "/" + day) );

        queue   = Volley.newRequestQueue(container.getContext());

        EdT     = new ArrayList<Classe>();
        adapter = new ArrayAdapter<Classe>(container.getContext(), android.R.layout.simple_list_item_1, new ArrayList<Classe>());
        classesList.setAdapter(adapter);

        parameters.load(getContext());
        url     = parameters.urlICS;
        request = newRequest(url);
        queue.add(request);

        bp_nextDay.setOnClickListener(new View.OnClickListener()     {
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
                        case 5:
                        case 7:
                        case 10:
                        case 12:
                            day = 30;
                            month--;
                            break;
                        case 2:
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

        bpToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long millis=System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                String str = date.toString();

                String yearStr = "" + str.charAt(0) + str.charAt(1) + str.charAt(2) + str.charAt(3);
                year = Integer.parseInt(yearStr);

                String monthStr = "" + str.charAt(5) + str.charAt(6);
                month = Integer.parseInt(monthStr);

                String dayStr = "" + str.charAt(8) + str.charAt(9);
                day = Integer.parseInt(dayStr);

                adapter.clear();
                for (Classe item : EdT) {
                    if ((item.year_start == year) && (item.month_start == month) && (item.day_start == day))
                        adapter.add(item);
                }
                txt_date.setText(new String("" + year + "/" + month + "/" + day) );

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        parameters.load(getContext());

        if (!url.equals(parameters.urlICS)) {
            url = parameters.urlICS;
            request = newRequest(url);
            queue.add(request);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private StringRequest newRequest(String reURL) {
        return new StringRequest(
                Request.Method.GET,
                reURL,
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
                    }
                }
        );
    }
}