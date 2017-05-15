package nguyenduchai.cse.com.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import nguyenduchai.cse.com.adapter.AdapterIconGridView;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

public class EditItemActivity extends AppCompatActivity{
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
    private TypeIncomeExpense typeItemSelect;
    private int isType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        context = this;
        db = new Database(context);
        getControlFromView();
        getDataFromMain();
        getDefaultData();
        gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageIcon.setImageResource(arrayIcon[position]);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTitle = editTitle.getText().toString().trim();
                if(TextUtils.isEmpty(strTitle)){
                    Toast.makeText(EditItemActivity.this, "Please write name", Toast.LENGTH_LONG).show();
                }else {
                    Bitmap image = ((BitmapDrawable) imageIcon.getDrawable()).getBitmap();
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    typeItemSelect.setName(strTitle);
                    typeItemSelect.setImage(imageInByte);
                    if(isType == 1) {
                        db.updateTypeIncome(typeItemSelect);
                        Toast.makeText(EditItemActivity.this, "Update type income success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        db.updateTypeExpense(typeItemSelect);
                        Toast.makeText(EditItemActivity.this, "Update type expense success", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTitle.setText("");
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

    private void getDataFromMain(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("EDIT_ITEM");
        typeItemSelect = (TypeIncomeExpense)bundle.getSerializable("SELECTED");
        isType = bundle.getInt("CODE_EDIT");
    }

    private void getDefaultData(){
        editTitle.setText(typeItemSelect.getName());
        Bitmap bmp = BitmapFactory.decodeByteArray(typeItemSelect.getImage(), 0, typeItemSelect.getImage().length);
        imageIcon.setImageBitmap(bmp);
    }

}
