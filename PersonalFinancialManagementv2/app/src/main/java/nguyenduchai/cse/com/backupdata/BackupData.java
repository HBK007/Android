package nguyenduchai.cse.com.backupdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import nguyenduchai.cse.com.database.Database;
import nguyenduchai.cse.com.model.IncomeExpense;
import nguyenduchai.cse.com.model.TypeIncomeExpense;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

/**
 * Created by Nguyen Duc Hai on 12/18/2015.
 */
public class BackupData {
    // url for database
    private final String dataPath = "//data//nguyenduchai.cse.com.activity//databases//";

    // name of main data
    private final String dataName = Database.DATABASE_NAME;

    // data main
    private final String data = dataPath + dataName;

    // name of temp data
    private final String dataTempName = Database.DATABASE_NAME + "_temp";

    // temp data for copy data from sd then copy data temp into main data
    private final String dataTemp = dataPath + dataTempName;

    // folder on sd to backup data
    private final String folderSD = Environment.getExternalStorageDirectory() + "/MyData";

    private Context context;

    public BackupData(Context context) {
        this.context = context;
    }

    // create folder if it not exist
    private void createFolder() {
        File sd = new File(folderSD);
        if (!sd.exists()) {
            sd.mkdir();
            System.out.println("create folder");
        } else {
            System.out.println("exits");
        }
    }

    /**
     * Copy database to sd card
     * name of file = database name + time when copy
     * When finish, we call onFinishExport method to send notify for activity
     */
    public void exportToSD() {

        String error = null;
        try {

            createFolder();

            File sd = new File(folderSD);

            if (sd.canWrite()) {

                SimpleDateFormat formatTime = new SimpleDateFormat("dd_MMMM_yyyy_hh:mm:ss a");
                String backupDBPath = dataName + "_" + formatTime.format(new Date());

                File currentDB = new File(Environment.getDataDirectory(), data);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                } else {
                    System.out.println("db not exist");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Error backup";
        }
        onBackupListener.onFinishExport(error);
    }

    /**
     * import data from file backup on sd card
     * we must create a temp database for copy file on sd card to it.
     * Then we copy all row of temp database into main database.
     * It will keep struct of curren database not change when struct backup database is old
     *
     * @param fileNameOnSD name of file database backup on sd card
     */
    public void importData(String fileNameOnSD) {

        File sd = new File(folderSD);

        // create temp database
        SQLiteDatabase dbBackup = context.openOrCreateDatabase(dataTempName,
                SQLiteDatabase.CREATE_IF_NECESSARY, null);

        String error = null;

        if (sd.canWrite()) {

            File currentDB = new File(Environment.getDataDirectory(), dataTemp);
            File backupDB = new File(sd, fileNameOnSD);

            if (currentDB.exists()) {
                FileChannel src;
                try {
                    src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    error = "Error load file";
                } catch (IOException e) {
                    error = "Error import";
                }
            }
        }
        /**
         *when copy old database into temp database success
         * we copy all row of table into main database
         */

        if (error == null) {
            new CopyDataAsyncTask(dbBackup).execute();
        } else {
            onBackupListener.onFinishImport(error);
        }
    }

    /**
     * show dialog for select backup database before import database
     * if user select yes, we will export curren database
     * then show dialog to select old database to import
     * else we onoly show dialog to select old database to import
     */
    public void importFromSD() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
        builder.setTitle(R.string.backup_data).setIcon(R.mipmap.ic_backup)
                .setMessage(R.string.backup_before_import);
        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialogListFile(folderSD);
            }
        });
        builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialogListFile(folderSD);
                exportToSD();
            }
        });
        builder.show();
    }

    /**
     * show dialog list all backup file on sd card
     * @param folderPath folder conatain backup file
     */
    private void showDialogListFile(String folderPath) {
        createFolder();

        File folder = new File(folderPath);
        File[] listFile = folder.listFiles();

        final String[] listFileName = new String[listFile.length];
        for (int i = 0, j = listFile.length - 1; i < listFile.length; i++, j--) {
            listFileName[j] = listFile[i].getName();
        }

        if (listFileName.length > 0) {

            // get layout for list
            LayoutInflater inflater = ((FragmentActivity) context).getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list_backup_file, null);

            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);

            // set view for dialog
            builder.setView(convertView);
            builder.setTitle(R.string.select_file).setIcon(R.mipmap.ic_backup);

            final AlertDialog alert = builder.create();

            ListView lv = (ListView) convertView.findViewById(R.id.lv_backup);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1, listFileName);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    alert.dismiss();
                    importData(listFileName[position]);
                }
            });
            alert.show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
            builder.setTitle(R.string.delete).setIcon(R.mipmap.ic_backup)
                    .setMessage(R.string.backup_empty);
            builder.show();
        }
    }

    /**
     * AsyncTask for copy data
     */
    class CopyDataAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progress = new ProgressDialog(context);
        SQLiteDatabase db;

        public CopyDataAsyncTask(SQLiteDatabase dbBackup) {
            this.db = dbBackup;
        }

        /**
         * will call first
         */

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress.setMessage("Importing...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            copyData(db);
            return null;
        }

        /**
         * end process
         */
        @Override
        protected void onPostExecute(Void error) {
            // TODO Auto-generated method stub
            super.onPostExecute(error);
            if (progress.isShowing()) {
                progress.dismiss();
            }
            onBackupListener.onFinishImport(null);
        }
    }

    /**
     * copy all row of temp database into main database
     * @param dbBackup
     */
    private void copyData(SQLiteDatabase dbBackup) {

        Database db = new Database(context);
        db.deleteTypeIncome(null);
        db.deleteTypeExpense(null);
        db.deleteIncome(null);
        db.deleteExpense(null);

        /** copy all row of subject table */

        /** copy row table type income*/
        Cursor cursorTypeIncome = dbBackup.query(true, Database.TABLE_TYPE_INCOME, null, null, null, null, null, null, null);
        cursorTypeIncome.moveToFirst();
        while (!cursorTypeIncome.isAfterLast()) {
            TypeIncomeExpense temp = db.cursorToTypeIncome(cursorTypeIncome);
            db.insertTypeIncome(temp);
            cursorTypeIncome.moveToNext();
        }
        cursorTypeIncome.close();

        /** copy row table income*/
        Cursor cursorIncome = dbBackup.query(true, Database.TABLE_INCOME, null, null, null, null, null, null, null);
        cursorIncome.moveToFirst();
        while (!cursorIncome.isAfterLast()) {
            IncomeExpense temp = db.cursorToIncome(cursorIncome);
            db.insertIncome(temp);
            cursorIncome.moveToNext();
        }
        cursorIncome.close();

        /** copy row table type expense*/
        Cursor cursorTypeExpense = dbBackup.query(true, Database.TABLE_TYPE_EXPENSE, null, null, null, null, null, null, null);
        cursorTypeExpense.moveToFirst();
        while (!cursorTypeExpense.isAfterLast()) {
            TypeIncomeExpense temp = db.cursorToTypeExpense(cursorTypeExpense);
            db.insertTypeExpense(temp);
            cursorTypeExpense.moveToNext();
        }
        cursorTypeExpense.close();

        /**copy row table expense*/
        Cursor cursorExpense = dbBackup.query(true, Database.TABLE_EXPENSE, null, null, null, null, null, null, null);
        cursorExpense.moveToFirst();
        while (!cursorExpense.isAfterLast()) {
            IncomeExpense temp = db.cursorToExpense(cursorExpense);
            db.insertExpense(temp);
            cursorExpense.moveToNext();
        }
        cursorExpense.close();

        context.deleteDatabase(dataTempName);
    }


    private OnBackupListener onBackupListener;

    public void setOnBackupListener(OnBackupListener onBackupListener) {
        this.onBackupListener = onBackupListener;
    }

    public interface OnBackupListener {
        public void onFinishExport(String error);

        public void onFinishImport(String error);
    }
}
