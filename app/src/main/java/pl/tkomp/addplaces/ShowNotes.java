package pl.tkomp.addplaces;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.ActionBar.*;


/*Aktywnosc odpowiedzialna za wyswietlenie wszystkich notatek , sortujac po dacie od najnowszej do najstarszej*/

public class ShowNotes extends android.support.v4.app.Fragment {

    Location loc;
    Context thisContext;
    final double LATITUDE = 53.168126, LONGITUDE = 17.970621;

    NoteList listaa = NoteList.getInstance();
    LinearLayout main, main2, row;
    Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_show_notes, container, false);
  //      table = (TableLayout) findViewById(R.id.tableLayout);
        main = (LinearLayout) v.findViewById(R.id.main);
        btn = (Button) v.findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Map.class);
                if (loc != null) {
                    intent.putExtra("long", loc.getLongitude());
                    intent.putExtra("lat", loc.getLatitude());
                } else {
                    intent.putExtra("long", LONGITUDE);
                    intent.putExtra("lat", LATITUDE);
                    Log.d("GPS", "Nie mam lokalizacji - kliknij odśwież");
                }
                startActivity(intent);
            }
        });

        thisContext = container.getContext();

        OsobnyWatek nowy = new OsobnyWatek();
        nowy.execute(null, null, null);

        return v;
    }

    private class OsobnyWatek extends AsyncTask<Void,Integer,Void> { //asyncTask - wszystkie notki
        ProgressDialog pd = new ProgressDialog(thisContext);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd.setTitle(getResources().getString(R.string.download));
            pd.setMessage(getResources().getString(R.string.wait));
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params){
            CallSoap cs;
            cs=new CallSoap();
            cs.selectAll();
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progress){

        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            pd.dismiss();
            createLay();
         /*   final String[] options = {getResources().getString(R.string.list_view), getResources().getString(R.string.map_view)};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.pick)
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Log.d("SOAP","SKONCZYLEM");
                                createLay();
                            } else {
                                Intent intent = new Intent(getActivity(), Map.class);
                                if (loc != null) {
                                    intent.putExtra("long", loc.getLongitude());
                                    intent.putExtra("lat", loc.getLatitude());
                                } else {
                                    intent.putExtra("long", LONGITUDE);
                                    intent.putExtra("lat", LATITUDE);
                                    Log.d("GPS", "Nie mam lokalizacji - kliknij odśwież");
                                }
                                startActivity(intent);
                            }

                        }
                    });
            final AlertDialog alert = builder.create();
            try{
                alert.show();
            }
            catch (Exception e){
                Log.d("SOAP",e.getMessage());
            }*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataa){
        Log.d("EDYCJA", "Po edycji");
        OsobnyWatek nowy = new OsobnyWatek();
        nowy.execute(null, null, null);
    }


    private void createLay() { //stworzenie layoutu - wyświetla notki
       // main2 = new LinearLayout(thisContext);
        main.removeAllViews();
        main2 = main;

        row = new LinearLayout(thisContext);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
        TextView t;
        params.setMargins(20,20,20,20);
        int counter=0;
        for(Notka note: listaa.lista) {
            String data = note.getDate();
            String tresc = note.getText();
            String x = note.getX() + "";
            String y = note.getY() + "";
            row = new LinearLayout(thisContext);

            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(params);

            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            t = new TextView(thisContext);
            t.setTextAppearance(thisContext, R.style.edit3);
            t.setBackground(getResources().getDrawable(R.drawable.edit3));
            t.setText(data.substring(9, 14) + "\n" + data.substring(0, 9));
            row.addView(t);

            t = new TextView(thisContext);
            t.setPadding(20, 0, 0, 0);
            t.setTextAppearance(thisContext, R.style.edit2);
            t.setText(tresc);
            t.setBackground(getResources().getDrawable(R.drawable.edit2));
            t.setLayoutParams(lp);

            final int i = counter;
            row.addView(t);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),EditNote.class);
                    intent.putExtra("index",i);
                    startActivityForResult(intent, 1);

                }
            });
            counter++;
            main2.addView(row);
        }

        main=main2;
    }

/*
    private class OsobnyWatek extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
      //      pb.setVisibility(ProgressBar.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params){
            CallSoap cs;
            cs=new CallSoap();
            cs.selectAll();
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            Log.d("SOAP","SKONCZYLEM");
            main2 = main;
            row = new LinearLayout(thisContext);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            TextView t;
            params.setMargins(20,20,20,20);
            int counter=0;
            for(Notka note: listaa.lista) {
                String data = note.getDate();
                String tresc = note.getText();
                String x = note.getX() + "";
                String y = note.getY() + "";
                row = new LinearLayout(thisContext);

                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(params);

                t = new TextView(thisContext);
                t.setText(data);
                t.setTextColor(getResources().getColor(R.color.white));
                row.addView(t);

                t = new TextView(thisContext);
                t.setPadding(20,0,0,0);
                t.setText(tresc);
                t.setTextColor(getResources().getColor(R.color.white));

                final int i = counter;
                row.addView(t);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   Intent intent = new Intent(ShowNotes.this,EditNote.class);
                        //   intent.putExtra("index",i);
                        //   startActivity(intent);

                    }
                });
                counter++;
                main2.addView(row);
            }
            main=main2;
        }


    }*/
}


