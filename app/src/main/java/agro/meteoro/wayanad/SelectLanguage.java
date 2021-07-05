package agro.meteoro.wayanad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class SelectLanguage extends AppCompatActivity
{
    SharedPreferences preferences;
    SharedPreferences.Editor PrefEditor;
    RadioGroup lang_radios;
    RadioButton lang_selected;
    ImageButton next_pref;
    int id;
    int counter =1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        lang_radios = findViewById(R.id.lang_radios);
        next_pref = findViewById(R.id.next_1);
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        PrefEditor = preferences.edit();

        next_pref.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                id = lang_radios.getCheckedRadioButtonId();
                lang_selected = findViewById(id);
                if(lang_selected.getText().equals("English"))
                {
                    change("default");
                    startActivity(new Intent(SelectLanguage.this,SelectCrop.class));
                    finish();

                }
                else
                {
                    change("ML");
                    startActivity(new Intent(SelectLanguage.this,SelectCrop.class));
                    finish();
                }
            }
        });
    }

    private void change(String localeCode)
    {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            config.setLocale(new Locale(localeCode.toLowerCase()));
        }
        else
        {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
        PrefEditor.putString("Language",localeCode);
        PrefEditor.commit();
    }

    @Override
    public void onBackPressed()
    {
        if(counter>=1)
        {
            Toast.makeText(getApplicationContext(),"Press once again to Exit",Toast.LENGTH_SHORT).show();
        }
        if(counter<1)
        {
            super.finish();
        }
        counter--;
    }


}
