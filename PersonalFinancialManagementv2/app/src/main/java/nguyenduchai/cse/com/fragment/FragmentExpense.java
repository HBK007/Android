package nguyenduchai.cse.com.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import nguyenduchai.cse.com.activity.AddDetailItemActivity;
import nguyenduchai.cse.com.activity.AddItemActivity;
import nguyenduchai.cse.com.activity.EditItemActivity;
import nguyenduchai.cse.com.adapter.AdapterCustomListView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 11/11/2015.
 */
public class FragmentExpense extends Fragment {

    private View view;
    private ImageButton btnAddItem;
    private ImageButton btnEdit;
    private ImageButton btnDelete;
    private ImageButton btnDown;
    private ImageButton btnUp;
    private ArrayList<TypeIncomeExpense> arrayListTypeExpense = new ArrayList<>();
    private AdapterCustomListView adapterTypeExpense;
    private ListView lvTyeExpense;
    private Context context;
    private Database db;
    private int pos;
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private Calendar dateCalendar = Calendar.getInstance();
    private TypeIncomeExpense typeExpenseSelected = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        db = new Database(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_custom_item_show, container, false);
//        get control for view
        getFromWidgets();
        addEventFromWidgets();
//        check value default of database
        getDatabase();
        return view;
    }


    private void getFromWidgets() {
        btnAddItem = (ImageButton) view.findViewById(R.id.btn_add_item);
        lvTyeExpense = (ListView) view.findViewById(R.id.lv_items);
        adapterTypeExpense = new AdapterCustomListView(getActivity(), R.layout.layout_custom_items, arrayListTypeExpense);
        lvTyeExpense.setAdapter(adapterTypeExpense);
    }

    private void addEventFromWidgets() {
        btnAddItem.setOnClickListener(new MyButtonEvents());
        lvTyeExpense.setOnItemClickListener(new MyListViewEvents());
        lvTyeExpense.setOnItemLongClickListener(new MyListViewEvents());
    }

    private boolean checkIsTableExits(String nameTable) {
        Cursor cursor = db.getAll(nameTable);
        if (cursor != null){
            if(cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void getDatabase(){
        if(!checkIsTableExits("SELECT * FROM " + Database.TABLE_TYPE_EXPENSE)){
//            insert initial data of database
            insertInitialDatabase();
        }
    }

    private void insertInitialDatabase() {
        db.open();
        //  set array icon income
        Resources resIcon = getResources();
        TypedArray arrIcon = resIcon.obtainTypedArray(R.array.icon_expense);
        //  set  title income
        Resources resTitle = getResources();
        String[] arrTitle = resTitle.getStringArray(R.array.title_expense);
        for (int i = 0; i < arrIcon.length(); i++) {
            Bitmap image = BitmapFactory.decodeResource(resIcon, arrIcon.getResourceId(i, -1));
            // convert bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
            TypeIncomeExpense newTypeExpense = new TypeIncomeExpense();
            newTypeExpense.setImage(imageInByte);
            newTypeExpense.setName(arrTitle[i]);
            newTypeExpense.setSumAmountMoney(0);
            db.insertTypeExpense(newTypeExpense);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadListTypeExpense();
    }

    @Override
    public void onPause() {
        super.onPause();
        setAllValueDefaultFlagCheck();
        setAllValueAmountTypeExpense();
    }

    private void setAllValueAmountTypeExpense() {
        ArrayList<TypeIncomeExpense> tempList = db.getListTypeExpense("SELECT * FROM " + Database.TABLE_TYPE_EXPENSE);
        for(int i = 0; i < tempList.size(); i++){
            TypeIncomeExpense tempTypeExpense = tempList.get(i);
            tempTypeExpense.setSumAmountMoney(0.00);
            db.updateTypeExpense(tempTypeExpense);
        }
    }

    private void setAllValueDefaultFlagCheck() {
        ArrayList<IncomeExpense> tempList = db.getListExpense("SELECT * FROM " + Database.TABLE_EXPENSE);
        for(int i = 0; i < tempList.size(); i++){
            IncomeExpense tempExpense = tempList.get(i);
            tempExpense.setFlagCheck(0);
            db.updateExpense(tempExpense);
        }
    }

    private void loadListTypeExpense() {
        setAmountMoneyForAllTypeExpense();
        // clear old list
        arrayListTypeExpense.clear();
        // add all notes from database, reverse list
        ArrayList<TypeIncomeExpense> tempList = db.getListTypeExpense("SELECT * FROM " + Database.TABLE_TYPE_EXPENSE);

        for (int i = 0; i < tempList.size(); i++) {
            arrayListTypeExpense.add(tempList.get(i));
        }
        adapterTypeExpense.notifyDataSetChanged();
    }

    public void setAmountMoneyForAllTypeExpense() {
        db.open();
        String date = formatDate.format(dateCalendar.getTime());
        ArrayList<TypeIncomeExpense> listTempTypeExpense = db.getListTypeExpense("SELECT * FROM " + Database.TABLE_TYPE_EXPENSE);
        if (checkDate(date)) {
            ArrayList<IncomeExpense> listTempExpense = db.getListExpense("SELECT * FROM " + Database.TABLE_EXPENSE);
            for (int i = 0; i < listTempTypeExpense.size(); i++) {
                TypeIncomeExpense tempTypeExpense = listTempTypeExpense.get(i);
                double tempAmountMoney = 0.0;
                for (int j = 0; j < listTempExpense.size(); j++) {
                    IncomeExpense tempExpense = listTempExpense.get(j);
                    String tempDate = formatDate.format(tempExpense.getDate());
                    if (date.equalsIgnoreCase(tempDate)) {
                        if (tempExpense.getName().equalsIgnoreCase(tempTypeExpense.getName()) && tempExpense.getFlagCheck() == 0) {
                            tempAmountMoney += tempExpense.getAmount();
                            tempExpense.setFlagCheck(1);
                            db.updateExpense(tempExpense);
                        }
                    }
                }
                tempAmountMoney += tempTypeExpense.getSumAmountMoney();
                tempTypeExpense.setSumAmountMoney(tempAmountMoney);
                db.updateTypeExpense(tempTypeExpense);
            }
        }else{
            for (int i = 0; i < listTempTypeExpense.size(); i++){
                TypeIncomeExpense tempTypeExpense = listTempTypeExpense.get(i);
                tempTypeExpense.setSumAmountMoney(0.00);
                db.updateTypeExpense(tempTypeExpense);
            }
        }
    }

    public boolean checkDate(String date){
        ArrayList<IncomeExpense> listExpense = db.getListExpense("SELECT * FROM " + Database.TABLE_EXPENSE);
        for(int i = 0; i < listExpense.size(); i++) {
            IncomeExpense tempExpense = listExpense.get(i);
            String tempDate = formatDate.format(tempExpense.getDate());
            if (date.equalsIgnoreCase(tempDate))
                return true;
        }
        return false;
    }

    private class MyButtonEvents implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_item:
//                    open activity add content of item selected
                    Intent intent = new Intent(context, AddItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("CODE_ITEM", 2);
                    intent.putExtra("ADD_ITEM", bundle);
                    startActivity(intent);
                    break;
            }
        }
    }

    private class MyListViewEvents implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            open activity add content of item
            typeExpenseSelected = (TypeIncomeExpense) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), AddDetailItemActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("SELECT_DETAIL", typeExpenseSelected);
            bundle.putInt("CODE_ADD_DETAIL", 2);
            intent.putExtra("DETAIL", bundle);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            typeExpenseSelected = arrayListTypeExpense.get(position);
            pos = position;
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_custom_dialog);
            dialog.show();

            btnEdit = (ImageButton) dialog.findViewById(R.id.btn_edit);
            btnDelete = (ImageButton) dialog.findViewById(R.id.btn_delete);
            btnDown = (ImageButton) dialog.findViewById(R.id.btn_down);
            btnUp = (ImageButton) dialog.findViewById(R.id.btn_up);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentEdit = new Intent(getActivity(), EditItemActivity.class);
                    Bundle bundleEdit = new Bundle();
                    bundleEdit.putSerializable("SELECTED", typeExpenseSelected);
                    bundleEdit.putInt("CODE_EDIT", 2);
                    intentEdit.putExtra("EDIT_ITEM", bundleEdit);
                    startActivity(intentEdit);
                    dialog.dismiss();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure delete " + typeExpenseSelected.getName() + " ?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteTypeExpense(Database.KEY_ID_TYPE_EXPENSE + "='" + typeExpenseSelected.getId() + "'");
                            loadListTypeExpense();
                        }
                    });
                    builder.show();
                    dialog.dismiss();
                }
            });
            btnDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pos < arrayListTypeExpense.size() - 1){
                        TypeIncomeExpense tempIncome = arrayListTypeExpense.get(pos + 1);
                        long idTemp = tempIncome.getId();
                        long idType = typeExpenseSelected.getId();
                        tempIncome.setId(idType);
                        typeExpenseSelected.setId(idTemp);
                        db.updateTypeExpense(tempIncome);
                        db.updateTypeExpense(typeExpenseSelected);
                        loadListTypeExpense();
                    }
                    dialog.dismiss();
                }
            });
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pos > 0){
                        TypeIncomeExpense tempExpense = arrayListTypeExpense.get(pos - 1);
                        long idTemp = tempExpense.getId();
                        long idType = typeExpenseSelected.getId();
                        tempExpense.setId(idType);
                        typeExpenseSelected.setId(idTemp);
                        db.updateTypeExpense(tempExpense);
                        db.updateTypeExpense(typeExpenseSelected);
                        loadListTypeExpense();
                    }
                    dialog.dismiss();
                }
            });
            return false;
        }
    }
}
