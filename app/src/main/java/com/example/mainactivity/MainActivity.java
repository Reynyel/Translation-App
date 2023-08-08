package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText editTxt;
    private Button translateBtn;

    private Spinner langSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTxt = findViewById(R.id.translateTxt);
        translateBtn = findViewById(R.id.translateBtn);
        langSpinner = findViewById(R.id.spinnerLang);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.supported_lang, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        langSpinner.setAdapter(adapter);

        langSpinner.setOnItemSelectedListener(this);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Checks if the edit text is empty or not
                * if it's empty, a message will appear, stating it so*/
                if(TextUtils.isEmpty(editTxt.getText().toString())){
                    Toast.makeText(MainActivity.this, "Text is empty", Toast.LENGTH_SHORT).show();
                }else{
                    // Build TranslatorOptions with the target language "tl" (Tagalog) and the source language "en" (English)
                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setTargetLanguage("tl")
                            .setSourceLanguage("en").build();

                    //String[] supportedLang;

                    //supportedLang = new String[]{"tl", "ja", "de"};

                    // Create a Translator instance based on the given options
                    Translator translator = Translation.getClient(options);

                    // Get the source language text from the edit text
                    String sourceLang = editTxt.getText().toString();

                    // Show a progress dialog while downloading the translation model
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Downloading translation model");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Downloads model if needed and dismisses the progress dialog on success or failure
                    translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });

                    // Translate the text using the Translator instance and listen for success or failure
                    Task<String> result = translator.translate(sourceLang).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            // If translation is successful, display the translated text using a toast
                            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // If translation is successful, display the translated text using a toast
                            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}