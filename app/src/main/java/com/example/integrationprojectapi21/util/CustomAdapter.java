package com.example.integrationprojectapi21.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.example.integrationprojectapi21.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> Keys_list;
    ArrayList<String> Values_list;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> Keys_list, ArrayList<String> Values_list) {
        this.context = context;
        this.Keys_list = Keys_list;
        this.Values_list = Values_list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // set the data in items
        holder.key_textview.setText(Keys_list.get(position));
        holder.value_textview.setText(Values_list.get(position));

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, Keys_list.get(holder.getBindingAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Keys_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView key_textview, value_textview;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            key_textview = (TextView) itemView.findViewById(R.id.key);
            value_textview = (TextView) itemView.findViewById(R.id.value);


        }


    }

    public void onLoadedList(ArrayList<String> keys,ArrayList<String> vals){
        this.Keys_list = keys;
        this.Values_list =vals;
        notifyDataSetChanged();// notification of data set changed

    }
}
