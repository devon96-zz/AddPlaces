package pl.tkomp.addplaces;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BaseBundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View.OnKeyListener;


public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);


        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(
                        getTabIndicator(mTabHost.getContext(), R.string.notes, R.drawable.menu_notatki)),
                ShowNotes.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(
                        getTabIndicator(mTabHost.getContext(), R.string.add, R.drawable.menu_point)),
                AddNote.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(
                        getTabIndicator(mTabHost.getContext(), R.string.profile, R.drawable.menu_profile)),
                YourProfile.class, null);

        mTabHost.setCurrentTabByTag("tab2");

        gips();
    }

    private void gips()
    {
        LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locMan.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Log.d("GPS", "GPS wyłączony");

            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.error)
                    .setMessage(R.string.no_provider)
                    .setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.net, null)
                    .show();
        }
    }

    public void wyloguj(View v){
        SharedPreferences prefs = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        prefs.edit().putInt("usrid",-1).apply();
        Intent intent = new Intent(this,LoginRegister.class);
        startActivity(intent);
    }

    private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.indicator_lay, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView2);
        tv.setText(title);
        return view;
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.exit)
        {
            System.exit(0);
        }
        return true;
    }
*/



/*
    public void refresh(View view) { //reakcja na guzik - odśwież lokalizację
        provider = locMan.getBestProvider(criteria, true); //Criteria - globalna - new Criteria()
        if(provider == null || provider.equals("passive"))
        {
            Log.d("GPS", "Brak dostępnych dostawców internetu");

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.error)
                    .setMessage(R.string.no_provider)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            return;
        }

        pb.setVisibility(ProgressBar.VISIBLE); //progress Bar globalny, opcjonalnie
        small.setText( getApplicationContext().getString(R.string.waiting) + " " + provider + "..");
        small.setVisibility(TextView.VISIBLE); //textView j/w
        btn.setVisibility(Button.VISIBLE);

        locMan.requestLocationUpdates(provider, 1000, 1, locationListener);
        Log.d("GPS", "Próbuję znaleźć lokalizację..");
    }

    public void anuluj() { //anulowanie wykrywania lokalizacji
        locMan.removeUpdates(locationListener);
        pb.setVisibility(ProgressBar.INVISIBLE); //progress Bar globalny, opcjonalnie
        small.setVisibility(TextView.INVISIBLE); //textView j/w
        btn.setVisibility(Button.INVISIBLE); //button j/w
    }

    public void cancel(View view) { anuluj(); } //reakcja na guzik - anuluj odświeżanie
*/

}
