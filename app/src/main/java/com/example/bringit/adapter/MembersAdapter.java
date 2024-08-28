package com.example.bringit.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bringit.AddingGroupMembersActivity;
import com.example.bringit.utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.example.bringit.R;
import com.example.bringit.model.MemberModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ItemViewHolder> {

    Context context;
    ArrayList<MemberModel> memberModelsList;

    public MembersAdapter(Context context, ArrayList<MemberModel> memberModelsList) {
        this.context = context;
        this.memberModelsList = memberModelsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_recycler_view_row,parent,false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        MemberModel memberModel = memberModelsList.get(position);
        holder.memberName.setText(memberModel.getMemberName());
    }

    @Override
    public int getItemCount() {
        return memberModelsList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder{


        TextView memberName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            memberName = itemView.findViewById(R.id.members_recycler_view_item);
        }
    }



}

