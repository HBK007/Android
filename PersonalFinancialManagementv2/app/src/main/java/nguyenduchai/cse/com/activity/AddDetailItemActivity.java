package nguyenduchai.cse.com.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

public class AddDetailItemActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editAmount;
    private EditText editDescription;
    private ImageButton btnSound;
    private ImageButton btnGraph;
    private Button btnSave;
    private Button btnCancel;
    private ImageButton btnDay;
    private ImageButton btnHour;
    private ImageButton btnCalculator;
    private TextView txtDate;
    private TextView txtHour;
    private TypeIncomeExpense typeIncomeSelected = null;
    private int codeItem;
    private static final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat formatHour = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private Calendar dateCalendar;
    private Calendar timeCalendar;
    private Context context;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail_item);
        setUpDatabase();
        getFromWidgets();
        getDataFromMain();
        setTitle(typeIncomeSelected.getName());
        getDefaultTime();
        addEvent();
        setSupportActionBar(toolbar);

    }

    private void getFromWidgets() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editAmount = (EditText)findViewById(R.id.edit_amount);
        editDescription = (EditText)findViewById(R.id.edit_descrip);
        btnSound = (ImageButton)findViewById(R.id.img_voice);
        btnGraph = (ImageButton)findViewById(R.id.img_graph);
        txtDate = (TextView)findViewById(R.id.txt_date);
        txtHour = (TextView)findViewById(R.id.txt_hour);
        btnSave = (Button)findViewById(R.id.btn_save);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnDay = (ImageButton)findViewById(R.id.btn_day);
        btnHour = (ImageButton)findViewById(R.id.btn_hour);
        btnCalculator = (ImageButton)findViewById(R.id.btn_calculator);
    }

    private void getDataFromMain() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DETAIL");
        typeIncomeSelected = (TypeIncomeExpense)bundle.getSerializable("SELECT_DETAIL");
        codeItem = bundle.getInt("CODE_ADD_DETAIL");
    }

    public void getDefaultTime(){
        calendar =  Calendar.getInstance();
        txtDate.setText(formatDate.format(calendar.getTime()));
        txtHour.setText(formatHour.format(calendar.getTime()));
    }

    public void addEvent(){
        btnDay.setOnClickListener(new MyEvent());
        btnHour.setOnClickListener(new MyEvent());
        btnSave.setOnClickListener(new MyEvent());
        btnCancel.setOnClickListener(new MyEvent());
        btnCalculator.setOnClickListener(new MyEvent());
    }

    public void setUpDatabase(){
        context = this;
        db = new Database(context);
    }
    public void saveData() throws ParseException {
        String strAmount = editAmount.getText().toString().trim();
        String strDescription = editDescription.getText().toString().trim();
        String strDate = txtDate.getText().toString().trim();
        String strHour = txtHour.getText().toString().trim();
        String notify = "";
        if(TextUtils.isEmpty(strAmount)){
            Toast.makeText(AddDetailItemActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(strDescription)){
            Toast.makeText(AddDetailItemActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
        }
        else{
            IncomeExpense newItem = new IncomeExpense();
            newItem.setName(typeIncomeSelected.getName());
            newItem.setAmount(Double.parseDouble(strAmount));
            newItem.setContent(strDescription);
            newItem.setDate(formatDate.parse(txtDate.getText().toString()));
            newItem.setHour(formatHour.parse(txtHour.getText().toString()));
            newItem.setFlagCheck(0);
            if(codeItem == 1) {
                if (db.insertIncome(newItem) > 0)
                    notify = "Insert database success";
                else
                    notify = "Insert database fail";
            }else{
                if (db.insertExpense(newItem) > 0)
                    notify = "Insert database success";
                else
                    notify = "Insert database fail";
            }
            Toast.makeText(AddDetailItemActivity.this, notify, Toast.LENGTH_LONG).show();
            resetAllFields();
            finish();
        }
    }

    private void resetAllFields() {
        editAmount.setText("");
        editDescription.setText("");
    }

    private void cancelData(){
        resetAllFields();
        finish();
    }

    private class MyEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_day:
                    showDatePickerDialog();
                    break;
                case R.id.btn_hour:
                    showTimePickerDialog();
                    break;
                case R.id.btn_save:
                    try {
                        saveData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_cancel:
                    cancelData();
                    break;
                case R.id.btn_calculator:
                    showCalculator();
                    break;
                default:
                    break;
            }
        }
    }
    public void showDatePickerDialog(){
        dateCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar tempCalendar = Calendar.getInstance();
                        tempCalendar.set(year, monthOfYear, dayOfMonth);
                        txtDate.setText(formatDate.format(tempCalendar.getTime()));
                    }
                }, dateCalendar.get(Calendar.YEAR),
                dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
    public void showTimePickerDialog(){
        timeCalendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar tempCalendar = Calendar.getInstance();
                        tempCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        tempCalendar.set(Calendar.MINUTE, minute);
                        txtHour.setText(formatHour.format(tempCalendar.getTime()));
                    }
                }, timeCalendar.get(Calendar.HOUR_OF_DAY), timeCalendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }
    public void showCalculator(){

    }

}
