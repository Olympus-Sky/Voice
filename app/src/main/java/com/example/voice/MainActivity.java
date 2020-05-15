package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private Button btnSpeakJ, btnListenJ;
    private EditText  edtVoiceJ;
    private TextToSpeech txtSpeak;
    private ListView lvWordJ;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeak = new TextToSpeech(MainActivity.this, MainActivity.this);
        edtVoiceJ = (EditText)findViewById(R.id.edtVoice);
        btnSpeakJ = (Button)findViewById(R.id.btnSpeak);
        btnListenJ = (Button)findViewById(R.id.btnListen);
        lvWordJ = (ListView)findViewById(R.id.lvWord);

        Buttons();
    }

    @Override
    public void onInit(int status) {
        locale = new Locale("pt", "BR");
        txtSpeak.setLanguage(locale);
    }

    private void Buttons() {
        btnSpeakJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = edtVoiceJ.getText().toString();
                txtSpeak.speak(txt, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, "1");
            }
        });

        btnListenJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                List<ResolveInfo>activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

                if (activities.size() != 0) {
                    Intent intent = getRecognizerIntent();
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    protected Intent getRecognizerIntent() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale aqui");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            ArrayList<String>words = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lvWordJ.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words));
        }
    }
}