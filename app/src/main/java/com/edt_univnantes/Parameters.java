package com.edt_univnantes;

import android.content.Context;
import android.widget.Toast;

import com.edt_univnantes.ui.home.HomeFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Parameters {

    public String urlICS;

    public Parameters() {
        urlICS = new String();
    }

    public void load(Context context) {
        String file = readFromFile(context);

        System.out.println(file);

        Scanner scanner = new Scanner(file);

        try {
            scanner.nextLine();
            urlICS = scanner.nextLine();
            /*add param here*/

        } catch (Exception e) {
            System.out.println("Parameters ERROR : " + e.toString());
            urlICS = "";
        }
        scanner.close();
    }

    public void save(Context context) {
        String data = urlICS /*add param here*/;

        writeToFile(data, context);
    }


    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("param.dat", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();

        }
        catch (IOException e) {
            System.out.println("Exception File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("param.dat");

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
}
