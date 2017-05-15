package nguyenduchai.cse.com.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 11/28/2015.
 */
public class AdapterCustomListView extends ArrayAdapter<TypeIncomeExpense> {

    private Activity context;
    private int layoutId;
    private ArrayList<TypeIncomeExpense> arrayList = null;

    public AdapterCustomListView(Activity context, int resource, ArrayList<TypeIncomeExpense> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.arrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        ImageView imgTypeExpense = (ImageView)convertView.findViewById(R.id.img_item);
        TextView txtNameTypeExpense = (TextView)convertView.findViewById(R.id.txt_name_item);
        TextView txtAmountMoneyTypeExpense = (TextView)convertView.findViewById(R.id.txt_amount_money);
//        get item in list type income
        TypeIncomeExpense tempItem = arrayList.get(position);
        DecimalFormat formatAmountMoney = new DecimalFormat("###,###.###");
//        load image type income
        byte [] outImage = tempItem.getImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        imgTypeExpense.setImageBitmap(theImage);
//        load name type income
        txtNameTypeExpense.setText(tempItem.getName());
//        load sum amount money type income
        txtAmountMoneyTypeExpense.setText(formatAmountMoney.format(tempItem.getSumAmountMoney()));
        return  convertView;
    }
}
