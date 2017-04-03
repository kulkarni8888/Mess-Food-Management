package com.example.vasuchand.messfood;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasu Chand on 3/27/2017.
 */

public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {

    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView t0,t1,t3,t4,t5,t2;

        protected RecyclerView recycler_view_list;
        private AdapterView.OnItemClickListener listener;
        public CardView cardView;
        private Activity activity;

        public MyViewHolder(View view) {
            super(view);
            t0 = (TextView)view.findViewById(R.id.t0);
            t1 = (TextView)view.findViewById(R.id.t1);
            t2 = (TextView)view.findViewById(R.id.t2);
            t3 = (TextView)view.findViewById(R.id.t3);
            t4 = (TextView)view.findViewById(R.id.t4);
            mContext = view.getContext();
        }
    }
    public adapter(Context c)
    {
        super();
        this.mContext = c;


    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);



        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 1;
    }

}