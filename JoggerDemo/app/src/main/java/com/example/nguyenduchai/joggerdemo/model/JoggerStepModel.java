package com.example.nguyenduchai.joggerdemo.model;

/**
 * Created by Nguyen Duc Hai on 5/14/2017.
 */

public class JoggerStepModel {
    private String mDate;
    private int mStep;

    public JoggerStepModel() {
    }

    public JoggerStepModel(String mDate, int mStep) {
        this.mDate = mDate;
        this.mStep = mStep;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getmStep() {
        return mStep;
    }

    public void setmStep(int mStep) {
        this.mStep = mStep;
    }
}
