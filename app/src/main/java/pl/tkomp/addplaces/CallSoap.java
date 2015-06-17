package pl.tkomp.addplaces;

import android.app.Application;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dryja.staz on 2015-06-09.
 */
public class CallSoap extends Application {

    public final String GIVEALL = "selectAll";

    public final String CALL = "Add";

    public final String CHECKUSER = "checkUser";

    public final String INSERT = "insertNote";

    public final String REGUSER = "registerUser";

    public final String SELECT_BY_DATE = "selectAllByDate";

    public final String DELETE = "deleteNote";

    public final String DELETEALL = "deleteAllNote";

    public final String DELETEACC = "deleteAccount";

    public final String UPDATE = "updateNote";

    public final String NAMESPACE = "http://tempuri.org/";

    public final String SOAP_ADDRESS = "http://10.172.17.146/wsAndroid/WebService.asmx";

    NoteList listaa = NoteList.getInstance();
    User uzytkowik = User.getInstance();


    public CallSoap() {

    }
    public int check_user(String pass, String login){
        PropertyInfo pi = new PropertyInfo();
        SoapObject request = new SoapObject(NAMESPACE,CHECKUSER);
        pi.setName("pass");
        pi.setValue(pass);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("login");
        pi.setValue(login);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        Object response=null;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Log.d("SOAP","Zaczynam wysylac");

        try
        {
            httpTransport.call(NAMESPACE+CHECKUSER,envelope);
            SoapObject resultRequestSOAP;
            resultRequestSOAP = (SoapObject) envelope.bodyIn;
            SoapPrimitive t =(SoapPrimitive)resultRequestSOAP.getProperty("checkUserResult");
            String str = t.toString();
            int i = Integer.parseInt(str);
            Log.d("SOAP",i+" "+login+" "+resultRequestSOAP.toString());
            return i;
        }
        catch(Exception e)
        {
            Log.e("SOAP",""+e.getMessage());
        }
        return 0;
    }
    public int reg_user(String pass, String login){
        PropertyInfo pi = new PropertyInfo();
        SoapObject request = new SoapObject(NAMESPACE,REGUSER);
        pi.setName("pass");
        pi.setValue(pass);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("login");
        pi.setValue(login);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        Object response=null;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        try
        {
            httpTransport.call(NAMESPACE+REGUSER,envelope);
            SoapObject resultRequestSOAP;
            resultRequestSOAP = (SoapObject) envelope.bodyIn;
            SoapPrimitive t =(SoapPrimitive)resultRequestSOAP.getProperty("registerUserResult");
            String str = t.toString();
            int i = Integer.parseInt(str);
            Log.d("SOAP",i+" "+login+" "+resultRequestSOAP.toString());
            return i;
        }
        catch(Exception e)
        {
            Log.e("SOAP",""+e.getMessage());
        }
        return 0;
    }
    public boolean insert(String note, double x, double y)
    {
        String xx = Double.toString(x);
        String yy = Double.toString(y);
        SoapObject request = new SoapObject(NAMESPACE,INSERT);
        PropertyInfo pi1=new PropertyInfo();
        pi1.setName("note");
        pi1.setValue(note);
        pi1.setType(String.class);
        request.addProperty(pi1);

        pi1=new PropertyInfo();
        pi1.setName("x");
        pi1.setValue(xx);
        pi1.setType(String.class);
        request.addProperty(pi1);
        pi1=new PropertyInfo();
        pi1.setName("y");
        pi1.setValue(yy);
        pi1.setType(String.class);
        request.addProperty(pi1);

        pi1=new PropertyInfo();
        pi1.setName("userid");
        pi1.setValue(uzytkowik.getId());
        pi1.setType(Integer.class);
        request.addProperty(pi1);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        MarshalDouble md= new MarshalDouble();
        md.register(envelope);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        Log.d("SOAP","WYSYLAM");
        try
        {
            httpTransport.call(NAMESPACE+INSERT,envelope);
            Log.d("SOAP","WYSLALEM");
        }
        catch(Exception e)
        {
            Log.e("SOAP",""+e.getMessage());
            return false;
        }

        return true;
    }

    public boolean update(String note, double x, double y, int id)
    {
        String xx = Double.toString(x);
        String yy = Double.toString(y);
        SoapObject request = new SoapObject(NAMESPACE,UPDATE);

        PropertyInfo pi1=new PropertyInfo();
        pi1.setName("note");
        pi1.setValue(note);
        pi1.setType(String.class);
        request.addProperty(pi1);

        pi1=new PropertyInfo();
        pi1.setName("x");
        pi1.setValue(xx);
        pi1.setType(String.class);
        request.addProperty(pi1);

        pi1=new PropertyInfo();
        pi1.setName("y");
        pi1.setValue(yy);
        pi1.setType(String.class);
        request.addProperty(pi1);

        pi1=new PropertyInfo();
        pi1.setName("id");
        pi1.setValue(id);
        pi1.setType(Integer.class);
        request.addProperty(pi1);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        MarshalDouble md= new MarshalDouble();
        md.register(envelope);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);



        try
        {
            httpTransport.call(NAMESPACE+UPDATE,envelope);
        }
        catch(Exception e)
        {
            Log.e("SOAP",""+e.getMessage());
            return false;
        }

        return true;
    }


    public boolean delete(int id,int wybrany)
    {
        SoapObject request = new SoapObject(NAMESPACE,DELETE);
        PropertyInfo pi1=new PropertyInfo();
        pi1.setName("id");
        pi1.setValue(id);
        pi1.setType(Integer.class);
        request.addProperty(pi1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Log.d("KASOWANIE", "ZACZYNAM KASOWANIE");
        Log.d("KASOWANIE", id+"");
        listaa.lista.remove(wybrany);
        try
        {
            httpTransport.call(NAMESPACE+DELETE,envelope);
            Log.d("SOAP","SKASOWALEM");
        }
        catch(Exception e)
        {
            Log.e("SOAP",""+e.getMessage());
            return false;
        }

        return true;
    }

    public boolean deleteAll()
    {
        SoapObject request = new SoapObject(NAMESPACE,DELETEALL);
        PropertyInfo pi1=new PropertyInfo();
        pi1.setName("usrid");
        pi1.setValue(uzytkowik.getId());
        pi1.setType(Integer.class);
        request.addProperty(pi1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        listaa.lista.clear();
        try
        {
            httpTransport.call(NAMESPACE+DELETEALL,envelope);
            Log.d("SOAP","SKASOWALEM");
        }
        catch(Exception e)
        {
            Log.e("SOAP",""+e.getMessage());
            return false;
        }

        return true;
    }

    public boolean deleteAccount()
    {
        SoapObject request = new SoapObject(NAMESPACE,DELETEACC);
        PropertyInfo pi1=new PropertyInfo();
        pi1.setName("usrid");
        pi1.setValue(uzytkowik.getId());
        pi1.setType(Integer.class);
        request.addProperty(pi1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        listaa.lista.clear();
        try
        {
            httpTransport.call(NAMESPACE+DELETEACC,envelope);
            Log.d("SOAP","SKASOWALEM");
        }
        catch(Exception e)
        {
            Log.e("SOAP",""+e.getMessage());
            return false;
        }

        return true;
    }

    public Object selectAll(){

        SoapObject request = new SoapObject(NAMESPACE,GIVEALL);

        PropertyInfo pi1=new PropertyInfo();
        pi1.setName("userid");
        pi1.setValue(uzytkowik.getId());
        pi1.setType(Integer.class);
        request.addProperty(pi1);

        SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        NoteList listaa = NoteList.getInstance();
        listaa.lista.clear();
        try
        {
            httpTransport.call(NAMESPACE+GIVEALL,envelope);
            response = envelope.getResponse();
            SoapObject resultRequestSOAP;
            resultRequestSOAP = (SoapObject) envelope.bodyIn;
            SoapObject t =(SoapObject)resultRequestSOAP.getProperty("selectAllResult");
            for(int i=0;i<t.getPropertyCount();i++){
                Notka n = new Notka();
                SoapObject note = (SoapObject)t.getProperty(i);
                n.setId(Integer.parseInt(note.getProperty("id").toString()));
                n.setText(note.getProperty("text").toString());
                n.setX(Double.parseDouble(note.getProperty("x").toString()));
                n.setY(Double.parseDouble(note.getProperty("y").toString()));
                n.setDate(note.getProperty("date").toString());
                Log.d("SOAP",n.getDate());
                listaa.dodaj(n);

            }
            Log.d("SOAP",listaa.toString());


        }
        catch(Exception e)
        {
            Log.e("SOAP",R.string.error + e.getMessage());
            response = e.toString();
        }
        return response;

    }

}