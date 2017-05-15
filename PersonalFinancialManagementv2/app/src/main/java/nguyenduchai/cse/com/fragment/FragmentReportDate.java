package nguyenduchai.cse.com.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nguyenduchai.cse.com.activity.ReportDetailDateActivity;
import nguyenduchai.cse.com.adapter.AdapterCustomListView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 11/27/2015.
 */
public class FragmentReportDate extends Fragment {
    private View view;
    private Context context;
    private Database db;
    private ArrayList<TypeIncomeExpense> arrayListReport = new ArrayList<>();
    private AdapterCustomListView adapterCustomListView;
    private ArrayList<TypeIncomeExpense> arrayListTempTypeIncome = new ArrayList<>();
    private ArrayList<TypeIncomeExpense> arrayListTempTypeExpense = new ArrayList<>();
    private Button btnShowTime;
    private ImageButton btnNextDay;
    private ImageButton btnPreDay;
    private ImageButton btnShowListReportIncome;
    private ImageButton btnShowListReportExpense;
    private ListView lvShowListItems;
    private TextView txtShowTime;
    private TextView txtAmountMoneyIncomeReport;
    private TextView txtAmountMoneyExpenseReport;
    private TextView txtAmountMoneyBalance;
    private TypeIncomeExpense typeItemSelected;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatTime = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);
    private boolean isPressIncome = false;
    private boolean isPressExpense = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        db = new Database(context);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_custom_item_report, container, false);
        getControlFromView();
        addEventControl();
        showCurrentTime();
        resetDefaultAmountMoney();
        getListTypeIncomeExpense();
//        default show income
        showListIncomeReport();
        statisticFinancialThisDate();
        return view;
    }

    private void statisticFinancialThisDate() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        double sumAmountIncome = getSumAmountIncome(arrayListTempTypeIncome);
        double sumAmountExpense = getSumAmountIncome(arrayListTempTypeExpense);
        txtAmountMoneyIncomeReport.setText(decimalFormat.format(sumAmountIncome));
        txtAmountMoneyExpenseReport.setText(decimalFormat.format(sumAmountExpense));
        txtAmountMoneyBalance.setText(decimalFormat.format(sumAmountIncome-sumAmountExpense));
    }

    private double getSumAmountIncome(ArrayList<TypeIncomeExpense> arrayListTempType) {
        double sum = 0.00;
        for(int i = 0; i < arrayListTempType.size(); i++)
            sum += arrayListTempType.get(i).getSumAmountMoney();
        return sum;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPressIncome) {
            btnShowListReportIncome.setImageResource(R.drawable.ic_list_down);
            btnShowListReportExpense.setImageResource(R.drawable.ic_item);
            loadListItems(arrayListTempTypeIncome);
        }
        if (isPressExpense) {
            btnShowListReportExpense.setImageResource(R.drawable.ic_list_down);
            btnShowListReportIncome.setImageResource(R.drawable.ic_item);
            loadListItems(arrayListTempTypeExpense);
        }
        statisticFinancialThisDate();
    }

    private void getControlFromView() {
        btnShowTime = (Button) view.findViewById(R.id.btn_show_time);
        btnNextDay = (ImageButton) view.findViewById(R.id.btn_next_day);
        btnPreDay = (ImageButton) view.findViewById(R.id.btn_pre_day);
        btnShowListReportExpense = (ImageButton) view.findViewById(R.id.btn_show_report_expense);
        btnShowListReportIncome = (ImageButton) view.findViewById(R.id.btn_show_report_income);
        lvShowListItems = (ListView) view.findViewById(R.id.lv_show_report);
        txtShowTime = (TextView) view.findViewById(R.id.txt_show_time);
        txtAmountMoneyIncomeReport = (TextView)view.findViewById(R.id.txt_amount_income_report);
        txtAmountMoneyExpenseReport = (TextView)view.findViewById(R.id.txt_amount_expense_report);
        txtAmountMoneyBalance = (TextView)view.findViewById(R.id.txt_amount_money_balance);
        adapterCustomListView = new AdapterCustomListView(getActivity(), R.layout.layout_custom_items, arrayListReport);
        lvShowListItems.setAdapter(adapterCustomListView);

    }

    private void addEventControl() {
        btnNextDay.setOnClickListener(new MyEvents());
        btnPreDay.setOnClickListener(new MyEvents());
        btnShowListReportIncome.setOnClickListener(new MyEvents());
        btnShowListReportExpense.setOnClickListener(new MyEvents());
        lvShowListItems.setOnItemClickListener(new MyListEvents());
    }

    private void showCurrentTime() {
        String date = formatTime.format(calendar.getTime());
        btnShowTime.setText(date);
        txtShowTime.setText(date);
    }

    public void resetDefaultAmountMoney() {
        ArrayList<TypeIncomeExpense> listTypeExpense = db.getListTypeExpense("SELECT * FROM " + db.TABLE_TYPE_EXPENSE);
        ArrayList<TypeIncomeExpense> listTypeIncome = db.getListTypeIncome("SELECT * FROM " + db.TABLE_TYPE_INCOME);
        resetDefaultAmountMoneyTypeItems(true, listTypeIncome);
        resetDefaultAmountMoneyTypeItems(false, listTypeExpense);
    }

    public void getListTypeIncomeExpense() {
        String date = String.valueOf(btnShowTime.getText());
        arrayListTempTypeIncome = db.getListTypeIncome("SELECT * FROM " + Database.TABLE_TYPE_INCOME);
        ArrayList<IncomeExpense> arrayListIncome = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
        if (arrayListIncome.size() > 0)
            setAmountMoneyTypeItem(date, arrayListTempTypeIncome, arrayListIncome);
        arrayListTempTypeExpense = db.getListTypeExpense("SELECT * FROM " + Database.TABLE_TYPE_EXPENSE);
        ArrayList<IncomeExpense> arrayListExpense = db.getListExpense("SELECT * FROM " + Database.TABLE_EXPENSE);
        if (arrayListExpense.size() > 0)
            setAmountMoneyTypeItem(date, arrayListTempTypeExpense, arrayListExpense);
    }


    public class MyEvents implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_next_day:
                    showNextDate();
                    break;
                case R.id.btn_pre_day:
                    showPreDate();
                    break;
                case R.id.btn_show_report_income:
                    showListIncomeReport();
                    break;
                case R.id.btn_show_report_expense:
                    showListExpenseReport();
                    break;
                default:
                    break;
            }
        }
    }

    public class MyListEvents implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            typeItemSelected = (TypeIncomeExpense)parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), ReportDetailDateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("NAME", typeItemSelected.getName());
            bundle.putDouble("AMOUNT", typeItemSelected.getSumAmountMoney());
            bundle.putString("TIME", String.valueOf(btnShowTime.getText()));
            if(isPressIncome)
                bundle.putInt("TYPE", 1);
            if(isPressExpense)
                bundle.putInt("TYPE", 2);
            intent.putExtra("ITEM", bundle);
            startActivity(intent);
        }
    }

    private void showPreDate() {
        String strDate = String.valueOf(btnShowTime.getText());
        try {
            Date date = formatTime.parse(strDate);
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.setTime(date);
            tempCalendar.add(Calendar.DATE, -1);
            Date preDate = tempCalendar.getTime();
            btnShowTime.setText(formatTime.format(preDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtShowTime.setText(String.valueOf(btnShowTime.getText()));
        resetDefaultAmountMoney();
        getListTypeIncomeExpense();
        if(isPressIncome)
            loadListItems(arrayListTempTypeIncome);
        if(isPressExpense)
            loadListItems(arrayListTempTypeExpense);
        statisticFinancialThisDate();
    }

    private void showNextDate() {
        String strDate = String.valueOf(btnShowTime.getText());
        try {
            Date date = formatTime.parse(strDate);
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.setTime(date);
            tempCalendar.add(Calendar.DATE, 1);
            Date nextDate = tempCalendar.getTime();
            btnShowTime.setText(formatTime.format(nextDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtShowTime.setText(String.valueOf(btnShowTime.getText()));
        resetDefaultAmountMoney();
        getListTypeIncomeExpense();
        if(isPressIncome)
            loadListItems(arrayListTempTypeIncome);
        if(isPressExpense)
            loadListItems(arrayListTempTypeExpense);
        statisticFinancialThisDate();
    }

    private void showListExpenseReport() {
        if(isPressIncome){
            btnShowListReportIncome.setImageResource(R.drawable.ic_item);
            isPressIncome = false;
        }
        if(!isPressIncome && !isPressExpense){
            isPressExpense = true;
            btnShowListReportExpense.setImageResource(R.drawable.ic_list_down);
            loadListItems(arrayListTempTypeExpense);
        }
    }


    private void showListIncomeReport() {
        if(isPressExpense){
            btnShowListReportExpense.setImageResource(R.drawable.ic_item);
            isPressExpense = false;
        }
        if(!isPressIncome && !isPressExpense){
            isPressIncome = true;
            btnShowListReportIncome.setImageResource(R.drawable.ic_list_down);
            loadListItems(arrayListTempTypeIncome);
        }
    }

    private void setAmountMoneyTypeItem(String date, ArrayList<TypeIncomeExpense> arrayListTypeItems, ArrayList<IncomeExpense> arrayListItems) {
        for (int i = 0; i < arrayListTypeItems.size(); i++) {
            TypeIncomeExpense typeItem = arrayListTypeItems.get(i);
            double tempSum = 0.00;
            for (int j = 0; j < arrayListItems.size(); j++) {
                IncomeExpense item = arrayListItems.get(j);
                if (typeItem.getName().equalsIgnoreCase(item.getName()) && date.equalsIgnoreCase(formatTime.format(item.getDate())))
                    tempSum += item.getAmount();
            }
            typeItem.setSumAmountMoney(tempSum);
        }
    }

    private void loadListItems(ArrayList<TypeIncomeExpense> arrayListTypeItems) {
        arrayListReport.clear();
        for (int i = 0; i < arrayListTypeItems.size(); i++)
            arrayListReport.add(arrayListTypeItems.get(i));
        adapterCustomListView.notifyDataSetChanged();
    }

    private void resetDefaultAmountMoneyTypeItems(boolean flag, ArrayList<TypeIncomeExpense> listTypeItems) {
        for (int i = 0; i < listTypeItems.size(); i++) {
            TypeIncomeExpense tempItem = listTypeItems.get(i);
            tempItem.setSumAmountMoney(0.00);
            if (flag)
                db.updateTypeIncome(tempItem);
            else
                db.updateTypeExpense(tempItem);
        }
    }

}
