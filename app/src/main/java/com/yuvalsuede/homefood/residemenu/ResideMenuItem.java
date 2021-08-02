package com.yuvalsuede.homefood.residemenu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuvalsuede.homefood.R;

public class ResideMenuItem extends LinearLayout {

    /** menu item  icon  */
    //private ImageView iv_icon;
    /**
     * menu item  title
     */
    private TextView tv_title;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        //iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        //iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    public ResideMenuItem(Context context, String title) {
        super(context);
        initViews(context);
        //iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        //iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    private boolean isActive = false;

    public void setActive() {
        tv_title.setTextColor(Color.parseColor("#de757a"));
        isActive = true;
    }

    public void setInactive() {
        tv_title.setTextColor(Color.parseColor("#fcfff6"));
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    //public void setIcon(int icon){
    //    iv_icon.setImageResource(icon);
    //}

    /**
     * set the title with resource
     * ;
     *
     * @param title
     */
    public void setTitle(int title) {
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
