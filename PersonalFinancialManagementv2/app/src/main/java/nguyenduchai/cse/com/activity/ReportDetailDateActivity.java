package nguyenduchai.cse.com.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import nguyenduchai.cse.com.adapter.AdapterCustomReportDateListView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 12/7/2015.
 */
public class ReportDetailDateActivity extends AppCompatActivity{

    private ListView lvReportDetailItem;
    private TextView txtTotalAmount;
    private TextView txtShowTime;
    private ArrayList<IncomeExpense> arrayListItem = new ArrayList<>();
    private ArrayList<IncomeExpense> arrayListTemp;
    private AdapterCustomReportDateListView adapterListItem;
    private String name;
    private String time;
    private double amountMoney;
    private int type;
    private Context context;
    private Database db;
    private SimpleDateFormat formatTime = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_report_detail);
        context = this;
        db = new Database(context);
        getControlFromView();
        getDataFromMain();
        setTitle(name);
        setValueForView();
    }

    private void setValueForView() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        txtShowTime.setText(time);
        txtTotalAmount.setText(decimalFormat.format(amountMoney));
        arrayListItem.clear();
        if(type == 1)
            arrayListTemp = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
        if(type == 2)
            arrayListTemp = db.getListExpense("SELECT * FROM " + Database.TABLE_EXPENSE);
        for (int i = 0; i < arrayListTemp.size(); i++){
            IncomeExpense tempItem = arrayListTemp.get(i);
            if(name.equalsIgnoreCase(tempItem.getName()) && time.equalsIgnoreCase(formatTime.format(tempItem.getDate())))
                arrayListItem.add(tempItem);
        }
        adapterListItem.notifyDataSetChanged();

    }

    private void getDataFromMain() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ITEM");
        name = bundle.getString("NAME");
        time = bundle.getString("TIME");
        type = bundle.getInt("TYPE");
        amountMoney = bundle.getDouble("AMOUNT");
    }

    private void getControlFromView() {
        lvReportDetailItem = (ListView)findViewById(R.id.lv_report_detail);
        txtTotalAmount = (TextView)findViewById(R.id.txt_amount_total);
        txtShowTime = (TextView)findViewById(R.id.txt_show_time);
        adapterListItem = new AdapterCustomReportDateListView(this, R.layout.layout_custom_item_report_detail, arrayListItem);
        lvReportDetailItem.setAdapter(adapterListItem);
    }
}
