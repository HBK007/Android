package nguyenduchai.cse.com.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 12/16/2015.
 */
public class AdapterCustomReportYearListView extends ArrayAdapter<IncomeExpense>{
    private Activity context;
    private int layoutId;
    private ArrayList<IncomeExpense> arrayListItem = null;
    public AdapterCustomReportYearListView(Activity context, int resource, ArrayList<IncomeExpense> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.arrayListItem = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        TextView txtContentItem = (TextView) convertView.findViewById(R.id.txt_show_content_item);
        TextView txtShowTime = (TextView) convertView.findViewById(R.id.txt_show_time);
        TextView txtShowAmount = (TextView)convertView.findViewById(R.id.txt_show_amount);
//        get item in list type income
        IncomeExpense tempItem = arrayListItem.get(position);
        DecimalFormat formatAmountMoney = new DecimalFormat("###,###.###");

        txtContentItem.setText(tempItem.getName());
        txtShowTime.setText(tempItem.getContent() + " deals");
        txtShowAmount.setText(formatAmountMoney.format(tempItem.getAmount()));
        return convertView;
    }
}
