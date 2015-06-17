package pl.tkomp.addplaces;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dryja.staz on 2015-06-10.
 */
public class NoteList {
    private static NoteList instance;
    public List<Notka> lista = new ArrayList<Notka>();
    private NoteList() {}
    public void dodaj(Notka n){
        this.lista.add(n);
    }
    public List<Notka> zwroc_liste(){
        return this.lista;
    }
    public static synchronized NoteList getInstance(){
        if(instance==null){
            instance=new NoteList();
        }
        return instance;
    }
}
