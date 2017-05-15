package nguyenduchai.cse.com.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import nguyenduchai.cse.com.adapter.AdapterCustomReportDateListView;
import nguyenduchai.cse.com.adapter.AdapterCustomReportMonthListView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

public class ReportDetailMonthActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvReportDetailItem;
    private TextView txtTotalAmount;
    private TextView txtShowTime;
    private ArrayList<IncomeExpense> arrayListItem = new ArrayList<>();
    private ArrayList<IncomeExpense> arrayListItemTemp = new ArrayList<>();
    private ArrayList<IncomeExpense> arrayListTemp;
    private IncomeExpense typeItemSelected;
    private AdapterCustomReportMonthListView adapterListItem;
    private String time;
    private int type;
    private String name;
    private double amountMoney;
    private Context context;
    private Database db;
    private SimpleDateFormat formatMonth = new SimpleDateFormat("MMMM-yyyy", Locale.US);
    private SimpleDateFormat formatDay = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);

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
        lvReportDetailItem.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void setValueForView() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        txtShowTime.setText(time);
        txtTotalAmount.setText(decimalFormat.format(amountMoney));
        arrayListItemTemp.clear();
        if(type == 1)
            arrayListTemp = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
        if(type == 2)
            arrayListTemp = db.getListExpense("SELECT * FROM " + Database.TABLE_EXPENSE);
//        fix here
        for (int i = 0; i < arrayListTemp.size(); i++){
            IncomeExpense tempItem = arrayListTemp.get(i);
            if(name.equalsIgnoreCase(tempItem.getName()) && time.equalsIgnoreCase(formatMonth.format(tempItem.getDate())))
                arrayListItemTemp.add(tempItem);
        }

        int i, j, count;
        double sumAmount;
        for(i = 0; i < arrayListItemTemp.size(); i++){
            sumAmount = 0.00;
            count = 0;
            for(j = 0; j < arrayListItemTemp.size(); j++){
                if(formatDay.format(arrayListItemTemp.get(i).getDate()).equalsIgnoreCase(formatDay.format(arrayListItemTemp.get(j).getDate() ))){
                    if( j < i)
                        break;
                    else{
                        count++;
                        sumAmount += arrayListItemTemp.get(j).getAmount();
                    }
                }
            }
            if(count != 0){
                arrayListItemTemp.get(i).setAmount(sumAmount);
                arrayListItemTemp.get(i).setFlagCheck(count);
                arrayListItem.add(arrayListItemTemp.get(i));
            }
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
        adapterListItem = new AdapterCustomReportMonthListView(this, R.layout.layout_custom_item_report_detail, arrayListItem);
        lvReportDetailItem.setAdapter(adapterListItem);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        typeItemSelected = (IncomeExpense)parent.getItemAtPosition(position);
        Intent intent = new Intent(ReportDetailMonthActivity.this, ReportDetailDateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("NAME", typeItemSelected.getName());
        bundle.putDouble("AMOUNT", typeItemSelected.getAmount());
        bundle.putString("TIME", formatDay.format(typeItemSelected.getDate()));
        bundle.putInt("TYPE", type);
        intent.putExtra("ITEM", bundle);
        startActivity(intent);
    }
}
