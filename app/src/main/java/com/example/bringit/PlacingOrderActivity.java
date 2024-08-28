package com.example.bringit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bringit.Prevalent.Prevalent;
import com.example.bringit.ViewHolder.ItemViewHolder;
import com.example.bringit.model.MemberModel;
import com.example.bringit.utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PlacingOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText orderTitleET,orderPlaceET;
    Button confirmOrderBtn;
    private String selectedMemberId = null;
    String selectedMemberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placing_order);

        orderTitleET = (EditText) findViewById(R.id.order_title_et);
        orderPlaceET = (EditText) findViewById(R.id.order_place_et);
        confirmOrderBtn = (Button) findViewById(R.id.confirm_order_btn);
        recyclerView = findViewById(R.id.placing_order_recyclerview);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        confirmOrderBtn.setOnClickListener(view -> {

            confirmOrderBtn();

        });
    }

    private void confirmOrderBtn() {

        String orderTitle = orderTitleET.getText().toString();
        String orderPlace = orderPlaceET.getText().toString();

        if(selectedMemberId == null){

            Toast.makeText(PlacingOrderActivity.this, "First Select member", Toast.LENGTH_SHORT).show();
            return;
        }
        if(orderTitle.isEmpty()){
            orderTitleET.setError("Empty field");
            return;
        }
        if(orderPlace.isEmpty()){
            orderTitleET.setError("Empty field");
            return;
        }

        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        DatabaseReference ref = FirebaseUtils.getOrdersRef(Prevalent.currentGroup.getGroupId()).push();

        HashMap<String,Object> orderMap = new HashMap<>();
        orderMap.put("orderTitle",orderTitle);
        orderMap.put("orderPlace",orderPlace);
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("memberId",selectedMemberId);
        orderMap.put("memberName",selectedMemberName);
        orderMap.put("orderId",ref.getKey());




        ref.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(PlacingOrderActivity.this, "Your Order has been confirmed successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlacingOrderActivity.this, AllOrdersActivity.class);
                //user can't go back to this activity by clicking back button
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<MemberModel> options =
                new FirebaseRecyclerOptions.Builder<MemberModel>()
                        .setQuery(FirebaseUtils.getMembersRef(Prevalent.currentGroup.getGroupId()),MemberModel.class)
                        .build();

        FirebaseRecyclerAdapter<MemberModel, ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<MemberModel, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull MemberModel model) {
                        holder.memberName.setText(model.getMemberName());

                        if (model.getMemberId().equals(selectedMemberId)) {
                            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.selected_color));
                        } else {
                            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                        }


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                selectedMemberId = model.getMemberId();
                                selectedMemberName = model.getMemberName();
                                notifyDataSetChanged();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_recycler_view_row,parent,false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };



        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

}