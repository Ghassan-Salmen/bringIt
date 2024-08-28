package com.example.bringit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bringit.Prevalent.Prevalent;
import com.example.bringit.ViewHolder.ItemViewHolder;
import com.example.bringit.ViewHolder.OrderViewHolder;
import com.example.bringit.model.MemberModel;
import com.example.bringit.model.Order;
import com.example.bringit.utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AllOrdersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noOrdersTextView;

    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_and_take_order);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.all_orders_recyclerview);
        noOrdersTextView = findViewById(R.id.no_orders_text_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(AllOrdersActivity.this, PlacingOrderActivity.class);
                startActivity(intent);

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_group_item:

                Intent intent = new Intent(AllOrdersActivity.this,GroupShareActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(FirebaseUtils.getOrdersRef(Prevalent.currentGroup.getGroupId()),Order.class)
                        .build();

        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {


                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycler_view_row,parent,false);
                        OrderViewHolder holder = new OrderViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {

                        holder.memberName.setText("ordered by: "+model.getMemberName());
                        holder.orderPlace.setText(model.getOrderPlace());
                        holder.orderTitle.setText(model.getOrderTitle());
                        holder.time.setText(model.getTime());
                        holder.date.setText(model.getDate());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(AllOrdersActivity.this);
                                builder.setTitle("Alert!!")
                                        .setMessage("Do you want to bring this order?")
                                        .setCancelable(true)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                FirebaseUtils.getOrdersRef(Prevalent.currentGroup.getGroupId())
                                                        .child(model.getOrderId())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(AllOrdersActivity.this, model.getMemberName()+ " group id " +Prevalent.currentGroup.getGroupId(), Toast.LENGTH_SHORT).show();
                                                                    Log.e("Tag", model.getOrderTitle()+" "+model.getMemberId()+" "+
                                                                            model.getMemberName()+FirebaseUtils.getOrdersRef(Prevalent.currentGroup.getGroupId())
                                                                            .child(model.getOrderId()));

                                                                }

                                                            }
                                                        });

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).show();
                            }
                        });

                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();

                        if (getItemCount() == 0) {
                            // No orders, show the TextView
                            noOrdersTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            // Orders present, hide the TextView and show the RecyclerView
                            noOrdersTextView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}