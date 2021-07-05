package agro.meteoro.wayanad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity
{
    TextView temp,humi,wind,rain,current_temp,cityText;
    ImageView weather_status;
    SharedPreferences preferences;
    SharedPreferences.Editor PrefEditor;
    CardView daily,weekly,monthly;
    LinearLayout m_lang, m_crop, m_about;
    FloatingActionButton dash_menu;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;
    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //hide_sys_ui.hideui(getWindow().getDecorView());
        super.onCreate(savedInstanceState);
        //Set Language
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        PrefEditor = preferences.edit();
        String lang = preferences.getString("Language","");
        setLang(lang); //End of Set Language
        setContentView(R.layout.activity_dashboard);

        current_temp = findViewById(R.id.current_temp);
        temp = findViewById(R.id.temp_value);
        humi = findViewById(R.id.humi_value);
        wind = findViewById(R.id.wind_value);
        rain = findViewById(R.id.rain_value);

        daily = findViewById(R.id.day_task);
        weekly = findViewById(R.id.week_task);
        monthly = findViewById(R.id.month_task);

        weather_status = findViewById(R.id.weather_state_img);
        cityText = findViewById(R.id.city);

        m_lang = findViewById(R.id.menu_lang);
        m_crop = findViewById(R.id.menu_crop);
        m_about= findViewById(R.id.menu_about);
        dash_menu = findViewById(R.id.dash_setting);

        m_lang.setVisibility(View.INVISIBLE);
        m_crop.setVisibility(View.INVISIBLE);
        m_about.setVisibility(View.INVISIBLE);

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        setClickListener();
        get_city();
        get_weather_now();
    }

    private void get_weather_now()
    {
        String url = "http://neutralizer.ml/weather/get_weather_now.php";
        RequestQueue weather_q = Volley.newRequestQueue(this);
        StringRequest weather_req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject params = new JSONObject(response);
                    current_temp.setText(getString(R.string.temp_unit, params.getString("temp")));
                    temp.setText(getString(R.string.temp_unit, params.get("temp")));
                    humi.setText(getString(R.string.humi_unit, params.getString("humi")));
                    wind.setText(getString(R.string.wind_unit, params.getString("wind")));
                    rain.setText(getString(R.string.rain_unit, params.getString("rain")));
                }
                catch (Exception e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        weather_q.add(weather_req);
    }
    private void setClickListener()
    {
        daily.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(),"Daily clicked",Toast.LENGTH_SHORT).show();
            }
        });
        weekly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(),"Weekly clicked",Toast.LENGTH_SHORT).show();
            }
        });
        monthly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(),"Monthly clicked",Toast.LENGTH_SHORT).show();
            }
        });

        m_lang.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog dialog = new Dialog(Dashboard.this);
                dialog.setContentView(R.layout.lang_pop);
                Button dialogButton = dialog.findViewById(R.id.lang_ok);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        RadioGroup lang_radios = dialog.findViewById(R.id.pop_lang_radios);
                        int id = lang_radios.getCheckedRadioButtonId();
                        RadioButton lang_selected = dialog.findViewById(id);
                        if(lang_selected.getText().equals("English"))
                        {
                            change("default");
                            Intent intent = getIntent();
                            overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(intent);

                        }
                        else
                        {
                            change("ML");
                            Intent intent = getIntent();
                            overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        m_crop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog dialog = new Dialog(Dashboard.this);
                dialog.setContentView(R.layout.crop_pop);
                Button dialogButton = dialog.findViewById(R.id.crop_ok);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        m_about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(),"About menu clicked",Toast.LENGTH_SHORT).show();
            }
        });
        dash_menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isOpen)
                {
                    m_lang.setVisibility(View.INVISIBLE);
                    m_crop.setVisibility(View.INVISIBLE);
                    m_about.setVisibility(View.INVISIBLE);

                    dash_menu.startAnimation(fab_close);
                    dash_menu.startAnimation(fab_anticlock);
                    isOpen = false;
                }
                else
                    {
                        m_lang.setVisibility(View.VISIBLE);
                        m_crop.setVisibility(View.VISIBLE);
                        m_about.setVisibility(View.VISIBLE);

                        dash_menu.startAnimation(fab_open);
                        dash_menu.startAnimation(fab_clock);
                        isOpen = true;
                }
            }
        });

    }
    private void get_city()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(),"Location Permission Denied",Toast.LENGTH_SHORT).show();
        }
        else
        {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            String city = get_city_name(location.getLatitude(),location.getLongitude());
            cityText.setText(city);
        }
    }
    private String get_city_name(double lat, double log)
    {
        String city = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocation(lat,log,10);
            if(addresses.size() > 0)
            {
                for(Address adr: addresses)
                {
                    if(adr.getLocality() != null && adr.getLocality().length() > 0)
                    {
                        city = adr.getLocality() +", "+ adr.getAdminArea() +"\n"+ adr.getCountryName();
                        break;
                    }
                }
            }
        }
        catch (IOException e){e.printStackTrace();}
        return city;
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
