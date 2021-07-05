package agro.meteoro.wayanad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class SelectCrop extends AppCompatActivity
{
    ImageButton next_dash, previous;
    SharedPreferences preferences;
    CheckBox tea,rice,coffee,bpepper;
    int counter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        String lang = preferences.getString("Language","");
        setLang(lang);

        setContentView(R.layout.activity_select_crop);
        next_dash = findViewById(R.id.next_2);
        previous = findViewById(R.id.prev_1);

        tea = findViewById(R.id.check_tea);
        rice = findViewById(R.id.check_rice);
        coffee = findViewById(R.id.check_coffee);
        bpepper = findViewById(R.id.check_bpepper);


        next_dash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JSONObject crops = new JSONObject();
                if(!(tea.isChecked())&& !(rice.isChecked()) && !(coffee.isChecked()) && !(bpepper.isChecked()))
                {
                    Toast.makeText(getApplicationContext(),R.string.need_crop,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        if(tea.isChecked())
                        {
                            crops.put("Tea",1);
                        }
                        else
                        {
                            crops.put("Tea",0);
                        }

                        if(rice.isChecked())
                        {
                            crops.put("Rice",1);
                        }
                        else
                        {
                            crops.put("Rice",0);
                        }

                        if(coffee.isChecked())
                        {
                            crops.put("Coffee",1);
                        }
                        else
                        {
                            crops.put("Coffee",0);
                        }

                        if(bpepper.isChecked())
                        {
                            crops.put("Bpepper",1);
                        }
                        else
                        {
                            crops.put("Bpepper",0);
                        }
                    }
                    catch (Exception e)
                    {e.printStackTrace();}
                    preferences.edit().putString("Crops",crops.toString()).commit();
                    startActivity(new Intent(SelectCrop.this,Dashboard.class));
                    finish();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectCrop.this,SelectLanguage.class));
                finish();
            }
        });
    }

    private void setLang(String locale)
    {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            config.setLocale(new Locale(locale.toLowerCase()));
        }
        else
        {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
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
