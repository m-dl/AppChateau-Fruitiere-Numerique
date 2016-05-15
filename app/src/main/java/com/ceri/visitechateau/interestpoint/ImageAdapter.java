package com.ceri.visitechateau.interestpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceri.visitechateau.R;

import java.util.ArrayList;

/**
 * Created by ekar on 15/05/16.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private final String[] mobileValues;
    private ArrayList<Bitmap> myBitmaps = null;

    public ImageAdapter(Context context, String[] mobileValues, ArrayList<Bitmap> x) {
        this.context = context;
        this.mobileValues = mobileValues;
        this.myBitmaps = x;
    }


    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(convertView == null){
            gridView = inflater.inflate(R.layout.mobile, null);
            //Set text
            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            textView.setText(mobileValues[position]);
            //Set image based on selected text
            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

            imageView.setImageBitmap(this.myBitmaps.get(position));

        }else{
            gridView = convertView;
        }

        return gridView;
    }


    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
