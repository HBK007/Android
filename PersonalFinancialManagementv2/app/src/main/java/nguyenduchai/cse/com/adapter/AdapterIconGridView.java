package nguyenduchai.cse.com.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Nguyen Duc Hai on 12/2/2015.
 */
public class AdapterIconGridView extends BaseAdapter{

    private Context context;
    private Integer[] arrayIconId;
    public AdapterIconGridView(Context context){
        this.context = context;
    }
    public AdapterIconGridView(Context context, Integer[]arrayIconId){
        this.context = context;
        this.arrayIconId = arrayIconId;
    }
    @Override
    public int getCount() {
        return arrayIconId.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }else
            imageView = (ImageView)convertView;
        imageView.setImageResource(arrayIconId[position]);
        return imageView;
    }
}
