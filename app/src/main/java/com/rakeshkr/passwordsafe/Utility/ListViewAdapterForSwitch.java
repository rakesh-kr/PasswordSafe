package com.rakeshkr.passwordsafe.Utility;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakeshkr.passwordsafe.R;

public class ListViewAdapterForSwitch extends BaseAdapter
{
    Activity context;
    String title[];
    Integer myImgId[];
    SharedPreferences sharedPreferences;
    public ListViewAdapterForSwitch(Activity context, String[] title, Integer[] imgId) {
        super();
        this.context = context;
        this.title = title;
        this.myImgId=imgId;
    }

    public int getCount() {
        return title.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView myImageView;
        TextView txtViewTitle;

    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        final String[] selectionItemsForLooping={"soundSettings","vibrationSettings"};
        final String NOTIFICATIONPREFERENCES="NotificationPreference";
        String Vibration="vibrationSettings";
        String Sound="soundSettings";


        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.notification_display_list_item, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
            holder.myImageView=(ImageView)convertView.findViewById(R.id.switch_img);

            convertView.setTag(holder);
            holder.myImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreferences=context.getSharedPreferences(NOTIFICATIONPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();



                }
            });
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewTitle.setText(title[position]);

        holder.myImageView.setImageResource(myImgId[position]);

        return convertView;
    }


}