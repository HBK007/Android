package com.example.nguyenduchai.freakingmath;

/**
 * Created by Nguyen Duc Hai on 3/21/2017.
 */

public class LevelModel {
    public int difficultLevel = 1;

    // operators
    public static final int ADD = 0;
    public static final int SUB = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;

    // operators text
    public static final String ADD_TEXT = "+";
    public static final String SUB_TEXT = "-";
    public static final String MUL_TEXT = "*";
    public static final String DIV_TEXT = ":";
    public static final String EQU_TEXT = "=";
    public static final String[] arrOperatorText = {ADD_TEXT,
                                                    SUB_TEXT,
                                                    MUL_TEXT,
                                                    DIV_TEXT,
                                                    EQU_TEXT};

    // component of operator
    public int x;
    public int y;
    public int result;
    public int operator;
    public boolean correctWrong;
    public String strOperator = "";
    public String strResult = "";

    // max value of operator in level: easy, medium, hard
    public static final int MAX_OPERATOR_EASY = 5;
    public static final int MAX_OPERATOR_MEDIUM = 10;
    public static final int MAX_OPERATOR_HARD = 50;
    public static final int MAX_OPERATOR_OTHER = 100;

    public static final int[] arrMaxOperatorValue = {MAX_OPERATOR_EASY,
                                                    MAX_OPERATOR_MEDIUM,
                                                    MAX_OPERATOR_HARD,
                                                    MAX_OPERATOR_OTHER};

}
