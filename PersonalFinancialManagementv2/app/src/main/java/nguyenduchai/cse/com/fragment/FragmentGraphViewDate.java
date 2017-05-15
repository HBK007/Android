package nguyenduchai.cse.com.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PieChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nguyenduchai.cse.com.adapter.AdapterCustomListView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 12/6/2015.
 */
public class FragmentGraphViewDate extends Fragment {
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
    private TextView txtShowTime;
    private TextView txtAmountMoneyIncomeReport;
    private TextView txtAmountMoneyExpenseReport;
    private TextView txtAmountMoneyBalance;
    private LinearLayout layoutChart;
    private GraphicalView incomeChart;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private boolean isPressIncome = false;
    private boolean isPressExpense = false;
    private String []itemTitle;
    private double []itemAmount;
    private int []colors;
    private DecimalFormat formatValue = new DecimalFormat("###,###.###");

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
        view = inflater.inflate(R.layout.layout_custom_graph_view, container, false);
        getControlFromView();
        addEventControl();
        showCurrentTime();
        resetDefaultAmountMoney();
        getListTypeIncomeExpense();
//        default show income
        showChartTypeIncome();
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
            drawChartTypeItem(true, arrayListTempTypeIncome);
        }
        if (isPressExpense) {
            btnShowListReportIncome.setImageResource(R.drawable.ic_item);
            btnShowListReportExpense.setImageResource(R.drawable.ic_list_down);
            drawChartTypeItem(false, arrayListTempTypeExpense);
        }
        statisticFinancialThisDate();
    }

    private void getControlFromView() {
        layoutChart = (LinearLayout)view.findViewById(R.id.graph_view);
        btnShowTime = (Button) view.findViewById(R.id.btn_show_time);
        btnNextDay = (ImageButton) view.findViewById(R.id.btn_next_day);
        btnPreDay = (ImageButton) view.findViewById(R.id.btn_pre_day);
        btnShowListReportExpense = (ImageButton) view.findViewById(R.id.btn_show_report_expense);
        btnShowListReportIncome = (ImageButton) view.findViewById(R.id.btn_show_report_income);
        txtShowTime = (TextView) view.findViewById(R.id.txt_show_time);
        txtAmountMoneyIncomeReport = (TextView)view.findViewById(R.id.txt_amount_income_report);
        txtAmountMoneyExpenseReport = (TextView)view.findViewById(R.id.txt_amount_expense_report);
        txtAmountMoneyBalance = (TextView)view.findViewById(R.id.txt_amount_money_balance);
    }

    private void addEventControl() {
        btnNextDay.setOnClickListener(new MyEvents());
        btnPreDay.setOnClickListener(new MyEvents());
        btnShowListReportIncome.setOnClickListener(new MyEvents());
        btnShowListReportExpense.setOnClickListener(new MyEvents());
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
                    showChartTypeIncome();
                    break;
                case R.id.btn_show_report_expense:
                    showChartTypeExpense();
                    break;
                default:
                    break;
            }
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
            drawChartTypeItem(true, arrayListTempTypeIncome);
        if(isPressExpense)
            drawChartTypeItem(true, arrayListTempTypeExpense);
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
            drawChartTypeItem(true, arrayListTempTypeIncome);
        if(isPressExpense)
            drawChartTypeItem(true, arrayListTempTypeExpense);
        statisticFinancialThisDate();
    }

    private void showChartTypeExpense() {
        if(isPressIncome){
            btnShowListReportIncome.setImageResource(R.drawable.ic_item);
            isPressIncome = false;
        }
        if(!isPressIncome && !isPressExpense){
            isPressExpense = true;
            btnShowListReportExpense.setImageResource(R.drawable.ic_list_down);
            drawChartTypeItem(false, arrayListTempTypeExpense);
        }
    }


    private void showChartTypeIncome() {
        if(isPressExpense){
            btnShowListReportExpense.setImageResource(R.drawable.ic_item);
            isPressExpense = false;
        }
        if(!isPressIncome && !isPressExpense){
            isPressIncome = true;
            btnShowListReportIncome.setImageResource(R.drawable.ic_list_down);
            drawChartTypeItem(true, arrayListTempTypeIncome);
        }
    }

    private boolean checkDataExitsThatDrawChart(boolean flag){
        if(flag){
            for(int i = 0; i < arrayListTempTypeIncome.size(); i++){
                if(arrayListTempTypeIncome.get(i).getSumAmountMoney() > 0.00)
                    return true;
            }
        }else{
            for (int i = 0; i < arrayListTempTypeExpense.size(); i++){
                if(arrayListTempTypeExpense.get(i).getSumAmountMoney() > 0.00)
                    return true;
            }
        }
        return false;
    }

    private void drawChartTypeItem(boolean flag, ArrayList<TypeIncomeExpense> tempArrayListItem){
        if(flag){
            if(!checkDataExitsThatDrawChart(flag)){
                layoutChart.removeAllViews();
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                return;
            }

        }else{
            if(!checkDataExitsThatDrawChart(flag)){
                layoutChart.removeAllViews();
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        int []tempColors = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.GRAY, Color.DKGRAY,
                Color.LTGRAY, Color.DKGRAY, Color.MAGENTA};
        itemTitle = new String[tempArrayListItem.size()];
        itemAmount = new double[tempArrayListItem.size()];
        colors = new int[tempArrayListItem.size()];
        for(int i = 0; i < tempArrayListItem.size(); i++){
            itemTitle[i] = tempArrayListItem.get(i).getName();
            itemAmount[i] = tempArrayListItem.get(i).getSumAmountMoney();
            colors[i] = tempColors[i];
        }
        CategorySeries distributionSeries = new CategorySeries(" Pie Chart");
        for (int i = 0; i < itemAmount.length; i++){
            distributionSeries.add(itemTitle[i], itemAmount[i]);
        }
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < itemAmount.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayBoundingPoints(true);
            seriesRenderer.setStroke(BasicStroke.SOLID);
            //Adding colors to the chart
            defaultRenderer.setBackgroundColor(Color.WHITE);
            defaultRenderer.setApplyBackgroundColor(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }
        defaultRenderer.isInScroll();
        defaultRenderer.setZoomEnabled(false);
        defaultRenderer.setPanEnabled(false);
        defaultRenderer.setZoomButtonsVisible(false); // set zoom button in Graph
        defaultRenderer.setApplyBackgroundColor(false);
        defaultRenderer.setBackgroundColor(Color.WHITE); // set background color
        defaultRenderer.setChartTitle("Pie Chart");
        defaultRenderer.setLabelsColor(Color.BLACK);
        defaultRenderer.setMargins(new int[]{0, 0, 0, 0});
        defaultRenderer.setLabelsTextSize(20);
        defaultRenderer.setChartTitleTextSize((float)20);
        defaultRenderer.setShowLabels(false);
        defaultRenderer.setLegendTextSize(16);
        defaultRenderer.setDisplayValues(false);
        defaultRenderer.setStartAngle(90);

        layoutChart.removeAllViews();
        incomeChart = ChartFactory.getPieChartView(getContext(),
                distributionSeries, defaultRenderer);
        layoutChart.addView(incomeChart);
        incomeChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = incomeChart.getCurrentSeriesAndPoint();
                if(seriesSelection == null){
                    Toast.makeText(getActivity(), "No chart element click", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), itemTitle[seriesSelection.getPointIndex()] + " = " + formatValue.format(seriesSelection.getValue()), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
