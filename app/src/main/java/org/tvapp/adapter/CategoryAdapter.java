package org.tvapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.tvapp.R;

import java.util.List;


public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<String> category;

    public CategoryAdapter(Context context, List<String> category) {
        this.context = context;
        this.category = category;
    }
    private ViewHolder holder;
    @Override
    public int getCount() {
        return category.size();
    }

    @Override
    public Object getItem(int position) {
        return category.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cateroge,null);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView = convertView.findViewById(R.id.tv_category);
        holder.mTextView.setText(category.get(position));
        return convertView;
    }

    static class ViewHolder{
        TextView mTextView;
    }
}
