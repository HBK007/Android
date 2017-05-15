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
import nguyenduchai.cse.com.activity.MainActivity;
import nguyenduchai.cse.com.adapter.AdapterCustomListView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 11/11/2015.
 */
public class FragmentIncome extends Fragment {

    private View view;
    private ImageButton btnAddItem;
    private ImageButton btnEdit;
    private ImageButton btnDelete;
    private ImageButton btnUp;
    private ImageButton btnDown;
    private ArrayList<TypeIncomeExpense> arrayListTypeIncome = new ArrayList<>();
    private AdapterCustomListView adapterTypeIncome;
    private ListView lvTypeIncome;
    private Context context;
    private Database db;
    private TypeIncomeExpense typeItemSelected = null;
    private int pos;
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private Calendar dateCalendar = Calendar.getInstance();

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
//        get control for
        getFromWidgets();
        addEventFromWidgets();
        getDatabase();
        return view;
    }

    private void getFromWidgets() {
        btnAddItem = (ImageButton) view.findViewById(R.id.btn_add_item);
        lvTypeIncome = (ListView) view.findViewById(R.id.lv_items);
        adapterTypeIncome = new AdapterCustomListView(getActivity(), R.layout.layout_custom_items, arrayListTypeIncome);
        lvTypeIncome.setAdapter(adapterTypeIncome);
    }

    private void addEventFromWidgets() {
        btnAddItem.setOnClickListener(new MyButtonEvent());
        lvTypeIncome.setOnItemClickListener(new MyListViewEvent());
        lvTypeIncome.setOnItemLongClickListener(new MyListViewEvent());
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

    public void getDatabase() {
        if (!checkIsTableExits("SELECT * FROM " + Database.TABLE_TYPE_INCOME)) {
//            insert initial data of database
            insertInitialDatabase();
        }
    }

    private void insertInitialDatabase() {
        db.open();
//        set array icon income
        Resources resIcon = getResources();
        TypedArray arrIcon = resIcon.obtainTypedArray(R.array.icon_income);
//         set  title income
        Resources resTitle = getResources();
        String[] arrTitle = resTitle.getStringArray(R.array.title_income);
        for (int i = 0; i < arrIcon.length(); i++) {
            Bitmap image = BitmapFactory.decodeResource(resIcon, arrIcon.getResourceId(i, -1));
            // convert bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
            TypeIncomeExpense newTypeIncome = new TypeIncomeExpense();
            newTypeIncome.setImage(imageInByte);
            newTypeIncome.setName(arrTitle[i]);
            newTypeIncome.setSumAmountMoney(0);
            db.insertTypeIncome(newTypeIncome);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadListTypeIncome();
    }

    @Override
    public void onPause() {
        super.onPause();
        setAllValueDefaultFlagCheck();
        setAllValueAmountTypeIncome();
    }

    private void setAllValueAmountTypeIncome() {
        ArrayList<TypeIncomeExpense> tempList = db.getListTypeIncome("SELECT * FROM " + Database.TABLE_TYPE_INCOME);
        for (int i = 0; i < tempList.size(); i++) {
            TypeIncomeExpense tempTypeIncome = tempList.get(i);
            tempTypeIncome.setSumAmountMoney(0.00);
            db.updateTypeIncome(tempTypeIncome);
        }
    }

    private void setAllValueDefaultFlagCheck() {
        ArrayList<IncomeExpense> tempList = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
        for (int i = 0; i < tempList.size(); i++) {
            IncomeExpense tempIncome = tempList.get(i);
            tempIncome.setFlagCheck(0);
            db.updateIncome(tempIncome);
        }
    }

    private void loadListTypeIncome() {
        setAmountMoneyForAllTypeIncome();
        // clear old list
        arrayListTypeIncome.clear();
        // add all notes from database, reverse list
        ArrayList<TypeIncomeExpense> tempList = db.getListTypeIncome("SELECT * FROM " + Database.TABLE_TYPE_INCOME);
        for (int i = 0; i < tempList.size(); i++) {
            arrayListTypeIncome.add(tempList.get(i));
        }
        adapterTypeIncome.notifyDataSetChanged();
    }

    public void setAmountMoneyForAllTypeIncome() {
        db.open();
        String date = formatDate.format(dateCalendar.getTime());
        ArrayList<TypeIncomeExpense> listTempTypeIncome = db.getListTypeIncome("SELECT * FROM " + Database.TABLE_TYPE_INCOME);
        if (checkDate(date)) {
            ArrayList<IncomeExpense> listTempIncome = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
            for (int i = 0; i < listTempTypeIncome.size(); i++) {
                TypeIncomeExpense tempTypeIncome = listTempTypeIncome.get(i);
                double tempAmountMoney = 0;
                for (int j = 0; j < listTempIncome.size(); j++) {
                    IncomeExpense tempIncome = listTempIncome.get(j);
                    String tempDate = formatDate.format(tempIncome.getDate());
                    if (date.equalsIgnoreCase(tempDate)) {
                        if (tempTypeIncome.getName().equalsIgnoreCase(tempIncome.getName()) && tempIncome.getFlagCheck() == 0) {
                            tempAmountMoney += tempIncome.getAmount();
                            tempIncome.setFlagCheck(1);
                            db.updateIncome(tempIncome);
                        }
                    }
                }
                tempAmountMoney += tempTypeIncome.getSumAmountMoney();
                tempTypeIncome.setSumAmountMoney(tempAmountMoney);
                db.updateTypeIncome(tempTypeIncome);
            }
        } else {
            for (int i = 0; i < listTempTypeIncome.size(); i++) {
                TypeIncomeExpense tempTypeIncome = listTempTypeIncome.get(i);
                tempTypeIncome.setSumAmountMoney(0.00);
                db.updateTypeIncome(tempTypeIncome);
            }
        }
    }

    public boolean checkDate(String date) {
        ArrayList<IncomeExpense> listIncome = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
        for (int i = 0; i < listIncome.size(); i++) {
            IncomeExpense tempIncome = listIncome.get(i);
            String tempDate = formatDate.format(tempIncome.getDate());
            if (date.equalsIgnoreCase(tempDate))
                return true;
        }
        return false;
    }

    private class MyButtonEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_item:
//                    open activity add content of item selected
                    Intent intentAdd = new Intent(context, AddItemActivity.class);
                    Bundle bundleAdd = new Bundle();
                    bundleAdd.putInt("CODE_ITEM", 1);
                    intentAdd.putExtra("ADD_ITEM", bundleAdd);
                    startActivity(intentAdd);
                    break;
                default:
                    break;

            }
        }
    }

    private class MyListViewEvent implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            open activity add content of item
            typeItemSelected = (TypeIncomeExpense) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), AddDetailItemActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("SELECT_DETAIL", typeItemSelected);
            bundle.putInt("CODE_ADD_DETAIL", 1);
            intent.putExtra("DETAIL", bundle);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            typeItemSelected = arrayListTypeIncome.get(position);
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
                    bundleEdit.putSerializable("SELECTED", typeItemSelected);
                    bundleEdit.putInt("CODE_EDIT", 1);
                    intentEdit.putExtra("EDIT_ITEM", bundleEdit);
                    startActivity(intentEdit);
                    dialog.dismiss();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure delete ?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String date = formatDate.format(dateCalendar.getTime());
                            ArrayList<IncomeExpense> arrayListIncome = db.getListIncome("SELECT * FROM " + Database.TABLE_INCOME);
                            for (int i = 0; i < arrayListIncome.size(); i++){
                                IncomeExpense tempIncome = arrayListIncome.get(i);
                                String tempDate = formatDate.format(tempIncome.getDate());
                                if (typeItemSelected.getName().equalsIgnoreCase(tempIncome.getName()) && date.equalsIgnoreCase(formatDate.format(tempIncome.getDate()))){
                                    db.deleteIncome(Database.KEY_ID_INCOME + "='" + tempIncome.getId() + "'");
                                }
                            }
                            db.deleteTypeIncome(Database.KEY_ID_TYPE_INCOME + "='" + typeItemSelected.getId() + "'");
                            loadListTypeIncome();
                        }
                    });
                    builder.show();
                    dialog.dismiss();
                }
            });
            btnDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos < arrayListTypeIncome.size() - 1) {
                        TypeIncomeExpense tempIncome = arrayListTypeIncome.get(pos + 1);
                        long idTemp = tempIncome.getId();
                        long idType = typeItemSelected.getId();
                        tempIncome.setId(idType);
                        typeItemSelected.setId(idTemp);
                        db.updateTypeIncome(tempIncome);
                        db.updateTypeIncome(typeItemSelected);
                        loadListTypeIncome();
                    }
                    dialog.dismiss();
                }
            });
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos > 0) {
                        TypeIncomeExpense tempIncome = arrayListTypeIncome.get(pos - 1);
                        long idTemp = tempIncome.getId();
                        long idType = typeItemSelected.getId();
                        tempIncome.setId(idType);
                        typeItemSelected.setId(idTemp);
                        db.updateTypeIncome(tempIncome);
                        db.updateTypeIncome(typeItemSelected);
                        loadListTypeIncome();
                    }
                    dialog.dismiss();
                }
            });
            return false;
        }
    }
}
