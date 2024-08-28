package com.example.bringit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bringit.Prevalent.Prevalent;
import com.example.bringit.ViewHolder.ItemViewHolder;
import com.example.bringit.adapter.MembersAdapter;
import com.example.bringit.model.MemberModel;
import com.example.bringit.utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AddingGroupMembersActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String groupId;
    String groupName;
    EditText memberNameET;
    Button addButton,createGroupBtn;
    int numberOfMembers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_group_members);

        groupName = Prevalent.currentGroup.getGroupName();
        groupId = Prevalent.currentGroup.getGroupId();


        Paper.init(this);
        recyclerView = findViewById(R.id.members_recycler_view);
        memberNameET = (EditText) findViewById(R.id.member_name_et);
        addButton = (Button) findViewById(R.id.add_member_btn);
        createGroupBtn = (Button) findViewById(R.id.create_group_btn);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        addButton.setOnClickListener(view -> {

            addMemberName();

        });
        createGroupBtn.setOnClickListener(view -> {
            if(numberOfMembers<2){

                Toast.makeText(AddingGroupMembersActivity.this, "Add At least two members", Toast.LENGTH_SHORT).show();

                return;
            }
            Paper.book().write("groupId", Prevalent.currentGroup.getGroupId());
            Intent intent = new Intent(AddingGroupMembersActivity.this, AllOrdersActivity.class);
            //user can't go back to this activity by clicking back button
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }

    private void addMemberName() {




        String memberName = memberNameET.getText().toString();
        if(memberName.isEmpty() || memberName.length() < 3) {

            memberNameET.setError("Enter a name with more than 3 characters");
            return;

        }
        memberNameET.setText("");
        String memberId = FirebaseUtils.getMembersRef(groupId).push().getKey();

        MemberModel memberModel= new MemberModel(memberId,groupId,memberName);
        FirebaseUtils.getMembersRef(groupId).child(memberId).setValue(memberModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        numberOfMembers++;

                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<MemberModel>options =
                new FirebaseRecyclerOptions.Builder<MemberModel>()
                        .setQuery(FirebaseUtils.getMembersRef(groupId),MemberModel.class)
                        .build();

        FirebaseRecyclerAdapter<MemberModel,ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<MemberModel, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull MemberModel model) {
                        holder.memberName.setText(model.getMemberName());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddingGroupMembersActivity.this);
                                builder.setTitle("Alert!!")
                                        .setMessage("Do you want to delete this member?")
                                        .setCancelable(true)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                FirebaseUtils.getMembersRef(groupId)
                                                        .child(model.getMemberId())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(AddingGroupMembersActivity.this, "member "+ model.getMemberName()+" removed successfully", Toast.LENGTH_SHORT).show();
                                                                    numberOfMembers--;
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddingGroupMembersActivity.this);
        builder.setTitle("Alert!!")
                .setMessage("Do you want to delete this group?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseUtils.getGroupsRef()
                                .child(groupId)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(AddingGroupMembersActivity.this, "group is  removed successfully", Toast.LENGTH_SHORT).show();
                                            finish();
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


}