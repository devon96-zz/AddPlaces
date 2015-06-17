package pl.tkomp.addplaces;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class EditNote extends ActionBarActivity {

    EditText edit1,edit2,edit3;
    int wybrany;
    NoteList listaa = NoteList.getInstance();
    Intent resultIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_note);

        Bundle bundle=getIntent().getExtras();
        wybrany=bundle.getInt("index");

        edit1 = (EditText) findViewById(R.id.editText);
        edit1.setText(listaa.lista.get(wybrany).getX() + "");
        edit1.setHint("Latitude");

        edit2 = (EditText) findViewById(R.id.editText5);
        edit2.setHint("Longitude");
        edit2.setText(listaa.lista.get(wybrany).getY()+"");


        edit3 = (EditText) findViewById(R.id.editText6);
        edit3.setHint("Your note");
        edit3.setText(listaa.lista.get(wybrany).getText()+"");
    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
    public void del(View v){
        OsobnyWatek_del d = new OsobnyWatek_del();
        d.execute(null,null,null);

    }
    public void ok(View v){
        if(isEmpty(edit1) || isEmpty(edit2) || isEmpty(edit3)){
            return;
        }
        OsobnyWatek_update d = new OsobnyWatek_update();
        d.execute(null,null,null);
    }
    private class OsobnyWatek_del extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params){
            CallSoap cs;
            cs=new CallSoap();
            cs.delete(listaa.lista.get(wybrany).getId(),wybrany);

            Log.d("KASOWANIE", "KASUJE");
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            Log.d("KASOWANIE", "SKONCZYLEM");
            setResult(1);
            finish();
        }


    }
    private class OsobnyWatek_update extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params){
            CallSoap cs;
            cs=new CallSoap();
            Double x,y;
            Log.d("EDYCJA","HOHO");
            x= Double.parseDouble(edit1.getText().toString());
            y= Double.parseDouble(edit2.getText().toString());
            Log.d("EDYCJA",edit3.getText().toString());
            cs.update(edit3.getText().toString(),x,y,listaa.lista.get(wybrany).getId());
            listaa.lista.get(wybrany).setText(edit3.getText().toString());
            listaa.lista.get(wybrany).setX(x);
            listaa.lista.get(wybrany).setY(y);
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            setResult(1);
            finish();
        }


    }

}
