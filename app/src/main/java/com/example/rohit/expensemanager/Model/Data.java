package com.example.rohit.expensemanager.Model;

/**
 * Created by Rohit on 7/5/2019.
 */

public class Data {

    private double amount;
    private String title;
    private String desc;
    private String id;
    private String date;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Data(double amount, String title, String desc, String id, String date) {
        this.amount = amount;
        this.title = title;
        this.desc = desc;

        this.id = id;
        this.date = date;
    }

    public Data()
    {

    }
}
