package com.example.bringit.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bringit.Interface.*;
import com.example.bringit.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView memberName;
    ItemItemClickListner listner;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        memberName = (TextView) itemView.findViewById(R.id.members_recycler_view_item);
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
