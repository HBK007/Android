package nguyenduchai.cse.com.model;

import java.io.Serializable;

/**
 * Created by Nguyen Duc Hai on 11/22/2015.
 */
public class TypeIncomeExpense extends Identifier implements Serializable{
    private double sumAmountMoney;
    private byte []image;

    public double getSumAmountMoney() {
        return sumAmountMoney;
    }

    public TypeIncomeExpense setSumAmountMoney(double sumAmountMoney) {
        this.sumAmountMoney = sumAmountMoney;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public TypeIncomeExpense setImage(byte[] image) {
        this.image = image;
        return this;
    }
}
