package pl.tkomp.addplaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class YourProfile extends android.support.v4.app.Fragment {

    User uzytkownik = User.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_your_profile, container, false);
        TextView tv = (TextView) v.findViewById(R.id.login);
        tv.setText(uzytkownik.getLogin());
        return v;
    }


}