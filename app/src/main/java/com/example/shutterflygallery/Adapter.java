package com.example.shutterflygallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Adapter extends BaseAdapter {

    Context context;
    List<Bitmap> images;
    Set<Integer> favIndexSet;

    public Adapter(Context context){
        this.context = context;
        images = Collections.synchronizedList(new ArrayList<Bitmap>());
        favIndexSet = new HashSet<>();
    }

    public void addImage(Bitmap bitmap){
        images.add(bitmap);
    }

    public void removeAll(){
        images.clear();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int pos) {
        return images.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        final int imagePosition = pos;
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.grid_item, null);

            final ToggleButton favorBtn = (ToggleButton) gridView.findViewById(R.id.toggleButton);
            favorBtn.setOnClickListener(new ToggleButton.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Log.d("d", "Image view is clicked : " + view + "\nAt index position : " + imagePosition);
                    if(favorBtn.isChecked()) {
                        favorBtn.setChecked(true);
                        if(!favIndexSet.contains(imagePosition)) {
                            favIndexSet.add(imagePosition);
                        }
                    }
                    else{
                        favorBtn.setChecked(false);
                        if(favIndexSet.contains(imagePosition)){
                            favIndexSet.remove(imagePosition);
                        }
                    }
                }
            });

        } else {
            gridView = (View)convertView;
        }

        ImageView imageView = (ImageView)gridView.findViewById(R.id.gridItemImage);
        if (pos < images.size()) {
            //Setting images
            imageView.setImageBitmap(images.get(pos));
        }
        return gridView;
    }
}
