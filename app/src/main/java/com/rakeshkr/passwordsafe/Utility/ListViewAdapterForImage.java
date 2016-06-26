package com.rakeshkr.passwordsafe.Utility;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.R;

public class ListViewAdapterForImage extends BaseAdapter
{
    Activity context;
    String title[];
    Integer myImgId[];

    public ListViewAdapterForImage(Activity context, String[] title, Integer[] imgId) {
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

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;

        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.social_account_details_list_view, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
            holder.myImageView=(ImageView)convertView.findViewById(R.id.social_img);

            convertView.setTag(holder);
            holder.myImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String bankName;
                    if (holder.txtViewTitle.getText().toString().trim().contains("-")){
                        bankName=holder.txtViewTitle.getText().toString().trim().split("-")[1];
                    }else if (holder.txtViewTitle.getText().toString().trim().contains("@")){
                        bankName=holder.txtViewTitle.getText().toString().trim().split("@")[1];
                    }else {
                        bankName=holder.txtViewTitle.getText().toString().trim();
                    }
                    Toast.makeText(context,bankName,Toast.LENGTH_SHORT).show();
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