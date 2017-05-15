package nguyenduchai.cse.com.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nguyen Duc Hai on 11/10/2015.
 */
public class IncomeExpense extends Identifier implements Serializable{

    private double amount;

    private String content;

    private Date date;

    private Date hour;

    private int flagCheck;

    public int getFlagCheck() {
        return flagCheck;
    }

    public IncomeExpense setFlagCheck(int flagCheck) {
        this.flagCheck = flagCheck;
        return this;
    }

    public Date getHour() {
        return hour;
    }

    public IncomeExpense setHour(Date hour) {
        this.hour = hour;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public IncomeExpense setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getContent() {
        return content;
    }

    public IncomeExpense setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public IncomeExpense setDate(Date date) {
        this.date = date;
        return this;
    }
}
