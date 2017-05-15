package nguyenduchai.cse.com.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import nguyenduchai.cse.com.adapter.AdapterIconGridView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Integer []arrayIcon = new Integer[]{R.drawable.ic_gift, R.drawable.ic_salary, R.drawable.ic_eat,
            R.drawable.ic_economize, R.drawable.ic_sell, R.drawable.ic_profit, R.drawable.ic_fuel,
            R.drawable.ic_shopping, R.drawable.ic_wedding_day, R.drawable.ic_brithday, R.drawable.ic_party,
            R.drawable.ic_flower, R.drawable.ic_bus, R.drawable.ic_airplane, R.drawable.ic_football,
            R.drawable.ic_volleyball, R.drawable.ic_tennis};
    private ImageView imageIcon;
    private GridView gvIcon;
    private Button btnCancel;
    private Button btnAdd;
    private EditText editTitle;
    private AdapterIconGridView adapterIconGridView;
    private Context context;
    private Database db;
    private int isType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        context = this;
        db = new Database(context);
        getControlFromView();
        gvIcon.setOnItemClickListener(this);
        Intent intent = getIntent();
        Bundle getData = intent.getBundleExtra("ADD_ITEM");
        isType = getData.getInt("CODE_ITEM");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTitle = editTitle.getText().toString().trim();
                if(checkTitleExits(isType, strTitle )) {
                    Toast.makeText(AddItemActivity.this, "Already available", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    if(TextUtils.isEmpty(strTitle))
                        Toast.makeText(AddItemActivity.this, "Please write name title", Toast.LENGTH_SHORT).show();
                    else {
                        TypeIncomeExpense newTypeItem = new TypeIncomeExpense();
                        Bitmap image = ((BitmapDrawable) imageIcon.getDrawable()).getBitmap();
                        // convert bitmap to byte
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte imageInByte[] = stream.toByteArray();
                        newTypeItem.setName(strTitle);
                        newTypeItem.setImage(imageInByte);
                        newTypeItem.setSumAmountMoney(0.00);
                        if (isType == 1)
                            db.insertTypeIncome(newTypeItem);
                        else
                            db.insertTypeExpense(newTypeItem);
                        Toast.makeText(AddItemActivity.this, "New Item insert success", Toast.LENGTH_SHORT).show();
                        editTitle.setText("");
                        finish();
                    }
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getControlFromView(){
        gvIcon = (GridView)findViewById(R.id.gv_icons);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnAdd = (Button)findViewById(R.id.btn_add);
        imageIcon = (ImageView)findViewById(R.id.img_item);
        editTitle = (EditText)findViewById(R.id.edit_title);
        adapterIconGridView = new AdapterIconGridView(this, arrayIcon);
        gvIcon.setAdapter(adapterIconGridView);
    }
    private boolean checkTitleExits(int isType, String strTitle) {
        ArrayList<TypeIncomeExpense> arrayList;
        if(isType == 1)
            arrayList = db.getListTypeIncome("SELECT * FROM " + Database.TABLE_TYPE_INCOME);
        else
            arrayList = db.getListTypeExpense("SELECT * FROM " + Database.TABLE_TYPE_EXPENSE);
        for(int i = 0; i < arrayList.size(); i++){
            TypeIncomeExpense temp = arrayList.get(i);
            if(strTitle.equalsIgnoreCase(temp.getName()))
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDetail(position);
    }

    private void showDetail(int position) {
        imageIcon.setImageResource(arrayIcon[position]);
    }
}
