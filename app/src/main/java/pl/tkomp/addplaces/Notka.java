package pl.tkomp.addplaces;

/**
 * Created by dryja.staz on 2015-06-10.
 */

public class Notka {
    private int id;
    private double x, y;
    private String text, date;

    Notka() {}

    Notka(int a, String b, String c, String d, double xx, double yy) {
        id = a;
        text = c;
        x = xx;
        y = yy;
        date = d;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setId(int i) {
        id = i;
    }

    public void setText(String i) {text = i; }

    public void setDate(String i) { date = i; }

    public void setX(Double i) { x = i; }

    public void setY(Double i) { y = i; }

}
