package agro.meteoro.wayanad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;

public class Initiate extends AppCompatActivity
{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        hide_sys_ui.hideui(getWindow().getDecorView());     //hide navigation keys
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate);
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);

        new CountDownTimer(1250, 1000)
        {
            public void onFinish()
            {
                ConnectivityStatus();
            }
            public void onTick(long millisUntilFinished)
            {}
        }.start();
    }

    private void ConnectivityStatus()
    {
        ConnectivityManager net = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert net != null;
        if (Objects.requireNonNull(net.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(net.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED)
        {
            LocationPermission();
        }
        else
            {
                Toast.makeText(getApplicationContext(), "Check your Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
    }

    private void LocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(Initiate.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else
                {
                    ActivityCompat.requestPermissions(Initiate.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
        }
        else
        {
            launchNext();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        launchNext();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Allow Location to Continue",Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void launchNext()
    {
        if(preferences.getString("Language","").equals(""))
        {
            startActivity(new Intent(Initiate.this, SelectLanguage.class));
            finish();
        }
        else if (preferences.getString("Crops","").equals(""))
        {
            startActivity(new Intent(Initiate.this, SelectCrop.class));
            finish();
        }
        else
        {
            startActivity(new Intent(Initiate.this, Dashboard.class));
            finish();
        }
    }
}