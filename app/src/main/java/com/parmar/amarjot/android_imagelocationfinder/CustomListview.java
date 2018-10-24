package com.parmar.amarjot.android_imagelocationfinder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;

public class CustomListview extends ArrayAdapter <String>{

    private String [] recipe_title;
    private String [] recipe_image_id;

    private Activity context;

    public CustomListview(@NonNull Context context, String [] _recipeName, String [] _recipeID) {
        super(context, R.layout.custom_listview_layout, _recipeName);

        this.context= (Activity) context;
        this.recipe_title=_recipeName;
        this.recipe_image_id=_recipeID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.custom_listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        Resources resources = getContext().getResources();
        final int resourceId = resources.getIdentifier(recipe_image_id[position], "drawable",
                getContext().getPackageName());

        viewHolder.textViewName.setText(recipe_title[position]);
        viewHolder.imageView.setImageResource(resourceId);

        return r;
    }

    class ViewHolder {
        TextView textViewName;
        CircularImageView imageView;
        ViewHolder (View v) {
            textViewName = v.findViewById(R.id.textTitle);
            imageView = v.findViewById(R.id.imageView);
            textViewName.setTextSize(20);
        }
    }
}