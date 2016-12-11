package com.rm.easy.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.rm.easy.crm.R;
import com.rm.easy.crm.jsonBean.Data;

import java.util.List;

/**
 * Created by Easy.D on 2016/11/13.
 */
public class SelectWarehouseAdapter extends ArrayAdapter<Data> {

    private int resourceId;
    private List<Data> list;

    public SelectWarehouseAdapter(Context context, int textViewResourceId, List<Data> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
        list = objs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = getItem(position);
        View view;
        convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView warehouseName = (TextView)convertView.findViewById(android.R.id.text1);
        warehouseName.setText(list.get(position).getWarehouseName());
        return convertView;
    }
}
