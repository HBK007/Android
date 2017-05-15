package nguyenduchai.cse.com.customdialogclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import java.util.ArrayList;

import nguyenduchai.cse.com.activity.EditItemActivity;
import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 12/16/2015.
 */
public class CustomDialogClass extends Dialog implements View.OnClickListener{

    private Activity dialogActivity;
    private Dialog dialog;
    private int keyType;
    private TypeIncomeExpense typeItemSelected;
    private int pos;
    private ArrayList<TypeIncomeExpense> arrayListItem;
    private ImageButton btnEdit;
    private ImageButton btnDelete;
    private ImageButton btnDown;
    private ImageButton btnUp;
    private Database db;
    public CustomDialogClass(Activity activity, int key, TypeIncomeExpense typeItemSelected, int pos, ArrayList<TypeIncomeExpense> arrayListItem) {
        super(activity);
        this.dialogActivity = activity;
        this.keyType = key;
        this.typeItemSelected = typeItemSelected;
        this.pos = pos;
        this.arrayListItem = arrayListItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_dialog);
//        connect view
        btnEdit = (ImageButton)findViewById(R.id.btn_edit);
        btnDelete = (ImageButton)findViewById(R.id.btn_delete);
        btnDown = (ImageButton)findViewById(R.id.btn_down);
        btnUp = (ImageButton)findViewById(R.id.btn_up);
//        set event for button
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit:
                Intent intentEdit = new Intent(getContext(), EditItemActivity.class);
                Bundle bundleEdit = new Bundle();
                bundleEdit.putSerializable("SELECTED", typeItemSelected);
                bundleEdit.putInt("CODE_EDIT", keyType);
                intentEdit.putExtra("EDIT_ITEM", bundleEdit);
                dialogActivity.startActivity(intentEdit);
                dialogActivity.finish();
                break;
            case R.id.btn_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure ?");
                builder.setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = new Database(getContext());
                        if(keyType == 1)
                            db.deleteTypeIncome(Database.KEY_ID_TYPE_INCOME + "='" + typeItemSelected.getId() + "'");
                        else
                            db.deleteTypeExpense(Database.KEY_ID_TYPE_EXPENSE + "='" + typeItemSelected.getId() + "'");
                    }
                });
                builder.show();
                dismiss();
                break;
            case R.id.btn_up:
                if(pos > 0){
                    db = new Database(getContext());
                    TypeIncomeExpense tempITem = arrayListItem.get(pos - 1);
                    long idTemp = tempITem.getId();
                    long idType = typeItemSelected.getId();
                    tempITem.setId(idType);
                    typeItemSelected.setId(idTemp);
                    if (keyType == 1) {
                        db.updateTypeIncome(tempITem);
                        db.updateTypeIncome(typeItemSelected);
                    }else{
                        db.updateTypeExpense(tempITem);
                        db.updateTypeExpense(typeItemSelected);
                    }
                }
                dismiss();
                break;
            case R.id.btn_down:
                if(pos < arrayListItem.size() - 1){
                    TypeIncomeExpense tempITem = arrayListItem.get(pos + 1);
                    long idTemp = tempITem.getId();
                    long idType = typeItemSelected.getId();
                    tempITem.setId(idType);
                    typeItemSelected.setId(idTemp);
                    if (keyType == 1) {
                        db.updateTypeIncome(tempITem);
                        db.updateTypeIncome(typeItemSelected);
                    }else{
                        db.updateTypeExpense(tempITem);
                        db.updateTypeExpense(typeItemSelected);
                    }
                }
                dismiss();
                break;
            default:
                break;
        }
    }
}
