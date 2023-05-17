package com.example.cardview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    Context context;
    List<Pojo> listPojos;

    public MyAdapter(Context context, List<Pojo> listPojos) {
        this.context = context;
        this.listPojos = listPojos;
    }

    @Override
    public int getCount() {
        return listPojos.size();
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
    public View getView(int position, View view, ViewGroup parent) {

        view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        ImageView image = view.findViewById(R.id.image);
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.desc);

        title.setText(listPojos.get(position).getTitle());
        description.setText(listPojos.get(position).getDescription());
        image.setImageResource(listPojos.get(position).getImage());

        return view;
    }
}

