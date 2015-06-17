package pl.tkomp.addplaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginRegister extends ActionBarActivity {

    CheckBox check;
    EditText login,pass,pass2;
    Button b;
    TextView info;
    Boolean checked = false;
    ProgressBar pb;
    String log,pas;
    User uzytkowik = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_register);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        Log.d("SESJA",prefs.getInt("usrid",-1)+"");
        if(prefs.getInt("usrid",-1)!=-1){
            uzytkowik.setId(prefs.getInt("usrid",-1));
            uzytkowik.setLogin(prefs.getString("login",""));
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        check = (CheckBox) findViewById(R.id.checkBox);
        b = (Button) findViewById(R.id.button10);
        login = (EditText) findViewById(R.id.editText10);
        pass = (EditText) findViewById(R.id.editText11);
        pass2 = (EditText) findViewById(R.id.editText13);
        info = (TextView) findViewById(R.id.info);
        pb = (ProgressBar) findViewById(R.id.progressBar5);
        info.setText("");
        pb.setVisibility(ProgressBar.INVISIBLE);
        pass2.setVisibility(EditText.INVISIBLE);
        login.setHint("Email");
        pass.setHint("Hasło");
        pass2.setHint("Powtórz hasło");
        Log.d("LOGOWANIE", "udalo sie");
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()==false){
                    b.setText("LOG IN");
                    pass2.setVisibility(EditText.INVISIBLE);
                    checked=false;
                }
                else{
                    b.setText("REGISTER");
                    pass2.setVisibility(EditText.VISIBLE);
                    checked=true;
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
    public void click(View v){
        pas=pass.getText().toString();
        log=login.getText().toString();
    //    Log.d("SOAP", pass.getText().toString().equals(pa)+" "+pass2.getText().toString());
        if(checked){
            if(isEmpty(login) || isEmpty(pass) || isEmpty(pass2)) return;
            if(!pass.getText().toString().equals(pass2.getText().toString())) { info.setText("Podane hasła nie zgadzają się"); return; }
            OsobnyWatek_reg regg = new OsobnyWatek_reg();
            regg.execute(null,null,null);
        }
        else{
            if(isEmpty(login) || isEmpty(pass)) return;
            OsobnyWatek_log logg = new OsobnyWatek_log();
            logg.execute(null,null,null);
        }
    }

    public void pomin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class OsobnyWatek_log extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            pb.setVisibility(ProgressBar.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params){
            CallSoap cs;
            cs=new CallSoap();
            int usid = cs.check_user(pas, log);
            uzytkowik.setId(usid);
            uzytkowik.setLogin(log);
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            pb.setVisibility(ProgressBar.INVISIBLE);
            super.onPostExecute(result);
            if(uzytkowik.getId()==0) info.setText("Nieprawidlowy login lub haslo");
            else{
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
                prefs.edit().putInt("usrid",uzytkowik.getId()).apply();
                prefs.edit().putString("login",uzytkowik.getLogin()).apply();
                Intent intent = new Intent(LoginRegister.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
    private class OsobnyWatek_reg extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            pb.setVisibility(ProgressBar.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params){
            CallSoap cs;
            cs=new CallSoap();
            int usid = cs.reg_user(pas, log);
            uzytkowik.setId(usid);
            uzytkowik.setLogin(log);
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            pb.setVisibility(ProgressBar.INVISIBLE);
            super.onPostExecute(result);
            if(uzytkowik.getId()==0) info.setText("User with given email is already registered!");
            else{
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
                prefs.edit().putInt("usrid",uzytkowik.getId()).apply();
                prefs.edit().putString("login",uzytkowik.getLogin()).apply();
                Intent intent = new Intent(LoginRegister.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }



}

