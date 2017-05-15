package nguyenduchai.cse.com.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import nguyenduchai.cse.com.adapter.AdapterCustomReportMonthListView;
import nguyenduchai.cse.com.adapter.AdapterCustomReportYearListView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

public class ReportDetailYearActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvReportDetailItem;
    private TextView txtTotalAmount;
    private TextView txtShowTime;
    private ArrayList<IncomeExpense> arrayListItem = new ArrayList<>();
    private ArrayList<IncomeExpense> arrayListTemp;
    private IncomeExpense typeItemSelected;
    private AdapterCustomReportYearListView adapterListItem;
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
    private void setValueForView() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        String []months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "November", "December"};
        txtShowTime.setText(time);
        txtTotalAmount.setText(decimalFormat.format(amountMoney));
        arrayListItem.clear();
        if(type == 1)
            arrayListTemp = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
        if(type == 2)
            arrayListTemp = db.getListExpense("SELECT * FROM " + Database.TABLE_EXPENSE);
//        fix here
        int i, j;
        int count = 0;
        double sumMoney = 0.00;
        for (i = 0; i < months.length; i++){
            IncomeExpense tempItem = new IncomeExpense();
            tempItem.setName(months[i]);
            for (count = j = 0; j < arrayListTemp.size(); j++){
                String []tempMonth = formatMonth.format(arrayListTemp.get(j).getDate()).split("-");
                if(name.equalsIgnoreCase(arrayListTemp.get(j).getName())&& months[i].equalsIgnoreCase(tempMonth[0])){
                    count++;
                    sumMoney += arrayListTemp.get(j).getAmount();
                }
            }
            tempItem.setAmount(sumMoney);
            tempItem.setContent(count + "");
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
        adapterListItem = new AdapterCustomReportYearListView(this, R.layout.layout_custom_item_report_detail, arrayListItem);
        lvReportDetailItem.setAdapter(adapterListItem);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        typeItemSelected = (IncomeExpense)parent.getItemAtPosition(position);
        Intent intent = new Intent(ReportDetailYearActivity.this, ReportDetailMonthActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        bundle.putDouble("AMOUNT", typeItemSelected.getAmount());
        bundle.putString("TIME", typeItemSelected.getName()+ "-" + time);
        bundle.putInt("TYPE", type);
        intent.putExtra("ITEM", bundle);
        startActivity(intent);
    }
}
