package com.waterbanana.meetapp;

/**
 * Created by Eddie on 6/19/2015.
 */
public class Ribbon {
    private int id;
    private String date;
    private int start;
    private int end;

    public Ribbon(){
        id = -1;
    }

    public Ribbon( int id ){
        this.id = id;
    }

    public Ribbon( int id, String date, int begin, int end ){
        this.id = id;
        this.date = date;
        this.start = begin;
        this.end = end;
    }

    public int getId(){
        return this.id;
    }

    public void setId( int id ){
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStart() {
        return start;
    }


    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
