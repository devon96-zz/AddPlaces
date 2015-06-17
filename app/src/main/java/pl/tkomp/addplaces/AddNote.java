package pl.tkomp.addplaces;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.app.Fragment;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View.OnKeyListener;

import com.google.android.gms.cast.CastRemoteDisplayLocalService;


public class AddNote extends android.support.v4.app.Fragment {

    EditText first, second, third;
    TextView mes;
    Button btn;
    ProgressBar pb;
    LocationManager locMan;
    Location loc;
    String gps, network, passive;
    Context thisContext;

    public static String rslt="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_note, container, false);
        thisContext = container.getContext();

        first = (EditText) v.findViewById(R.id.textView);
        second = (EditText) v.findViewById(R.id.textView4);
        third = (EditText) v.findViewById(R.id.textView5);
        btn = (Button) v.findViewById(R.id.button);
        mes = (TextView) v.findViewById(R.id.message);
        pb = (ProgressBar) v.findViewById(R.id.progressBar);
        pb.setVisibility(ProgressBar.INVISIBLE);
        first.setHint("Your note");
        second.setHint("Latitude");
        third.setHint("Longitude");
        mes.setVisibility(View.INVISIBLE);

        gips();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

        return v;
    }

    private void gips()
    {
        locMan = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        gps = LocationManager.GPS_PROVIDER;
        network = LocationManager.NETWORK_PROVIDER;
        passive = LocationManager.PASSIVE_PROVIDER;
        getLoc();
    }

    private void getLoc()
    {
        if(locMan.isProviderEnabled(gps)) locMan.requestLocationUpdates(gps, 1000, 1, locationListener1);
        if(locMan.isProviderEnabled(network)) locMan.requestLocationUpdates(network, 1000, 1, locationListener2);
        if(locMan.isProviderEnabled(passive)) locMan.requestLocationUpdates(passive, 1000, 1, locationListener3);
        mes.setText(R.string.waiting);
        mes.setVisibility(View.VISIBLE);
        Log.d("GPS", "Próbuję znaleźć lokalizację..");
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(locMan.isProviderEnabled(provider)) getLoc();
        else new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.error)
                .setMessage(R.string.cannot)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .show();
    }
*/

    @Override
    public void onPause()
    {
        super.onPause();
        clear();
    }

    public boolean isEmpty()
    {
        if (first.getText().toString().trim().length() == 0) return true;
        if (second.getText().toString().trim().length() == 0) return true;
        if (third.getText().toString().trim().length() == 0) return true;
        return false;
    }

    public String getNote()
    {
        return first.getText().toString();
    }

    public double getLat()
    {
        return Double.parseDouble(second.getText().toString());
    }

    public double getLong()
    {
        return Double.parseDouble(third.getText().toString());
    }

    public void clear()
    {
        first.setText("");
        second.setText("");
        third.setText("");
    }


    public void click() {
        OsobnyWatek th = new OsobnyWatek();
        th.execute(null,null,null);
    }


    private class OsobnyWatek extends AsyncTask<Void,Void,Void>{
        int ok = 0;
        @Override
        protected void onPreExecute(){
            pb.setVisibility(ProgressBar.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params){
            if (isEmpty())
            {
                ok = 1;
                return null;
            }
            String notka = getNote();
            Double x = getLat();
            Double y = getLong();
            CallSoap cs;
            cs=new CallSoap();
            if(!cs.insert(notka,x,y)) ok = -1;
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            pb.setVisibility(ProgressBar.INVISIBLE);
            String mess = "";
            if(ok == 0)
            {
                mess = getResources().getString(R.string.added);
                Toast.makeText(getActivity(), mess, Toast.LENGTH_LONG).show();
                clear();
            } else
            if(ok == 1)
            {
                mess = getResources().getString(R.string.non_empty);
                Toast.makeText(getActivity(), mess, Toast.LENGTH_LONG).show();
            }
            else {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.error)
                        .setMessage(R.string.connection)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }

            super.onPostExecute(result);
        }
    }

    private void cancelAll()
    {
        if(locMan.isProviderEnabled(gps)) locMan.removeUpdates(locationListener1);
        if(locMan.isProviderEnabled(network)) locMan.removeUpdates(locationListener2);
        if(locMan.isProviderEnabled(passive)) locMan.removeUpdates(locationListener3);
        mes.setVisibility(View.INVISIBLE);
    }

    private Location savedLocation = null; //słuchacz zmiany lokalizacji
    private LocationListener locationListener1 = new LocationListener() {
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
        public void onLocationChanged(Location location) {

            cancelAll();
            second.setText(location.getLatitude() + "");
            third.setText(location.getLongitude() + "");
            loc = location; //globalna zmienna!

            if(savedLocation == null) savedLocation = locMan.getLastKnownLocation(gps);
            Log.d("GPS", "znalazłem " + gps+ " " + loc.getLongitude());
        }
    };

    private LocationListener locationListener2 = new LocationListener() {
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
        public void onLocationChanged(Location location) {

            cancelAll();
            if(second.getText().toString().equals("")) {
                second.setText(location.getLatitude() + "");
                third.setText(location.getLongitude() + "");
                loc = location; //globalna zmienna!
            }

            if(savedLocation == null) savedLocation = locMan.getLastKnownLocation(network);
            Log.d("GPS", "znalazłem " + network+ " " + loc.getLongitude());
        }
    };

    private LocationListener locationListener3 = new LocationListener() {
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
        public void onLocationChanged(Location location) {

            cancelAll();
            if(second.getText().toString().equals("")) {
                second.setText(location.getLatitude() + "");
                third.setText(location.getLongitude() + "");
                loc = location; //globalna zmienna!
            }

            if(savedLocation == null) savedLocation = locMan.getLastKnownLocation(passive);
            Log.d("GPS", "znalazłem " + passive + " " + loc.getLongitude());
        }
    };
}
