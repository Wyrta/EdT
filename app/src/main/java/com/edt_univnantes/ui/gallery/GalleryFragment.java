package com.edt_univnantes.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.edt_univnantes.Agenda;
import com.edt_univnantes.Classe;
import com.edt_univnantes.Date_parser;
import com.edt_univnantes.databinding.FragmentGalleryBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    private Button   bpAjouter;
    private ListView classesList;

    private ArrayAdapter<Agenda> adapter;
    private ArrayList<Agenda> agenda;

    final int REQUEST_CODE = 177013;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bpAjouter   = binding.bpAjouter;
        classesList = binding.agendaList;

        agenda = new ArrayList<Agenda>();
        adapter = new ArrayAdapter<Agenda>(container.getContext(), android.R.layout.simple_list_item_1, new ArrayList<Agenda>());
        classesList.setAdapter(adapter);

        reloadAgendaList();

        bpAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent request = new Intent(container.getContext(), addAgenda.class);
                someActivityResultLauncher.launch(request);
            }
        });

        classesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.remove(adapter.getItem(i) );
                saveAgenda(container.getContext());
            }
        });

        return root;
    }

    private void reloadAgendaList() {
        String agendaSaved = readFromFile(getContext());
        Scanner scanner = new Scanner(agendaSaved);
        adapter.clear();
        Agenda newagenda;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            newagenda = Agenda.parseStr(line);

            if (newagenda == null)
                continue;

            adapter.add(newagenda);
        }
        scanner.close();
    }

    private void saveAgenda(Context context) {
        writeToFileReset(context);
        for (int i = adapter.getCount()-1; i >= 0; i--) {
            writeToFile(Agenda.toData(adapter.getItem(i).year, adapter.getItem(i).month, adapter.getItem(i).day, adapter.getItem(i).description), context);
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            String dataToWrite = data + readFromFile(context);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("agenda.dat", Context.MODE_PRIVATE));
            outputStreamWriter.write(dataToWrite);
            outputStreamWriter.close();

        }
        catch (IOException e) {
            System.out.println("Exception File write failed: " + e.toString());
        }
    }

    private void writeToFileReset(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("agenda.dat", Context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();

        }
        catch (IOException e) {
            System.out.println("Exception File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("agenda.dat");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("login activity File not found: " + e.toString());
        } catch (IOException e) {
            System.out.println("login activity Can not read file: " + e.toString());
        }

        return ret;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String date = result.getData().getStringExtra("KEY_DATE");
                        String desc = result.getData().getStringExtra("KEY_DESC");

                        System.out.println("onActivityResult");

                        Agenda requestagenda = null;
                        requestagenda = Agenda.parseStr(date + "/" + desc + "/", "/");

                        try {
                            writeToFile(Agenda.toData(requestagenda.year, requestagenda.month, requestagenda.day, requestagenda.description), getContext());
                            reloadAgendaList();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Erreur formulaire" + e.toString(), Toast.LENGTH_LONG).show();

                        }

                    }
                }
            });
}