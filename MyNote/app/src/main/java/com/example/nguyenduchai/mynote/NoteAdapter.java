package com.example.nguyenduchai.mynote;

/**
 * Created by Nguyen Duc Hai on 4/9/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nguyen Duc Hai on 4/9/2017.
 */

public class NoteAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private ArrayList<NoteModel> arrayList;


    // define class viewholder
    static class ViewHolder{
        private TextView lblNote, lblDateTime;
        private ImageView btnDelete;
    }

    public NoteAdapter(Context context, ArrayList<NoteModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final ViewHolder viewHolder;
        if(rowView == null)
        {
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.lblNote = (TextView)rowView.findViewById(R.id.lbl_note);
            viewHolder.lblDateTime = (TextView)rowView.findViewById(R.id.lbl_datetime);
            viewHolder.btnDelete = (ImageView) rowView.findViewById(R.id.btn_delete);
            rowView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)rowView.getTag();
        }
        NoteModel note = arrayList.get(position);

        viewHolder.btnDelete.setTag(note);
        viewHolder.btnDelete.setOnClickListener(this);

        viewHolder.lblNote.setText(note.getNote());
        viewHolder.lblDateTime.setText(note.getDatetime());
        return rowView;
    }
    @Override
    public void onClick(View v) {
        ((MainActivity)this.context).deleteNote((NoteModel) v.getTag());
    }
}
