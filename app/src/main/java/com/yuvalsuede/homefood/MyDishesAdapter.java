package com.yuvalsuede.homefood;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yuvalsuede.homefood.model.DishModel;

import java.util.List;

public class MyDishesAdapter extends ArrayAdapter<DishModel> {
    private final Activity context;
    private final List<DishModel> items;

    public MyDishesAdapter(Activity context, List<DishModel> items) {
        super(context, R.layout.activity_user_details_dish, items);
        this.context = context;
        this.items = items;
    }

    static class ViewHolder {
        public ImageView dishImage;
        public TextView dishTitle;
        public TextView dishDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.activity_user_details_dish, null, true);
            holder = new ViewHolder();
            holder.dishTitle = (TextView) rowView.findViewById(R.id.user_details_dish_title);
            holder.dishDescription = (TextView) rowView.findViewById(R.id.user_details_dish_details_text);
            holder.dishImage = (ImageView) rowView.findViewById(R.id.user_details_dish_image);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.dishTitle.setText(items.get(position).getTitle());
        holder.dishDescription.setText(items.get(position).getDescription());
        Picasso.with(context).load(Cloudinary.getImageLinkFromCloudinary(items.get(position).getMainPhoto()))
                .resize(150, 100)
                .centerCrop()
                .placeholder(R.drawable.placeholder_mana)
                .into(holder.dishImage);
        return rowView;
    }
}
