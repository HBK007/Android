package com.example.nguyenduchai.mydiary.controller;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nguyenduchai.mydiary.R;
import com.example.nguyenduchai.mydiary.model.NoteModel;
import com.example.nguyenduchai.mydiary.utility.Config;
import com.example.nguyenduchai.mydiary.utility.Util;


import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Nguyen Duc Hai on 4/11/2017.
 */

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NoteModel> arrNotes;
    private String[] arrColors = {"#CDDC39", "#FF3D00", "#FFFF00", "#AEEA00", "#00B8D8", "#311B92", "#FF1744", "#4A148C", "#F44336"};
    private Random random;
    // define viewHolder
    static class ViewHolder{
        private LinearLayout layoutMark;
        private TextView lblTitle, lblContent, lblDay, lblDate, lblTime;
        private ImageView imgAttach;
    }

    public NoteAdapter(Context context, ArrayList<NoteModel> arrNotes) {
        this.context = context;
        this.arrNotes = arrNotes;
        random = new Random();
    }

    @Override
    public int getCount() {
        return arrNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return arrNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final ViewHolder viewHolder;
        if(rowView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            rowView = (View)inflater.inflate(R.layout.note_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.layoutMark = (LinearLayout)rowView.findViewById(R.id.layout_mark);
            viewHolder.lblTitle = (TextView)rowView.findViewById(R.id.lbl_title);
            viewHolder.lblContent = (TextView)rowView.findViewById(R.id.lbl_content);
            viewHolder.lblDay = (TextView)rowView.findViewById(R.id.lbl_day);
            viewHolder.lblDate = (TextView)rowView.findViewById(R.id.lbl_date);
            viewHolder.lblTime = (TextView)rowView.findViewById(R.id.lbl_time);
            viewHolder.imgAttach = (ImageView) rowView.findViewById(R.id.img_attach);

            rowView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) rowView.getTag();
        }
        NoteModel noteModel = arrNotes.get(position);
        // process datetime
        Date date = Util.convertStringToDate(noteModel.getDatetime());
        String dayOfTheWeek = (String) DateFormat.format("EE", date);
        String month = (String) DateFormat.format("MMM", date);
        String day = (String) DateFormat.format("dd", date);
        String time = (String) DateFormat.format("hh:mm", date);
        // set color background for layout mark
        viewHolder.layoutMark.setBackgroundColor(Color.parseColor(arrColors[random.nextInt(arrColors.length)]));
        viewHolder.lblTitle.setText(noteModel.getTitle());
        viewHolder.lblContent.setText(noteModel.getContent());
        viewHolder.lblDay.setText(dayOfTheWeek);
        viewHolder.lblDate.setText(day + " " + month);
        viewHolder.lblTime.setText(time);
        Util.setBitmapToImage(context, Config.FOLDER_IMAGES, noteModel.getImage(), viewHolder.imgAttach);
        return rowView;
    }
}
