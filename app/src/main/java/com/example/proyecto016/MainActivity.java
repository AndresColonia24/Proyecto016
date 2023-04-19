package com.example.proyecto016;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout textInputLayoutNote;
    private EditText editTextNote;
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    String formattedDate = sdf.format(date);
    private TextView noteDate;;
    private Button themeButton;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputLayoutNote = findViewById(R.id.textField);
        editTextNote = textInputLayoutNote.getEditText();

        noteDate = findViewById(R.id.noteDate);
        noteDate.setText(formattedDate);
        themeButton = findViewById(R.id.theme_button);

        String[] fileList = fileList();
        if(exists(fileList, "notes.txt"))
            try {
                InputStreamReader file = new InputStreamReader(openFileInput("notes.txt"));
                BufferedReader br = new BufferedReader(file);
                String line = br.readLine();
                String all = "";
                while (line != null) {
                    all = all + line + "\n";
                    line = br.readLine();
                }
                br.close();
                file.close();
                editTextNote.setText(all);
            } catch (IOException e) {

            }

        int themeId = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (themeId == Configuration.UI_MODE_NIGHT_YES) {
            themeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_dark_mode_24), null, null, null);
        } else {
            themeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_light_mode_24), null, null, null);
        }
    }

    private boolean exists(String[] filelist, String fileSearch) {
        for (int f = 0; f < filelist.length; f++)
            if (fileSearch.equals(filelist[f]))
                return true;
        return false;
    }

    public void save(View v) {
        try {
            OutputStreamWriter file = new OutputStreamWriter(openFileOutput("notes.txt", Activity.MODE_PRIVATE));
            file.write(editTextNote.getText().toString());
            file.flush();
            file.close();
        } catch (IOException e) {
        }
        Toast t = Toast.makeText(this, "Los datos fueron grabados",Toast.LENGTH_SHORT);
        t.show();
    }


    public void cambiarTema(View view) {
        // Obtén la preferencia actual del tema de la aplicación
        int temaActual = getSharedPreferences("MiApp", MODE_PRIVATE).getInt("tema", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        // Calcula el nuevo tema de la aplicación
        int nuevoTema = temaActual == AppCompatDelegate.MODE_NIGHT_YES ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;

        // Guarda la preferencia del nuevo tema de la aplicación
        getSharedPreferences("MiApp", MODE_PRIVATE).edit().putInt("tema", nuevoTema).apply();

        // Establece el nuevo tema de la aplicación
        AppCompatDelegate.setDefaultNightMode(nuevoTema);

        // Recrea la actividad con el nuevo tema
        recreate();
    }
}