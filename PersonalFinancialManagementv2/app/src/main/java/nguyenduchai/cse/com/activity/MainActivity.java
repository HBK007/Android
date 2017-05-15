package nguyenduchai.cse.com.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import nguyenduchai.cse.com.adapter.AdapterViewPagerTypeItems;
import nguyenduchai.cse.com.backupdata.BackupData;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BackupData.OnBackupListener {

    private  DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapterViewPager;
    private Toolbar toolbar;
    private TextView txtAmountMoneyForIncome;
    private TextView txtAmountMoneyForExpense;
    private TextView txtAmountMoneyBalance;
    private Context context;
    private Database db;
    private BackupData backupData;
    private Calendar monthCalendar = Calendar.getInstance();
    private SimpleDateFormat formatMonth = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doConnectControlView();
        setSupportActionBar(toolbar);
        doSetNavigationView();
        doSetViewPager();
//      access database
        context = this;
        db = new Database(context);
        backupData = new BackupData(context);
        backupData.setOnBackupListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayInformationFinancialThisMonth();
    }

    private void displayInformationFinancialThisMonth() {
        DecimalFormat decimalFormatOp = new DecimalFormat("###,###.###");
        String time = formatMonth.format(monthCalendar.getTime());
        String[] strArrTemp = time.split("-");
        int month = Integer.parseInt(strArrTemp[1]);
        String nameTableIncome = "SELECT * FROM " + Database.TABLE_INCOME;
        String nameTableExpense = "SELECT * FROM " + Database.TABLE_EXPENSE;
        if (checkIsTableExits(nameTableIncome) || checkIsTableExits(nameTableExpense)) {
            ArrayList<IncomeExpense> listIncome = db.getListIncome(nameTableIncome);
            ArrayList<IncomeExpense> listExpense = db.getListExpense(nameTableExpense);
            double sumAmountMoneyIncome = sumAmountMoneyAllItems(month, listIncome);
            double sumAmountMoneyExpense = sumAmountMoneyAllItems(month, listExpense);
            txtAmountMoneyForIncome.setText(decimalFormatOp.format(sumAmountMoneyIncome));
            txtAmountMoneyForExpense.setText(decimalFormatOp.format(sumAmountMoneyExpense));
            txtAmountMoneyBalance.setText(decimalFormatOp.format(sumAmountMoneyIncome - sumAmountMoneyExpense));

        } else {
            txtAmountMoneyForIncome.setText(decimalFormatOp.format(0.00));
            txtAmountMoneyForExpense.setText(decimalFormatOp.format(0.00));
            txtAmountMoneyBalance.setText(decimalFormatOp.format(0.00));
        }
    }

    private boolean checkIsTableExits(String nameTable) {
        Cursor cursor = db.getAll(nameTable);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    private double sumAmountMoneyAllItems(int month, ArrayList<IncomeExpense> listItem) {
        double sumMoney = 0.0;
        for (int i = 0; i < listItem.size(); i++) {
            IncomeExpense item = listItem.get(i);
            String time = formatMonth.format(item.getDate());
            String[] tempArr = time.split("-");
            if (month == Integer.parseInt(tempArr[1]))
                sumMoney += item.getAmount();
        }
        return sumMoney;
    }

    private void doConnectControlView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        txtAmountMoneyBalance = (TextView) findViewById(R.id.txt_money_balance);
        txtAmountMoneyForIncome = (TextView) findViewById(R.id.txt_amount_income_report);
        txtAmountMoneyForExpense = (TextView) findViewById(R.id.txt_amount_expense);
    }

    private void doSetNavigationView() {
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void doSetViewPager() {
        adapterViewPager = new AdapterViewPagerTypeItems(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_title_income_expense));
        viewPager.setAdapter(adapterViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_backup:
                backupData.exportToSD();
                break;
            case R.id.menu_import:
                backupData.importFromSD();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_report) {
            // Handle open report
            String itemTitleReport = String.valueOf(item.getTitle());
            Intent intentReport = new Intent(this, ReportActivity.class);
            Bundle bundleReport = new Bundle();
            bundleReport.putString("TITLE", itemTitleReport);
            intentReport.putExtra("DATA", bundleReport);
            startActivity(intentReport);

        } else if (id == R.id.nav_calculator) {

        } else if (id == R.id.nav_graph_view) {
            String itemTitleGraphView = String.valueOf(item.getTitle());
            Intent intentGraphView = new Intent(this, GraphViewActivity.class);
            Bundle bundleGraphView = new Bundle();
            bundleGraphView.putString("TITLE", itemTitleGraphView);
            intentGraphView.putExtra("DATA", bundleGraphView);
            startActivity(intentGraphView);

        } else if (id == R.id.nav_search) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public void onFinishExport(String error) {
        String notify = error;
        if (error == null) {
            notify = "Export success";
        }
        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishImport(String error) {
        String notify = error;
        if (error == null) {
            notify = "Import success";
            displayInformationFinancialThisMonth();
        }
        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
    }
}
