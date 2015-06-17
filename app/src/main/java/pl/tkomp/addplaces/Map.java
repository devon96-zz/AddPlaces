package pl.tkomp.addplaces;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class Map extends AppCompatActivity implements OnMapReadyCallback {

    NoteList listaa = NoteList.getInstance();
    double x = 0, y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        x = bundle.getDouble("lat");
        y = bundle.getDouble("long");
    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(x, y), 10));
        Log.d("WSPOLRZEDNE", "ZACZYNAM");
        for(Notka notee : listaa.lista) {

            Log.d("WSPOLRZEDNE", notee.getText()+" "+notee.getX() + " " + notee.getY());
            LatLng pos = new LatLng(notee.getX(), notee.getY());
            map.addMarker(new MarkerOptions()
                    .title(notee.getText())
                  //  .snippet(notee.getText())
                    .position(pos));
        }
    }
}
