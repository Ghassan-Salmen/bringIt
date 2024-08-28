package com.example.bringit.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bringit.Interface.ItemItemClickListner;
import com.example.bringit.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView memberName,orderPlace,orderTitle,time,date;
    ItemItemClickListner listner;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderPlace = (TextView) itemView.findViewById(R.id.order_place_item_text);
        time = (TextView) itemView.findViewById(R.id.order_time_text);
        orderTitle = (TextView) itemView.findViewById(R.id.order_title_item_text);
        date = (TextView) itemView.findViewById(R.id.order_date_text);
        memberName = (TextView) itemView.findViewById(R.id.order_member_name_item_text);


    }


    public void setItemClickListner(ItemItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view,getAbsoluteAdapterPosition(),false);
    }
}
