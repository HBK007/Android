package nguyenduchai.cse.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;

/**
 * Created by Nguyen Duc Hai on 11/11/2015.
 */
public class Database extends SQLiteOpenHelper {

    private static final SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    private static final SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm: a", Locale.getDefault());

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    /**
     * Name database
     */

    public static final String DATABASE_NAME = "finance_personal.db";

    /**
     * Table of user
     * Table contain id, name user login, full name user, password user
     */
    public static final String KEY_ID_USER = "id_user";
    public static final String KEY_NAME_LOGIN_USER = "name_user_login";
    public static final String KEY_PASS_WORD_USER = "pass_word";
    public static final String KEY_ICON_USER = "icon_user";
    public static final String TABLE_USER = "tb_user";

    /**
     * Table represent kind of income
     */
    public static final String KEY_ID_TYPE_INCOME = "id_type_income";
    public static final String KEY_NAME_TYPE_INCOME = "name_type_income";
    public static final String KEY_SUM_AMOUNT_MONEY_INCOME = "sum_amount_money_income";
    public static final String KEY_ICON_TYPE_INCOME = "icon_type_income";
    public static final String TABLE_TYPE_INCOME = "tb_type_income";

    /**
     * Table of type income
     * Table contain id, name, icon and amount of Income
     */
    public static final String KEY_ID_INCOME = "id_income";
    public static final String KEY_NAME_INCOME = "name_income";
    public static final String KEY_AMOUNT_MONEY_INCOME = "amount_money_income";
    public static final String KEY_CONTENT_INCOME = "content_income";
    public static final String KEY_CREATE_DATE_INCOME = "create_date_income";
    public static final String KEY_CREATE_TIME_INCOME = "create_time_income";
    public static final String KEY_CHECK_FLAG_INCOME = "check_flag_income";
    public static final String TABLE_INCOME = "tb_income";

    /**
     * Table represent kind of expense
     */
    public static final String KEY_ID_TYPE_EXPENSE = "id_type_expense";
    public static final String KEY_NAME_TYPE_EXPENSE = "name_type_expense";
    public static final String KEY_SUM_AMOUNT_MONEY_EXPENSE = "sum_amount_money_expense";
    public static final String KEY_ICON_TYPE_EXPENSE = "icon_type_expense";
    public static final String TABLE_TYPE_EXPENSE = "tb_type_expense";
    /**
     * Table of expense
     * Table contain id and amount of Expense
     */
    public static final String KEY_ID_EXPENSE = "id_expense";
    public static final String KEY_NAME_EXPENSE = "name_expense";
    public static final String KEY_AMOUNT_MONEY_EXPENSE = "amount_money_expense";
    public static final String KEY_CONTENT_EXPENSE = "content_expense";
    public static final String KEY_CREATE_DATE_EXPENSE = "create_date_expense";
    public static final String KEY_CREATE_TIME_EXPENSE = "create_time_expense";
    public static final String KEY_CHECK_FLAG_EXPENSE = "check_flag_expense";
    public static final String TABLE_EXPENSE = "tb_expense";

    /**
     * String create tables
    */
    public static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + KEY_ICON_USER + " BLOB NOT NULL, "
                    + KEY_NAME_LOGIN_USER + " TEXT NOT NULL,"
                    + KEY_PASS_WORD_USER + " TEXT NOT NULL"
                    + ")";


    public static final String CREATE_TABLE_TYPE_INCOME =
            "CREATE TABLE " + TABLE_TYPE_INCOME + "("
                    + KEY_ID_TYPE_INCOME + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + KEY_ICON_TYPE_INCOME + " BLOB NOT NULL, "
                    + KEY_NAME_TYPE_INCOME + " TEXT NOT NULL, "
                    + KEY_SUM_AMOUNT_MONEY_INCOME + " DOUBLE NOT NULL"
                    + ")";

    public static final String CREATE_TABLE_INCOME =
            "CREATE TABLE " + TABLE_INCOME + "("
                    + KEY_ID_INCOME + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + KEY_NAME_INCOME + " TEXT NOT NULL, "
                    + KEY_CONTENT_INCOME + " TEXT NOT NULL, "
                    + KEY_AMOUNT_MONEY_INCOME + " DOUBLE NOT NULL, "
                    + KEY_CREATE_DATE_INCOME + " DATE NOT NULL, "
                    + KEY_CREATE_TIME_INCOME + " DATE NOT NULL, "
                    + KEY_CHECK_FLAG_INCOME + " INTEGER NOT NULL, FOREIGN KEY (" + KEY_NAME_INCOME + ") REFERENCES " + TABLE_TYPE_INCOME + " (" + KEY_ID_TYPE_INCOME + ")"
                    + ")";

    public static final String CREATE_TABLE_TYPE_EXPENSE =
            "CREATE TABLE " + TABLE_TYPE_EXPENSE + "("
                    + KEY_ID_TYPE_EXPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + KEY_ICON_TYPE_EXPENSE + " BLOB NOT NULL, "
                    + KEY_NAME_TYPE_EXPENSE + " TEXT NOT NULL, "
                    + KEY_SUM_AMOUNT_MONEY_EXPENSE + " DOUBLE NOT NULL"
                    + ")";

    public static final String CREATE_TABLE_EXPENSE =
            "CREATE TABLE " + TABLE_EXPENSE + "("
                    + KEY_ID_EXPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + KEY_NAME_EXPENSE + " TEXT NOT NULL, "
                    + KEY_CONTENT_EXPENSE + " TEXT NOT NULL, "
                    + KEY_AMOUNT_MONEY_EXPENSE + " DOUBLE NOT NULL, "
                    + KEY_CREATE_DATE_EXPENSE + " DATE NOT NULL, "
                    + KEY_CREATE_TIME_EXPENSE + " DATE NOT NULL, "
                    + KEY_CHECK_FLAG_EXPENSE + " INTEGER NOT NULL, FOREIGN KEY (" + KEY_NAME_EXPENSE + ") REFERENCES " + TABLE_TYPE_EXPENSE + " (" + KEY_ID_TYPE_EXPENSE + ")"
                    + ")";

    /**
     * key value version database
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * sqlite database
     */
    SQLiteDatabase db;

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_TYPE_INCOME);
            db.execSQL(CREATE_TABLE_INCOME);
            db.execSQL(CREATE_TABLE_TYPE_EXPENSE);
            db.execSQL(CREATE_TABLE_EXPENSE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do something later
    }

    /**
     * open database
     */
    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * close database
     */
    public void close() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /******************Function access database************************/

    /**
     * Get all row of database with sql return cursor
     * @param sql
     * @return
     */
    public Cursor getAll(String sql) {
        open();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    /**
     * insert data into database
     * @param table
     * @param contentValues
     * @return
     */
    public long insert(String table, ContentValues contentValues) {
        open();
        long index = db.insert(table, null, contentValues);
        return index;
    }

    /**
     * update data into database
     * @param table
     * @param contentValues
     * @param where
     * @return
     */
    public boolean update(String table, ContentValues contentValues, String where) {
        open();
        long index = db.update(table, contentValues, where, null);
        return index > 0;
    }

    /**
     * delete row of database
     * @param table
     * @param where
     * @return
     */
    public boolean delete(String table, String where) {
        open();
        long index = db.delete(table, where, null);
        return index > 0;
    }
    /**
     *  function access user
     *  update new version
     */

    /**
     * function access table type income
     */
    public TypeIncomeExpense getTypeIncome(String sql) {
        TypeIncomeExpense typeIncome = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            typeIncome = cursorToTypeIncome(cursor);
            cursor.close();
        }
        return typeIncome;
    }

    public ArrayList<TypeIncomeExpense> getListTypeIncome(String sql) {
        ArrayList<TypeIncomeExpense> listTypeIncome = new ArrayList<>();
        Cursor cursor = getAll(sql);
        while (!cursor.isAfterLast()) {
            listTypeIncome.add(cursorToTypeIncome(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return listTypeIncome;
    }

    public long insertTypeIncome(TypeIncomeExpense typeIncome) {
        return insert(TABLE_TYPE_INCOME, typeIncomeToValues(typeIncome));
    }

    public boolean updateTypeIncome(TypeIncomeExpense typeIncome) {
        return update(TABLE_TYPE_INCOME, typeIncomeToValues(typeIncome), KEY_ID_TYPE_INCOME + "=" + typeIncome.getId());
    }

    public boolean deleteTypeIncome(String where) {
        return delete(TABLE_TYPE_INCOME, where);
    }

    private ContentValues typeIncomeToValues(TypeIncomeExpense typeIncome) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ICON_TYPE_INCOME, typeIncome.getImage());
        contentValues.put(KEY_NAME_TYPE_INCOME, typeIncome.getName());
        contentValues.put(KEY_SUM_AMOUNT_MONEY_INCOME, typeIncome.getSumAmountMoney());
        return contentValues;
    }

    public TypeIncomeExpense cursorToTypeIncome(Cursor cursor) {
        TypeIncomeExpense typeIncome = new TypeIncomeExpense();
        typeIncome.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_TYPE_INCOME)));
        typeIncome.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_TYPE_INCOME)));
        typeIncome.setImage(cursor.getBlob(cursor.getColumnIndex(KEY_ICON_TYPE_INCOME)));
        typeIncome.setSumAmountMoney(cursor.getDouble(cursor.getColumnIndex(KEY_SUM_AMOUNT_MONEY_INCOME)));
        return typeIncome;
    }

    /**
     * function access table income
     */
    public IncomeExpense getIncome(String sql) {
        IncomeExpense income = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            income = cursorToIncome(cursor);
            cursor.close();
        }
        return income;
    }

    public ArrayList<IncomeExpense> getListIncome(String sql) {
        ArrayList<IncomeExpense> listIncome = new ArrayList<>();
        Cursor cursor = getAll(sql);
        while (!cursor.isAfterLast()) {
            listIncome.add(cursorToIncome(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return listIncome;
    }

    public long insertIncome(IncomeExpense income) {
        return insert(TABLE_INCOME, typeIncomeToValues(income));
    }

    public boolean updateIncome(IncomeExpense income) {
        return update(TABLE_INCOME, typeIncomeToValues(income), KEY_ID_INCOME + "=" + income.getId());
    }

    public boolean deleteIncome(String where) {
        return delete(TABLE_INCOME, where);
    }

    private ContentValues typeIncomeToValues(IncomeExpense income) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME_INCOME, income.getName());
        contentValues.put(KEY_CONTENT_INCOME, income.getContent());
        contentValues.put(KEY_AMOUNT_MONEY_INCOME, income.getAmount());
        contentValues.put(KEY_CREATE_DATE_INCOME, formatDate.format(income.getDate()));
        contentValues.put(KEY_CREATE_TIME_INCOME, formatTime.format(income.getHour()));
        contentValues.put(KEY_CHECK_FLAG_INCOME, income.getFlagCheck());
        return contentValues;
    }

    public IncomeExpense cursorToIncome(Cursor cursor) {
        IncomeExpense income = new IncomeExpense();
        income.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_INCOME)));
        income.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_INCOME)));
        income.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT_INCOME)));
        income.setAmount(cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT_MONEY_INCOME)));
        try {
            income.setDate(formatDate.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATE_DATE_INCOME))));
        } catch (ParseException e) {
            income.setDate(null);
        }
        try {
            income.setHour(formatTime.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATE_TIME_INCOME))));
        } catch (ParseException e) {
            income.setHour(null);
        }
        income.setFlagCheck(cursor.getInt(cursor.getColumnIndex(KEY_CHECK_FLAG_INCOME)));
        return income;
    }
    /**
     * function access type expense
     */
    public TypeIncomeExpense getTypeExpense(String sql) {
        TypeIncomeExpense typeExpense = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            typeExpense = cursorToTypeExpense(cursor);
            cursor.close();
        }
        return typeExpense;
    }

    public ArrayList<TypeIncomeExpense> getListTypeExpense(String sql) {
        ArrayList<TypeIncomeExpense> listTypeExpense = new ArrayList<>();
        Cursor cursor = getAll(sql);
        while (!cursor.isAfterLast()) {
            listTypeExpense.add(cursorToTypeExpense(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return listTypeExpense;
    }

    public long insertTypeExpense(TypeIncomeExpense typeExpense) {
        return insert(TABLE_TYPE_EXPENSE, typeExpenseToValues(typeExpense));
    }

    public boolean updateTypeExpense(TypeIncomeExpense typeExpense) {
        return update(TABLE_TYPE_EXPENSE, typeExpenseToValues(typeExpense), KEY_ID_TYPE_EXPENSE + "=" + typeExpense.getId());
    }

    public boolean deleteTypeExpense(String where) {
        return delete(TABLE_TYPE_EXPENSE, where);
    }

    private ContentValues typeExpenseToValues(TypeIncomeExpense typeExpense) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ICON_TYPE_EXPENSE, typeExpense.getImage());
        contentValues.put(KEY_NAME_TYPE_EXPENSE, typeExpense.getName());
        contentValues.put(KEY_SUM_AMOUNT_MONEY_EXPENSE, typeExpense.getSumAmountMoney());
        return contentValues;
    }

    public TypeIncomeExpense cursorToTypeExpense(Cursor cursor) {
        TypeIncomeExpense typeExpense = new TypeIncomeExpense();
        typeExpense.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_TYPE_EXPENSE)));
        typeExpense.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_TYPE_EXPENSE)));
        typeExpense.setImage(cursor.getBlob(cursor.getColumnIndex(KEY_ICON_TYPE_EXPENSE)));
        typeExpense.setSumAmountMoney(cursor.getDouble(cursor.getColumnIndex(KEY_SUM_AMOUNT_MONEY_EXPENSE)));
        return typeExpense;
    }

/***************************function access table expense****************/

    public IncomeExpense getExpense(String sql) {
        IncomeExpense expense = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            expense = cursorToExpense(cursor);
            cursor.close();
        }
        return expense;
    }

    public ArrayList<IncomeExpense> getListExpense(String sql) {
        ArrayList<IncomeExpense> listExpense = new ArrayList<>();
        Cursor cursor = getAll(sql);
        while (!cursor.isAfterLast()) {
            listExpense.add(cursorToExpense(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return listExpense;
    }

    public long insertExpense(IncomeExpense expense) {
        return insert(TABLE_EXPENSE, expenseToValues(expense));
    }

    public boolean updateExpense(IncomeExpense expense) {
        return update(TABLE_EXPENSE, expenseToValues(expense), KEY_ID_EXPENSE + "=" + expense.getId());
    }

    public boolean deleteExpense(String where) {
        return delete(TABLE_EXPENSE, where);
    }

    private ContentValues expenseToValues(IncomeExpense expense) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME_EXPENSE, expense.getName());
        contentValues.put(KEY_CONTENT_EXPENSE, expense.getContent());
        contentValues.put(KEY_AMOUNT_MONEY_EXPENSE, expense.getAmount());
        contentValues.put(KEY_CREATE_DATE_EXPENSE, formatDate.format(expense.getDate()));
        contentValues.put(KEY_CREATE_TIME_EXPENSE, formatTime.format(expense.getHour()));
        contentValues.put(KEY_CHECK_FLAG_EXPENSE, expense.getFlagCheck());
        return contentValues;
    }

    public IncomeExpense cursorToExpense(Cursor cursor) {
        IncomeExpense expense = new IncomeExpense();
        expense.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_EXPENSE)));
        expense.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_EXPENSE)));
        expense.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT_EXPENSE)));
        expense.setAmount(cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT_MONEY_EXPENSE)));
        try {
            expense.setDate(formatDate.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATE_DATE_EXPENSE))));
        } catch (ParseException e) {
            expense.setDate(null);
        }
        try {
            expense.setHour(formatTime.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATE_TIME_EXPENSE))));
        } catch (ParseException e) {
            expense.setHour(null);
        }
        expense.setFlagCheck(cursor.getInt(cursor.getColumnIndex(KEY_CHECK_FLAG_EXPENSE)));
        return expense;
    }

}
